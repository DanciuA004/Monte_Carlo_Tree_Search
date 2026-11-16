package mcts.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import mcts.model.ai.MctsAgent;
import mcts.model.game.C4Board;
import mcts.model.game.C4Game;
import mcts.model.game.C4Game.GameStatus;
import mcts.model.game.C4Game.MoveResult;

/**
 * Handles JavaFX events and renders the player-versus-AI game.
 */
public class GuiPvEcontroller implements Initializable {
  private final Circle[][] circles = new Circle[C4Board.ROWS][C4Board.COLUMNS];
  private final C4Game game = new C4Game();
  private final MctsAgent ai = new MctsAgent();

  @FXML
  private Label labelPvP;
  @FXML
  private GridPane gridPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    for (int row = 0; row < C4Board.ROWS; row++) {
      for (int column = 0; column < C4Board.COLUMNS; column++) {
        Circle circle = new Circle(45);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);
        circles[row][column] = circle;
        gridPane.add(circle, column, row);
      }
    }
  }

  public void clickColumn(ActionEvent event) {
    if (game.getCurrentPlayer() != game.getPlayerOne()) {
      return;
    }
    int column = getColumn(event);
    if (column < 0) {
      return;
    }

    MoveResult result = game.playMove(column);
    if (!result.isAccepted()) {
      return;
    }
    renderMove(result, column);
    if (result.status() != GameStatus.IN_PROGRESS) {
      showGameResult(result);
      return;
    }

    labelPvP.setText("AI Opponent's Turn!");
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ignored -> playAiTurn()));
    timeline.setCycleCount(1);
    timeline.play();
  }

  private void playAiTurn() {
    int column = ai.chooseMove(game.getBoard(), game.getPlayerTwo(), game.getPlayerOne());
    MoveResult result = game.playMove(column);
    if (!result.isAccepted()) {
      throw new IllegalStateException("The AI selected an invalid move: " + column);
    }
    renderMove(result, column);
    if (result.status() != GameStatus.IN_PROGRESS) {
      showGameResult(result);
    } else {
      labelPvP.setText("Player 1, please select a column!");
    }
  }

  private int getColumn(ActionEvent event) {
    String id = ((Node) event.getSource()).getId();
    if (id == null || !id.startsWith("buttonColumn")) {
      return -1;
    }
    try {
      return Integer.parseInt(id.substring("buttonColumn".length())) - 1;
    } catch (NumberFormatException exception) {
      return -1;
    }
  }

  private void renderMove(MoveResult result, int column) {
    circles[result.row()][column].setFill(
        result.player().getDisc() == 'R' ? Color.RED : Color.YELLOW);
  }

  private void showGameResult(MoveResult result) {
    Platform.runLater(() -> {
      Alert alert = new Alert(AlertType.INFORMATION);
      if (result.status() == GameStatus.DRAW) {
        alert.setTitle("Draw!");
        alert.setHeaderText("No Winner! The game is a draw.");
      } else {
        alert.setTitle("Winner!");
        alert.setHeaderText("The winner is: " + result.player().getName());
      }
      alert.setContentText("The game will now restart.");
      if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
        restart();
      }
    });
  }

  public void switchToMenu(ActionEvent event) throws IOException {
    restart();
    Parent root = FXMLLoader.load(getClass().getResource("/mcts/GuiMenu.fxml"));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(root));
    stage.show();
  }

  public void restart() {
    game.restart();
    for (Circle[] row : circles) {
      for (Circle circle : row) {
        circle.setFill(Color.WHITE);
      }
    }
    labelPvP.setText("Player 1, please select a column!");
  }
}
