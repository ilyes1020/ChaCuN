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

    public Map<PlayerColor, Integer> points(){
        Map<PlayerColor,Integer> playerPoints = new HashMap<>();
        for (Message message: messages) {
            for (PlayerColor playerColor: message.scorers()) {
                playerPoints.put(playerColor, playerPoints.getOrDefault(playerColor, 0) + message.points());
            }
        }
        return playerPoints;
    }

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

    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest){

            String text = textMaker.playerClosedForestWithMenhir(player);

            List<Message> updatedMessages = new ArrayList<>(this.messages);
            updatedMessages.add(new Message(text, 0, Set.of(), forest.tileIds()));
            return new MessageBoard(textMaker , updatedMessages);
    }

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

    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem){

        int lakeCount = Area.lakeCount(riverSystem);
        int points = Points.forLogboat(lakeCount);
        String text = textMaker.playerScoredLogboat(scorer, points, lakeCount);

        List<Message> updatedMessages = new ArrayList<>(this.messages);
        updatedMessages.add(new Message(text, points, Set.of(scorer), riverSystem.tileIds()));
        return new MessageBoard(textMaker , updatedMessages);
    }

    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals){
        if (meadow.isOccupied()){
            Set<Animal> animals = Area.animals(meadow,cancelledAnimals);
            Map<Animal.Kind, Integer> animalCountMap = new HashMap<>();
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

    public MessageBoard withWinners(Set<PlayerColor> winners, int points){
        String text = textMaker.playersWon(winners, points);

        List<Message> updatedMessages = new ArrayList<>(this.messages);
        updatedMessages.add(new Message(text, points, winners, Set.of()));
        return new MessageBoard(textMaker , updatedMessages);
    }



    public record Message(String text, int points, Set<PlayerColor> scorers, Set<Integer> tileIds){
        public Message {
            Preconditions.checkArgument(points >= 0);
            Objects.requireNonNull(text);
            scorers = Set.copyOf(scorers);
            tileIds = Set.copyOf(tileIds);
        }
    }
}
