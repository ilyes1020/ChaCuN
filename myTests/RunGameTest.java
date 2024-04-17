import ch.epfl.chacun.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author Leopold Popper (363077)
 */
public class RunGameTest {

    public static void main(String[] args) throws InterruptedException {
       // try{
            fullGame();
        /*}catch (Exception e) {
            System.out.println(allCommands);
            saveIntegers(allCommands, "commands.txt");
            System.out.println(e);


        }*/
    }

    static List<Integer> allCommands = new ArrayList<>();

    static void fullGame() throws InterruptedException {


        //#Initialize players
        List<PlayerColor> players = new ArrayList<>(List.of(PlayerColor.RED, PlayerColor.GREEN, PlayerColor.BLUE));


        //#Initialize tile decks
        List<Tile> startTile = new ArrayList<>();
        List<Tile> normalTiles = new ArrayList<>();
        List<Tile> menhirTiles = new ArrayList<>();

        startTile.add(Tiles.TILES.get(56));

        normalTiles.add(Tiles.TILES.get(18));
        normalTiles.add(Tiles.TILES.get(74));
        normalTiles.add(Tiles.TILES.get(32));
        normalTiles.add(Tiles.TILES.get(41));
        normalTiles.add(Tiles.TILES.get(40));
        normalTiles.add(Tiles.TILES.get(0));
        normalTiles.add(Tiles.TILES.get(62));
        normalTiles.add(Tiles.TILES.get(37));
        normalTiles.add(Tiles.TILES.get(30));
        normalTiles.add(Tiles.TILES.get(4));
        normalTiles.add(Tiles.TILES.get(59));
        normalTiles.add(Tiles.TILES.get(31));
        normalTiles.add(Tiles.TILES.get(39));
        normalTiles.add(Tiles.TILES.get(73));
        normalTiles.add(Tiles.TILES.get(72));
        normalTiles.add(Tiles.TILES.get(60));
        normalTiles.add(Tiles.TILES.get(45));
        normalTiles.add(Tiles.TILES.get(29));
        normalTiles.add(Tiles.TILES.get(35));



        menhirTiles.add(Tiles.TILES.get(92));
        menhirTiles.add(Tiles.TILES.get(88));
        menhirTiles.add(Tiles.TILES.get(85));
        menhirTiles.add(Tiles.TILES.get(94)); //menhir tile closes forest with menhir //rotated left
        menhirTiles.add(Tiles.TILES.get(93)); //rotated right
        menhirTiles.add(Tiles.TILES.get(91));

        //Scanner scanner = new Scanner(System.in);
        InputOrSavedInput scanner = new InputOrSavedInput(false, "commands.txt");



        TileDecks tileDecks = new TileDecks(startTile,normalTiles,menhirTiles);
        TileDecks tileDecksSimple = new TileDecks(startTile, List.of(Tiles.TILES.get(18), Tiles.TILES.get(74)), List.of());
        GameState gameState = getInitialGameState(tileDecks);

        int roundCount = 0;
        PlayerColor currentPlayer = gameState.currentPlayer();

        System.out.print("Welcome to the game : 0 for starting a new game, 1 for loading a game : ");
            int act1 = scanner.nextInt();
            allCommands.add(act1);
            if(act1 == 1) {

                gameState = loadGameState("gameState");
            }

        while(gameState.nextAction() != GameState.Action.END_GAME){
            currentPlayer = gameState.currentPlayer();
            roundCount++;
            saveIntegers(allCommands, "commands.txt");
            System.out.println("====================================");
            System.out.println("New turn : " + roundCount);
            System.out.println("Current Player : " + gameState.currentPlayer().name());

            System.out.println("Here are the stats for messageBoard : this player has : " + gameState.messageBoard().points().getOrDefault(gameState.currentPlayer(),0));//TODO check if we need to do get or default

            System.out.printf("Enter 0 for printing the board, 1 for saving the game and continuing :   ");

            int act = scanner.nextInt();
            allCommands.add(act);
            if(act == 1){
                saveGameState(gameState, "gameState");

            }

            System.out.println("Here is the board : =================");

            System.out.println(printBoard(gameState.board()));

            System.out.println("========================");

            while (gameState.currentPlayer() == currentPlayer && gameState.nextAction() != GameState.Action.END_GAME){

                switch (gameState.nextAction()){

                    case RETAKE_PAWN:
                        System.out.println("You placed the shaman tile! Lets see if you can remove a pawn");
                        int placedOccupants = gameState.board().occupantCount(currentPlayer, Occupant.Kind.PAWN);
                        if(placedOccupants == 0) {
                            System.out.println("You cannot remove a pawn since you haven't placed one yet!");
                            gameState = gameState.withOccupantRemoved(null);
                        }

                        // Print a newline character after the loading animation is finished
                        System.out.println("\nYES YOU CAN!");

                        Occupant occupantToRemove = new Occupant(Occupant.Kind.PAWN,  321);
                        System.out.println("Lets see what occupants you have placed: " + occupantToRemove);
                        System.out.println("Do you wish to remove it? [1/0] : ");
                        int input = scanner.nextInt();
                        allCommands.add(input);
                        if(input == 1){
                            gameState = gameState.withOccupantRemoved(occupantToRemove);
                        } else {
                            gameState = gameState.withOccupantRemoved(null);
                        }

                        break;

                    case PLACE_TILE :
                        if(gameState.tileToPlace().kind() == Tile.Kind.MENHIR){
                            System.out.println("MENHIR TILE TO PLACE!");
                        }
                        System.out.println("You have got to place tile id : " + String.valueOf(gameState.tileToPlace().id()));
                        System.out.print("Enter x coordinate : ");
                        int x = scanner.nextInt();
                        allCommands.add(x);
                        System.out.print("Enter y coordinate : ");
                        int y = scanner.nextInt();
                        allCommands.add(y);

                        Pos pos = new Pos(x,y);

                        System.out.print("You want to rotate ? (0 for NONE, 1 for RIGHT, 2 for HALF_TURN, 3 for LEFT turn) : ");
                        int rotationId = scanner.nextInt();
                        allCommands.add(rotationId);
                        Rotation rot = Rotation.ALL.get(rotationId);


                        PlacedTile placedTile = new PlacedTile(gameState.tileToPlace(), gameState.currentPlayer(), rot, pos);

                        gameState = gameState.withPlacedTile(placedTile);
                        System.out.println("----");
                        break;

                    case OCCUPY_TILE:
                        System.out.println("You have to occupy the tile : " + String.valueOf( gameState.board().lastPlacedTile().id()));

                        System.out.println("Here are the potential zones you can occupy : ");

                        Set<Occupant> potOcc = gameState.lastTilePotentialOccupants();
                        List<Occupant> occ = new ArrayList<>(potOcc);
                        occ.sort(Comparator.comparingInt(Occupant::zoneId));
                        occ.sort(Comparator.comparing(Occupant::kind));

                        for(Occupant o : occ){
                            System.out.println(o.kind() + " " + o.zoneId());
                        }
                        System.out.println("-----------");
                        System.out.println("Available -> PAWN: " + gameState.freeOccupantsCount(gameState.currentPlayer(), Occupant.Kind.PAWN) + " | HUT: " + gameState.freeOccupantsCount(gameState.currentPlayer(), Occupant.Kind.HUT));
                        System.out.println("-----------");
                        System.out.print("Choose which occupant to place (index of the printing (0 = first one displayed) ; -1  = no occupant : ");
                        int chosenOccupant = scanner.nextInt();

                        allCommands.add(chosenOccupant);

                        if(chosenOccupant == -1){
                            gameState = gameState.withNewOccupant(null);
                            break;
                        }else{
                            gameState = gameState.withNewOccupant(occ.get(chosenOccupant));
                        }

                        break;


                }

            }



        }

        System.out.println("Game is ended : ");
        System.out.println("FINAL BOARD: ");
        System.out.println(printBoard(gameState.board()));
        System.out.println("=========================");
        for(MessageBoard.Message m : gameState.messageBoard().messages()){
            System.out.println("---------------");
            System.out.println("Scored tiles: " + m.tileIds() + " Scorers: " + m.scorers() + " Points: " + m.points() + " Type: " + m.text());

        }
        System.out.println(gameState.messageBoard().points());
        getFinalSuperGameMessageBoard(getInitialGameState(tileDecks), gameState);
        saveIntegers(allCommands, "commands.txt");

    }


