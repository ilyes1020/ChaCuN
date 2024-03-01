package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public class MyPlacedTileSideTest {

    Zone.Meadow meadowZone1 = new Zone.Meadow(560, new ArrayList<>(), null);
    Zone.Forest forestZone = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Meadow meadowZone2 = new Zone.Meadow(562, new ArrayList<>(), null);
    Zone.Lake lakeZone = new Zone.Lake(568,1, Zone.SpecialPower.LOGBOAT);
    Zone.River riverZone = new Zone.River(563,0, lakeZone);

    TileSide.Forest forestSide = new TileSide.Forest(forestZone);
    TileSide.Meadow meadowSide = new TileSide.Meadow(meadowZone1);
    TileSide.River riverSide = new TileSide.River(meadowZone2, riverZone, meadowZone1);

    Tile tile = new Tile(56, Tile.Kind.START, meadowSide, forestSide, forestSide, riverSide);
    PlacedTile placedTileWithRotationNone = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));
    PlacedTile placedTileWithRotationRight = new PlacedTile(tile, PlayerColor.RED, Rotation.RIGHT, new Pos(0, 0));
    PlacedTile placedTileWithRotationHalfTurn = new PlacedTile(tile, PlayerColor.RED, Rotation.HALF_TURN, new Pos(0, 0));
    PlacedTile placedTileWithRotationLeft = new PlacedTile(tile, PlayerColor.RED, Rotation.LEFT, new Pos(0, 0));
    @Test
    void constructionFailsWithNullParameter() {
        assertThrows(NullPointerException.class, () -> new PlacedTile(null, PlayerColor.RED, Rotation.NONE, new Pos(0, 0)));
        assertThrows(NullPointerException.class, () -> new PlacedTile(tile, PlayerColor.RED, null, new Pos(0, 0)));
        assertThrows(NullPointerException.class, () -> new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, null));
    }

    @Test
    void idWorksOnNonTrivialPlacedTile() {
        assertEquals(56, placedTileWithRotationNone.id());
    }

    @Test
    void kindWorksOnNonTrivialPlacedTile() {
        assertEquals(Tile.Kind.START, placedTileWithRotationNone.kind());
    }

    @Test
    void sideWorksOnNonTrivialPlacedTile() {
        //test with rotation NONE
        assertEquals(tile.n(), placedTileWithRotationNone.side(Direction.N));
        assertEquals(tile.e(), placedTileWithRotationNone.side(Direction.E));
        assertEquals(tile.s(), placedTileWithRotationNone.side(Direction.S));
        assertEquals(tile.w(), placedTileWithRotationNone.side(Direction.W));

        //test with rotation RIGHT
        assertEquals(tile.w(), placedTileWithRotationRight.side(Direction.N));
        assertEquals(tile.n(), placedTileWithRotationRight.side(Direction.E));
        assertEquals(tile.e(), placedTileWithRotationRight.side(Direction.S));
        assertEquals(tile.s(), placedTileWithRotationRight.side(Direction.W));

        //test with rotation HALF_TURN
        assertEquals(tile.s(), placedTileWithRotationHalfTurn.side(Direction.N));
        assertEquals(tile.w(), placedTileWithRotationHalfTurn.side(Direction.E));
        assertEquals(tile.n(), placedTileWithRotationHalfTurn.side(Direction.S));
        assertEquals(tile.e(), placedTileWithRotationHalfTurn.side(Direction.W));

        //test with rotation LEFT
        assertEquals(tile.e(), placedTileWithRotationLeft.side(Direction.N));
        assertEquals(tile.s(), placedTileWithRotationLeft.side(Direction.E));
        assertEquals(tile.w(), placedTileWithRotationLeft.side(Direction.S));
        assertEquals(tile.n(), placedTileWithRotationLeft.side(Direction.W));
    }

    @Test
    void zoneWithIdWorksOnNonTrivialPlacedTile() {
        assertEquals(meadowZone1, placedTileWithRotationNone.zoneWithId(560));
        assertEquals(forestZone, placedTileWithRotationNone.zoneWithId(561));
        assertEquals(meadowZone2, placedTileWithRotationNone.zoneWithId(562));
        assertEquals(lakeZone, placedTileWithRotationNone.zoneWithId(568));
        assertEquals(riverZone, placedTileWithRotationNone.zoneWithId(563));
    }

    @Test
    void zoneWithIdFailsWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> placedTileWithRotationNone.zoneWithId(0));
        assertThrows(IllegalArgumentException.class, () -> placedTileWithRotationNone.zoneWithId(-1));
    }

    @Test
    void specialPowerZoneWorksOnNonTrivialPlacedTile() {
        assertEquals(placedTileWithRotationNone.zoneWithId(568), placedTileWithRotationNone.specialPowerZone());
    }

