package controllers.tableview;

import client.SetTableSender;
import client.tableviewsenders.CloseTableSender;
import client.tableviewsenders.PrintStartersSender;
import client.tableviewsenders.TableValuesSender;
import controllers.Controller;
import databaseparams.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.json.JSONArray;
import org.json.JSONObject;
import packettypes.TableValuesColumns;
import java.io.IOException;
import java.util.*;

public class MainTableController extends Controller {
    ArrayList<AddedProductHBox> addedProductHBoxes = new ArrayList<>();
    @FXML
    private ListView<HBox> addedProductsListView;
    @FXML
    private ListView<String> productsListView;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private ListView<String> categoriesListView;
    @FXML
    private TextField peopleCountTextField;
    @FXML
    private Label tableNameLabelSHUTDOWN;
    private String oldPeopleCount;
    private String tableName;


    public void setTableWindow(String tableNameToSet) throws IOException {
        tableName = tableNameToSet;
        setTableNameLabel(tableNameToSet);
        fetchTableValues();
        oldPeopleCount = peopleCountTextField.getText();
        setCategoriesListView();
    }

    private void fetchTableValues() throws IOException {
        ProgressIndicator progressIndicator = setProgressIndicator(mainPane);
        TableValuesSender tableValuesSender = new TableValuesSender(getClientSocket().getSocket());
        JSONObject tableValues = tableValuesSender.sendPacket(tableNameLabelSHUTDOWN.getText());
        mainPane.getChildren().remove(progressIndicator);
        peopleCountTextField.setText(String.valueOf(tableValues.getInt(TableValuesColumns.PEOPLECOUNT.toString())));
        JSONArray productsJSONArr = tableValues.getJSONArray(TableValuesColumns.PRODUCTS.toString());
        for (int JSONIndex = 0; JSONIndex < productsJSONArr.length(); ++JSONIndex) {
            JSONObject productJSONObj = productsJSONArr.getJSONObject(JSONIndex);
            String productName = productJSONObj.getString(TableValuesColumns.PRODUCTNAME.toString());
            int productQuantity = productJSONObj.getInt(TableValuesColumns.PRODUCTQUANTITY.toString());
            float productPrice = (float) productJSONObj.getDouble(TableValuesColumns.PRODUCTPRICE.toString());
            int productType = productJSONObj.getInt(TableValuesColumns.PRODUCTTYPE.toString()) - 1;
            String productComment = productJSONObj.getString(TableValuesColumns.PRODUCTCOMMENT.toString());
            addedProductHBoxes.add(new AddedProductHBox(productName, productPrice, addedProductsListView, productType, productComment));
            addedProductHBoxes.get(addedProductHBoxes.size() - 1).setQuantity(productQuantity);
        }
    }

