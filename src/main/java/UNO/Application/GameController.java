package UNO.Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.*;

public class GameController {

    @FXML
    private Label deckLabel;

    @FXML
    private Label playerLabel;

    @FXML
    private HBox cardsBox;

    @FXML
    private Button drawButton;

    private int currentPlayer = 1;
    private int totalPlayers = 2;
    private int direction = 1;

    private List<String> deck = new ArrayList<>();
    private List<List<String>> hands = new ArrayList<>();
    private List<Integer> activePlayers = new ArrayList<>();
    private List<Integer> ranking = new ArrayList<>();
    private String topCard = "";
    private String topColor = "";

    private boolean skipNext = false;
    private boolean blockNext = false;
    private boolean hasDrawn = false;

    public void initGame(int nbPlayers) {
        this.totalPlayers = nbPlayers;
        generateDeck();
        dealCards();
        activePlayers.clear();
        for (int i = 1; i <= nbPlayers; i++) activePlayers.add(i);
        ranking.clear();
        topCard = deck.remove(0);
        topColor = getCardColor(topCard);
        currentPlayer = activePlayers.get(0);
        direction = 1;
        skipNext = false;
        blockNext = false;
        hasDrawn = false;
        updateView();
    }

    private void generateDeck() {
        deck.clear();
        String[] colors = {"Rouge", "Bleu", "Vert", "Jaune"};
        for (String color : colors) {
            for (int i = 0; i <= 9; i++) {
                deck.add(color + " " + i);
                if (i != 0) deck.add(color + " " + i);
            }
            for (int i = 0; i < 2; i++) {
                deck.add(color + " +2");
                deck.add(color + " Inverse");
                deck.add(color + " Passe");
            }
        }
        for (int i = 0; i < 4; i++) {
            deck.add("Joker");
            deck.add("Joker +4");
        }
        Collections.shuffle(deck);
    }

