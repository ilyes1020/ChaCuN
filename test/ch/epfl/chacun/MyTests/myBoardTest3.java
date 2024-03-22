package ch.epfl.chacun.MyTests;


import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;
import ch.epfl.chacun.utils.ChaCuNUtils;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class myBoardTest3 {
    @Test
    void tileAtBoardEmpty() {
        Board empty = Board.EMPTY;
        int reach = Board.REACH;

        for (int x = -reach; x <= reach; x++) {
            for (int y = -reach; y <= reach ; y++) {
                Pos pos = new Pos(x, y);

                assertNull(empty.tileAt(pos));
            }
        }
    }

    @Test
    void tileAt() {
        Board board = Board.EMPTY;

        Pos pos = new Pos(0, 0);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );
        Board updatedBoard = board.withNewTile(placedTile);

        assertEquals(placedTile, updatedBoard.tileAt(pos));
    }

    @Test
    void tileAtEmptyCell() {
        Board board = Board.EMPTY;

        Pos pos = new Pos(0, 0);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );
        Board updatedBoard = board.withNewTile(placedTile);

        Pos pos2 = new Pos(0, 1);
        assertNull(updatedBoard.tileAt(pos2));
    }

    @Test
    void tileWithIdBoardEmpty() {
        Board board = Board.EMPTY;

        for (int id = 0; id < 95; id++) {
            int finalId = id;
            assertThrows(IllegalArgumentException.class, () -> board.tileWithId(finalId));
        }
    }

    @Test
    void tileWithId() {
        Board board = Board.EMPTY;

        Pos pos = new Pos(0, 0);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );
        Board updatedBoard = board.withNewTile(placedTile);

        assertEquals(placedTile, updatedBoard.tileWithId(123));
    }

    @Test
    void tileWithIdException() {
        Board board = Board.EMPTY;

        Pos pos = new Pos(0, 0);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );
        Board updatedBoard = board.withNewTile(placedTile);

        assertThrows(IllegalArgumentException.class, () -> updatedBoard.tileWithId(125));
    }

    @Test
    void cancelledAnimalsBoardEmpty() {
        Board board = Board.EMPTY;

        assertEquals(Set.of(), board.cancelledAnimals());
    }

    @Test
    void occupantsBoardEmpty() {
        Board empty = Board.EMPTY;

        assertEquals(Set.of(), empty.occupants());
    }

    @Test
    void occupants() {
        Board board = Board.EMPTY;

        Occupant occupant = new Occupant(Occupant.Kind.PAWN, 1234);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237),
                occupant
        );
        board = board.withNewTile(placedTile);

        Occupant occupant1 = new Occupant(Occupant.Kind.PAWN, 1254);
        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                Tile.Kind.NORMAL,
                PlayerColor.RED,
                new Pos(1, 0),
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1256),
                ChaCuNUtils.createForestTileSide(1257),
                occupant1
        );
        board = board.withNewTile(placedTile);

        assertEquals(Set.of(occupant, occupant1), board.occupants());
    }

    @Test
    void occupantsEmptyTiles() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                Tile.Kind.NORMAL,
                PlayerColor.RED,
                new Pos(1, 0),
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1256),
                ChaCuNUtils.createForestTileSide(1257)
        );
        board = board.withNewTile(placedTile);

        assertEquals(Set.of(), board.occupants());
    }

    @Test
    void areasEmptyBoardZoneNotFound() {
        Board empty = Board.EMPTY;

        assertThrows(IllegalArgumentException.class, () -> empty.forestArea(ChaCuNUtils.createForest(0)));
        assertThrows(IllegalArgumentException.class, () -> empty.meadowArea(ChaCuNUtils.createMeadowZone(0)));
        assertThrows(IllegalArgumentException.class, () -> empty.riverArea(ChaCuNUtils.createRiverZone(0, 0)));
        assertThrows(IllegalArgumentException.class, () -> empty.riverSystemArea(ChaCuNUtils.createRiverZone(0)));
    }

    @Test
    void forestAreaOneAreaOneZone() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createMeadowTileSide(1235),
                ChaCuNUtils.createMeadowTileSide(1236),
                ChaCuNUtils.createMeadowTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        Area<Zone.Forest> expectedZone = ChaCuNUtils.createAreaWithNoOccupant(
                1, // we're on a side
                ChaCuNUtils.createForest(1234)
        );

        assertEquals(expectedZone, board.forestArea(ChaCuNUtils.createForest(1234)));
    }

    @Test
    void forestAreaOneAreaTwoZone() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createMeadowTileSide(1236),
                ChaCuNUtils.createMeadowTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        Area<Zone.Forest> expectedZone = ChaCuNUtils.createAreaWithNoOccupant(
                2, // we're two sides now
                ChaCuNUtils.createForest(1234)
        );

        assertEquals(expectedZone, board.forestArea(ChaCuNUtils.createForest(1234)));
    }

    @Test
    void forestAreaOneAreaTwoTiles() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createMeadowTileSide(1235),
                ChaCuNUtils.createMeadowTileSide(1236),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createMeadowTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        placedTile = ChaCuNUtils.createPlacedTile(
                124,
                Tile.Kind.NORMAL,
                PlayerColor.BLUE,
                new Pos(0, 1),
                ChaCuNUtils.createForestTileSide(1248),
                ChaCuNUtils.createMeadowTileSide(1245),
                ChaCuNUtils.createMeadowTileSide(1246),
                ChaCuNUtils.createMeadowTileSide(1247)
        );
        board = board.withNewTile(placedTile);

        Area<Zone.Forest> expectedZone = ChaCuNUtils.createAreaWithNoOccupant(
                0, // we're on a side
                ChaCuNUtils.createForest(1234),
                ChaCuNUtils.createForest(1248)
        );

        assertEquals(expectedZone, board.forestArea(ChaCuNUtils.createForest(1234)));
    }

    @Test
    void forestAreaOneAreaTwoTilesZoneNotFound() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createMeadowTileSide(1235),
                ChaCuNUtils.createMeadowTileSide(1236),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createMeadowTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        placedTile = ChaCuNUtils.createPlacedTile(
                124,
                Tile.Kind.NORMAL,
                PlayerColor.BLUE,
                new Pos(0, 1),
                ChaCuNUtils.createForestTileSide(1248),
                ChaCuNUtils.createMeadowTileSide(1245),
                ChaCuNUtils.createMeadowTileSide(1246),
                ChaCuNUtils.createMeadowTileSide(1247)
        );
        board = board.withNewTile(placedTile);

        Board finalBoard = board;
        assertThrows(IllegalArgumentException.class, () -> finalBoard.forestArea(ChaCuNUtils.createForest(1278)));
    }

    @Test
    void meadowAreaOneAreaOneZone() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createMeadowTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        Area<Zone.Meadow> expectedZone = ChaCuNUtils.createAreaWithNoOccupant(
                1, // we're on a side
                ChaCuNUtils.createMeadowZone(1237)
        );

        assertEquals(expectedZone, board.meadowArea(ChaCuNUtils.createMeadowZone(1237)));
    }

    @Test
    void meadowAreaOneAreaTwoZone() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createMeadowTileSide(1236),
                ChaCuNUtils.createMeadowTileSide(1236)
        );
        board = board.withNewTile(placedTile);

        Area<Zone.Meadow> expectedZone = ChaCuNUtils.createAreaWithNoOccupant(
                2, // we're two sides now
                ChaCuNUtils.createMeadowZone(1236)
        );

        assertEquals(expectedZone, board.meadowArea(ChaCuNUtils.createMeadowZone(1236)));
    }

    @Test
    void meadowAreaOneAreaTwoTiles() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createMeadowTileSide(1234),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        placedTile = ChaCuNUtils.createPlacedTile(
                124,
                Tile.Kind.NORMAL,
                PlayerColor.BLUE,
                new Pos(0, 1),
                ChaCuNUtils.createMeadowTileSide(1248),
                ChaCuNUtils.createForestTileSide(1245),
                ChaCuNUtils.createForestTileSide(1246),
                ChaCuNUtils.createForestTileSide(1247)
        );
        board = board.withNewTile(placedTile);

        Area<Zone.Meadow> expectedZone = ChaCuNUtils.createAreaWithNoOccupant(
                0, // we're on a side
                ChaCuNUtils.createMeadowZone(1234),
                ChaCuNUtils.createMeadowZone(1248)
        );

        assertEquals(expectedZone, board.meadowArea(ChaCuNUtils.createMeadowZone(1234)));
    }

    @Test
    void meadowAreaOneAreaTwoTilesZoneNotFound() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createMeadowTileSide(1234),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        placedTile = ChaCuNUtils.createPlacedTile(
                124,
                Tile.Kind.NORMAL,
                PlayerColor.BLUE,
                new Pos(0, 1),
                ChaCuNUtils.createMeadowTileSide(1248),
                ChaCuNUtils.createForestTileSide(1245),
                ChaCuNUtils.createForestTileSide(1246),
                ChaCuNUtils.createForestTileSide(1247)
        );
        board = board.withNewTile(placedTile);

        Board finalBoard = board;
        assertThrows(IllegalArgumentException.class, () -> finalBoard.meadowArea(ChaCuNUtils.createMeadowZone(1278)));
    }

    @Test
    void riverAreaOneAreaOneZone() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1230),
                        ChaCuNUtils.createRiverZone(1238), // connected to a lake normally
                        ChaCuNUtils.createMeadowZone(1232)
                )
        );
        board = board.withNewTile(placedTile);

        Area<Zone.River> expectedZone = ChaCuNUtils.createAreaWithNoOccupant(
                1, // we're on a side
                ChaCuNUtils.createRiverZone(1238)
        );

        assertEquals(expectedZone, board.riverArea(ChaCuNUtils.createRiverZone(1238)));
    }

    @Test
    void riverAreaOneAreaTwoZone() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1236),
                        ChaCuNUtils.createRiverZone(1238),
                        ChaCuNUtils.createMeadowZone(1230)
                ),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1230),
                        ChaCuNUtils.createRiverZone(1238),
                        ChaCuNUtils.createMeadowZone(1232)
                )
        );
        board = board.withNewTile(placedTile);

        Area<Zone.River> expectedZone = ChaCuNUtils.createAreaWithNoOccupant(
                2, // we're on two sides now
                ChaCuNUtils.createRiverZone(1238)
        );

        assertEquals(expectedZone, board.riverArea(ChaCuNUtils.createRiverZone(1238)));
    }

    @Test
    void riverAreaOneAreaTwoTiles() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1230),
                        ChaCuNUtils.createRiverZone(1238), // connected to a lake normally
                        ChaCuNUtils.createMeadowZone(1232)
                ),
                ChaCuNUtils.createForestTileSide(1236)
        );
        board = board.withNewTile(placedTile);

        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 1),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1250),
                        ChaCuNUtils.createRiverZone(1258), // connected to a lake normally
                        ChaCuNUtils.createMeadowZone(1252)
                ),
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1256)
        );
        board = board.withNewTile(placedTile);

        Area<Zone.River> expectedZone = ChaCuNUtils.createAreaWithNoOccupant(
                0,
                ChaCuNUtils.createRiverZone(1238),
                ChaCuNUtils.createRiverZone(1258)
        );

        assertEquals(expectedZone, board.riverArea(ChaCuNUtils.createRiverZone(1238)));
    }

    @Test
    void riverAreaOneAreaTwoTilesZoneNotFound() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1230),
                        ChaCuNUtils.createRiverZone(1238), // connected to a lake normally
                        ChaCuNUtils.createMeadowZone(1232)
                ),
                ChaCuNUtils.createForestTileSide(1236)
        );
        board = board.withNewTile(placedTile);

        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 1),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1250),
                        ChaCuNUtils.createRiverZone(1258), // connected to a lake normally
                        ChaCuNUtils.createMeadowZone(1252)
                ),
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1256)
        );
        board = board.withNewTile(placedTile);

        Board finalBoard = board;
        assertThrows(IllegalArgumentException.class, () -> finalBoard.riverArea(ChaCuNUtils.createRiverZone(1243)));
    }


    @Test
    void riverSystemAreaOneAreaOneZone() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1230),
                        ChaCuNUtils.createRiverZone(1238, 0, ChaCuNUtils.createLake(1239, 1)),
                        ChaCuNUtils.createMeadowZone(1232)
                )
        );
        board = board.withNewTile(placedTile);

        Area<Zone.Water> expectedZone = ChaCuNUtils.createAreaWithNoOccupant(
                1, // we're on a side
                ChaCuNUtils.createRiverZone(1238, 0, ChaCuNUtils.createLake(1239, 1)),
                ChaCuNUtils.createLake(1239, 1)
        );

        assertEquals(expectedZone, board.riverSystemArea(ChaCuNUtils.createRiverZone(1238, 0, ChaCuNUtils.createLake(1239, 1))));
        assertEquals(expectedZone, board.riverSystemArea(ChaCuNUtils.createLake(1239, 1)));
    }

    @Test
    void riverSystemAreaOneAreaTwoZone() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1236),
                        ChaCuNUtils.createRiverZone(1238),
                        ChaCuNUtils.createMeadowZone(1230)
                ),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1230),
                        ChaCuNUtils.createRiverZone(1238),
                        ChaCuNUtils.createMeadowZone(1232)
                )
        );
        board = board.withNewTile(placedTile);

        Area<Zone.Water> expectedZone = ChaCuNUtils.createAreaWithNoOccupant(
                2, // we're on two sides now
                ChaCuNUtils.createRiverZone(1238)
        );

        assertEquals(expectedZone, board.riverSystemArea(ChaCuNUtils.createRiverZone(1238)));
    }

    @Test
    void riverSystemAreaOneAreaTwoTiles() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1236),
                        ChaCuNUtils.createRiverZone(1238),
                        ChaCuNUtils.createMeadowZone(1230)
                ),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1230),
                        ChaCuNUtils.createRiverZone(1238),
                        ChaCuNUtils.createMeadowZone(1232)
                )
        );
        board = board.withNewTile(placedTile);

        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 1),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1250),
                        ChaCuNUtils.createRiverZone(1258, 0, ChaCuNUtils.createLake(1259, 1)), // connected to a lake normally
                        ChaCuNUtils.createMeadowZone(1252)
                ),
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1256)
        );
        board = board.withNewTile(placedTile);

        Area<Zone.Water> expectedZone = ChaCuNUtils.createAreaWithNoOccupant(
                1,
                ChaCuNUtils.createRiverZone(1238),
                ChaCuNUtils.createRiverZone(1258, 0, ChaCuNUtils.createLake(1259, 1)),
                ChaCuNUtils.createLake(1259, 1)
        );

        assertEquals(expectedZone, board.riverSystemArea(ChaCuNUtils.createRiverZone(1238)));
    }

    @Test
    void riverSystemAreaOneAreaTwoTilesZoneNotFound() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1236),
                        ChaCuNUtils.createRiverZone(1238),
                        ChaCuNUtils.createMeadowZone(1230)
                ),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1230),
                        ChaCuNUtils.createRiverZone(1238),
                        ChaCuNUtils.createMeadowZone(1232)
                )
        );
        board = board.withNewTile(placedTile);

        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 1),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1250),
                        ChaCuNUtils.createRiverZone(1258, 0, ChaCuNUtils.createLake(1259, 1)), // connected to a lake normally
                        ChaCuNUtils.createMeadowZone(1252)
                ),
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1256)
        );
        board = board.withNewTile(placedTile);

        Board finalBoard = board;
        assertThrows(IllegalArgumentException.class, () -> finalBoard.riverSystemArea(ChaCuNUtils.createLake(1278, 1)));
    }

    @Test
    void meadowAreas() {
    }

    @Test
    void riverSystemAreas() {
    }

    @Test
    void adjacentMeadow() {
    }

    @Test
    void occupantCountTwoPawns() {
        Board board = Board.EMPTY;

        Occupant occupant = new Occupant(Occupant.Kind.PAWN, 1234);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237),
                occupant
        );
        board = board.withNewTile(placedTile);

        Occupant occupant1 = new Occupant(Occupant.Kind.PAWN, 1254);
        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                Tile.Kind.NORMAL,
                PlayerColor.RED,
                new Pos(1, 0),
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1256),
                ChaCuNUtils.createForestTileSide(1257),
                occupant1
        );
        board = board.withNewTile(placedTile);

        assertEquals(1, board.occupantCount(PlayerColor.BLUE, Occupant.Kind.PAWN));
        assertEquals(1, board.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
    }

    @Test
    void occupantCountOnePawn2Tiles() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        Occupant occupant1 = new Occupant(Occupant.Kind.PAWN, 1254);
        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                Tile.Kind.NORMAL,
                PlayerColor.RED,
                new Pos(1, 0),
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1256),
                ChaCuNUtils.createForestTileSide(1257),
                occupant1
        );
        board = board.withNewTile(placedTile);

        assertEquals(0, board.occupantCount(PlayerColor.BLUE, Occupant.Kind.PAWN));
        assertEquals(1, board.occupantCount(PlayerColor.RED, Occupant.Kind.PAWN));
    }

    @Test
    void occupantCountBoardEmpty() {
        Board board = Board.EMPTY;

        for (PlayerColor color : PlayerColor.values()) {
            for (Occupant.Kind kind : Occupant.Kind.values()) {
                assertEquals(0, board.occupantCount(color, kind));
            }
        }
    }

    @Test
    void insertionPositionsOneTile() {
        Board board = Board.EMPTY;

        Pos pos = new Pos(0, 0);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        Set<Pos> expected = Set.of(
                new Pos(-1, 0),
                new Pos(1, 0),
                new Pos(0, -1),
                new Pos(0, 1)
        );
        assertEquals(expected, board.insertionPositions());
    }

    @Test
    void insertionPositionsTwoAdjacentTiles() {
        Board board = Board.EMPTY;

        Pos pos = new Pos(0, 0);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        pos = new Pos(1, 0);
        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1256),
                ChaCuNUtils.createForestTileSide(1257)
        );
        board = board.withNewTile(placedTile);

        Set<Pos> expected = Set.of(
                new Pos(-1, 0),
                new Pos(0, -1),
                new Pos(0, 1),
                new Pos(1, -1),
                new Pos(1, 1),
                new Pos(2, 0)
        );
        assertEquals(expected, board.insertionPositions());
    }

    @Test
    void lastPlacedTileEmptyBoard() {
        Board empty = Board.EMPTY;

        assertNull(empty.lastPlacedTile());
    }

    @Test
    void lastPlacedTile() {
        Board board = Board.EMPTY;

        Pos pos = new Pos(0, 0);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        pos = new Pos(1, 0);
        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1256),
                ChaCuNUtils.createForestTileSide(1257)
        );
        board = board.withNewTile(placedTile);

        assertEquals(placedTile, board.lastPlacedTile());
    }

    @Test
    void forestsClosedByLastTileNone() {
        Board board = Board.EMPTY;

        Pos pos = new Pos(0, 0);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                pos,
                // have the same id, so it's the same zone on all sides
                ChaCuNUtils.createForestTileSide(1237),
                ChaCuNUtils.createForestTileSide(1237),
                ChaCuNUtils.createForestTileSide(1237),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        pos = new Pos(1, 0);
        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1257),
                ChaCuNUtils.createForestTileSide(1257),
                ChaCuNUtils.createForestTileSide(1257),
                ChaCuNUtils.createForestTileSide(1257)
        );
        board = board.withNewTile(placedTile);

        assertEquals(Set.of(), board.forestsClosedByLastTile());
    }

    @Test
    void forestsClosedByLastTileOne() {
        Board board = Board.EMPTY;

        Pos pos = new Pos(0, 0);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                pos,
                // have the same id, so it's the same zone on all sides
                ChaCuNUtils.createMeadowTileSide(1233),
                ChaCuNUtils.createMeadowTileSide(1234),
                ChaCuNUtils.createForestTileSide(1237),
                ChaCuNUtils.createMeadowTileSide(1236)
        );
        board = board.withNewTile(placedTile);

        pos = new Pos(0, 1);
        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1252),
                ChaCuNUtils.createMeadowTileSide(1256),
                ChaCuNUtils.createForestTileSide(1253),
                ChaCuNUtils.createForestTileSide(1255)
        );
        board = board.withNewTile(placedTile);

        Set<Area<Zone>> expected = Set.of(
                ChaCuNUtils.createClosedAreaWithNoOccupants(
                        ChaCuNUtils.createForest(1237),
                        ChaCuNUtils.createForest(1252)
                )
        );
        assertEquals(expected, board.forestsClosedByLastTile());
    }


    @Test
    void riversClosedByLastTileNoRivers() {
        Board board = Board.EMPTY;

        Pos pos = new Pos(0, 0);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                pos,
                // have the same id, so it's the same zone on all sides
                ChaCuNUtils.createForestTileSide(1237),
                ChaCuNUtils.createForestTileSide(1237),
                ChaCuNUtils.createForestTileSide(1237),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        pos = new Pos(1, 0);
        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1257),
                ChaCuNUtils.createForestTileSide(1257),
                ChaCuNUtils.createForestTileSide(1257),
                ChaCuNUtils.createForestTileSide(1257)
        );
        board = board.withNewTile(placedTile);

        assertEquals(Set.of(), board.riversClosedByLastTile());
    }

    @Test
    void riversClosedByLastTileNone() {
        Board board = Board.EMPTY;

        Pos pos = new Pos(0, 0);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                pos,
                // have the same id, so it's the same zone on all sides
                ChaCuNUtils.createForestTileSide(1237),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1236),
                        ChaCuNUtils.createRiverZone(1238),
                        ChaCuNUtils.createMeadowZone(1230)
                ),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1230),
                        ChaCuNUtils.createRiverZone(1238),
                        ChaCuNUtils.createMeadowZone(1232)
                ),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        pos = new Pos(1, 0);
        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1257),
                ChaCuNUtils.createForestTileSide(1257),
                ChaCuNUtils.createForestTileSide(1257),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1256),
                        ChaCuNUtils.createRiverZone(1258, 0, ChaCuNUtils.createLake(1259, 0)),
                        ChaCuNUtils.createMeadowZone(1250)
                )
        );
        board = board.withNewTile(placedTile);

        assertEquals(Set.of(), board.riversClosedByLastTile());
    }

    @Test
    void riversClosedByLastTileOne() {
        Board board = Board.EMPTY;

        Pos pos = new Pos(0, 0);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                pos,
                // have the same id, so it's the same zone on all sides
                ChaCuNUtils.createForestTileSide(1237),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1236),
                        ChaCuNUtils.createRiverZone(1238),
                        ChaCuNUtils.createMeadowZone(1230)
                ),
                ChaCuNUtils.createForestTileSide(1237),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        pos = new Pos(1, 0);
        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1257),
                ChaCuNUtils.createForestTileSide(1257),
                ChaCuNUtils.createForestTileSide(1257),
                ChaCuNUtils.createRiverTileSide(
                        ChaCuNUtils.createMeadowZone(1256),
                        ChaCuNUtils.createRiverZone(1258, 0, ChaCuNUtils.createLake(1259, 0)),
                        ChaCuNUtils.createMeadowZone(1250)
                )
        );
        board = board.withNewTile(placedTile);

        Set<Area<Zone>> expected = Set.of(
                ChaCuNUtils.createClosedAreaWithNoOccupants(
                        ChaCuNUtils.createRiverZone(1258, 0, ChaCuNUtils.createLake(1259, 0)),
                        ChaCuNUtils.createRiverZone(1238)
                )
        );

        assertEquals(expected, board.riversClosedByLastTile());
    }

    @Test
    void canAddTileOnEmptyBoard() {
        Board board = Board.EMPTY;

        Pos pos = new Pos(0, 0);
        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                pos,
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );

        assertFalse(board.canAddTile(placedTile));
    }

    @Test
    void canAddTileTwoAdjacentTiles() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        PlacedTile placedTile2 = ChaCuNUtils.createPlacedTile(
                125,
                Tile.Kind.NORMAL,
                PlayerColor.BLUE,
                new Pos(1, 0),
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1256),
                ChaCuNUtils.createForestTileSide(1257)
        );

        assertTrue(board.canAddTile(placedTile2));
    }

    @Test
    void canAddTileTwoIncompatibleTiles() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        PlacedTile placedTile2 = ChaCuNUtils.createPlacedTile(
                125,
                Tile.Kind.NORMAL,
                PlayerColor.BLUE,
                new Pos(1, 0),
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1257),
                ChaCuNUtils.createMeadowTileSide(1256)
        );

        assertFalse(board.canAddTile(placedTile2));
    }

    @Test
    void couldPlaceTileBoardEmpty() {
        Board board = Board.EMPTY;

        Tile tile = ChaCuNUtils.createTile(
                123,
                Tile.Kind.START,
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );

        assertFalse(board.couldPlaceTile(tile));
    }

    @Test
    void couldPlaceTileTwoAdjacentTilesManyPossibilities() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        Tile placedTile2 = ChaCuNUtils.createTile(
                125,
                Tile.Kind.NORMAL,
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1256),
                ChaCuNUtils.createForestTileSide(1257)
        );

        assertTrue(board.couldPlaceTile(placedTile2));
    }

    @Test
    void couldPlaceTileTwoAdjacentTilesOnePossibility() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createMeadowTileSide(1235),
                ChaCuNUtils.createMeadowTileSide(1236),
                ChaCuNUtils.createMeadowTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        Tile placedTile2 = ChaCuNUtils.createTile(
                125,
                Tile.Kind.NORMAL,
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1256),
                ChaCuNUtils.createForestTileSide(1257)
        );

        assertTrue(board.couldPlaceTile(placedTile2));
    }

    @Test
    void couldPlaceTileTwoTilesNoPossibilities() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        Tile tile = ChaCuNUtils.createTile(
                125,
                Tile.Kind.NORMAL,
                ChaCuNUtils.createMeadowTileSide(1254),
                ChaCuNUtils.createMeadowTileSide(1255),
                ChaCuNUtils.createMeadowTileSide(1257),
                ChaCuNUtils.createMeadowTileSide(1256)
        );

        assertFalse(board.couldPlaceTile(tile));
    }

    @Test
    void couldPlaceTile3TilesOnePossibility() {
        Board board = Board.EMPTY;

        PlacedTile placedTile = ChaCuNUtils.createPlacedTile(
                123,
                Tile.Kind.START,
                PlayerColor.BLUE,
                new Pos(0, 0),
                ChaCuNUtils.createForestTileSide(1234),
                ChaCuNUtils.createForestTileSide(1235),
                ChaCuNUtils.createForestTileSide(1236),
                ChaCuNUtils.createForestTileSide(1237)
        );
        board = board.withNewTile(placedTile);

        placedTile = ChaCuNUtils.createPlacedTile(
                125,
                Tile.Kind.NORMAL,
                PlayerColor.RED,
                new Pos(-1, 0),
                ChaCuNUtils.createForestTileSide(1254),
                ChaCuNUtils.createForestTileSide(1255),
                ChaCuNUtils.createForestTileSide(1256),
                ChaCuNUtils.createMeadowTileSide(1257)
        );
        board = board.withNewTile(placedTile);

        Tile tile = ChaCuNUtils.createTile(
                125,
                Tile.Kind.NORMAL,
                ChaCuNUtils.createMeadowTileSide(1254),
                ChaCuNUtils.createMeadowTileSide(1255),
                ChaCuNUtils.createMeadowTileSide(1257),
                ChaCuNUtils.createMeadowTileSide(1256)
        );

        assertTrue(board.couldPlaceTile(tile));
    }
}