    public static void getFinalSuperGameMessageBoard( GameState gameState, GameState gameStateToCheck){

        //for now, we instantiate only the messageboard and the cancelled animals
        MessageBoard m = gameState.messageBoard();
        Board board = gameState.board();

        Area<Zone.Forest> a = new Area<Zone.Forest>(Set.of((Zone.Forest) getZone(743),(Zone.Forest) getZone(320),(Zone.Forest) getZone(561), (Zone.Forest)getZone(412)),new ArrayList<>(), 0);
        m = m.withScoredForest(a);
        m = m.withClosedForestWithMenhir(PlayerColor.RED, a);

        a = new Area<>(Set.of((Zone.Forest) getZone(411), (Zone.Forest)getZone(401), (Zone.Forest)getZone(4)), new ArrayList<>() ,0);
        m = m.withScoredForest(a);
        m = m.withClosedForestWithMenhir(PlayerColor.GREEN, a);


        a = new Area<>(Set.of((Zone.Forest) getZone(370) , (Zone.Forest) getZone(301),  (Zone.Forest)getZone(43)), List.of(PlayerColor.BLUE), 0 );

        m = m.withScoredForest(a);

        m = m.withClosedForestWithMenhir(PlayerColor.RED,a);

        Area<Zone.River> b = new Area<>(Set.of((Zone.River)getZone(591), (Zone.River) getZone(45)), List.of(PlayerColor.BLUE), 0 );
        m = m.withScoredRiver(b);


        a = new Area<>(Set.of((Zone.Forest)getZone(723) , (Zone.Forest)getZone(310), (Zone.Forest)getZone(593)),List.of(PlayerColor.GREEN), 0);

        m = m.withScoredForest(a);

        m = m.withClosedForestWithMenhir(PlayerColor.GREEN, a);

        Area<Zone.Meadow> adjacentMeadow = new Area<>(Set.of( (Zone.Meadow)getZone(941) , (Zone.Meadow)getZone(300) , (Zone.Meadow)getZone(392) , (Zone.Meadow)getZone(850) , (Zone.Meadow)getZone(880)  ), List.of(PlayerColor.RED, PlayerColor.GREEN), 4);
        m = m.withScoredHuntingTrap(PlayerColor.GREEN, adjacentMeadow);

        a = new Area<>(Set.of((Zone.Forest)getZone(733) , (Zone.Forest)getZone(391), (Zone.Forest)getZone(940)),List.of(PlayerColor.RED), 0);
        m = m.withScoredForest(a);






        a = new Area<>(Set.of((Zone.Forest)getZone(883), (Zone.Forest)getZone(920), (Zone.Forest)getZone(601), (Zone.Forest)getZone(456)), List.of(PlayerColor.RED), 0);
        m = m.withScoredForest(a);

        m = m.withClosedForestWithMenhir(PlayerColor.BLUE, a);


        Area<Zone.Water> c = new Area<>(Set.of((Zone.Water)getZone(731), (Zone.Water)getZone(933), (Zone.Water)getZone(41), (Zone.Water)getZone(938), (Zone.Water)getZone(48), (Zone.Water)getZone(591), (Zone.Water)getZone(45), (Zone.Water)getZone(598)), List.of(PlayerColor.RED), 2);
        m = m.withScoredLogboat(PlayerColor.BLUE, c);
        b = new Area<>(Set.of((Zone.River)getZone(731), (Zone.River)getZone(933), (Zone.River)getZone(41)), List.of(PlayerColor.BLUE), 0);
        m = m.withScoredRiver(b);

        a = new Area<>(Set.of((Zone.Forest)getZone(351), (Zone.Forest)getZone(290), (Zone.Forest)getZone(3)), List.of(PlayerColor.GREEN), 0);
        m = m.withScoredForest(a);
        m = m.withClosedForestWithMenhir(PlayerColor.RED, a);

        b = new Area<>(Set.of((Zone.River)getZone(915), (Zone.River)getZone(931)), List.of(PlayerColor.BLUE), 0);
        m = m.withScoredRiver(b);

        MessageBoard msgEndGame = new MessageBoard(m.textMaker(), new ArrayList<>());


        //initialize the messages here on msgEndGame

        //ici  ---------------------------------
        Area<Zone.Meadow> d = new Area<>(Set.of((Zone.Meadow)getZone(402), (Zone.Meadow)getZone(350)), List.of(PlayerColor.BLUE), 2);
        msgEndGame = msgEndGame.withScoredMeadow(d, Set.of());

        d = new Area<>(Set.of((Zone.Meadow)getZone(410),(Zone.Meadow)getZone(921), (Zone.Meadow)getZone(880), (Zone.Meadow)getZone(560), (Zone.Meadow)getZone(620), (Zone.Meadow)getZone(850),(Zone.Meadow)getZone(941), (Zone.Meadow)getZone(740), (Zone.Meadow)getZone(180), (Zone.Meadow)getZone(371), (Zone.Meadow)getZone(300), (Zone.Meadow)getZone(392), (Zone.Meadow)getZone(311), (Zone.Meadow)getZone(592), (Zone.Meadow)getZone(44)),List.of(PlayerColor.RED, PlayerColor.GREEN), 7 );
        //Only canceled animal is from hunting trap, tigers flee from wildfire
        msgEndGame = msgEndGame.withScoredMeadow(d, Set.of(new Animal(3000, Animal.Kind.DEER)));

        d = new Area<>(Set.of((Zone.Meadow)getZone(921), (Zone.Meadow)getZone(410), (Zone.Meadow)getZone(560), (Zone.Meadow)getZone(620), (Zone.Meadow)getZone(850), (Zone.Meadow)getZone(880)), List.of(PlayerColor.RED, PlayerColor.GREEN), 7);
        msgEndGame = msgEndGame.withScoredPitTrap(d, Set.of());

        d = new Area<>(Set.of((Zone.Meadow)getZone(914), (Zone.Meadow)getZone(932), (Zone.Meadow)getZone(732), (Zone.Meadow)getZone(42), (Zone.Meadow)getZone(390)), List.of(PlayerColor.RED), 1);
        msgEndGame = msgEndGame.withScoredMeadow(d, Set.of());



        Area<Zone.Water> e = new Area<>(Set.of((Zone.Water)getZone(911), (Zone.Water)getZone(913), (Zone.Water)getZone(915), (Zone.Water)getZone(918), (Zone.Water)getZone(931), (Zone.Water)getZone(933), (Zone.Water)getZone(935), (Zone.Water)getZone(938), (Zone.Water)getZone(731), (Zone.Water)getZone(41), (Zone.Water)getZone(45), (Zone.Water)getZone(48), (Zone.Water)getZone(591), (Zone.Water)getZone(598)), List.of(PlayerColor.RED), 3);
        msgEndGame = msgEndGame.withScoredRiverSystem(e); //4 pts (4 fish)
        msgEndGame = msgEndGame.withScoredRaft(e); //4 pts (4 lakes)

        e = new Area<>(Set.of((Zone.Water)getZone(181), (Zone.Water)getZone(563), (Zone.Water)getZone(568), (Zone.Water)getZone(741)), List.of(PlayerColor.RED), 1);
        msgEndGame = msgEndGame.withScoredRiverSystem(e); //2 pts (2 fish)

        e = new Area<>(Set.of((Zone.Water)getZone(451), (Zone.Water)getZone(1), (Zone.Water)getZone(8)), List.of(PlayerColor.GREEN), 1);
        msgEndGame = msgEndGame.withScoredRiverSystem(e); //3 pts (3 fish)

        Set<PlayerColor> winners = Set.of(PlayerColor.RED);
        int maxPoints = 35;
        msgEndGame = msgEndGame.withWinners(winners, maxPoints);

        //then :
        List<MessageBoard.Message> messagesAtEndGame;
        messagesAtEndGame = msgEndGame.messages();

        int indexOfEndGameMessages = m.messages().size();
        List<MessageBoard.Message> messagesOfBoardBeforeEnd = gameStateToCheck.messageBoard().messages();
        messagesOfBoardBeforeEnd = messagesOfBoardBeforeEnd.subList(0, indexOfEndGameMessages);

        if(m.messages().equals(messagesOfBoardBeforeEnd)){
            System.out.println("TEST PASSED (BEFORE END GAME)");
        }else{
            throw new IllegalArgumentException("NONONO");
        }

        List<MessageBoard.Message> newMessages = new ArrayList<>(m.messages());
        newMessages.addAll(messagesAtEndGame);
        boolean work = true ;
        //now we check the last messages:
        messagesOfBoardBeforeEnd = gameStateToCheck.messageBoard().messages().subList(indexOfEndGameMessages, gameStateToCheck.messageBoard().messages().size());

        for(MessageBoard.Message message : messagesOfBoardBeforeEnd){
           if(! messagesAtEndGame.contains(message)){
               work = false;
               System.out.println(message);
               System.out.println("NOT CONTAIN");
            }
        }

        if(work){
            System.out.println("TEST PASSED (ENDGAME)");
        }else{
            throw new IllegalArgumentException("no");
        }

        //now we check the messages :

    }



