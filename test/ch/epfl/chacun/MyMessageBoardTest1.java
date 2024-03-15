package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public class MyMessageBoardTest1 {
    public class theTextMaker implements TextMaker {

        @Override
        public String playerName(PlayerColor playerColor) {
            return "Player: " + playerColor.toString();
        }

        @Override
        public String points(int points) {
            return "Points: " + points;
        }

        @Override
        public String playerClosedForestWithMenhir(PlayerColor player) {
            return "Player: " + player.toString();
        }

        @Override
        public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
            return new StringJoiner(" ")
                    .add(scorers.toString())
                    .add(String.valueOf(points))
                    .add(String.valueOf(mushroomGroupCount))
                    .add(String.valueOf(tileCount))
                    .toString();
        }

        @Override
        public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
            return new StringJoiner(" ")
                    .add(scorers.toString())
                    .add(String.valueOf(points))
                    .add(String.valueOf(fishCount))
                    .add(String.valueOf(tileCount))
                    .toString();
        }

        @Override
        public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
            return new StringJoiner(" ")
                    .add(scorer.toString())
                    .add(String.valueOf(points))
                    .add(String.valueOf(animals))
                    .toString();
        }

        @Override
        public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
            return new StringJoiner(" ")
                    .add(scorer.toString())
                    .add(String.valueOf(points))
                    .add(String.valueOf(lakeCount))
                    .toString();
        }

        @Override
        public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
            return new StringJoiner(" ")
                    .add(scorers.toString())
                    .add(String.valueOf(points))
                    .add(String.valueOf(animals))
                    .toString();
        }

        @Override
        public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
            return new StringJoiner(" ")
                    .add(scorers.toString())
                    .add(String.valueOf(points))
                    .add(String.valueOf(fishCount))
                    .toString();
        }

        @Override
        public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
            return new StringJoiner(" ")
                    .add(scorers.toString())
                    .add(String.valueOf(points))
                    .add(String.valueOf(animals))
                    .toString();
        }

        @Override
        public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
            return new StringJoiner(" ")
                    .add(scorers.toString())
                    .add(String.valueOf(points))
                    .add(String.valueOf(lakeCount))
                    .toString();
        }

        @Override
        public String playersWon(Set<PlayerColor> winners, int points) {
            return new StringJoiner(" ")
                    .add(winners.toString())
                    .add(String.valueOf(points))
                    .toString();
        }

        @Override
        public String clickToOccupy() {
            return "Click to occupy";
        }

        @Override
        public String clickToUnoccupy() {
            return "Click to unoccupy";
        }
    }

    MessageBoard messageBoard = new MessageBoard(new theTextMaker(),new ArrayList<>());

    List<PlayerColor> playerColorList = new ArrayList<>(List.of(PlayerColor.RED,PlayerColor.GREEN,PlayerColor.BLUE,PlayerColor.RED));
    List<PlayerColor> playerColorList2 = new ArrayList<>(List.of(PlayerColor.BLUE,PlayerColor.GREEN,PlayerColor.PURPLE));
    List<PlayerColor> playerColorList3 = new ArrayList<>(List.of(PlayerColor.YELLOW));
    List<PlayerColor> playerColorList4 = new ArrayList<>(List.of(PlayerColor.BLUE,PlayerColor.PURPLE));
    List<PlayerColor> playerColorList5 = new ArrayList<>(List.of(PlayerColor.BLUE));
    List<PlayerColor> playerColorList6 = new ArrayList<>(List.of(PlayerColor.RED,PlayerColor.PURPLE));
    List<PlayerColor> playerColorList7 = new ArrayList<>(List.of(PlayerColor.BLUE,PlayerColor.YELLOW));
    List<PlayerColor> playerColorList8 = new ArrayList<>(List.of(PlayerColor.RED,PlayerColor.GREEN));

    Zone.Forest forest1 = new Zone.Forest(110, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Forest forest2 = new Zone.Forest(331, Zone.Forest.Kind.WITH_MUSHROOMS);
    Zone.Forest forest3 = new Zone.Forest(442, Zone.Forest.Kind.PLAIN);
    Zone.Forest forest4 = new Zone.Forest(552, Zone.Forest.Kind.PLAIN);
    Zone.Forest forest5 = new Zone.Forest(781, Zone.Forest.Kind.WITH_MUSHROOMS);
    Zone.Forest forest6 = new Zone.Forest(105, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Forest forest7 = new Zone.Forest(135,Zone.Forest.Kind.PLAIN);
    Zone.Forest forest8 = new Zone.Forest(198, Zone.Forest.Kind.PLAIN);
    Set<Zone.Forest> setForest = new HashSet<>(Set.of(forest1, forest2, forest3));
    Set<Zone.Forest> setForest2 = new HashSet<>(Set.of(forest4,forest5));
    Set<Zone.Forest> setForest3 = new HashSet<>(Set.of(forest6,forest7));
    Set<Zone.Forest> setForest4 = new HashSet<>(Set.of(forest8));


    Animal animal1 = new Animal(1130, Animal.Kind.AUROCHS);
    Animal animal2 = new Animal(1131, Animal.Kind.TIGER);
    Animal animal3 = new Animal(2240, Animal.Kind.MAMMOTH);
    Animal animal4 = new Animal(2241, Animal.Kind.DEER);
    Animal animal5 = new Animal(3380, Animal.Kind.DEER);
    Animal animal6 = new Animal(3900, Animal.Kind.DEER);

    Animal animal7 = new Animal(1520, Animal.Kind.MAMMOTH);
    Animal animal8 = new Animal(1521, Animal.Kind.MAMMOTH);
    Animal animal9 = new Animal(1600, Animal.Kind.AUROCHS);
    Animal animal10 = new Animal(1601, Animal.Kind.TIGER);





    List<Animal> animalList = new ArrayList<>(List.of(animal1,animal2));
    List<Animal> animalList2 = new ArrayList<>(List.of(animal3,animal4));
    List<Animal> animalList3 = new ArrayList<>(List.of(animal5));
    List<Animal> animalList4 = new ArrayList<>(List.of(animal6));
    List<Animal> animalList5 = new ArrayList<>(List.of(animal7,animal8));
    List<Animal> animalList6 = new ArrayList<>(List.of(animal9,animal10));
    List<Animal> animalEmptyList = new ArrayList<>(List.of());
    Set<Animal> cancelledAnimalSet = new HashSet<>(List.of(animal4));

    Zone.Meadow meadow1 = new Zone.Meadow(113,animalList,null);
    Zone.Meadow meadow2 = new Zone.Meadow(224,animalList2, Zone.SpecialPower.HUNTING_TRAP);
    Zone.Meadow meadow3 = new Zone.Meadow(338,animalList3, Zone.SpecialPower.PIT_TRAP);
    Zone.Meadow meadow4 = new Zone.Meadow(390,animalList4, null);
    Zone.Meadow meadow5 = new Zone.Meadow(152,animalList5, null);
    Zone.Meadow meadow6 = new Zone.Meadow(155, Collections.emptyList(), Zone.SpecialPower.SHAMAN);
    Zone.Meadow meadow7 = new Zone.Meadow(160,animalList6, null);
    Zone.Meadow meadow8 = new Zone.Meadow(165,animalEmptyList, null);
    Zone.Meadow emptyMeadow = new Zone.Meadow(177,Collections.emptyList(), null);
    Set<Zone.Meadow> meadowSet = new HashSet<>(List.of(meadow1,meadow2));
    Set<Zone.Meadow> meadowSet2 = new HashSet<>(List.of(meadow3,meadow4));
    Set<Zone.Meadow> meadowSet3 = new HashSet<>(List.of(meadow5,meadow6));
    Set<Zone.Meadow> meadowSet4 = new HashSet<>(List.of(meadow7,meadow8));
    Set<Zone.Meadow> meadowSet5 = new HashSet<>(List.of(emptyMeadow));

    Zone.Lake lake1 = new Zone.Lake(118,2, Zone.SpecialPower.RAFT);
    Zone.Lake lake2 = new Zone.Lake(138,4,null);
    Zone.Lake lake3 = new Zone.Lake(289,1,null);
    Zone.River river1 = new Zone.River(115,2,lake1);
    Zone.River river2 = new Zone.River(126,3,null);
    Zone.River river3 = new Zone.River(137,1, lake2);
    Zone.River river4 = new Zone.River(221,0, null);
    Zone.River river5 = new Zone.River(288,2, lake3);
    Zone.River river6 = new Zone.River(312,4, null);

    Set<Zone.Water> riverSystem = new HashSet<>(List.of(lake1,river1,river2));
    Set<Zone.Water> riverSystem2 = new HashSet<>(List.of(river3,river4,lake2));
    Set<Zone.Water> riverSystem3 = new HashSet<>(List.of(river5,lake3));
    Set<Zone.Water> riverSystem4 = new HashSet<>(List.of(river6));
    Set<Zone.River> riverSet = new HashSet<>(List.of(river1,river2));
    Set<Zone.River> riverSet2 = new HashSet<>(List.of(river3,river4));
    Set<Zone.River> riverSet3 = new HashSet<>(List.of(river5));
    Set<Zone.River> riverSet4 = new HashSet<>(List.of(river6));
    Area<Zone.River> riverArea = new Area<>(riverSet,playerColorList2,3);
    Area<Zone.River> riverArea2 = new Area<>(riverSet2,playerColorList8,1);
    Area<Zone.River> riverArea3 = new Area<>(riverSet3,playerColorList5,2);
    Area<Zone.River> riverArea4 = new Area<>(riverSet3,playerColorList6,2);




    Area<Zone.Water> riverSystemArea = new Area<>(riverSystem,playerColorList,4);
    Area<Zone.Water> riverSystemArea2 = new Area<>(riverSystem2,playerColorList,4);


    Area<Zone.Meadow> meadowArea = new Area<>(meadowSet,playerColorList,1);
    Area<Zone.Meadow> meadowArea2 = new Area<>(meadowSet2,playerColorList5,1);
    Area<Zone.Meadow> meadowArea3 = new Area<>(meadowSet3,playerColorList6,1);
    Area<Zone.Meadow> meadowArea4 = new Area<>(meadowSet4,playerColorList7,1);
    Area<Zone.Meadow> meadowArea5 = new Area<>(meadowSet5,playerColorList7,4);

    Set<Area<Zone.Meadow>> meadowAreaSet = new HashSet<>(List.of(meadowArea,meadowArea2));
    Set<Area<Zone.Meadow>> meadowAreaSet2 = new HashSet<>(List.of(meadowArea3,meadowArea4));
    ZonePartition<Zone.Meadow> meadowZonePartition = new ZonePartition<>(meadowAreaSet);
    ZonePartition<Zone.Meadow> meadowZonePartition2 = new ZonePartition<>(meadowAreaSet2);


    Area<Zone.Forest> forestArea= new Area<>(setForest,playerColorList,2);
    Area<Zone.Forest> forestArea2= new Area<>(setForest2,playerColorList2,2);
    Area<Zone.Forest> forestArea3= new Area<>(setForest3,playerColorList3,3);
    Area<Zone.Forest> forestArea4= new Area<>(setForest4,playerColorList4,1);
    Set<Area<Zone.Forest>> forestAreaSet = new HashSet<>(List.of(forestArea,forestArea2));
    Set<Area<Zone.Forest>> forestAreaSet2 = new HashSet<>(List.of(forestArea3,forestArea4));
    ZonePartition<Zone.Forest> forestZonePartition = new ZonePartition<>(forestAreaSet);
    ZonePartition<Zone.Forest> forestZonePartition2 = new ZonePartition<>(forestAreaSet2);


    @Test
    void points() {
        messageBoard = messageBoard.withScoredRiverSystem(riverSystemArea);
        messageBoard = messageBoard.withScoredRiver(riverArea);
        messageBoard = messageBoard.withScoredHuntingTrap(PlayerColor.RED,meadowArea);
        messageBoard = messageBoard.withScoredForest(forestArea);
        Map<PlayerColor, Integer> points = new HashMap<>();
        points.put(PlayerColor.RED,22);
        points.put(PlayerColor.BLUE,9);
        points.put(PlayerColor.GREEN,9);
        points.put(PlayerColor.PURPLE,9);
        assertEquals(4,messageBoard.messages().size());

        assertEquals(points,messageBoard.points());
    }

    @Test
    void withScoredForest() {
        Set<PlayerColor> set = new HashSet<>();
        set.add(PlayerColor.RED);

        assertEquals(1,messageBoard.withScoredForest(forestArea).messages().size());
        assertEquals(new theTextMaker().playersScoredForest(set,9,1,3),messageBoard.withScoredForest(forestArea).messages().getFirst().text());
    }

    @Test
    void withClosedForestWithMenhir() {
        assertEquals(1,messageBoard.withClosedForestWithMenhir(PlayerColor.BLUE,forestArea).messages().size());
        assertEquals(new theTextMaker().playerClosedForestWithMenhir(PlayerColor.BLUE),messageBoard.withClosedForestWithMenhir(PlayerColor.BLUE,forestArea).messages().getFirst().text());
    }

    @Test
    void withScoredRiver() {
        Set<PlayerColor> set = new HashSet<>();
        set.add(PlayerColor.BLUE);
        set.add(PlayerColor.GREEN);
        set.add(PlayerColor.PURPLE);
        assertEquals(1,messageBoard.withScoredRiver(riverArea).messages().size());
        assertEquals(new theTextMaker().playersScoredRiver(set,9,7,2),messageBoard.withScoredRiver(riverArea).messages().getFirst().text());
    }

    @Test
    void withScoredHuntingTrap() {
        Map<Animal.Kind,Integer> map= new HashMap<>(); //att
        map.put(Animal.Kind.MAMMOTH,1);
        map.put(Animal.Kind.DEER,1); //att.
        map.put(Animal.Kind.TIGER,1);
        map.put(Animal.Kind.AUROCHS,1);
        assertEquals(new theTextMaker().playerScoredHuntingTrap(PlayerColor.RED,6,map), messageBoard.withScoredHuntingTrap(PlayerColor.RED,meadowArea).messages().getFirst().text());
        messageBoard = messageBoard.withScoredHuntingTrap(PlayerColor.RED,meadowArea);
        assertEquals(2,messageBoard.withScoredHuntingTrap(PlayerColor.RED,meadowArea4).messages().size());
    }

    @Test
    void withScoredLogboat() {
        assertEquals(new theTextMaker().playerScoredLogboat(PlayerColor.YELLOW,2,1),messageBoard.withScoredLogboat(PlayerColor.YELLOW,riverSystemArea2).messages().getFirst().text());
    }

    @Test
    void withScoredMeadow() {
        Set<PlayerColor> set = new HashSet<>();
        set.add(PlayerColor.RED);
        TreeMap<Animal.Kind,Integer> map= new TreeMap<>();
        map.put(Animal.Kind.AUROCHS,1);
        map.put(Animal.Kind.TIGER,1);
        map.put(Animal.Kind.MAMMOTH,1);

        assertEquals(new theTextMaker().playersScoredMeadow(set,5,map), messageBoard.withScoredMeadow(meadowArea,cancelledAnimalSet).messages().getFirst().text());
    }

    @Test
    void withScoredRiverSystem() {
        Set<PlayerColor> set = new HashSet<>();
        set.add(PlayerColor.RED);

        assertEquals(new theTextMaker().playersScoredRiverSystem(set,7,7),messageBoard.withScoredRiverSystem(riverSystemArea).messages().getFirst().text());
    }

    @Test
    void withScoredPitTrap() {
        Set<PlayerColor> set = new HashSet<>();
        set.add(PlayerColor.RED);

        Map<Animal.Kind,Integer> map= new TreeMap<>(); //att
        map.put(Animal.Kind.MAMMOTH,1);
        map.put(Animal.Kind.AUROCHS,1);
        map.put(Animal.Kind.TIGER,1);

        assertEquals(new theTextMaker().playersScoredPitTrap(set,5,map), messageBoard.withScoredPitTrap(meadowArea,cancelledAnimalSet).messages().getFirst().text());
    }

    @Test
    void withScoredRaft(){
        Set<PlayerColor> set = new HashSet<>();
        set.add(PlayerColor.RED);
        assertEquals(new theTextMaker().playersScoredRaft(set,1,1),messageBoard.withScoredRaft(riverSystemArea).messages().getFirst().text());
    }

    @Test
    void withWinners(){
        Set<PlayerColor> set = new HashSet<>();
        set.add(PlayerColor.RED);
        set.add(PlayerColor.GREEN);
        set.add(PlayerColor.BLUE);
        assertEquals(new theTextMaker().playersWon(set,9),messageBoard.withWinners(new HashSet<>(playerColorList),9).messages().getFirst().text());
    }
}
