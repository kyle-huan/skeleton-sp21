package game2048;

import java.util.*;

/** The state of a game of 2048.
 *  @author huan li
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
    /** The minimum size of the column to exit the loop. */
    private final int  EDGE_SIZE = 2;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;


    public static boolean canMerge = false;





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
//    public boolean tilt(Side side) {
//        boolean changed;
//        changed = false;
//
//        // TODO: Modify this.board (and perhaps this.score) to account
//        // for the tilt to the Side SIDE. If the board changed, set the
//        // changed local variable to true.
//
//
////        iterateColumnFromWestToEast(board);
//
//        changed = true;
//
//        checkGameOver();
//        if (changed) {
//            setChanged();
//        }
//        return changed;
//    }

//    private void handleSingleColumn(Board b) {
//        for (int c = 0; c < b.size(); c++) {
//            LinkedList<Tile> thisColumn = new LinkedList<>();
//            for (int r = 0; r < b.size(); r++) {
//                if (tile(c, r) != null) {
//                    thisColumn.add(tile(c, r));
//                }
//            }
//            int temp = 1;
//            while (thisColumn.size() >= 2) {
//                Tile first = thisColumn.pop();
//                Tile second = thisColumn.peek();
//                assert second != null;
//                if (first.value() == second.value()) {
//                    //TODO some problems here
//                    for (int i = b.size() - 1; i >= 0; i--) {
//                        if (b.tile(i, c) != null) {
//                            b.tile(i, c) = null;
//                        }
//                    }
//                    int newScore = first.value() + second.value();
//                    score += newScore;
//                    thisColumn.pop();
//                } else {
//                    thisColumn.pop();
//                    board.move(c, (b.size() - temp), first);
//                }
//                temp++;
//            }
//            if (thisColumn.size() != 0) {
//                board.move(c, (b.size() - temp), thisColumn.get(0));
//            }
//        }
//
//    }


