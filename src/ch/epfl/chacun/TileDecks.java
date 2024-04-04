package ch.epfl.chacun;

import java.util.List;
import java.util.function.Predicate;

/**
 * Record representing a deck of tiles.
 * This record encapsulates the start tiles, normal tiles, and menhir tiles used in the game.
 * Each deck is represented as a list of tiles.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public record TileDecks(List<Tile> startTiles, List<Tile> normalTiles, List<Tile> menhirTiles) {

    /**
     * Compact constructor that makes TileDecks immutable
     *
     * @param startTiles  The list of start tiles.
     * @param normalTiles The list of normal tiles.
     * @param menhirTiles The list of menhir tiles.
     */
    public TileDecks{
        startTiles = List.copyOf(startTiles);
        normalTiles = List.copyOf(normalTiles);
        menhirTiles = List.copyOf(menhirTiles);
    }

    /**
     * Returns the size of the specified tile deck.
     *
     * @param kind The type of tile deck (START, NORMAL, or MENHIR).
     * @return The size of the specified tile deck.
     */
    public int deckSize(Tile.Kind kind){
        return switch (kind) {
            case START -> startTiles.size();
            case NORMAL -> normalTiles.size();
            case MENHIR -> menhirTiles.size();
        };
    }

    /**
     * Returns the top tile from the specified tile deck.
     *
     * @param kind The type of tile deck (START, NORMAL, or MENHIR).
     * @return The top tile from the specified tile deck, or null if the deck is empty.
     */
    public Tile topTile(Tile.Kind kind) {
        if (deckSize(kind) == 0) {
            return null;
        } else {
            return switch (kind) {
                case START -> startTiles.getFirst();
                case NORMAL -> normalTiles.getFirst();
                case MENHIR -> menhirTiles.getFirst();
            };
        }
    }

    /**
     * Returns a new TileDecks record with the top tile removed from the specified tile deck.
     *
     * @param kind The type of tile deck (START, NORMAL, or MENHIR).
     * @return A new TileDecks record with the top tile removed from the specified tile deck.
     * @throws IllegalArgumentException If the specified tile deck is empty.
     */
    public TileDecks withTopTileDrawn(Tile.Kind kind) {
        return switch (kind) {
            case START -> {
                if (startTiles.isEmpty()) {
                    throw new IllegalArgumentException("Start tiles deck is empty");
                }
                yield new TileDecks(startTiles.subList(1, startTiles.size()), normalTiles, menhirTiles);
            }
            case NORMAL -> {
                if (normalTiles.isEmpty()) {
                    throw new IllegalArgumentException("Normal tiles deck is empty");
                }
                yield new TileDecks(startTiles, normalTiles.subList(1, normalTiles.size()), menhirTiles);
            }
            case MENHIR -> {
                if (menhirTiles.isEmpty()) {
                    throw new IllegalArgumentException("Menhir tiles deck is empty");
                }
                yield new TileDecks(startTiles, normalTiles, menhirTiles.subList(1, menhirTiles.size()));
            }
        };
    }

    /**
     * Returns a new TileDecks record with tiles removed from the specified tile deck until the predicate returns false.
     *
     * @param kind      The type of tile deck (START, NORMAL, or MENHIR).
     * @param predicate The predicate specifying the condition that the top tile must satisfy to be removed.
     * @return A new TileDecks record with tiles removed from the specified tile deck until the predicate returns false.
     */
    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate) {
        List<Tile> updatedStartTiles = startTiles;
        List<Tile> updatedNormalTiles = normalTiles;
        List<Tile> updatedMenhirTiles = menhirTiles;

        switch (kind) {
            case START:
                while (!updatedStartTiles.isEmpty() && !predicate.test(updatedStartTiles.getFirst())) {
                    updatedStartTiles = updatedStartTiles.subList(1, updatedStartTiles.size());
                }
                break;
            case NORMAL:
                while (!updatedNormalTiles.isEmpty() && !predicate.test(updatedNormalTiles.getFirst())) {
                    updatedNormalTiles = updatedNormalTiles.subList(1, updatedNormalTiles.size());
                }
                break;
            case MENHIR:
                while (!updatedMenhirTiles.isEmpty() && !predicate.test(updatedMenhirTiles.getFirst())) {
                    updatedMenhirTiles = updatedMenhirTiles.subList(1, updatedMenhirTiles.size());
                }
                break;
        }
        return new TileDecks(updatedStartTiles, updatedNormalTiles, updatedMenhirTiles);
    }
}
