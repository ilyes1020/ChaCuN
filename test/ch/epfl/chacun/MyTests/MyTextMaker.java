package ch.epfl.chacun.MyTests;

import ch.epfl.chacun.Animal;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.TextMaker;
import ch.epfl.chacun.Zone;

import java.util.Map;
import java.util.Set;

/**
 * @author Leopold Popper (363077)
 */
public class MyTextMaker implements TextMaker {

    @Override
    public String playerName(PlayerColor playerColor) {
        return "playerName";
    }

    @Override
    public String points(int points) {
        return "points";
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return "player " + player + " ClosedForestWithMenhir";
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        return "playersScoredForest";
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        return "playersScoredRiver";
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        return "playerScoredHuntingTrap";
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return "playerScoredLogboat";
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {

        return "playersScoredMeadow " + animals ;
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return "playersScoredRiverSystem";
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return "playersScoredPitTrap" + animals;
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return "playersScoredRaft";
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return "playersWon : " + winners + " with " + points + " points";
    }

    @Override
    public String clickToOccupy() {
        return "clickToOccupy";
    }

    @Override
    public String clickToUnoccupy() {
        return "clickToUnoccupy";
    }
}
