package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public class MyAreaTest2 {
    List<PlayerColor> playerColorList = new ArrayList<>(List.of(PlayerColor.RED,PlayerColor.GREEN,PlayerColor.BLUE,PlayerColor.RED));
    List<PlayerColor> playerColorList2 = new ArrayList<>(List.of(PlayerColor.BLUE,PlayerColor.GREEN,PlayerColor.PURPLE));

    Zone.Forest forest1 = new Zone.Forest(110, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Forest forest2 = new Zone.Forest(331, Zone.Forest.Kind.WITH_MUSHROOMS);
    Zone.Forest forest3 = new Zone.Forest(442, Zone.Forest.Kind.PLAIN);
    Set<Zone.Forest> setForest = new HashSet<>(Set.of(forest1, forest2, forest3));
    Set<Zone.Forest> setForest2 = new HashSet<>(Set.of(forest3));

    Animal animal1 = new Animal(1130, Animal.Kind.AUROCHS);
    Animal animal2 = new Animal(1131, Animal.Kind.TIGER);
    Animal animal3 = new Animal(2240, Animal.Kind.MAMMOTH);
    Animal animal4 = new Animal(2241, Animal.Kind.DEER);
    List<Animal> animalList = new ArrayList<>(List.of(animal1,animal2));
    List<Animal> animalList2 = new ArrayList<>(List.of(animal3,animal4));
    Set<Animal> cancelledAnimalSet = new HashSet<>(List.of(animal2,animal4));

    Zone.Meadow meadow1 = new Zone.Meadow(113,animalList,null);
    Zone.Meadow meadow2 = new Zone.Meadow(224,animalList2, Zone.SpecialPower.HUNTING_TRAP);
    Set<Zone.Meadow> meadowSet = new HashSet<>(List.of(meadow1,meadow2));

    Zone.Lake lake1 = new Zone.Lake(118,5, Zone.SpecialPower.RAFT);
    Zone.Lake lake2 = new Zone.Lake(149,4,null);
    Zone.River river1 = new Zone.River(115,2,null);
    Zone.River river2 = new Zone.River(126,3,lake1);
    Zone.River river3 = new Zone.River(137,1, lake1);
    Set<Zone.Water> riverSystem = new HashSet<>(List.of(lake1,river1,river2,river3,lake2));
    Set<Zone.River> riverSet = new HashSet<>(List.of(river1,river2,river3));
    Area<Zone.River> riverArea = new Area<>(riverSet,List.of(),0);

    Area<Zone.Water> riverSystemArea = new Area<>(riverSystem,playerColorList,0);

    Area<Zone.Meadow> meadowArea = new Area<>(meadowSet,playerColorList,1);

    Area<Zone.Forest> forestArea= new Area<>(setForest,playerColorList2,2);
    Area<Zone.Forest> forestArea2= new Area<>(setForest2,playerColorList2,2);


    @Test
    void hasMenhir() {
        assertTrue(Area.hasMenhir(forestArea));
        assertFalse(Area.hasMenhir(forestArea2));
    }

    @Test
    void mushroomGroupCount() {
        assertEquals(1,Area.mushroomGroupCount(forestArea));
        assertEquals(0,Area.mushroomGroupCount(forestArea2));
    }

    @Test
    void animals() {
        Set<Animal> expectedAnimals = new HashSet<>(List.of(animal1,animal3));
        Set<Animal> returnedAnimals = new HashSet<>(Area.animals(meadowArea, cancelledAnimalSet));
        assertEquals(expectedAnimals,returnedAnimals);

    }

    @Test
    void riverFishCount() {
        assertEquals(11,Area.riverFishCount(riverArea));
    }

    @Test
    void riverSystemFishCount() {
        assertEquals(15,Area.riverSystemFishCount(riverSystemArea));
    }

    @Test
    void lakeCount() {
        assertEquals(2,Area.lakeCount(riverSystemArea));
    }

    @Test
    void isClosed() {
        assertTrue(riverSystemArea.isClosed());
        assertFalse(meadowArea.isClosed());
        assertTrue(riverArea.isClosed());
        assertFalse(forestArea.isClosed());
    }

    @Test
    void isOccupied() {
        assertFalse(riverArea.isOccupied());
        assertTrue(riverSystemArea.isOccupied());
        assertTrue(forestArea.isOccupied());
        assertTrue(meadowArea.isOccupied());
    }

    @Test
    void majorityOccupants() {
        assertEquals(new HashSet<>(List.of(PlayerColor.RED)),meadowArea.majorityOccupants());
        assertEquals(new HashSet<>(List.of(PlayerColor.BLUE,PlayerColor.GREEN,PlayerColor.PURPLE)),forestArea.majorityOccupants());
        assertEquals(new HashSet<>(List.of(PlayerColor.BLUE,PlayerColor.GREEN,PlayerColor.PURPLE)),forestArea2.majorityOccupants());
    }

    @Test
    void connectTo() {
        Zone.Forest forest1 = new Zone.Forest(110, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Forest forest2 = new Zone.Forest(331, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest3 = new Zone.Forest(442, Zone.Forest.Kind.PLAIN);

        Set<Zone.Forest> setForest = new HashSet<>(Set.of(forest1, forest2));
        Set<Zone.Forest> setForest2 = new HashSet<>(Set.of(forest3));
        Set<Zone.Forest> setForest3 = new HashSet<>(Set.of(forest1, forest2,forest3));

        List<PlayerColor> playerColorList = new ArrayList<>(List.of(PlayerColor.RED, PlayerColor.GREEN, PlayerColor.BLUE, PlayerColor.RED));
        List<PlayerColor> playerColorList2 = new ArrayList<>(List.of(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.PURPLE));
        List<PlayerColor> playerColorList3 = new ArrayList<>(List.of(PlayerColor.RED, PlayerColor.GREEN, PlayerColor.BLUE, PlayerColor.RED,PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.PURPLE));

        Area<Zone.Forest> forestArea = new Area<>(setForest, playerColorList, 2);
        Area<Zone.Forest> forestArea2 = new Area<>(setForest2, playerColorList2, 2);
        Area<Zone.Forest> forestArea3 = new Area<>(setForest3, playerColorList3, 2);



        assertEquals(forestArea3,forestArea.connectTo(forestArea2));

    }

    @Test
    void withInitialOccupant() {
        assertEquals(new Area<Zone.River>(riverSet,List.of(PlayerColor.YELLOW),0),riverArea.withInitialOccupant(PlayerColor.YELLOW));
    }

    @Test
    void withoutOccupant() {
        assertEquals(new Area<Zone.Forest>(setForest,new ArrayList<PlayerColor>(List.of(PlayerColor.BLUE,PlayerColor.GREEN)),2),forestArea.withoutOccupant(PlayerColor.PURPLE));
        assertEquals(new Area<Zone.Meadow>(meadowSet, new ArrayList<PlayerColor>(List.of(PlayerColor.RED,PlayerColor.BLUE,PlayerColor.GREEN)),1),meadowArea.withoutOccupant(PlayerColor.RED));
    }

    @Test
    void withoutOccupants() {
        assertEquals(new Area<Zone.Forest>(setForest, Collections.emptyList(),2),forestArea.withoutOccupants());
        assertEquals(new Area<Zone.Meadow>(meadowSet,List.of(),1),meadowArea.withoutOccupants());
        assertEquals(new Area<Zone.Water>(riverSystem,List.of(),0),riverSystemArea.withoutOccupants());
    }

    @Test
    void tileIds() {
        assertEquals(new HashSet<Integer>(List.of(11,33,44)),forestArea.tileIds());
        assertEquals(new HashSet<Integer>(List.of(11,22)),meadowArea.tileIds());
        assertEquals(new HashSet<Integer>(List.of(11,12,13,14)),riverSystemArea.tileIds());
    }

    @Test
    void zoneWithSpecialPower() {
        assertEquals(meadow2,meadowArea.zoneWithSpecialPower(Zone.SpecialPower.HUNTING_TRAP));
        assertEquals(lake1,riverSystemArea.zoneWithSpecialPower(Zone.SpecialPower.RAFT));
    }

    @Test
    void zones() {
        assertEquals(new HashSet<Zone.Forest>(List.of(forest1,forest2,forest3)),forestArea.zones());
        assertEquals(new HashSet<Zone.Meadow>(List.of(meadow1,meadow2)),meadowArea.zones());
    }

    @Test
    void occupants() {
        assertEquals(new ArrayList<>(List.of(PlayerColor.RED,PlayerColor.RED,PlayerColor.BLUE,PlayerColor.GREEN)),meadowArea.occupants());
        assertEquals(playerColorList2,forestArea.occupants());
        assertEquals(Collections.emptyList(),riverArea.occupants());
    }

    @Test
    void openConnections() {
        assertEquals(0,riverSystemArea.openConnections());
        assertEquals(1,meadowArea.openConnections());
        assertEquals(2,forestArea.openConnections());
    }
}

