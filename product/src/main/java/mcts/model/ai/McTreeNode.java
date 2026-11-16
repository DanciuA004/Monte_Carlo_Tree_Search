package mcts.model.ai;

import mcts.model.game.C4Board;
import mcts.model.game.C4Logic;
import mcts.model.game.C4Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class hold the tree structure used for the monte-carlo algorithm.
 */
public class McTreeNode {
  private static final int MAX_SIMULATIONS = 1000;
  private final C4Board board;
  private final List<McTreeNode> children;
  private final C4Player aiPlayer;
  private final C4Player opponent;
  private int visits;
  private double wins;
  private final int column;
  private final McTreeNode parent;

  McTreeNode(C4Board board, C4Player aiPlayer, C4Player opponent, int column, McTreeNode parent) {
    this.board = board;
    this.children = new ArrayList<>();
    this.aiPlayer = aiPlayer;
    this.opponent = opponent;
    this.column = column;
    this.parent = parent;
  }

  int chooseMove() {
    if (children.isEmpty()) {
      expand();
    }
    if (children.isEmpty()) {
      return -1;
    }

    for (McTreeNode child : children) {
      simulate(child);
    }
    return select().column;
  }

  private McTreeNode select() {
    double maxUcb = Double.NEGATIVE_INFINITY;
    McTreeNode selectedChild = null;

    for (McTreeNode child : children) {
      if (child.visits == 0) {
        return child;
      }

      double meanReward = child.wins / child.visits;
      double confidenceInterval = Math.sqrt(2 * Math.log(Math.max(1, visits)) / child.visits);
      double ucbValue = meanReward + confidenceInterval;

      if (ucbValue > maxUcb) {
        maxUcb = ucbValue;
        selectedChild = child;
      }
    }
    return selectedChild;
  }

  private void expand() {
    for (int column = 0; column < board.getColumns(); column++) {
      if (board.isValidMove(column)) {
        C4Board boardCopy = board.copyBoard();
        boardCopy.dropDisc(column, aiPlayer.getDisc());
        children.add(new McTreeNode(boardCopy, aiPlayer, opponent, column, this));
      }
    }
  }

  private void simulate(McTreeNode node) {
    C4Logic gameLogic = new C4Logic();
    Random random = new Random();

    for (int simulation = 0; simulation < MAX_SIMULATIONS; simulation++) {
      C4Board simulationBoard = node.board.copyBoard();
      C4Player currentPlayer = opponent;
      boolean finished = false;

      if (gameLogic.checkWin(simulationBoard)) {
        node.backpropagation(1);
        continue;
      }

      while (!gameLogic.checkDraw(simulationBoard)) {
        List<Integer> validColumns = new ArrayList<>();
        for (int column = 0; column < simulationBoard.getColumns(); column++) {
          if (simulationBoard.isValidMove(column)) {
            validColumns.add(column);
          }
        }

        int column = validColumns.get(random.nextInt(validColumns.size()));
        simulationBoard.dropDisc(column, currentPlayer.getDisc());
        if (gameLogic.checkWin(simulationBoard)) {
          node.backpropagation(currentPlayer == aiPlayer ? 1 : 0);
          finished = true;
          break;
        }
        currentPlayer = currentPlayer == aiPlayer ? opponent : aiPlayer;
      }
      if (!finished) {
        node.backpropagation(0);
      }
    }
  }

  private void backpropagation(double reward) {
    McTreeNode node = this;
    while (node != null) {
      node.visits++;
      node.wins += reward;
      node = node.parent;
    }
  }
}
