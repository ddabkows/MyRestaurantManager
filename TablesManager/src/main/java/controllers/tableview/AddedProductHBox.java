package controllers.tableview;

import controllers.resources.Images;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.Objects;
import java.util.Optional;

public class AddedProductHBox {
    private final String product;
    private final float price;
    private int quantity = 1;
    private int type;
    private final TextField quantityTextField = new TextField("1");
    private final HBox productHBox;
    private String comment;
    private final Button commentButton = new Button();
    private final Label quantityPriceText;

    public AddedProductHBox(String productToSet, float priceToSet, ListView<HBox> listViewToSet, int productType, String productComment) {
        comment = productComment;
        product = productToSet;
        price = priceToSet;
        Label productLabel = new Label(productToSet);
        productLabel.setFont(new Font(13));
        productLabel.setPrefSize(115, 7);
        Button minusButton = new Button("-");
        minusButton.setPrefSize(7, 7);
        minusButton.setFont(new Font(10));
        Label priceText = new Label(String.valueOf(price));
        priceText.setFont(new Font(10));
        priceText.setPrefSize(30, 7);
        quantityPriceText = new Label(String.valueOf(price));
        quantityPriceText.setFont(new Font(10));
        quantityPriceText.setPrefSize(30, 7);
        quantityTextField.setFont(new Font(10));
        quantityTextField.setPrefSize(30, 7);
        ChoiceBox<Integer> typeComboBox = new ChoiceBox<>();
        typeComboBox.setPrefSize(5, 9);
        commentButton.setPrefSize(5, 5);
        commentButton.setFont(new Font(10));
        commentButton.setStyle("""
                                -fx-background-color: transparent;
                                """);
        Image commentImage = new Image(Images.COMMENT.toString());
        ImageView commentImgView = new ImageView(commentImage);
        commentImgView.setFitWidth(15); commentImgView.setFitHeight(15);
        commentButton.setGraphic(commentImgView);
        HBox separator = new HBox();
        separator.setPrefSize(5, 1);
        typeComboBox.setStyle("""
                                -fx-background-color:  #f8b195;
                                -fx-arrows-visible: false;
                                -fx-font-size: 10.0;
                                """);
        for (int type=1; type<5; ++type) {
            typeComboBox.getItems().add(type);
        }
        typeComboBox.getSelectionModel().select(productType);
        type = typeComboBox.getSelectionModel().getSelectedItem();
        Button plusButton = new Button("+");
        plusButton.setFont(new Font(10));
        plusButton.setPrefSize(7, 7);
        setListeners(minusButton, plusButton, typeComboBox);
        productHBox = new HBox(productLabel, priceText, quantityPriceText, commentButton, minusButton, quantityTextField, plusButton, separator, typeComboBox);
        productHBox.setAlignment(Pos.CENTER);
        listViewToSet.getItems().add(productHBox);
    }

    private String getFoodComment(String productName, String productComment) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Comment");
        dialog.setHeaderText(productName);

        TextArea textArea = new TextArea(productComment);
        dialog.getDialogPane().setContent(textArea);
        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) {
                return textArea.getText();
            }
            else {
                return productComment;
            }
        });
        Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }

    private void setListeners(Button minusButton, Button plusButton, ChoiceBox<Integer> typeComboBox) {
        minusButton.setOnAction(actionEvent -> decQuantity());
        plusButton.setOnAction(actionEvent -> incQuantity());
        commentButton.setOnAction(actionEvent -> comment = getFoodComment(product, comment));
        typeComboBox.setOnAction(actionEvent -> this.type = typeComboBox.getSelectionModel().getSelectedItem());
        quantityTextField.setOnKeyReleased(keyEvent -> {
            if (!Objects.equals(quantityTextField.getText(), "")) {
                try {
                    quantity = Integer.parseInt(quantityTextField.getText());
                } catch (Exception ignored) {}
            } else {
                quantity = 0;
            }
            quantityTextField.setText(String.valueOf(quantity));
            quantityPriceText.setText(String.valueOf(quantity * price));
            quantityTextField.positionCaret(quantityTextField.getText().length());
        });

    }

    public void setQuantity(int quantityToSet) {
        quantity = quantityToSet;
        quantityTextField.setText(String.valueOf(quantityToSet));
        quantityPriceText.setText(String.valueOf(quantityToSet * price));
    }

    private void decQuantity() {
        if (quantity > 1) {
            --quantity;
            quantityTextField.setText(String.valueOf(quantity));
            quantityPriceText.setText(String.valueOf(quantity * price));
        }
    }

    public void incQuantity() {
        ++ quantity;
        quantityTextField.setText(String.valueOf(quantity));
        quantityPriceText.setText(String.valueOf(quantity * price));
    }

    public String getProduct() {return product;}
    public float getPrice() {return price;}
    public int getQuantity() {return quantity;}
    public HBox getProductHBox() {return productHBox;}

    public int getType() {return type;}
    public String getComment() {return comment;}
}
