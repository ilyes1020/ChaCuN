package ch.epfl.chacun;

/**
 * Class representing the points of the game.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public final class Points {

    /**
     * Private constructor to prevent instantiation
     */
    private Points() {}

    /**
     * Point for closing a forest.
     *
     * @param tileCount nb of tile constituting the forest
     * @param mushroomGroupCount nb of mushroom group in the forest
     * @return the point gained by the player for closing a forest (int)
     */
    public static int forClosedForest(int tileCount, int mushroomGroupCount) {
        Preconditions.checkArgument(tileCount > 1 && mushroomGroupCount >= 0);
        return (2 * tileCount) + (3 * mushroomGroupCount);
    }

    /**
     * Point for closing a river.
     *
     * @param tileCount nb of tile constituting a river
     * @param fishCount nb of fish in the river
     * @return the point gained by the player for closing a river (int)
     * @throws IllegalArgumentException if the tileCount is < 2 or fishCount < 0
     */
    public static int forClosedRiver(int tileCount, int fishCount){
        Preconditions.checkArgument(tileCount > 1 && fishCount >= 0);
        return tileCount + fishCount;
    }

    /**
     * Point for occupying a meadow.
     *
     * @param mammothCount nb of mommoth in the meadow
     * @param aurochsCount nb of aurochs in the meadow
     * @param deerCount nb of deer in the meadow
     * @return the nb of point gained by the player for occupying a meadow
     * @throws IllegalArgumentException if the mammothCount or aurochsCount or deerCount is < 0
     */
    public static int forMeadow(int mammothCount, int aurochsCount, int deerCount){
        Preconditions.checkArgument(mammothCount >= 0 && aurochsCount >= 0 && deerCount >= 0);
        return (3 * mammothCount) + (2 * aurochsCount) + deerCount;
    }
    /**
     * Point for occupying a river system.
     *
     * @param fishCount nb of fish in the river system
     * @return the nb of point gained by the player for occupying a river system
     * @throws IllegalArgumentException if the fishCount is < 0
     */
    public static int forRiverSystem(int fishCount){
        Preconditions.checkArgument(fishCount >= 0);
        return fishCount;
    }

    /**
     * Point for having a logboat in the river system.
     *
     * @param lakeCount nb of lake in the river system
     * @return the bonus point gained by the player for closing a river system with logboat
     * @throws IllegalArgumentException if the lakeCount is < 1
     */
    public static int forLogboat(int lakeCount){
        Preconditions.checkArgument(lakeCount > 0);
        return 2 * lakeCount;
    }

    /**
     * Point for having a raft in the river system.
     *
     * @param lakeCount nb of lake in the river system
     * @return the point gained by the player for occupying a river system with raft
     * @throws IllegalArgumentException if the lakeCount is < 1
     */
    public static int forRaft(int lakeCount){
        Preconditions.checkArgument(lakeCount > 0);
        return lakeCount;
    }

}
