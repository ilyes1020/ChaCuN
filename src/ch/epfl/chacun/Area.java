package ch.epfl.chacun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * record representing an area
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public record Area<Z>(Set<Z> zones, List<PlayerColor> occupants, int openConnections) {
    public Area {
        Preconditions.checkArgument(openConnections >= 0);

        //sort occupants by the color of the player
        List<PlayerColor> sortedOccupants = new ArrayList<>(occupants);
        Collections.sort(sortedOccupants);

        zones = Set.copyOf(zones);
        occupants = List.copyOf(sortedOccupants);
    }

//    public static boolean hasMenhir(Area<Zone.Forest> forest){
//        return ;
//    }
//    public static int mushroomGroupCount(Area<Zone.Forest> forest){
//        return ;
//    }
//    public static Set<Animal> animals(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals){
//        return ;
//    }
//    public static int riverFishCount(Area<Zone.River> river){
//        return ;
//    }
//    public static int riverSystemFishCount(Area<Zone.Water> riverSystem){
//        return ;
//    }
//    public static int lakeCount(Area<Zone.Water> riverSystem){
//        return ;
//    }

//    public boolean isClosed(){
//        return ;
//    }
//    public boolean isOccupied(){
//        return ;
//    }
//    public Set<PlayerColor> majorityOccupants(){
//        return ;
//    }
//    public Area<Z> connectTo(Area<Z> that){
//        return ;
//    }
//    public Area<Z> withInitialOccupant(PlayerColor occupant){
//        return ;
//    }
//    public Area<Z> withoutOccupant(PlayerColor occupant){
//        return ;
//    }
//    public Area<Z> withoutOccupants(){
//        return ;
//    }
//    public Set<Integer> tileIds(){
//        return ;
//    }
//    public Zone zoneWithSpecialPower(Zone.SpecialPower specialPower){
//        return ;
//    }

}