    private void dealCards() {
        hands.clear();
        for (int i = 0; i < totalPlayers; i++) {
            List<String> hand = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                hand.add(deck.remove(0));
            }
            hands.add(hand);
        }
    }

    private List<String> getCurrentHand() {
        return hands.get(currentPlayer - 1);
    }

    private void updateView() {
        deckLabel.setText("Paquet actuel : " + topCard + " (" + topColor + ")");
        playerLabel.setText("Tour du joueur " + currentPlayer);
        updateHandButtons();
        if (drawButton != null) drawButton.setDisable(canPlayAnyCard() || deck.isEmpty());
    }

    private void updateHandButtons() {
        cardsBox.getChildren().clear();
        for (String card : getCurrentHand()) {
            String imageName = card.replace(" ", "_") + ".png";
            Image image = null;
            try {
                image = new Image(getClass().getResourceAsStream("/images/" + imageName));
                if (image.isError() || image.getWidth() == 0) throw new Exception();
            } catch (Exception e) {
                image = null;
            }
            if (image == null || image.isError() || image.getWidth() == 0) {
                Button btn = new Button(card);
                btn.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
                btn.setOnAction(ev -> playCard(card));
                cardsBox.getChildren().add(btn);
                continue;
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(80);
            imageView.setFitHeight(120);
            imageView.setPreserveRatio(true);

            imageView.setOnMouseEntered(e -> imageView.setStyle("-fx-effect: dropshadow(gaussian, #333, 10, 0, 0, 0); -fx-cursor: hand;"));
            imageView.setOnMouseExited(e -> imageView.setStyle(""));
            imageView.setOnMouseClicked(e -> playCard(card));

            cardsBox.getChildren().add(imageView);
        }
    }

    private boolean isCardPlayable(String card) {
        if (card.startsWith("Joker")) return true;
        String color = getCardColor(card);
        String value = getCardValue(card);
        String topValue = getCardValue(topCard);
        return color.equals(topColor) || value.equals(topValue);
    }

    private String getCardColor(String card) {
        if (card.startsWith("Joker")) return topColor.isEmpty() ? "Rouge" : topColor;
        return card.split(" ")[0];
    }

    private String getCardValue(String card) {
        if (card.startsWith("Joker")) return card;
        return card.substring(card.indexOf(" ") + 1);
    }

    private String askColorCancelable() {
        List<String> colors = Arrays.asList("Rouge", "Bleu", "Vert", "Jaune");
        ChoiceDialog<String> dialog = new ChoiceDialog<>(colors.get(0), colors);
        dialog.setTitle("Choix de la couleur");
        dialog.setHeaderText("Vous devez choisir une couleur pour continuer");
        dialog.setContentText("Couleur :");
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private void playCard(String card) {
        if (!isCardPlayable(card)) {
            showAlert("Carte non valide", "Vous ne pouvez pas poser cette carte !");
            return;
        }

        String value = getCardValue(card);

        if (value.equals("+4")) {
            String chosenColor = askColorCancelable();
            if (chosenColor == null) return;
            getCurrentHand().remove(card);
            topCard = card;
            topColor = chosenColor;
            // Passe au joueur suivant
            nextPlayer();
            // Donne 4 cartes au nouveau joueur courant
            drawCards(getCurrentHand(), 4);
            blockNext = false; // Pas besoin de bloquer, le joueur a déjà été avancé
            updateView();
            return;
        } else {
            getCurrentHand().remove(card);
            topCard = card;
            topColor = getCardColor(card);

            if (value.equals("Inverse")) {
                if (activePlayers.size() == 2) {
                    skipNext = true;
                } else {
                    direction *= -1;
                }
            } else if (value.equals("Passe")) {
                skipNext = true;
            } else if (value.equals("+2")) {
                int nextIdx = (activePlayers.indexOf(currentPlayer) + direction + activePlayers.size()) % activePlayers.size();
                int nextPlayer = activePlayers.get(nextIdx);
                drawCards(hands.get(nextPlayer - 1), 2);
                blockNext = true;
            } else if (card.startsWith("Joker")) {
                String chosenColor = askColorCancelable();
                if (chosenColor == null) return;
                topColor = chosenColor;
            }
        }

        if (getCurrentHand().isEmpty()) {
            ranking.add(currentPlayer);
            activePlayers.remove((Integer) currentPlayer);

            if (activePlayers.size() == 1 || totalPlayers == 2) {
                if (activePlayers.size() == 1) ranking.add(activePlayers.get(0));
                showRankingScene();
                return;
            }
        }
        nextPlayer();
    }

    public void nextPlayer() {
        if (activePlayers.size() == 0) return;
        int idx = activePlayers.indexOf(currentPlayer);

        // Gestion du blocage (pour +2/+4)
        if (blockNext) {
            idx = (idx + direction + activePlayers.size()) % activePlayers.size();
            currentPlayer = activePlayers.get(idx);
            blockNext = false;
            updateView();
            return;
        }

        // Gestion du saut de tour (Passe/Inverse à 2 joueurs)
        if (skipNext) {
            idx = (idx + direction + activePlayers.size()) % activePlayers.size(); // saute 1
            idx = (idx + direction + activePlayers.size()) % activePlayers.size(); // saute 2
            currentPlayer = activePlayers.get(idx);
            skipNext = false;
            updateView();
            return;
        }

        // Passage normal au joueur suivant
        idx = (idx + direction + activePlayers.size()) % activePlayers.size();
        currentPlayer = activePlayers.get(idx);
        updateView();
    }

    private void drawCards(List<String> hand, int n) {
        for (int i = 0; i < n && !deck.isEmpty(); i++) {
            hand.add(deck.remove(0));
        }
    }

    @FXML
    private void drawCardAction() {
        if (deck.isEmpty()) {
            showAlert("Pioche vide", "Il n'y a plus de cartes à piocher.");
            return;
        }
        getCurrentHand().add(deck.remove(0));
        updateHandButtons();
        if (drawButton != null) drawButton.setDisable(canPlayAnyCard() || deck.isEmpty());
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private boolean canPlayAnyCard() {
        for (String card : getCurrentHand()) {
            if (isCardPlayable(card)) return true;
        }
        return false;
    }


    private String askColorMandatory() {
        List<String> colors = Arrays.asList("Rouge", "Bleu", "Vert", "Jaune");
        String chosenColor = null;
        while (chosenColor == null) {
            ChoiceDialog<String> dialog = new ChoiceDialog<>(colors.get(0), colors);
            dialog.setTitle("Choix de la couleur");
            dialog.setHeaderText("Vous devez choisir une couleur pour continuer");
            dialog.setContentText("Couleur :");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                chosenColor = result.get();
            }
        }
        return chosenColor;
    }

    private void showRankingScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RankingView.fxml"));
            Parent rankingRoot = loader.load();

            RankingController rankingController = loader.getController();
            rankingController.setRanking(ranking);

            Stage stage = (Stage) cardsBox.getScene().getWindow();
            stage.setScene(new Scene(rankingRoot, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}