package game2048;

import java.util.Formatter;
import java.util.Iterator;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // Finished
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        Board b = this.board;
        int size = b.size();
        b.startViewingFrom(side);
        int tmpRow = -1, tmpCol = -1;
        for (int col = size - 1; col >= 0; col -= 1) {
            for (int row = size - 1; row >= 0; row -= 1) {
                for (int i = row - 1; i >= 0; i -= 1) {
                    Tile nextTile = b.tile(col, i);
                    if (b.tile(col, row) != null && nextTile != null) {
                        if (b.tile(col, row).value() == nextTile.value()) { // Only if there are NULLs or nothing between these tiles
                            Tile currentTile = b.tile(col, row);
                            Tile nextCurrentTile = nextTile;
                            if (checkNullsOrNothing(b, side, b.tile(col, row), nextTile)) {
                                if (col != tmpCol || row != tmpRow) {
                                    this.score += b.tile(col, row).value() * 2;
                                    changed = true;
                                    if (b.move(col, row, nextTile)) { // Judge if this move is a MERGE
                                        tmpRow = row;
                                        row -= 1;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // If there are NULLs in front of the current tile,
        // then move the current one to the NULL tile in the near front
        for (int col = size - 1; col >= 0; col -= 1) {
            for (int row = size - 1; row >= 0; row -= 1) {
                if (b.tile(col, row) != null) {
                    // Find the last tile in the row which is NULL,
                    // and put the current tile to that col COL, row ROW
                    int pointer = row + 1;
                    int marker = row;
                    while (pointer < size) {
                        if (b.tile(col, pointer) == null) {
                            marker = pointer;
                        }
                        pointer += 1;
                    }
                    b.move(col, marker, b.tile(col, row));
                    if (marker != row) {
                        changed = true;
                    }
                }
            }
        }

        checkGameOver();
        if (changed) {
            setChanged();
        }

        if (side != Side.NORTH) {
            b.setViewingPerspective(Side.NORTH);
        }

        return changed;
    }

    /** Check if there are NULLs or nothing between these tiles*/
    private boolean checkNullsOrNothing(Board b, Side side, Tile tile, Tile nextTile) {
        b.startViewingFrom(Side.NORTH);
        int tileCol = tile.col();
        int nextTileCol = nextTile.col();
        int tileRow = tile.row();
        int nextTileRow = nextTile.row();
        if (tileRow - nextTileRow > 1) {
            for (int i = tileRow - 1; i > nextTileRow; i -= 1) {
                Tile tmp = b.tile(tileCol, i);
                if (b.tile(tileCol, i) != null) {
                    b.setViewingPerspective(side);
                    return false;
                }
            }
        } else if (nextTileRow - tileRow > 1) {
            for (int i = nextTileRow - 1; i > tileRow; i -= 1) {
                Tile tmp = b.tile(tileCol, i);
                if (b.tile(tileCol, i) != null) {
                    b.setViewingPerspective(side);
                    return false;
                }
            }
        }
        if (tileCol - nextTileCol > 1) {
            for (int i = tileCol - 1; i > nextTileCol; i -= 1) {
//                Tile tmp = b.tile(0, 2);
                if (b.tile(i, tileRow) != null) {
//                if (tmp != null) {
                    b.setViewingPerspective(side);
                    return false;
                }
            }
        } else if (nextTileCol - tileCol > 1) {
            for (int i = nextTileCol - 1; i > tileCol; i -= 1) {
                Tile tmp = b.tile(i, tileRow);
                if (b.tile(i, tileRow) != null) {
                    b.setViewingPerspective(side);
                    return false;
                }
            }
        }
        b.setViewingPerspective(side);
        return true;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        for(Tile tile : b){
            if (tile == null){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        for (Tile tile : b) {
            if(tile != null && tile.value() == MAX_PIECE){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        int size = b.size();
        final int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        for (Tile tile : b) {
            if (tile == null) {
                return true;
            }
            for (int[] d : directions) {
                int row = tile.row() + d[0];
                int col = tile.col() + d[1];
                if (0 <= row && row < size && 0 <= col && col < size) {
                    Tile next = b.tile(col, row);
                    if(next != null && next.value() == tile.value()){
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
