package ch.epfl.chacun;

import java.util.*;

/**
 * record representing an area
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public record Area<Z extends Zone>(Set<Z> zones, List<PlayerColor> occupants, int openConnections) {
    public Area {
        Preconditions.checkArgument(openConnections >= 0);

        //sort occupants by the color of the player
        List<PlayerColor> sortedOccupants = new ArrayList<>(occupants);
        Collections.sort(sortedOccupants);

        zones = Set.copyOf(zones);
        occupants = List.copyOf(sortedOccupants);
    }

    public static boolean hasMenhir(Area<Zone.Forest> forest){
        for(Zone.Forest aForest : forest.zones()){
            if (aForest.kind() == Zone.Forest.Kind.WITH_MENHIR){
                return true;
            }
        }
        return false;
    }
    public static int mushroomGroupCount(Area<Zone.Forest> forest){
        int count = 0;
        for(Zone.Forest aForest : forest.zones()){
            if (aForest.kind() == Zone.Forest.Kind.WITH_MUSHROOMS){
                count++;
            }
        }
        return count;
    }
    public static Set<Animal> animals(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals){
        Set <Animal> animals = new HashSet<>();
        for(Zone.Meadow aMeadow : meadow.zones()){
            animals.addAll(aMeadow.animals());
        }
        animals.removeAll(cancelledAnimals);
        return animals;
    }
    public static int riverFishCount(Area<Zone.River> river) {
        Set<Zone.Lake> lakesEncountered = new HashSet<>();
        int fishCount = 0;

        for (Zone.River aRiver : river.zones()) {
            if (aRiver.hasLake()) { //a revoir
                if (lakesEncountered.add(aRiver.lake())) {
                    fishCount += aRiver.lake().fishCount();
                }
            } else {
                fishCount += aRiver.fishCount();
            }
        }
        return fishCount;
    }
    public static int riverSystemFishCount(Area<Zone.Water> riverSystem){
        int fishCount = 0;
        for(Zone.Water aWater : riverSystem.zones()){
            fishCount += aWater.fishCount();
        }
        return fishCount;
    }
    public static int lakeCount(Area<Zone.Water> riverSystem){
        int lakeCount = 0;
        for(Zone.Water aWater : riverSystem.zones()){
            if (aWater instanceof Zone.Lake){
                lakeCount++;
            }
        }
        return lakeCount;
    }
    public boolean isClosed(){
        return openConnections == 0;
    }
    public boolean isOccupied(){
        return !occupants.isEmpty();
    }
    public Set<PlayerColor> majorityOccupants() {
        int[] occupantsCount = new int[PlayerColor.values().length];

        for (PlayerColor color : occupants) {
            occupantsCount[color.ordinal()]++;
        }

        int maxOccupants = Arrays.stream(occupantsCount).max().orElse(0);

        if (maxOccupants > 0) {
            Set<PlayerColor> majorityColors = new HashSet<>();
            for (PlayerColor color : PlayerColor.values()) {
                if (occupantsCount[color.ordinal()] == maxOccupants) {
                    majorityColors.add(color);
                }
            }
            return majorityColors;
        } else {
            return Collections.emptySet();
        }
    }
    public Area<Z> connectTo(Area<Z> that) {
        Set<Z> combinedZones = new HashSet<>(this.zones);
        combinedZones.addAll(that.zones);

        List<PlayerColor> combinedOccupants = new ArrayList<>(this.occupants);
        combinedOccupants.addAll(that.occupants);

        int combinedOpenConnections;
        if (this == that) {
            combinedOpenConnections = this.openConnections - 2; // -2 because we are connecting the same area
        } else {
            combinedOpenConnections = this.openConnections + that.openConnections;
        }

        return new Area<>(combinedZones, combinedOccupants, combinedOpenConnections);
    }

    public Area<Z> withInitialOccupant(PlayerColor occupant){
        Preconditions.checkArgument(!isOccupied());
        return new Area<>(zones, List.of(occupant), openConnections);
    }
    public Area<Z> withoutOccupant(PlayerColor occupant){
        //throw IllegalArgumentException if this has not occupant of the given color
        Preconditions.checkArgument(occupants.contains(occupant));

        List<PlayerColor> updatedOccupants = new ArrayList<>(occupants);
        updatedOccupants.remove(occupant);

        return new Area<>(zones, updatedOccupants, openConnections);
    }
    public Area<Z> withoutOccupants(){
        return new Area<>(zones, List.of(), openConnections);
    }
    public Set<Integer> tileIds(){
        Set<Integer> tileIds = new HashSet<>();
        for(Z zone : zones){
            tileIds.add(zone.tileId());
        }
        return tileIds;
    }
    public Zone zoneWithSpecialPower(Zone.SpecialPower specialPower) {
        for (Z zone : zones) {
            if (zone.specialPower() == specialPower) {
                return zone;
            }
        }
        return null;
    }
}
