package UNO.Application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class RankingController {

    @FXML
    private VBox rankingBox;

    public void setRanking(List<Integer> ranking) {
        rankingBox.getChildren().clear();
        for (int i = 0; i < ranking.size(); i++) {
            int player = ranking.get(i);
            Label label = new Label((i + 1) + "ᵉ place : Joueur " + player);
            label.setStyle("-fx-font-size: 18px;");
            rankingBox.getChildren().add(label);
        }
    }
}