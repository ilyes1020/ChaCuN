package ch.epfl.chacun;

import java.util.*;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public record MessageBoard(TextMaker textMaker, List<Message> messages) {
    public MessageBoard {
        messages = List.copyOf(messages);
    }

    /**
     * Computes the points earned by each player based on the messages on the message board.
     *
     * @return A map associating each player with the total number of points they have earned.
     */
    public Map<PlayerColor, Integer> points(){
        Map<PlayerColor,Integer> playerPoints = new HashMap<>();
        for (Message message: messages) {
            for (PlayerColor playerColor: message.scorers()) {
                playerPoints.put(playerColor, playerPoints.getOrDefault(playerColor, 0) + message.points());
            }
        }
        return playerPoints;
    }

    /**
     * Returns a new message board identical to the current one, except if the given forest area is occupied.
     * If occupied, a new message is added indicating the majority occupants have scored points for its closure.
     *
     * @param forest The forest area to check for occupancy.
     * @return A new message board reflecting the changes, if any.
     */
    public MessageBoard withScoredForest(Area<Zone.Forest> forest){
        if (forest.isOccupied()){

            int tileCount = forest.tileIds().size();
            int mushroomGroupCount = Area.mushroomGroupCount(forest);
            int points = Points.forClosedForest(tileCount, mushroomGroupCount);
            String text = textMaker.playersScoredForest(forest.majorityOccupants(), points, mushroomGroupCount, tileCount);

            List<Message> updatedMessages = new ArrayList<>(this.messages);
            updatedMessages.add(new Message(text, points, forest.majorityOccupants(), forest.tileIds()));
            return new MessageBoard(textMaker , updatedMessages);
        }
        return this;
    }

    /**
     * Returns a new message board identical to the current one, but with a message indicating
     * that the given player has the right to take a second turn after closing the specified forest,
     * as it contains one or more menhirs.
     *
     * @param player The player who closed the forest.
     * @param forest The forest area closed by the player.
     * @return A new message board reflecting the changes.
     */
    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest){

            String text = textMaker.playerClosedForestWithMenhir(player);

            List<Message> updatedMessages = new ArrayList<>(this.messages);
            updatedMessages.add(new Message(text, 0, Set.of(), forest.tileIds()));
            return new MessageBoard(textMaker , updatedMessages);
    }

    /**
     * Returns a new message board identical to the current one, except if the given river area is occupied.
     * If occupied, a new message is added indicating the majority occupants have scored points for its closure.
     *
     * @param river The river area to check for occupancy.
     * @return A new message board reflecting the changes, if any.
     */
    public MessageBoard withScoredRiver(Area<Zone.River> river){
        if (river.isOccupied()){
            int tileCount = river.tileIds().size();
            int fishCount = Area.riverFishCount(river);
            int points = Points.forClosedRiver(tileCount, fishCount);
            String text = textMaker.playersScoredRiver(river.majorityOccupants(), points, fishCount, tileCount);

            List<Message> updatedMessages = new ArrayList<>(this.messages);
            updatedMessages.add(new Message(text, points, river.majorityOccupants(), river.tileIds()));
            return new MessageBoard(textMaker , updatedMessages);
        }
        return this;
    }

    /**
     * Returns a new message board identical to the current one, except if the placement of the hunting trap
     * by the given player resulted in scoring points.
     *
     * @param scorer        The player who placed the hunting trap.
     * @param adjacentMeadow The meadow area adjacent to the hunting trap.
     * @return A new message board reflecting the changes, if any.
     */
    public MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow){

        Set<Animal> animals = Area.animals(adjacentMeadow, Set.of());
        Map<Animal.Kind, Integer> animalCountMap = new HashMap<>();
        for (Animal animal : animals) {
            animalCountMap.put(animal.kind(), animalCountMap.getOrDefault(animal.kind(), 0) + 1);
        }

        int points = Points.forMeadow(animalCountMap.get(Animal.Kind.MAMMOTH), animalCountMap.get(Animal.Kind.AUROCHS), animalCountMap.get(Animal.Kind.DEER));
        if (points > 0){
        String text = textMaker.playerScoredHuntingTrap(scorer, points, animalCountMap);

        List<Message> updatedMessages = new ArrayList<>(this.messages);
        updatedMessages.add(new Message(text, points, Set.of(scorer), adjacentMeadow.tileIds()));
        return new MessageBoard(textMaker , updatedMessages);
        }
        return this;
    }

    /**
     * Returns a new message board identical to the current one, but with a message indicating
     * that the given player has scored points for placing a logboat in the specified river system.
     *
     * @param scorer      The player who placed the logboat.
     * @param riverSystem The water area representing the river system.
     * @return A new message board reflecting the changes.
     */
    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem){

        int lakeCount = Area.lakeCount(riverSystem);
        int points = Points.forLogboat(lakeCount);
        String text = textMaker.playerScoredLogboat(scorer, points, lakeCount);

        List<Message> updatedMessages = new ArrayList<>(this.messages);
        updatedMessages.add(new Message(text, points, Set.of(scorer), riverSystem.tileIds()));
        return new MessageBoard(textMaker , updatedMessages);
    }

    /**
     * Returns a new message board identical to the current one, except if the given meadow area is occupied.
     * If occupied, a new message is added indicating the majority occupants have scored points for its closure.
     *
     * @param meadow          The meadow area to check for occupancy.
     * @param cancelledAnimals The set of animals whose points are canceled.
     * @return A new message board reflecting the changes, if any.
     */
    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals){
        if (meadow.isOccupied()){
            Set<Animal> animals = Area.animals(meadow,cancelledAnimals);
            Map<Animal.Kind, Integer> animalCountMap = new TreeMap<>();
            for (Animal animal : animals) {
                animalCountMap.put(animal.kind(), animalCountMap.getOrDefault(animal.kind(), 0) + 1);
            }

            int points = Points.forMeadow(animalCountMap.get(Animal.Kind.MAMMOTH), animalCountMap.get(Animal.Kind.AUROCHS), animalCountMap.get(Animal.Kind.DEER));
            String text = textMaker.playersScoredMeadow(meadow.majorityOccupants(), points, animalCountMap);

            List<Message> updatedMessages = new ArrayList<>(this.messages);
            updatedMessages.add(new Message(text, points, meadow.majorityOccupants(), meadow.tileIds()));
            return new MessageBoard(textMaker , updatedMessages);
        }
        return this;
    }

    /**
     * Returns a new message board identical to the current one, except if the given river system area is occupied.
     * If occupied, a new message is added indicating the majority occupants have scored points for its closure.
     *
     * @param riverSystem The water area representing the river system.
     * @return A new message board reflecting the changes, if any.
     */
    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem){
        int fishCount = Area.riverSystemFishCount(riverSystem);
        int points = Points.forRiverSystem(fishCount);

        if (riverSystem.isOccupied() && points > 0) {
            String text = textMaker.playersScoredRiverSystem(riverSystem.majorityOccupants(), points, fishCount);

            List<Message> updatedMessages = new ArrayList<>(this.messages);
            updatedMessages.add(new Message(text, points, riverSystem.majorityOccupants(), riverSystem.tileIds()));
            return new MessageBoard(textMaker , updatedMessages);
        }
        return this;
    }

    /**
     * Returns a new message board identical to the current one, except if the given meadow area containing
     * the pit trap is occupied. If occupied, a new message is added indicating the majority occupants
     * have scored points for its closure.
     *
     * @param adjacentMeadow   The meadow area containing the pit trap.
     * @param cancelledAnimals The set of animals whose points are canceled.
     * @return A new message board reflecting the changes, if any.
     */
    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals){

            Set<Animal> animals = Area.animals(adjacentMeadow, cancelledAnimals);
            Map<Animal.Kind, Integer> animalCountMap = new HashMap<>();
            for (Animal animal : animals) {
                animalCountMap.put(animal.kind(), animalCountMap.getOrDefault(animal.kind(), 0) + 1);
            }

            int points = Points.forMeadow(animalCountMap.get(Animal.Kind.MAMMOTH), animalCountMap.get(Animal.Kind.AUROCHS), animalCountMap.get(Animal.Kind.DEER));

            if (adjacentMeadow.isOccupied() && points > 0){
                String text = textMaker.playersScoredPitTrap(adjacentMeadow.majorityOccupants(), points, animalCountMap);

                List<Message> updatedMessages = new ArrayList<>(this.messages);
                updatedMessages.add(new Message(text, points, adjacentMeadow.majorityOccupants(), adjacentMeadow.tileIds()));
                return new MessageBoard(textMaker , updatedMessages);
            }
            return this;
    }

    /**
     * Returns a new message board identical to the current one, but with a message indicating
     * that the majority occupants of the specified river system have scored points for having a raft.
     *
     * @param riverSystem The water area representing the river system with the raft.
     * @return A new message board reflecting the changes.
     */
    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem){

        if (riverSystem.isOccupied()) {
            int lakeCount = Area.lakeCount(riverSystem);
            int points = Points.forRaft(lakeCount);

            String text = textMaker.playersScoredRaft(riverSystem.majorityOccupants(), points, lakeCount);

            List<Message> updatedMessages = new ArrayList<>(this.messages);
            updatedMessages.add(new Message(text, points, riverSystem.majorityOccupants(), riverSystem.tileIds()));
            return new MessageBoard(textMaker , updatedMessages);
        }
        return this;
    }

    /**
     * Returns a new message board identical to the current one, but with a message indicating
     * the given winners of the game and the points they have scored.
     *
     * @param winners The set of winners.
     * @param points  The points scored by the winners.
     * @return A new message board reflecting the changes.
     */
    public MessageBoard withWinners(Set<PlayerColor> winners, int points){
        String text = textMaker.playersWon(winners, points);

        List<Message> updatedMessages = new ArrayList<>(this.messages);
        updatedMessages.add(new Message(text, points, winners, Set.of()));
        return new MessageBoard(textMaker , updatedMessages);
    }



    /**
     * Represents a message within the message board.
     *
     * @param text     The text content of the message.
     * @param points   The points associated with the message.
     * @param scorers  The set of players who scored points with this message.
     * @param tileIds  The set of tile IDs related to this message.
     */
    public record Message(String text, int points, Set<PlayerColor> scorers, Set<Integer> tileIds) {

        /**
         * Compact constructor for a message with the provided text, points, scorers, and tileIds.
         *
         * @throws IllegalArgumentException if points are negative.
         * @throws NullPointerException     if text is null.
         */
        public Message {
            Preconditions.checkArgument(points >= 0);
            Objects.requireNonNull(text, "Text must not be null");
            scorers = Set.copyOf(scorers);
            tileIds = Set.copyOf(tileIds);
        }
    }
}
