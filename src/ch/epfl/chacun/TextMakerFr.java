package ch.epfl.chacun;

import java.util.Map;
import java.util.Set;

/**
 * Immutable class that handles the construction of text messages in ChaCuN in French.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public final class TextMakerFr implements TextMaker{
    private final Map<PlayerColor, String> playerNames;

    public TextMakerFr(Map<PlayerColor, String> playerNames) {
        this.playerNames = playerNames;
    }

    /**
     * @param playerColor the color of the player
     * @return the name of the player
     */
    @Override
    public String playerName(PlayerColor playerColor) {
        return playerNames.get(playerColor);
    }

    /**
     * @param points le nombre de points
     * @return
     */
    @Override
    public String points(int points) {
        return null;
    }

    /**
     * @param player le joueur ayant fermé la forêt
     * @return
     */
    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return null;
    }

    /**
     * @param scorers            les occupants majoritaires de la forêt
     * @param points             les points remportés
     * @param mushroomGroupCount le nombre de groupes de champignons que la forêt contient
     * @param tileCount          le nombre de tuiles qui constitue la forêt
     * @return
     */
    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        return null;
    }

    /**
     * @param scorers   les occupants majoritaires de la rivière
     * @param points    les points remportés
     * @param fishCount le nombre de poissons nageant dans la rivière ou les lacs adjacents
     * @param tileCount le nombre de tuiles qui constitue la rivière
     * @return
     */
    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        return null;
    }

    /**
     * @param scorer  le joueur ayant déposé la fosse à pieux
     * @param points  les points remportés
     * @param animals les animaux présents dans le même pré que la fosse et sur les 8 tuiles voisines
     * @return
     */
    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return null;
    }

    /**
     * @param scorer    le joueur ayant déposé la pirogue
     * @param points    les points remportés
     * @param lakeCount le nombre de lacs accessibles à la pirogue
     * @return
     */
    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return null;
    }

    /**
     * @param scorers les occupants majoritaires du pré
     * @param points  les points remportés
     * @param animals les animaux présents dans le pré (sans ceux ayant été précédemment annulés)
     * @return
     */
    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return null;
    }

    /**
     * @param scorers   les occupants majoritaires du réseau hydrographique
     * @param points    les points remportés
     * @param fishCount le nombre de poissons nageant dans le réseau hydrographique
     * @return
     */
    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return null;
    }

    /**
     * @param scorers les occupants majoritaires du pré contenant la fosse à pieux
     * @param points  les points remportés
     * @param animals les animaux présents sur les tuiles voisines de la fosse (sans ceux ayant été précédemment annulés)
     * @return
     */
    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return null;
    }

    /**
     * @param scorers   les occupants majoritaires du réseau hydrographique comportant le radeau
     * @param points    les points remportés
     * @param lakeCount le nombre de lacs contenus dans le réseau hydrographique
     * @return
     */
    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return null;
    }

    /**
     * @param winners l'ensemble des joueurs ayant remporté la partie
     * @param points  les points des vainqueurs
     * @return
     */
    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public String clickToOccupy() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public String clickToUnoccupy() {
        return null;
    }
}
