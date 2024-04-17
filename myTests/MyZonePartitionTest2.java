import ch.epfl.chacun.Area;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.Zone;
import ch.epfl.chacun.ZonePartition;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public class MyZonePartitionTest2 {

    Zone.Forest zone1 = new Zone.Forest(1111, Zone.Forest.Kind.WITH_MUSHROOMS);
    Zone.Forest zone2 = new Zone.Forest(2222, Zone.Forest.Kind.PLAIN);
    Zone.Forest zone3 = new Zone.Forest(3333, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Forest zone4 = new Zone.Forest(4444, Zone.Forest.Kind.WITH_MUSHROOMS);
    Zone.Forest zone5 = new Zone.Forest(5555, Zone.Forest.Kind.PLAIN);
    Zone.Forest zone6 = new Zone.Forest(6666, Zone.Forest.Kind.WITH_MENHIR);



    Set<Zone.Forest> listForest1=new HashSet<>(List.of(zone1,zone2));
    Set<Zone.Forest> listForest2=new HashSet<>(List.of(zone3,zone4));
    Set<Zone.Forest> listForest3=new HashSet<>(List.of(zone5));
    Set<Zone.Forest> listForest4=new HashSet<>(List.of(zone1,zone2,zone3,zone4));



    List<PlayerColor> playerColorList = new ArrayList<>(List.of(PlayerColor.RED,PlayerColor.RED,PlayerColor.BLUE));
    List<PlayerColor> playerColorList12 = new ArrayList<>(List.of(PlayerColor.RED,PlayerColor.RED,PlayerColor.BLUE,PlayerColor.RED,PlayerColor.RED,PlayerColor.BLUE));
    List<PlayerColor> playerColorListEmpty = new ArrayList<>();
    List<PlayerColor> playerColorListGreen = new ArrayList<>(List.of(PlayerColor.GREEN));



    Area<Zone.Forest> area1=new Area<>(listForest1,playerColorList,3);
    Area<Zone.Forest> area11=new Area<>(listForest1,playerColorList,1);
    Area<Zone.Forest> area12=new Area<>(listForest4,playerColorList12,3);
    Area<Zone.Forest> area2=new Area<>(listForest2,playerColorList,2);
    Area<Zone.Forest> area3=new Area<>(listForest3,playerColorList,2);
    Area<Zone.Forest> area4=new Area<>(listForest1,playerColorListEmpty,3);
    Area<Zone.Forest> area5=new Area<>(listForest2,playerColorList,2);
    Area<Zone.Forest> area6=new Area<>(listForest3,playerColorList,2);
    Area<Zone.Forest> area7=new Area<>(listForest1,playerColorListGreen,3);
    Area<Zone.Forest> area8=new Area<>(listForest2,playerColorList,2);
    Area<Zone.Forest> area9=new Area<>(listForest3,playerColorList,2);



    Set<Area<Zone.Forest>> areaForest = new HashSet<>(List.of(area1,area2,area3));
    Set<Area<Zone.Forest>> areaForest11 = new HashSet<>(List.of(area11,area2,area3));
    Set<Area<Zone.Forest>> areaForest12 = new HashSet<>(List.of(area12,area3));
    Set<Area<Zone.Forest>> areaForest2 = new HashSet<>(List.of(area4,area5,area6));
    Set<Area<Zone.Forest>> areaForest3 = new HashSet<>(List.of(area7,area8,area9));
    Set<Area<Zone.Forest>> areaForestImmu = Collections.unmodifiableSet(areaForest);


    ZonePartition<Zone.Forest> zonePartitionForest1 = new ZonePartition<>(areaForestImmu);
    ZonePartition<Zone.Forest> zonePartitionForest11 = new ZonePartition<>(areaForest11);
    ZonePartition<Zone.Forest> zonePartitionForest12 = new ZonePartition<>(areaForest12);
    ZonePartition<Zone.Forest> zonePartitionForest2 = new ZonePartition<>(areaForest2);
    ZonePartition<Zone.Forest> zonePartitionForest3 = new ZonePartition<>(areaForest3);
    ZonePartition<Zone.Forest> zonePartitionForestSame = new ZonePartition<>(new HashSet<>(List.of(area1.connectTo(area1), area2, area3)));

    ZonePartition.Builder<Zone.Forest> builder = new ZonePartition.Builder<>(zonePartitionForest1);
    ZonePartition.Builder<Zone.Forest> builder2 = new ZonePartition.Builder<>(zonePartitionForest2);
    ZonePartition.Builder<Zone.Forest> builder3 = new ZonePartition.Builder<>(zonePartitionForest3);

    @Test
    void areaContaining() {

        assertEquals(area1,zonePartitionForest1.areaContaining(zone1));
        assertNotEquals(area3,zonePartitionForest1.areaContaining(zone4));

    }

    @Test
    void areas() {
        assertEquals(areaForest,zonePartitionForest1.areas());
    }

    @Test
    void testAddSingleton(){
        builder.addSingleton(zone6,2);
        assertTrue(builder.build().areaContaining(zone6) != null);

    }


    @Test
    void testAddInitialOccupant(){
        builder2.addInitialOccupant(zone1,PlayerColor.GREEN);
        assertEquals(zonePartitionForest3,builder2.build());


    }


    @Test
    void testRemoveOccupant(){
        builder3.removeOccupant(zone1,PlayerColor.GREEN);
        assertEquals(zonePartitionForest2,builder3.build());

    }

    @Test
    void testRemoveAllOccupantsOf(){
        builder.removeAllOccupantsOf(area1);
        assertEquals(zonePartitionForest2,builder.build());
    }



    @Test
    void testUnion(){
        builder.union(zone1,zone3);
        assertEquals(zonePartitionForest12,builder.build());
    }

    @Test
    void testUnionWithSameZone(){

        builder.union(zone1,zone2);
        assertEquals(zonePartitionForestSame, builder.build());
    }

    @Test
    void testBuilder(){
        assertEquals(zonePartitionForest1,builder.build());
    }
}