//    @Test
//    void specialPowerZoneReturnsNoneOnNonTrivialPlacedTileWithoutSpecialPower() {
//        assertEquals(null, placedTileWithRotationNone.specialPowerZone());
//    }

    @Test
    void forestZoneWorksOnNonTrivialPlacedTile() {
        assertTrue(placedTileWithRotationNone.forestZones().contains(forestZone));
    }

    @Test
    void meadowZoneWorksOnNonTrivialPlacedTile() {
        assertTrue(placedTileWithRotationNone.meadowZones().contains(meadowZone1));
        assertTrue(placedTileWithRotationNone.meadowZones().contains(meadowZone2));
    }

    @Test
    void riverZoneWorksOnNonTrivialPlacedTile() {
        assertTrue(placedTileWithRotationNone.riverZones().contains(riverZone));
    }

    @Test
    void potentialOccupantsWorksOnNonTrivialPlacedTile() {
        assertEquals(5, placedTileWithRotationNone.potentialOccupants().size());
        System.out.println(placedTileWithRotationNone.potentialOccupants());

    }

    @Test
    void potentialOccupantsReturnsEmptySetIfPlacerIsNull() {
        PlacedTile placedTile = new PlacedTile(tile, null, Rotation.NONE, new Pos(0, 0));
        assertEquals(0, placedTile.potentialOccupants().size());
    }

    @Test
    void withOccupantWorksOnNonTrivialPlacedTile() {
        Occupant occupant = new Occupant(Occupant.Kind.PAWN, 560);
        PlacedTile placedTile = placedTileWithRotationNone.withOccupant(occupant);
    }

    @Test
    void withOccupantFailsIfTileIsAlreadyOccupied() {
        Occupant occupant = new Occupant(Occupant.Kind.PAWN, 560);
        Occupant occupant2 = new Occupant(Occupant.Kind.PAWN, 562);
        PlacedTile placedTile = placedTileWithRotationNone.withOccupant(occupant);
        assertThrows(IllegalArgumentException.class, () -> placedTile.withOccupant(occupant2));
    }

    @Test
    void withNoOccupantWorksOnNonTrivialPlacedTile() {
        Occupant occupant = new Occupant(Occupant.Kind.PAWN, 560);
        PlacedTile placedTile = placedTileWithRotationNone.withOccupant(occupant);
        assertEquals(null, placedTile.withNoOccupant().occupant());
    }

    @Test
    void idOfZoneOccupiedByWorksOnNonTrivialPlacedTile() {
        Occupant occupant = new Occupant(Occupant.Kind.PAWN, 560);
        PlacedTile placedTile = placedTileWithRotationNone.withOccupant(occupant);
        assertEquals(560, placedTile.idOfZoneOccupiedBy(occupant.kind()));
    }

    @Test
    void idOfZoneOccupiedByReturnsMinusOneWithWrongArguments() {

        Occupant occupant = new Occupant(Occupant.Kind.PAWN, 560);

        PlacedTile placedTile = placedTileWithRotationNone.withOccupant(occupant);
        assertEquals(-1, placedTile.idOfZoneOccupiedBy(Occupant.Kind.HUT));

        PlacedTile placedTileWithNoOccupant = placedTile.withNoOccupant();
        assertEquals(-1, placedTileWithNoOccupant.idOfZoneOccupiedBy(Occupant.Kind.PAWN));
    }
}
