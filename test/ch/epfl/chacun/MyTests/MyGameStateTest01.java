package ch.epfl.chacun.MyTests;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MyGameStateTest01 {


    TileDecks getFullTileDecks(){
        List<Tile> tiles = Tiles.TILES;
        List<Tile> normal = new ArrayList<>();
        List<Tile> menhir = new ArrayList<>();
        List<Tile> start = new ArrayList<>();

        for(Tile t : tiles){
            switch (t.kind()){
                case NORMAL -> normal.add(t);
                case MENHIR -> menhir.add(t);
                case START -> start.add(t);

            }
        }
        //System.out.println(start.size());

        return new TileDecks(start,normal,menhir);
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

    @Test
    void simpleScenarioGame(){
        List<Tile> str = new ArrayList<>();
        List<Tile> nrml = new ArrayList<>();
        List<Tile> menhir = new ArrayList<>();
        str.add(Tiles.TILES.get(56));
        nrml.add(Tiles.TILES.get(64));
        nrml.add(Tiles.TILES.get(78));
        nrml.add(Tiles.TILES.get(67));
        nrml.add(Tiles.TILES.get(13)); //we cannot place this one normally
        nrml.add(Tiles.TILES.get(26));

        menhir.add(Tiles.TILES.get(82));

        TileDecks deck = new TileDecks(str,nrml,menhir);
        GameState s = getInitialGameState(deck);

        assertEquals(Tiles.TILES.get(64), s.tileToPlace());

        //part 1 : place tile 64 at pos 1,0
        PlacedTile p = new PlacedTile(s.tileToPlace(), s.currentPlayer(), Rotation.NONE, new Pos(1,0));
        s = s.withPlacedTile(p);

        //occupy it
        Occupant o = new Occupant(Occupant.Kind.PAWN, 642);
        assertEquals(GameState.Action.OCCUPY_TILE, s.nextAction());
        assertEquals(null, s.tileToPlace());
        s = s.withNewOccupant(o);

        //Part 2;
        assertEquals(PlayerColor.BLUE,s.currentPlayer());
        assertEquals(Tiles.TILES.get(78), s.tileToPlace());

        assertEquals(4, s.freeOccupantsCount(PlayerColor.RED, Occupant.Kind.PAWN));

        //second tile placed
        p = new PlacedTile(s.tileToPlace(), s.currentPlayer(), Rotation.NONE, new Pos(0,1));
        s = s.withPlacedTile(p);

        Set<Occupant> potOccupantTest = new HashSet<>(Set.of(new Occupant(Occupant.Kind.PAWN, 781)));
        assertEquals(potOccupantTest, s.lastTilePotentialOccupants());

        GameState finalS = s;
        assertThrows(IllegalArgumentException.class, ()-> finalS.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 780)));
        //occupy second tuile by blue
        o = new Occupant(Occupant.Kind.PAWN, 781);
        s = s.withNewOccupant(o);

        //place 3rd tile occ GREEN
        assertEquals(GameState.Action.PLACE_TILE, s.nextAction());
        assertEquals(Tiles.TILES.get(67), s.tileToPlace());

        p = new PlacedTile(s.tileToPlace(), PlayerColor.GREEN, Rotation.HALF_TURN, new Pos(1,1));
        s = s.withPlacedTile(p);

        assertEquals(GameState.Action.OCCUPY_TILE, s.nextAction());

        assertEquals(PlayerColor.GREEN, s.currentPlayer());

        o = new Occupant(Occupant.Kind.PAWN, 672);
        s = s.withNewOccupant(o);

        //green now has to place special tile
        assertEquals(4, s.freeOccupantsCount(PlayerColor.GREEN, Occupant.Kind.PAWN));

        assertEquals(PlayerColor.GREEN, s.currentPlayer());

        assertEquals(Tiles.TILES.get(82), s.tileToPlace());
        assertEquals(GameState.Action.PLACE_TILE, s.nextAction());

        p = new PlacedTile(s.tileToPlace(), s.currentPlayer(), Rotation.RIGHT, new Pos(-1,0));
        s = s.withPlacedTile(p);

        assertEquals(PlayerColor.GREEN, s.currentPlayer());

        s = s.withNewOccupant(null);

        assertEquals(PlayerColor.RED, s.currentPlayer());
        assertEquals(4,s.freeOccupantsCount(PlayerColor.GREEN, Occupant.Kind.PAWN) );

        assertEquals(Tiles.TILES.get(26), s.tileToPlace());
        assertEquals(GameState.Action.PLACE_TILE, s.nextAction());


        //il a sauter la bonne tuile
        assertEquals( 8,s.messageBoard().points().get(PlayerColor.RED));




    }



    @Test
    void testBasic(){
        GameState s = getInitialGameState(getFullTileDecks());

        assertEquals(PlayerColor.RED,s.currentPlayer() );


        System.out.println("Placing tile " +  s.tileToPlace().id());
        PlacedTile tile = new PlacedTile(s.tileToPlace(), s.currentPlayer(), Rotation.NONE, new Pos(1,0));
        s = s.withPlacedTile(tile);
        System.out.println("Successfully PlacedTile, placing occupant");
        Occupant newOcc = new Occupant(Occupant.Kind.PAWN, 4);
        s = s.withNewOccupant(newOcc);
        System.out.println(s.nextAction());
        assertEquals(PlayerColor.BLUE, s.currentPlayer());
        assertEquals(GameState.Action.PLACE_TILE, s.nextAction());


    }





}















