package mcts.model.game;

/**
 * A class for the board that the game will be played in.
 */
public class C4Board {
  public static final int ROWS = 6;
  public static final int COLUMNS = 7;

  private final char[][] grid;

  /**
   * Constructor to initialise the grid.
   */
  public C4Board() {
    grid = new char[ROWS][COLUMNS];
    // Fill the grid with empty spaces
    for (int r = 0; r < ROWS; r++) {
      for (int c = 0; c < COLUMNS; c++) {
        grid[r][c] = 0;
      }
    }
  }

  /**
   * This method uses the built-in arraycopy method to create a new board and copy the
   * contents of the original board to the new one.
   *
   * @return the copy of the original board.
   */
  public C4Board copyBoard() {
    C4Board boardCopy = new C4Board();
    for (int r = 0; r < ROWS; r++) {
      System.arraycopy(this.grid[r], 0, boardCopy.grid[r], 0, COLUMNS);
    }
    return boardCopy;
  }

  /**
   * Drops a disc into a column on the board.
   *
   * @param column The column where the disc will be dropped.
   * @param disc   The disc to be dropped (e.g., "R" or "Y").
   * @return the row where the disc was placed, or -1 if the move was invalid.
   */
  public int dropDisc(int column, char disc) {
    if (!isValidMove(column)) {
      return -1; // Invalid column
    }

    for (int r = ROWS - 1; r >= 0; r--) {
      if (grid[r][column] == 0) {
        grid[r][column] = disc;
        return r; // Return the row where the disc was placed
      }
    }
    return -1; // Column is full
  }

  /**
   * This method simply returns a boolean checking whether the column is full.
   *
   * @param column the column that needs to be checked whether a disc can be dropped or not
   * @return boolean whether its full or not.
   */
  public boolean isValidMove(int column) {
    return column >= 0 && column < COLUMNS && grid[0][column] == 0;

  }

  public char getCell(int row, int column) {
    return grid[row][column];
  }

  public int getRows() {
    return ROWS;
  }

  public int getColumns() {
    return COLUMNS;
  }

}
