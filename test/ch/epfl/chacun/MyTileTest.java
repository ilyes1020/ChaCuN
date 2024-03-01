package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */

public class MyTileTest {

    Zone.Meadow meadowZone1 = new Zone.Meadow(560, new ArrayList<>(), null);
    Zone.Forest forestZone = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Meadow meadowZone2 = new Zone.Meadow(562, new ArrayList<>(), null);
    Zone.Lake lakeZone = new Zone.Lake(568,1,null);
    Zone.River riverZone = new Zone.River(563,0, lakeZone);

    TileSide.Forest forestSide = new TileSide.Forest(forestZone);
    TileSide.Meadow meadowSide = new TileSide.Meadow(meadowZone1);
    TileSide.River riverSide = new TileSide.River(meadowZone2, riverZone, meadowZone1);

    Tile tile = new Tile(56, Tile.Kind.START, meadowSide, forestSide, forestSide, riverSide);

    @Test
    public void sidesWorkOnNonTrivialTileSides() {

        List<TileSide> sides = tile.sides();

        assertEquals(4, sides.size());
        assertEquals(meadowSide, sides.get(0));
        assertEquals(forestSide, sides.get(1));
        assertEquals(forestSide, sides.get(2));
        assertEquals(riverSide, sides.get(3));
    }

    @Test
    public void sideZonesWorkOnNonTrivialTileSides() {

        Set<Zone> sideZones = tile.sideZones();

//        System.out.println(sideZones);
//        System.out.println(tile.w().zones());

        assertEquals(4, sideZones.size());
        assertTrue(sideZones.containsAll(tile.n().zones()));
        assertTrue(sideZones.containsAll(tile.e().zones()));
        assertTrue(sideZones.containsAll(tile.s().zones()));
        assertTrue(sideZones.containsAll(tile.w().zones()));
        assertFalse(sideZones.contains(lakeZone));
    }

    @Test
    public void zonesWorkOnNonTrivialTileSides() {

        Set<Zone> zones = tile.zones();

        assertEquals(5, zones.size());
        assertTrue(zones.containsAll(tile.sideZones()));
        assertTrue(zones.contains(lakeZone));
    }
}