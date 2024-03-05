package ch.epfl.chacun;

import java.util.HashSet;
import java.util.Set;

/**
 * record representing a zone partition of a specific type of Zone
 * (Set of Areas forming a partition)
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public record ZonePartition<Z> (Set<Area<Z>> areas) {
    public ZonePartition {
        areas = Set.copyOf(areas);
    }
    public ZonePartition(){
        this(new HashSet<>());
    }

    public Area<Z> areaContaining(Z zone){
        Area<Z> areaWithZone = null;
        for (Area<Z> area : areas){
            if (area.zones().contains(zone)){
                areaWithZone = area;
            }
        }
        Preconditions.checkArgument(areaWithZone != null);
        return areaWithZone;
    }

    public static final class Builder<Z> {
        private HashSet<Area<Z>> areas;

        public Builder(ZonePartition<Z> zonePartition) {
            this.areas = new HashSet<>(zonePartition.areas);
        }

//        public void addSingleton(Z zone, int openConnections){
//
//        }
//        public void addInitialOccupant(Z zone, PlayerColor color){
//            Preconditions.checkArgument();
//        }
//        public void removeOccupant(Z zone, PlayerColor color){
//            Preconditions.checkArgument();
//        }
//        public void removeAllOccupantsOf(Area<Z> area){
//            Preconditions.checkArgument();
//        }
//        public void union(Z zone1, Z zone2){
//            Preconditions.checkArgument();
//        }
//        public ZonePartition<Z> build(){
//            return ;
//        }

    }
}
