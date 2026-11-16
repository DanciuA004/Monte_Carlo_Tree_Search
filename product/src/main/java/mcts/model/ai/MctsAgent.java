package mcts.model.ai;

import mcts.model.game.C4Board;
import mcts.model.game.C4Player;

/**
 * Runs Monte Carlo tree search to choose a move for the specified player.
 */
public class MctsAgent {
  public int chooseMove(C4Board board, C4Player aiPlayer, C4Player opponent) {
    McTreeNode root = new McTreeNode(board, aiPlayer, opponent, -1, null);
    return root.chooseMove();
  }
}
