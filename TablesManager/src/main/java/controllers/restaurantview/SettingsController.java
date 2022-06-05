package controllers.restaurantview;

import controllers.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SettingsController extends Controller {
    @FXML
    private ComboBox<String> printersComboBox;

    public void back(ActionEvent actionEvent) throws IOException {
        goToMainMenu(actionEvent);
    }

    public void setPrintersComboBox() {
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
        PrintRequestAttributeSet patts = new HashPrintRequestAttributeSet();
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, patts);
        for (PrintService printService : printServices) {
            printersComboBox.getItems().add(printService.getName());
        }
    }

    private void setConfigFile() {
        try {
            BufferedReader file = new BufferedReader(new FileReader("config.cfg"));
            StringBuilder inputBuffer = new StringBuilder();
            String line;
            while ((line = file.readLine()) != null) {
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            file.close();
            String inputStr = inputBuffer.toString();
            int firsIndex = inputStr.indexOf("printer = ");
            int lastIndex = 0;
            for (int i=firsIndex; i<inputStr.length(); ++i) {
                if (inputStr.charAt(i) == '\n') {
                    lastIndex = i + 1;
                    break;
                }
            }
            String subStringToReplace = inputStr.substring(firsIndex, lastIndex);
            String printer = printersComboBox.getSelectionModel().getSelectedItem();
            if (printer != null) {
                inputStr = inputStr.replaceAll(subStringToReplace, "printer = " + printer + "\n");
                setPrinterPath(printer);
            } else {
                inputStr = inputStr.replaceAll(subStringToReplace, "printer = none\n");
                setPrinterPath("none");
            }
            FileOutputStream fileOutputStream = new FileOutputStream("config.cfg");
            fileOutputStream.write(inputStr.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        setConfigFile();
    }
}
