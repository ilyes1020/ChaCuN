package ch.epfl.chacun;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * record representing a deck of tiles
 * This record encapsulates the start tiles, normal tiles, and menhir tiles used in the game.
 * Each deck is represented as a list of tiles.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public record TileDecks(List<Tile> startTiles, List<Tile> normalTiles, List<Tile> menhirTiles) {
    public TileDecks{
        startTiles = List.copyOf(startTiles);
        normalTiles = List.copyOf(normalTiles);
        menhirTiles = List.copyOf(menhirTiles);
    }

    public int deckSize(Tile.Kind kind){
        return switch (kind) {
            case START -> startTiles.size();
            case NORMAL -> normalTiles.size();
            case MENHIR -> menhirTiles.size();
        };
    }

    public Tile topTile(Tile.Kind kind) {
        if (deckSize(kind) == 0) {
            return null;
        } else {
            return switch (kind) {
                case START -> startTiles.get(0);
                case NORMAL -> normalTiles.get(0);
                case MENHIR -> menhirTiles.get(0);
            };
        }
    }

    public TileDecks withTopTileDrawn(Tile.Kind kind) {
        switch (kind) {
            case START:
                if (startTiles.isEmpty()) {
                    throw new IllegalArgumentException("Start tiles deck is empty");
                }
                startTiles.removeFirst();
                break;
            case NORMAL:
                if (normalTiles.isEmpty()) {
                    throw new IllegalArgumentException("Normal tiles deck is empty");
                }
                normalTiles.removeFirst();
                break;
            case MENHIR:
                if (menhirTiles.isEmpty()) {
                    throw new IllegalArgumentException("Menhir tiles deck is empty");
                }
                menhirTiles.removeFirst();
                break;
        }
        return new TileDecks(startTiles, normalTiles, menhirTiles);
    }
    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate){
        switch (kind) {
            case START:
                if (startTiles.isEmpty()) {
                    throw new IllegalArgumentException("Start tiles deck is empty");
                }
                while (!startTiles.isEmpty() && predicate.test(startTiles.getFirst())) {
                    startTiles.removeFirst();
                }
                break;
            case NORMAL:
                if (normalTiles.isEmpty()) {
                    throw new IllegalArgumentException("Normal tiles deck is empty");
                }
                while (!normalTiles.isEmpty() && predicate.test(normalTiles.getFirst())) {
                    normalTiles.removeFirst();
                }
                break;
            case MENHIR:
                if (menhirTiles.isEmpty()) {
                    throw new IllegalArgumentException("Menhir tiles deck is empty");
                }
                while (!menhirTiles.isEmpty() && predicate.test(menhirTiles.getFirst())) {
                    menhirTiles.removeFirst();
                }
                break;
        }
        return new TileDecks(startTiles, normalTiles, menhirTiles);
    }
}
