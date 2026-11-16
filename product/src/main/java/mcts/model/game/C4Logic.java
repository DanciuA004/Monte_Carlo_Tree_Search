package mcts.model.game;

/**
 * The class that holds the important methods for game logic, checking for wins and draws.
 */
public class C4Logic {

  /**
   * Checks if the current state of the board has a winning condition.
   *
   * @return true if there is a win, false otherwise.
   */
  public boolean checkWin(C4Board board) {
    // Check horizontal
    for (int r = 0; r < board.getRows(); r++) {
      for (int c = 0; c <= board.getColumns() - 4; c++) {
        char disc = board.getCell(r, c);
        if (disc != 0 && disc == board.getCell(r, c + 1) && disc == board.getCell(r, c + 2)
            && disc == board.getCell(r, c + 3)) {
          return true;
        }
      }
    }

    // Check vertical
    for (int c = 0; c < board.getColumns(); c++) {
      for (int r = 0; r <= board.getRows() - 4; r++) {
        char disc = board.getCell(r, c);
        if (disc != 0 && disc == board.getCell(r + 1, c) && disc == board.getCell(r + 2, c)
            && disc == board.getCell(r + 3, c)) {
          return true;
        }
      }
    }

    // Check diagonal (bottom-left to top-right)
    for (int r = 3; r < board.getRows(); r++) {
      for (int c = 0; c <= board.getColumns() - 4; c++) {
        char disc = board.getCell(r, c);
        if (disc != 0 && disc == board.getCell(r - 1, c + 1) && disc == board.getCell(r - 2, c + 2)
            && disc == board.getCell(r - 3, c + 3)) {
          return true;
        }
      }
    }
    
    // Check diagonal (top-left to bottom-right)
    for (int r = 0; r <= board.getRows() - 4; r++) {
      for (int c = 0; c <= board.getColumns() - 4; c++) {
        char disc = board.getCell(r, c);
        if (disc != 0 && disc == board.getCell(r + 1, c + 1) && disc == board.getCell(r + 2, c + 2)
            && disc == board.getCell(r + 3, c + 3)) {
          return true;
        }
      }
    }
    return false; // No win condition found
  }

  /**
   * Checks if the game is a draw (i.e., no valid moves left).
   *
   * @return true if the game is a draw, false otherwise.
   */
  public boolean checkDraw(C4Board board) {
    for (int c = 0; c < board.getColumns(); c++) {
      if (board.isValidMove(c)) {
          return false; // There is at least one valid move
      }
    }
    return true; // No valid moves left
  }
}
