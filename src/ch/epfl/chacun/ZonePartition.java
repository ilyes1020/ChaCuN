package ch.epfl.chacun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Record representing a zone partition of a specific type of Zone
 * (Set of Areas forming a partition).
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public record ZonePartition<Z extends Zone>(Set<Area<Z>> areas) {

    /**
     * Compact constructor that makes ZonePartition immutable.
     *
     * @param areas the set of areas in the partition
     */
    public ZonePartition {
        areas = Set.copyOf(areas);
    }

    public ZonePartition() {
        this(Set.of());
    }

    /**
     * Gets the area containing the given zone.
     *
     * @param zone the zone in the area we want
     * @return the area containing the zone
     * @throws IllegalArgumentException if the zone does not belong to any area in the partition
     */
    public Area<Z> areaContaining(Z zone) {
        return areas.stream()
                .filter(area -> area.zones().contains(zone))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Zone does not belong to any area in the partition"));
    }

    /**
     * A builder class for constructing instances of the {@code ZonePartition} class.
     * This builder allows for the modification and creation of a {@code ZonePartition} by manipulating its internal areas.
     *
     * @param <Z> the type of zones in the ZonePartition
     */
    public static final class Builder<Z extends Zone> {
        private Set<Area<Z>> areas;

        /**
         * Constructor to instantiate a ZonePartition's Builder from an existing ZonePartition.
         *
         * @param zonePartition the existing ZonePartition
         */
        public Builder(ZonePartition<Z> zonePartition) {
            this.areas = new HashSet<>(zonePartition.areas);
        }

        /**
         * Adds a new unoccupied area to the current partition under construction, consisting only of the given zone,
         * and with the specified number of open connections
         *
         * @param zone            the zone to be added to the partition
         * @param openConnections the number of open connections for the new area
         */
        public void addSingleton(Z zone, int openConnections) {
            areas.add(new Area<>(new HashSet<>(Collections.singleton(zone)), new ArrayList<>(), openConnections));
        }

        /**
         * Adds an initial occupant of the specified color to the area containing the given zone.
         *
         * @param zone  the zone to add the initial occupant to
         * @param color the color of the initial occupant
         * @throws IllegalArgumentException if the zone does not belong to any area in the partition or if the area is already occupied
         */
        public void addInitialOccupant(Z zone, PlayerColor color) {
            Area<Z> areaWithZone = areaContaining(zone);
            areas.remove(areaWithZone);
            areas.add(areaWithZone.withInitialOccupant(color));
        }

        /**
         * Removes an occupant of the specified color from the area containing the given zone
         *
         * @param zone  the zone to remove the occupant from
         * @param color the color of the occupant to be removed
         * @throws IllegalArgumentException if the zone does not belong to any area in the partition or if the area is not occupied by an occupant of the specified color
         */
        public void removeOccupant(Z zone, PlayerColor color) {
            Area<Z> areaWithZone = areaContaining(zone);
            areas.remove(areaWithZone);
            areas.add(areaWithZone.withoutOccupant(color));
        }

        /**
         * Removes all occupants from the given area
         *
         * @param area the area from which all occupants should be removed
         * @throws IllegalArgumentException if the area is not in the partition
         */
        public void removeAllOccupantsOf(Area<Z> area) {
            Preconditions.checkArgument(areas.contains(area));
            areas.remove(area);
            areas.add(area.withoutOccupants());
        }

        /**
         * Connects the two areas containing the two distinct zones to form a new area.
         * The areas containing the two zones can be the same.
         *
         * @param zone1 the zone in the first area
         * @param zone2 the zone in the second area
         * @throws IllegalArgumentException if either zone does not belong to an area in the partition
         */
        public void union(Z zone1, Z zone2) {
            Area<Z> areaWithZone1 = areaContaining(zone1);
            Area<Z> areaWithZone2 = areaContaining(zone2);
            Area<Z> connectedArea = areaWithZone1.connectTo(areaWithZone2);
            areas.remove(areaWithZone1);
            areas.remove(areaWithZone2);
            areas.add(connectedArea);
        }

        private Area<Z> areaContaining(Z zone) {
            return areas.stream()
                    .filter(area -> area.zones().contains(zone))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Zone does not belong to any area in the partition"));
        }

        /**
         * Builds the zone partition.
         *
         * @return the new zone partition
         */
        public ZonePartition<Z> build() {
            return new ZonePartition<>(areas);
        }
    }
}