    private void setCategoriesListView() {
        categoriesListView.getItems().clear();
        categoriesListView.setOnMouseClicked(mouseEvent -> {
            try {
                String category = categoriesListView.getSelectionModel().getSelectedItem();
                Categories categoryEnum = Enum.valueOf(Categories.class, category.toUpperCase(Locale.ROOT));
                productsListView.getItems().clear();
                for (Enum<?> products : categoryEnum.getComponents()) {
                    productsListView.getItems().add(products.toString());
                }
            } catch (Exception ignored) {}
        });
        productsListView.setOnMouseClicked(mouseEvent -> {
            String product = productsListView.getSelectionModel().getSelectedItem();
            boolean absent = true;
            for (int alreadyAddedProductIndex = 0; alreadyAddedProductIndex < addedProductHBoxes.size(); ++alreadyAddedProductIndex) {
                if (Objects.equals(addedProductHBoxes.get(alreadyAddedProductIndex).getProduct(), product)) {
                    addedProductHBoxes.get(alreadyAddedProductIndex).incQuantity();
                    absent = false;
                } else if (addedProductHBoxes.get(alreadyAddedProductIndex).getQuantity() == 0) {
                    addedProductsListView.getItems().remove(addedProductHBoxes.get(alreadyAddedProductIndex).getProductHBox());
                    addedProductHBoxes.remove(addedProductHBoxes.get(alreadyAddedProductIndex));
                    --alreadyAddedProductIndex;
                }
            }
            if (absent) {
                String category = categoriesListView.getSelectionModel().getSelectedItem();
                if (Objects.equals(category, "Makis")) {
                    for (Makis makisEnum : Makis.values()) {
                        if (Objects.equals(makisEnum.toString(), product)) {
                            addedProductHBoxes.add(new AddedProductHBox(product, makisEnum.getPrice(), addedProductsListView, 1, ""));
                        }
                    }
                }
                else if (Objects.equals(category, "Nigiris")) {
                    for (Nigiris nigirisEnum : Nigiris.values()) {
                        if (Objects.equals(nigirisEnum.toString(), product)) {
                            addedProductHBoxes.add(new AddedProductHBox(product, nigirisEnum.getPrice(), addedProductsListView, 1, ""));
                        }
                    }
                } else if (Objects.equals(category, "Softs")) {
                    for (Softs softsEnum : Softs.values()) {
                        if (Objects.equals(softsEnum.toString(), product)) {
                            addedProductHBoxes.add(new AddedProductHBox(product, softsEnum.getPrice(), addedProductsListView, 3, ""));
                        }
                    }
                } else if (Objects.equals(category, "Starters")) {
                    for (Starters startersEnum : Starters.values()) {
                        if (Objects.equals(startersEnum.toString(), product)) {
                            addedProductHBoxes.add(new AddedProductHBox(product, startersEnum.getPrice(), addedProductsListView, 0, ""));
                        }
                    }
                } else if (Objects.equals(category, "Urumakis")) {
                    for (Urumakis urumakisEnum : Urumakis.values()) {
                        if (Objects.equals(urumakisEnum.toString(), product)) {
                            addedProductHBoxes.add(new AddedProductHBox(product, urumakisEnum.getPrice(), addedProductsListView, 1, ""));
                        }
                    }

                }
            }
        });
        categoriesListView.getItems().add("       ");
        for (Categories category : Categories.values()) {
            categoriesListView.getItems().add(category.toString());
        }
    }

    private void setTableNameLabel(String tableName) {
        tableNameLabelSHUTDOWN.setText(tableName);
    }

    public void setPeopleCount() {
        if (!Objects.equals(peopleCountTextField.getText(), "")) {
             try {
                 Integer.parseInt(peopleCountTextField.getText());
                 oldPeopleCount = peopleCountTextField.getText();
             } catch (Exception ignored) {
                 if (peopleCountTextField.getText().length() > 1) {
                     peopleCountTextField.setText(oldPeopleCount);
                 } else {
                     peopleCountTextField.setText("");
                 }
             }
             if (Objects.equals(peopleCountTextField.getText(), "")) {
                 peopleCountTextField.setText("0");
             }
             peopleCountTextField.positionCaret(peopleCountTextField.getText().length());
        }
    }

    public void positionCaret() {
        peopleCountTextField.positionCaret(peopleCountTextField.getText().length());
    }

    public void back(ActionEvent actionEvent) throws IOException {
        int peopleCount = Integer.parseInt(peopleCountTextField.getText());
        List<AddedProductHBox> allProducts = addedProductHBoxes;
        SetTableSender setTableSender = new SetTableSender(getClientSocket().getSocket());
        setTableSender.sendPacket(tableName, peopleCount, allProducts);
        goToMainMenu(actionEvent);
    }

    public void close(ActionEvent actionEvent) throws IOException {
        ProgressIndicator progressIndicator = setProgressIndicator(mainPane);
        CloseTableSender closeTableSender = new CloseTableSender(getClientSocket().getSocket());
        closeTableSender.sendPacket(tableName);
        mainPane.getChildren().remove(progressIndicator);
        goToMainMenu(actionEvent);
    }

    public void printDishes(ActionEvent actionEvent) {
    }

    public void printStarters() throws IOException {
        ProgressIndicator progressIndicator = setProgressIndicator(mainPane);
        PrintStartersSender printStartersSender = new PrintStartersSender(getClientSocket().getSocket());
        boolean confirmed = printStartersSender.sendPacket(tableName);
        if (!confirmed) {
            alert("List no printed.");
        }
        mainPane.getChildren().remove(progressIndicator);
    }

    public void printDrinks(ActionEvent actionEvent) {
    }
}
