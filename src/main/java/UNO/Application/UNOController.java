package UNO.Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UNOController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button startButton;

    @FXML
    private ComboBox<Integer> playerCount;

    @FXML
    private Button confirmButton;



    @FXML
    public void initialize() {
        welcomeLabel.setText("Bienvenue dans le jeu UNO !");
        playerCount.getItems().addAll(2, 3, 4, 5, 6);
        playerCount.setVisible(false);
        confirmButton.setVisible(false);

        startButton.setOnAction(e -> {
            welcomeLabel.setText("Choisissez le nombre de joueurs :");
            startButton.setVisible(false);
            playerCount.setVisible(true);
            confirmButton.setVisible(true);
        });

        confirmButton.setOnAction(e -> {
            Integer selectedPlayers = playerCount.getValue();
            if (selectedPlayers != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameView.fxml"));
                    Parent gameRoot = loader.load();

                    GameController gameController = loader.getController();
                    gameController.initGame(selectedPlayers);

                    Stage stage = (Stage) confirmButton.getScene().getWindow();
                    stage.setScene(new Scene(gameRoot, 800, 600));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                welcomeLabel.setText("Veuillez sélectionner un nombre de joueurs !");
            }
        });
    }
}