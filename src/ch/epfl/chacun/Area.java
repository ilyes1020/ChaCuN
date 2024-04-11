package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Record representing an area.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public record Area<Z extends Zone>(Set<Z> zones, List<PlayerColor> occupants, int openConnections) {

    /**
     * Compact constructor for the immutable Area record, sorts the occupants by player colors.
     * @param zones the set of zones in the area
     * @param occupants the list of player occupying the area
     * @param openConnections the number of open connections of the area
     */
    public Area {
        Preconditions.checkArgument(openConnections >= 0);

        //sort occupants by the color of the player
        List<PlayerColor> sortedOccupants = new ArrayList<>(occupants);
        Collections.sort(sortedOccupants);

        zones = Set.copyOf(zones);
        occupants = List.copyOf(sortedOccupants);
    }

    /**
     * Checks if a forest area has at least one menhir.
     *
     * @param forest the forest area to check
     * @return true if the forest contains at least one menhir, false otherwise
     */
    public static boolean hasMenhir(Area<Zone.Forest> forest) {
        return forest.zones().stream()
                .anyMatch(aForest -> aForest.kind() == Zone.Forest.Kind.WITH_MENHIR);
    }

    /**
     * Counts the number of mushroom groups in a forest area.
     *
     * @param forest the forest area to count mushroom groups in
     * @return the number of mushroom groups in the forest
     */

    public static int mushroomGroupCount(Area<Zone.Forest> forest) {
        return (int) forest.zones().stream()
                .filter(aForest -> aForest.kind() == Zone.Forest.Kind.WITH_MUSHROOMS)
                .count();
    }

    /**
     * Gets the set of animals in a meadow area excluding a set of cancelled animals.
     *
     * @param meadow the meadow area to get animals from
     * @param cancelledAnimals the set of cancelled animals
     * @return the set of animals in the meadow excluding the cancelled ones
     */
    public static Set<Animal> animals(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals){
        Set<Animal> animals = meadow.zones().stream()
                .flatMap(aMeadow -> aMeadow.animals().stream())
                .collect(Collectors.toSet());

        if (cancelledAnimals != null){
            animals.removeAll(cancelledAnimals);
        }

        return animals;
    }

    /**
     * Counts the number of river fish in a river area.
     *
     * @param river the river area to count fish in
     * @return the number of river fish
     */
    public static int riverFishCount(Area<Zone.River> river) {
        Set<Zone.Lake> lakesEncountered = new HashSet<>();
        return river.zones().stream()
                .mapToInt(aRiver -> {
                    int fishCount = aRiver.fishCount();
                    if (aRiver.hasLake() && lakesEncountered.add(aRiver.lake())) {
                        fishCount += aRiver.lake().fishCount();
                    }
                    return fishCount;
                })
                .sum();
    }

    /**
     * Counts the total number of fish in a water area representing a river system.
     *
     * @param riverSystem the water area representing the river system
     * @return the total number of fish in the river system
     */
    public static int riverSystemFishCount(Area<Zone.Water> riverSystem){
        return riverSystem.zones().stream()
                .mapToInt(Zone.Water::fishCount)
                .sum();
    }

    /**
     * Counts the number of lakes in a water area representing a river system.
     *
     * @param riverSystem the water area representing the river system
     * @return the number of lakes in the river system
     */
    public static int lakeCount(Area<Zone.Water> riverSystem){
        return (int) riverSystem.zones().stream()
                .filter(aWater -> aWater instanceof Zone.Lake)
                .count();
    }

    /**
     * Checks if the area is closed (has no open connections).
     *
     * @return true if the area is closed, false otherwise
     */
    public boolean isClosed(){
        return openConnections == 0;
    }

    /**
     * Checks if the area is occupied by at least one occupant.
     *
     * @return true if the area is occupied, false otherwise
     */
    public boolean isOccupied(){
        return !occupants.isEmpty();
    }

    /**
     * Gets the set of majority occupants in the area.
     *
     * @return the set of majority occupants
     */

    public Set<PlayerColor> majorityOccupants() {
        int[] occupantsCount = new int[PlayerColor.values().length];

        for (PlayerColor color : occupants) {
            occupantsCount[color.ordinal()]++;
        }

        int maxOccupants = Arrays.stream(occupantsCount).max().orElse(0);

        if (maxOccupants > 0) {
            return Arrays.stream(PlayerColor.values())
                    .filter(color -> occupantsCount[color.ordinal()] == maxOccupants)
                    .collect(Collectors.toSet());
        } else {
            return Set.of();
        }
    }

    /**
     * Connects the area to another area and return the resulting area.
     *
     * @param that the area to connect to
     * @return the area resulting from the connection
     */
    public Area<Z> connectTo(Area<Z> that) {
        Set<Z> combinedZones = new HashSet<>(this.zones);
        combinedZones.addAll(that.zones);

        List<PlayerColor> combinedOccupants = new ArrayList<>(this.occupants);

        int combinedOpenConnections;
        if (this == that) {
            combinedOpenConnections = this.openConnections - 2; // -2 because we are connecting the same area
        } else {
            combinedOpenConnections = this.openConnections + that.openConnections -2;
            combinedOccupants.addAll(that.occupants);
        }

        return new Area<>(combinedZones, combinedOccupants, combinedOpenConnections);
    }


    /**
     * Creates a new area with an initial occupant of the specified color.
     *
     * @param occupant the color of the initial occupant
     * @return a new area with the initial occupant
     */
    public Area<Z> withInitialOccupant(PlayerColor occupant){
        Preconditions.checkArgument(!isOccupied());
        return new Area<>(zones, List.of(occupant), openConnections);
    }

    /**
     * Creates a new area without an occupant of the specified color.
     *
     * @param occupant the color of the occupant to be removed
     * @return a new area without the specified occupant
     */
    public Area<Z> withoutOccupant(PlayerColor occupant){
        //throw IllegalArgumentException if this has no occupant of the given color
        Preconditions.checkArgument(occupants.contains(occupant));

        List<PlayerColor> updatedOccupants = new ArrayList<>(occupants);
        updatedOccupants.remove(occupant);

        return new Area<>(zones, updatedOccupants, openConnections);
    }

    /**
     * Creates a new area without any occupants.
     *
     * @return a new area without any occupants
     */
    public Area<Z> withoutOccupants(){
        return new Area<>(zones, new ArrayList<>(), openConnections);
    }

    /**
     * Gets the set of tile IDs associated with the zones in the area.
     *
     * @return the set of tile IDs
     */
    public Set<Integer> tileIds(){
        return zones.stream()
                .map(Zone::tileId)
                .collect(Collectors.toSet());
    }

    /**
     * Gets the zone with a specific special power in the area.
     *
     * @param specialPower the special power to search for
     * @return the zone with the specified special power, or null if not found
     */
    public Zone zoneWithSpecialPower(Zone.SpecialPower specialPower){
        return zones.stream()
                .filter(zone -> zone.specialPower() == specialPower)
                .findFirst()
                .orElse(null);
    }
}
