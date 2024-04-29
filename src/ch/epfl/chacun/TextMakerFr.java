package ch.epfl.chacun;

import java.util.Iterator;
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
     * @param points the number of points
     * @return
     */
    @Override
    public String points(int points) {
        return Integer.toString(points);
    }

    /**
     * @param player the player who closed the forest
     * @return the name of the player who closed the forest with a menhir in it
     */
    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return STR."\{playerName(player)} a fermé une forêt contenant un menhir et peut donc placer une tuile menhir.";
    }

    /**
     * @param scorers            the majority occupant of the forest
     * @param points             the scored points
     * @param mushroomGroupCount the number of mushroom group in the forest
     * @param tileCount          the number of tiles that constituting the forest
     * @return                   a string message indicating the number of points that one or more player scored by closing a forest
     */
    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        if (scorers.size() == 1) {
            if (mushroomGroupCount == 0) {
                return STR."\{scorersSyntax(scorers)} a remporté \{withSIfPlural("point", points)} en tant qu'occupant·e majoritaire d'une forêt composée de \{tileCount} tuiles.";
            } else {
                return STR."\{scorersSyntax(scorers)} a remporté \{withSIfPlural("point", points)} en tant qu'occupant·e majoritaire d'une forêt composée de \{tileCount} tuiles et de \{withSIfPlural("groupe", mushroomGroupCount)} de champignons.";
            }
        } else {
            if (mushroomGroupCount == 0) {
                return STR."\{scorersSyntax(scorers)} ont remporté \{withSIfPlural("point", points)} en tant qu'occupant·e·s majoritaires d'une forêt composée de \{tileCount} tuiles.";
            } else {
                return STR."\{scorersSyntax(scorers)} ont remporté \{withSIfPlural("point", points)} en tant qu'occupant·e·s majoritaires d'une forêt composée de \{tileCount} tuiles et de \{withSIfPlural("groupe", mushroomGroupCount)} de champignons.";
            }
        }
    }

    /**
     * @param scorers   the majority occupant of the river
     * @param points    the scored points
     * @param fishCount the number of fishes swimming in the river or in the adjacent lake
     * @param tileCount the number of tile constituting the river
     * @return          a string message indicating the number of points that one or more player scored by closing a river
     */
    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        if (scorers.size() == 1) {
            if (fishCount == 0) {
                return STR."\{scorersSyntax(scorers)} a remporté \{points} \{withSIfPlural("point", points)} en tant qu'occupant·e majoritaire d'une rivière composée de \{tileCount} tuiles.";
            } else {
                return STR."\{scorersSyntax(scorers)} a remporté \{points} \{withSIfPlural("point", points)} en tant qu'occupant·e majoritaire d'une rivière composée de \{tileCount} tuiles et contenant \{withSIfPlural("poisson", fishCount)}.";
            }
        } else {
            if (fishCount == 0) {
                return STR."\{scorersSyntax(scorers)} ont remporté \{points} \{withSIfPlural("point", points)} en tant qu'occupant·e·s majoritaires d'une rivière composée de \{tileCount} tuiles.";
            } else {
                return STR."\{scorersSyntax(scorers)} ont remporté \{points} \{withSIfPlural("point", points)} en tant qu'occupant·e·s majoritaires d'une rivière composée de \{tileCount} tuiles et de \{withSIfPlural("poisson", fishCount)}.";
            }
        }
    }

    /**
     * @param scorer  the player who placed the hunting trap
     * @param points  the scored points
     * @param animals the animals present in the same meadow as the hunting trap and 8 tiles around it
     * @return        a string message indicating the number of points that a player scored by placing a hunting trap
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

    private String scorersSyntax(Set<PlayerColor> scorers) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<PlayerColor> scorerIterator = scorers.iterator();

        int counter = 0;
        while (scorerIterator.hasNext()) {
            if (counter == 0) {
                stringBuilder.append(playerName((scorerIterator.next())));
                counter++;
            } else if (counter != scorers.size() - 1) {
                stringBuilder.append(", ");
                stringBuilder.append(playerName((scorerIterator.next())));
                counter++;
            } else {
                stringBuilder.append(" et ");
                stringBuilder.append(playerName((scorerIterator.next())));
            }
        }
        return stringBuilder.toString();
    }

    private String withSIfPlural(String noun, int count) {
        Preconditions.checkArgument(count > 0);
        if (count == 1) {
            return STR."\{count} \{noun}";
        } else {
        return STR."\{count} \{noun}s";
        }
    }
}