//    private void iterateColumnFromWestToEast(Board b) {
//        ArrayList<LinkedList<Tile>> repo = new ArrayList<>();
//        for (int c = 0; c < b.size(); c++) {
//            LinkedList<Tile> thisColumn = new LinkedList<>();
//            for (int r = 0; r < b.size(); r++) {
//                if (tile(c, r) != null) {
//                    thisColumn.add(tile(c, r));
//                }
//            }
//            repo.add(thisColumn);
//        }
//        b.clear();
//
//        for (int c = 0; c < b.size(); c++) {
//            LinkedList<Tile> tiles = repo.get(c);
//            if (tiles.size() == 0) {
//                continue;
//            }
//            while (tiles.size() >= EDGE_SIZE) {
//                Tile first = tiles.pop();
//                Tile second = tiles.peek();
//                if (first.value() == second.value()) {
//                    for (int r = b.size() - 1; r >= 0; r--) {
//                        if (b.tile(c, r) == null) {
//                            int newScore = first.value() + second.value();
//                            Tile t = Tile.create(newScore, c, r);
//                            b.addTile(t);
//                            score += newScore;
//                            tiles.pop();
//                            break;
//                        }
//                    }
//                } else {
//                    for (int r = b.size() - 1; r >= 0; r--) {
//                        if (b.tile(c, r) == null) {
//                            Tile t = Tile.create(first.value(), c, r);
//                            b.addTile(t);
//                            break;
//                        }
//                    }
//                }
//            }
//
//            while (tiles.size() == 1) {
//                for (int r = b.size() - 1; r >= 0; r--) {
//                    if (b.tile(c, r) == null) {
//                        Tile first = tiles.pop();
//                        Tile t = Tile.create(first.value(), c, r);
//                        b.addTile(t);
//                        break;
//                    }
//                }
//            }
//        }
//
//    }
//
//    private void iterateColumnByMove(Board b) {
//        for (int c = 0; c < b.size(); c++) {
//            int currentSize = 0;
//            for (int r = b.size() - 1; r >= 0; r--) {
//                if (b.tile(c, r) != null) {
//                    currentSize++;
//                }
//            }
//
//            int slow = b.size() - 1;
//            int fast = b.size() - 1;
//            while (fast >= 0) {
//                if (b.tile(c, fast) != null) {
//                    fast++;
//                } else {
//                    fast++;
//                }
//            }
//        }
//    }
//
//    private int nextValidRow(Board b, int c) {
//        for (int r = b.size() - 1; r >= 0 ; r--) {
//            if (b.tile(c, r) == null) {
//                return r;
//            }
//        }
//        return -1;
//    }



    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        board.setViewingPerspective(side);

        boolean[][] isMerged = new boolean[board.size()][board.size()];
        for (int row = board.size() - 2; row >= 0; row -= 1) {
            for (int col = 0; col < board.size(); col += 1) {
                Tile t = board.tile(col, row);
                if (t == null) {
                    continue;
                }
                if (moveTileUp(t, isMerged,side)) {
                    changed = true;
                }
                //                if(board.tile(col,row)!=null){
                //                    board.move(col,3,t);
                //                    changed = true;
                //                    score+=7;
                //                }
            }
        }
        board.setViewingPerspective(Side.NORTH);

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    private boolean moveTileUp(Tile t, boolean[][] isMerged,Side s) {
        int targetRow;
        Tile nearest = findNearestTile(t,s);
        if (nearest == null) {
            targetRow = board.size() - 1;
        } else if (nearest.value() == t.value() &&
            !isMerged[col(nearest,s)][row(nearest,s)]) {
            targetRow = row(nearest,s);
        } else if (row(nearest,s) == row(t,s) + 1) {
            return false;
        } else {
            targetRow = row(nearest,s) - 1;
        }
        isMerged[col(t,s)][targetRow] = board.move(col(t,s), targetRow, t);
        if (isMerged[col(t,s)][targetRow]) {
            score += t.value() * 2;
        }
        return true;
    }

    private Tile findNearestTile(Tile t,Side s) {
        for (int row = row(t,s)+1; row < board.size(); row += 1) {
            Tile current = board.tile(col(t,s), row);
            if (current != null) {
                return current;
            }
        }
        return null;
    }

    /** Returns the side relative of side S. */
    static Side relative(Side s) {
        if (s == Side.NORTH) {
            return Side.NORTH;
        } else if (s == Side.SOUTH) {
            return Side.SOUTH;
        } else if (s == Side.EAST) {
            return Side.WEST;
        } else {
            return Side.EAST;
        }
    }


    public int col(Tile t,Side s){
        return Model.relative(s).col(t.col(),t.row(),board.size());
    }
    public int row(Tile t,Side s){
        return Model.relative(s).row(t.col(),t.row(),board.size());
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
        for (int i = 0; i < b.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (b.tile(i, j) == null) {
                    return true;
                }
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
        for (int i = 0; i < b.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (b.tile(i, j) != null && b.tile(i, j).value() == MAX_PIECE) {
                    return true;
                }
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
        for (int i = 0; i < b.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (b.tile(i, j) == null) {
                    return true;
                }
                if (validCoordinate(b, i + 1, j)
                    && b.tile(i + 1, j) != null
                    && b.tile(i + 1, j).value() == b.tile(i, j).value()) {
                    return true;
                }

                if (validCoordinate(b, i - 1, j)
                    && b.tile(i - 1, j) != null
                    && b.tile(i - 1, j).value() == b.tile(i, j).value()) {
                    return true;
                }

                if (validCoordinate(b, i, j + 1)
                    && b.tile(i, j + 1) != null
                    && b.tile(i, j + 1).value() == b.tile(i, j).value()) {
                    return true;
                }

                if (validCoordinate(b, i, j - 1)
                    && b.tile(i, j - 1) != null
                    && b.tile(i, j - 1).value() == b.tile(i, j).value()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean validCoordinate(Board b, int row, int col) {
        if (row >= 0 && row < b.size() && col >= 0 && col < b.size()) {
            return true;
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
