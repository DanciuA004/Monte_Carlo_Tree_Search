package mcts.model.game;

/**
 * Owns the current Connect Four state and applies game rules to each move.
 */
public class C4Game {
  private final C4Logic logic = new C4Logic();
  private final C4Player playerOne = new C4Player("Player 1", 'R');
  private final C4Player playerTwo = new C4Player("Player 2", 'Y');

  private C4Board board = new C4Board();
  private C4Player currentPlayer = playerOne;
  private GameStatus status = GameStatus.IN_PROGRESS;

  public MoveResult playMove(int column) {
    if (status != GameStatus.IN_PROGRESS) {
      return MoveResult.rejected(status);
    }

    C4Player mover = currentPlayer;
    int row = board.dropDisc(column, mover.getDisc());
    if (row < 0) {
      return MoveResult.rejected(status);
    }

    if (logic.checkWin(board)) {
      status = mover == playerOne ? GameStatus.PLAYER_ONE_WON : GameStatus.PLAYER_TWO_WON;
    } else if (logic.checkDraw(board)) {
      status = GameStatus.DRAW;
    } else {
      currentPlayer = getOpponent(mover);
    }
    return new MoveResult(true, row, mover, status);
  }

  public void restart() {
    board = new C4Board();
    currentPlayer = playerOne;
    status = GameStatus.IN_PROGRESS;
  }

  public C4Board getBoard() {
    return board;
  }

  public C4Player getCurrentPlayer() {
    return currentPlayer;
  }

  public C4Player getPlayerOne() {
    return playerOne;
  }

  public C4Player getPlayerTwo() {
    return playerTwo;
  }

  public C4Player getOpponent(C4Player player) {
    if (player == playerOne) {
      return playerTwo;
    }
    if (player == playerTwo) {
      return playerOne;
    }
    throw new IllegalArgumentException("Player does not belong to this game");
  }

  public enum GameStatus {
    IN_PROGRESS,
    PLAYER_ONE_WON,
    PLAYER_TWO_WON,
    DRAW
  }

  public static final class MoveResult {
    private final boolean accepted;
    private final int row;
    private final C4Player player;
    private final GameStatus status;

    private MoveResult(boolean accepted, int row, C4Player player, GameStatus status) {
      this.accepted = accepted;
      this.row = row;
      this.player = player;
      this.status = status;
    }

    private static MoveResult rejected(GameStatus status) {
      return new MoveResult(false, -1, null, status);
    }

    public boolean isAccepted() {
      return accepted;
    }

    public int row() {
      return row;
    }

    public C4Player player() {
      return player;
    }

    public GameStatus status() {
      return status;
    }
  }
}
