package ch.epfl.chacun;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Immutable class that handles the construction of text messages in ChaCuN in French.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class TextMakerFr implements TextMaker {

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
     * @return the number of points as a string
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
        return STR."\{playerName(player)} a fermé une forêt contenant un menhir"
                + STR." et peut donc placer une tuile menhir.";
    }

    /**
     * @param scorers            the majority occupants of the forest
     * @param points             the scored points
     * @param mushroomGroupCount the number of mushroom groups in the forest
     * @param tileCount          the number of tiles constituting the forest
     * @return a string message indicating the number of points that one or more players scored by closing a forest
     */
    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        String message = STR."\{scorersSyntax(scorers)}"
                + STR." \{aIfSingularOntIfPlural(scorers.size())} remporté"
                + STR." \{points} \{withSIfPlural("point", points, false)}"
                + STR." en tant qu'\{withSIfPlural("occupant·e", scorers.size(), true)}"
                + STR." \{withSIfPlural("majoritaire", scorers.size(), false)}"
                + STR." d'une forêt composée de \{tileCount} tuiles";

        return mushroomGroupCount == 0 ?
                STR."\{message}." :
                STR."\{message} et de \{mushroomGroupCount}"
                        + STR." \{withSIfPlural("groupe", mushroomGroupCount, false)} de champignons.";
    }

    /**
     * @param scorers   the majority occupants of the river
     * @param points    the scored points
     * @param fishCount the number of fishes swimming in the river or in the adjacent lake
     * @param tileCount the number of tiles constituting the river
     * @return a string message indicating the number of points that one or more player scored by closing a river
     */
    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        String message = STR."\{scorersSyntax(scorers)}"
                + STR." \{aIfSingularOntIfPlural(scorers.size())} remporté"
                + STR." \{points} \{withSIfPlural("point", points, false)}"
                + STR." en tant qu'\{withSIfPlural("occupant·e", scorers.size(), true)}"
                + STR." \{withSIfPlural("majoritaire", scorers.size(), false)}"
                + STR." d'une rivière composée de \{tileCount} tuiles";

        return fishCount == 0 ?
                STR."\{message}." :
                STR."\{message} et contenant"
                        + STR." \{fishCount} \{withSIfPlural("poisson", fishCount, false)}.";
    }

    /**
     * @param scorer  the player who placed the hunting trap
     * @param points  the scored points
     * @param animals the animals present in the same meadow as the hunting trap and 8 tiles around it
     * @return        a string message indicating the number of points that a player scored by placing a hunting trap
     */
    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{playerName(scorer)} a remporté"
                + STR." \{points} \{withSIfPlural("point", points, false)}"
                + STR." en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de"
                + STR." \{animalSyntax(animals)}.";
    }

    /**
     * @param scorer    the player who placed the logboat
     * @param points    the scored points
     * @param lakeCount the number of lake accessible to the logboat
     * @return          a string message indicating the number of points that a player scored by placing a logboat
     */
    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return STR."\{playerName(scorer)} a remporté"
                + STR." \{points} \{withSIfPlural("point", points, false)}"
                + STR." en plaçant la pirogue dans un réseau hydrographique contenant"
                + STR." \{lakeCount} \{withSIfPlural("lac", lakeCount, false)}";
    }

    /**
     * @param scorers the majority occupants of the meadow
     * @param points  the scored points
     * @param animals the animals present in the meadow (without those who were previously cancelled)
     * @return a string message indicating the number of points that one or more players scored by closing a meadow
     */
    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{scorersSyntax(scorers)}"
                + STR." \{aIfSingularOntIfPlural(scorers.size())} remporté"
                + STR." \{points} \{withSIfPlural("point", points, false)}"
                + STR." en tant qu'\{withSIfPlural("occupant·e", scorers.size(), true)}"
                + STR." majoritaire d'un pré contenant"
                + STR." \{animalSyntax(animals)}.";
    }

    /**
     * @param scorers   the majority occupants of the river system
     * @param points    the scored points
     * @param fishCount the number of fish swimming in the river system
     * @return a string message indicating the number of points that one or more players scored by closing a river system
     */
    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return STR."\{scorersSyntax(scorers)}"
                + STR." \{aIfSingularOntIfPlural(scorers.size())} remporté"
                + STR." \{points} \{withSIfPlural("point", points, false)}"
                + STR." en tant qu'\{withSIfPlural("occupant·e", scorers.size(), true)}"
                + STR." majoritaire d'un réseau hydrographique contenant"
                + STR." \{fishCount} \{withSIfPlural("poisson", fishCount, false)}.";
    }

    /**
     * @param scorers the majority occupants of the meadow with the pit trap
     * @param points  the scored points
     * @param animals the animals present in the adjacent meadow (without those who were previously cancelled)
     * @return a string message indicating the number of points that one or more players scored with a pit trap
     */
    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{scorersSyntax(scorers)}"
                + STR." \{aIfSingularOntIfPlural(scorers.size())} remporté"
                + STR." \{points} \{withSIfPlural("point", points, false)}"
                + STR." en tant qu'\{withSIfPlural("occupant·e", scorers.size(), true)}"
                + STR." majoritaire d'un pré contenant la grande fosse à pieux et entourée de"
                + STR." \{animalSyntax(animals)}.";
    }

    /**
     * @param scorers   the majority occupants of the river system with the raft
     * @param points    the scored points
     * @param lakeCount the number of lake in the river system
     * @return a string message indicating the number of points that one or more players scored with a raft
     */
    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return STR."\{scorersSyntax(scorers)}"
                + STR." \{aIfSingularOntIfPlural(scorers.size())} remporté"
                + STR." \{points} \{withSIfPlural("point", points, false)}"
                + STR." en tant qu'\{withSIfPlural("occupant·e", scorers.size(), true)}"
                + STR." majoritaire d'un réseau hydrographique contenant le radeau et"
                + STR." \{lakeCount} \{withSIfPlural("lac", lakeCount, false)}.";
    }

    /**
     * @param winners the set of the winners
     * @param points  the winners' points
     * @return a string message indicating the winners of the game
     */
    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return STR."\{scorersSyntax(winners)}"
                + STR." \{aIfSingularOntIfPlural(winners.size())} remporté la partie avec"
                + STR." \{points} \{withSIfPlural("point", points, false)}!";
    }

    /**
     * @return a string message indicating that the player can choose whether to occupy a zone or not
     */
    @Override
    public String clickToOccupy() {
        return "Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.";
    }

    /**
     * @return a string message indicating that the player can choose whether to remove a pawn from a zone or not
     */
    @Override
    public String clickToUnoccupy() {
        return "Cliquez sur le pion que vous désirez reprendre, ou ici pour ne pas en reprendre.";
    }

    private String scorersSyntax(Set<PlayerColor> scorers) {
        List<PlayerColor> sortedScorers = scorers
                .stream()
                .sorted()
                .toList();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sortedScorers.size(); i++) {
            if (i > 0) {
                if (i == sortedScorers.size() - 1) {
                    builder.append(" et ");
                } else {
                    builder.append(", ");
                }
            }
            builder.append(playerName(sortedScorers.get(i)));
        }
        return builder.toString();
    }

    private String animalSyntax(Map<Animal.Kind, Integer> animals) {
        List<Animal.Kind> sortedAnimals = animals.keySet()
                .stream()
                .filter(k -> !k.equals(Animal.Kind.TIGER))
                .sorted().toList();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sortedAnimals.size(); i++) {
            Animal.Kind animalKind = sortedAnimals.get(i);
            if (i > 0) {
                if (i == sortedAnimals.size() - 1) {
                    builder.append(" et ");
                } else {
                    builder.append(", ");
                }
            }
            builder.append(STR."\{animals.get(animalKind)} \{withSIfPlural(animalFrName(animalKind), animals.get(animalKind), false)}");
        }
        return builder.toString();
    }

    private String animalFrName(Animal.Kind kind) {
        return switch (kind) {
            case MAMMOTH -> "mammouth";
            case AUROCHS -> "auroch";
            case DEER -> "cerf";
            case TIGER -> "tigre";
        };
    }

    private String aIfSingularOntIfPlural(int count) {
        return count == 1 ? "a" : "ont";
    }

    private String withSIfPlural(String noun, int count, boolean withMedianPoint) {
        if (count == 1) return noun;
        return withMedianPoint ? STR."\{noun}·s" : STR."\{noun}s";
    }
}