    public static void generatePermutations(List<MessageBoard.Message> messages, int index) {
        if (index == messages.size() - 1) {
            System.out.println(messages);
            return;
        }

        for (int i = index; i < messages.size(); i++) {
            swap(messages, index, i);
            generatePermutations(messages, index + 1);
            swap(messages, index, i); // backtrack
        }
    }

    public static void swap(List<MessageBoard.Message> messages, int i, int j) {
        MessageBoard.Message temp = messages.get(i);
        messages.set(i, messages.get(j));
        messages.set(j, temp);
    }


    static Zone getZone(int id){
        for(Tile t : Tiles.TILES){
            for(Zone z : t.zones() ){
                if(z.id() == id){
                    return z;
                }
            }
        }
        return null;
    }



    public static void saveIntegers(List<Integer> integers, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Integer num : integers) {
                writer.write(num.toString() + "\n"); // Write each integer followed by a newline character
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> loadIntegers(String filename) {
        List<Integer> integers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                integers.add(Integer.parseInt(line.trim())); // Parse each line as an integer and add it to the list
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(integers);
        return integers;
    }

        public static void saveGameState(GameState gameState, String filePath)  {

        }

        public static GameState loadGameState(String filePath)  {
         return null;
        }


    static String printBoard(Board board){
        System.out.println(board.occupants());
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_BLUE = "\u001B[34m";


        StringBuilder superString = new StringBuilder();

        for(int y = -12; y <= 12; y++){
            for(int x = -12 ; x <= 12 ; x++){
                PlacedTile p = board.tileAt(new Pos(x,y));
                if(p != null){
                    int idInt = p.tile().id();
                    String id = ((String.valueOf( p.tile().id())));
                    if(p.placer() == PlayerColor.RED){
                        id = ANSI_RED + id + ANSI_RESET;
                    }
                    if(p.placer() == PlayerColor.GREEN){
                        id = ANSI_GREEN + id + ANSI_RESET;
                    }
                    if(p.placer() == PlayerColor.BLUE){
                        id = ANSI_BLUE + id + ANSI_RESET;
                    }


                    if(idInt < 10){
                        if( p.occupant() != null){
                            id = id + " " + (p.zoneWithId( p.occupant().zoneId()).localId());
                        }else{
                            id = id + " |";
                        }
                        superString.append("  " +id + " ");
                    }else{
                        if( p.occupant() != null){
                            id = id + " " + (p.zoneWithId( p.occupant().zoneId()).localId());
                        }else{
                            id = id + " N";
                        }
                        superString.append(" " +id + " ");

                    }
                }else{
                    superString.append("      ");
                }
            }
            superString.append("\n");
        }

        return superString.toString();

    }

    static GameState getInitialGameState(TileDecks t){

        TextMaker text = new MyTextMaker();
        List<PlayerColor> players = new ArrayList<>();
        players.add(PlayerColor.RED);
        players.add(PlayerColor.BLUE);
        players.add(PlayerColor.GREEN);
        GameState g = GameState.initial(players, t, text);
        g = g.withStartingTilePlaced();
        return g;
    }
}

class InputOrSavedInput{
    boolean liveInput;
    Scanner sc;
    String file;
    List<Integer> inputs = new ArrayList<>();
    Iterator<Integer> it;
    InputOrSavedInput(boolean liveInput, String file){
        this.file = file;
        sc = new Scanner(System.in);

        if(!liveInput){
            inputs = RunGameTest.loadIntegers(file);
            it = inputs.iterator();
        }
    }

    int nextInt(){
        if(liveInput){
            int out = sc.nextInt();
            inputs.add(out);
            RunGameTest.saveIntegers(inputs, file);
            return out;
        }else{
            if(it.hasNext()) {

                return it.next();
            }else{
                liveInput = true;
                return nextInt();
            }
        }
    }

}