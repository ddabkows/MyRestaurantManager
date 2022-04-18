package controllers.restaurantview;

import controllers.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;

public class PeopleCountToTableController extends Controller {
    @FXML
    public TextField peopleCountInputTextField;
    @FXML
    public Button people2;
    @FXML
    public Button people3;
    @FXML
    public Button people4;
    @FXML
    public Button people5;
    @FXML
    public Button people6;
    @FXML
    public Button confirmButton;
    @FXML
    public Button cancelButton;

    private int countInput;

    public void decreaseInputCount() {
        if (countInput > 1) {
            --countInput;
            peopleCountInputTextField.setText(String.valueOf(countInput));
        }
    }

    public void increaseInputCount() {
        ++countInput;
        peopleCountInputTextField.setText(String.valueOf(countInput));
    }

    public int getCountInput() {return this.countInput;}

    public void setCountInput(int countInputToSet) {
        this.countInput = countInputToSet;
    }

    public void setPeopleCountInputTextField() {
        peopleCountInputTextField.setText(String.valueOf(countInput));
    }
}
