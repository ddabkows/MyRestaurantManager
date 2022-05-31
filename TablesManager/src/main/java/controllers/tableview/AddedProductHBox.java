package controllers.tableview;

import databaseparams.Starters;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.Objects;

public class AddedProductHBox {
    private final String product;
    private final float price;
    private int quantity = 1;
    private int type;
    private final TextField quantityTextField = new TextField("1");
    private final ChoiceBox<Integer> typeComboBox = new ChoiceBox<>();
    private final HBox productHBox;
    private ListView<HBox> listView;

    public AddedProductHBox(String productToSet, float priceToSet, ListView<HBox> listViewToSet, int productType) {
        product = productToSet;
        price = priceToSet;
        listView = listViewToSet;
        Label productLabel = new Label(productToSet);
        productLabel.setFont(new Font(15));
        productLabel.setPrefSize(170, 7);
        Button minusButton = new Button("-");
        minusButton.setPrefSize(7, 7);
        minusButton.setFont(new Font(10));
        quantityTextField.setPrefSize(40, 7);
        quantityTextField.setFont(new Font(10));
        typeComboBox.setPrefSize(5, 9);
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
        setListeners(minusButton, plusButton);
        productHBox = new HBox(productLabel, minusButton, quantityTextField, plusButton, separator, typeComboBox);
        productHBox.setAlignment(Pos.CENTER);
        listView.getItems().add(productHBox);
    }

    private void setListeners(Button minusButton, Button plusButton) {
        minusButton.setOnAction(actionEvent -> {
            decQuantity();
        });
        plusButton.setOnAction(actionEvent -> {
            incQuantity();
        });
        quantityTextField.setOnKeyReleased(keyEvent -> {
            if (!Objects.equals(quantityTextField.getText(), "")) {
                try {
                    quantity = Integer.parseInt(quantityTextField.getText());
                } catch (Exception ignored) {}
            } else {
                quantity = 0;}
            quantityTextField.setText(String.valueOf(quantity));
            quantityTextField.positionCaret(quantityTextField.getText().length());
        });
    }

    public void setQuantity(int quantityToSet) {
        quantity = quantityToSet;
        quantityTextField.setText(String.valueOf(quantityToSet));
    }

    private void decQuantity() {
        if (quantity > 1) {
            --quantity;
            quantityTextField.setText(String.valueOf(quantity));
        }
    }

    public void incQuantity() {
        ++ quantity;
        quantityTextField.setText(String.valueOf(quantity));
    }

    public String getProduct() {return product;}
    public float getPrice() {return price;}
    public int getQuantity() {return quantity;}
    public HBox getProductHBox() {return productHBox;}

    public int getType() {return type;}
}
