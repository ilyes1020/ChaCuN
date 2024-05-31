package ch.epfl.chacun;


import java.util.Comparator;
import java.util.List;

/**
 * The ActionEncoder class provides methods for encoding and decoding game actions into and from Base32 strings,
 * facilitating the communication of game state changes in a compact format to make online gaming easier.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public final class ActionEncoder {
    private ActionEncoder() {}

    /**
     * Represents a state-action pair.
     */
    public record StateAction(GameState gameState, String actionB32) {}

    /**
     * Encodes a game state change caused by placing a tile.
     *
     * @param gameState  The current game state.
     * @param placedTile The tile being placed.
     * @return a StateAction object representing the updated game state and the encoded action.
     */
    public static StateAction withPlacedTile(GameState gameState, PlacedTile placedTile) {
        List<Pos> sortedFringe = gameState.board().insertionPositions()
                .stream()
                .sorted(Comparator.comparing(Pos::x).thenComparing(Pos::y))
                .toList();

        int position = sortedFringe.indexOf(placedTile.pos());
        int rotation = placedTile.rotation().ordinal();

        int packedInfo = 0b1111111111 & ((position << 2) | rotation);

        return new StateAction(gameState.withPlacedTile(placedTile)
                , Base32.encodeBits10(packedInfo));
    }

    /**
     * Encodes a game state change caused by placing a new occupant.
     *
     * @param gameState The current game state.
     * @param occupant  The new occupant being placed.
     * @return a StateAction object representing the updated game state and the encoded action.
     */
    public static StateAction withNewOccupant(GameState gameState, Occupant occupant) {
        if (occupant == null) {
            return new StateAction(gameState.withNewOccupant(null), Base32.encodeBits5(0b11111));
        }

        int occupantKind = occupant.kind().ordinal();
        int occupiedZone = Zone.localId(occupant.zoneId());

        int packedInfo = 0b11111 & ((occupantKind << 4) | occupiedZone);

        return new StateAction(gameState.withNewOccupant(occupant), Base32.encodeBits5(packedInfo));
    }

    /**
     * Encodes a game state change caused by removing an occupant.
     *
     * @param gameState The current game state.
     * @param occupant  The occupant being removed.
     * @return a StateAction object representing the updated game state and the encoded action.
     */
    public static StateAction withOccupantRemoved(GameState gameState, Occupant occupant) {
        if (occupant == null) {
            return new StateAction(gameState.withOccupantRemoved(null), Base32.encodeBits5(0b11111));
        }

        List<Occupant> sortedPawns = gameState.board().occupants()
                .stream()
                .filter(o -> o.kind() == Occupant.Kind.PAWN)
                .sorted(Comparator.comparingInt(Occupant::zoneId))
                .toList();

        int occupantIndex = sortedPawns.indexOf(occupant);

        return new StateAction(gameState.withOccupantRemoved(occupant), Base32.encodeBits5(occupantIndex));
    }

    /**
     * Decodes the action code and applies the corresponding action to the game state.
     *
     * @param gameState  The current game state.
     * @param actionCode The Base32 encoded action code.
     * @return a StateAction object representing the updated game state and the action code,
     * or null if the action code or the action is invalid.
     */
    public static StateAction decodeAndApply(GameState gameState, String actionCode) {
        try {
            return decodeAndApplyAndThrowsIfActionImpossible(gameState, actionCode);
        } catch (RuntimeException exception) {
            System.out.println("Given action code invalid or decoded action impossible to execute.");
            return null;
        }
    }

    private static StateAction decodeAndApplyAndThrowsIfActionImpossible(GameState gameState, String actionCode) throws IllegalArgumentException {
        Preconditions.checkArgument(Base32.isValid(actionCode));
        Preconditions.checkArgument(
                (actionCode.length() == 2 && gameState.nextAction() == GameState.Action.PLACE_TILE)
                        ^ (actionCode.length() == 1 && (gameState.nextAction() == GameState.Action.OCCUPY_TILE || gameState.nextAction() == GameState.Action.RETAKE_PAWN)));

        //when the next action is PLACE_TILE
        if (gameState.nextAction() == GameState.Action.PLACE_TILE) {

            List<Pos> sortedFringe = gameState.board().insertionPositions()
                    .stream()
                    .sorted(Comparator.comparing(Pos::x).thenComparing(Pos::y))
                    .toList();

            //case when the placement index is out of bound of the fringe position list
            if ((Base32.decode(actionCode) >>> 2) >= sortedFringe.size()) {
                throw new IndexOutOfBoundsException();
            }

            //to unpack the position and rotation from the action code
            Pos placedTilePos = sortedFringe.get(Base32.decode(actionCode) >>> 2);
            Rotation rotation = Rotation.ALL.get(Base32.decode(actionCode) & 0b11);

            PlacedTile placedTileToPlace = new PlacedTile(gameState.tileToPlace(), gameState.currentPlayer(), rotation, placedTilePos);

            //case when the placement is not allowed on the board (ie : pos not in the fringe or sides not compatibles)
            if (!gameState.board().canAddTile(placedTileToPlace)) {
                throw new IllegalArgumentException();
            }

            return new StateAction(gameState.withPlacedTile(placedTileToPlace), actionCode);
        }
        //when the next action is OCCUPY_TILE
        else if (gameState.nextAction() == GameState.Action.OCCUPY_TILE) {

            //case when the action code means to not occupy any zone
            if (Base32.decode(actionCode) == 0b11111) {
                return new StateAction(gameState.withNewOccupant(null), actionCode);

            } else {
                assert gameState.board().lastPlacedTile() != null;

                //case when the occupant's zone local ID is >= 10 (which is not possible because a tile has max 9 zones)
                if ((Base32.decode(actionCode) & 0b01111) >= 10) {
                    throw new IllegalArgumentException();
                }

                Occupant.Kind occupantKind = Occupant.Kind.values()[Base32.decode(actionCode) >>> 4];
                int occupantZoneId = Integer.parseInt(STR."\{gameState.board().lastPlacedTile().id()}\{Base32.decode(actionCode) & 0b1111}");

                Occupant occupant = new Occupant(occupantKind, occupantZoneId);

                //case when the occupant can't be placed in the last tile
                if (!gameState.lastTilePotentialOccupants().contains(occupant)) {
                    throw new IllegalArgumentException();
                }

                return new StateAction(gameState.withNewOccupant(occupant), actionCode);
            }
        }
        //when the next action is RETAKE_PAWN
        else {

            //case when the action code means to not retake any pawn
            if (Base32.decode(actionCode) == 0b11111) {
                return new StateAction(gameState.withOccupantRemoved(null), actionCode);

            } else {
                List<Occupant> sortedPawns = gameState.board().occupants()
                        .stream()
                        .filter(o -> o.kind() == Occupant.Kind.PAWN)
                        .sorted(Comparator.comparingInt(Occupant::zoneId))
                        .toList();

                //case when the occupant's index is greater than the number of pawn on the board
                if (Base32.decode(actionCode) >= sortedPawns.size()) {
                    throw new IllegalArgumentException();
                }

                Occupant pawnToBeRemoved = sortedPawns.get(Base32.decode(actionCode));
                PlayerColor placerOfTheOccupant = gameState.board().tileWithId(Zone.tileId(pawnToBeRemoved.zoneId())).placer();

                //case when the pawn to be removed is not placed by the same player who wants to retake it
                if (gameState.currentPlayer() != placerOfTheOccupant) {
                    throw new IllegalArgumentException();
                }

                return new StateAction(gameState.withOccupantRemoved(pawnToBeRemoved), actionCode);
            }
        }
    }
}
