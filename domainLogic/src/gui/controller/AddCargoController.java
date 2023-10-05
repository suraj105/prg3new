package gui.controller;

import administration.Customer;
import cargo.CargoType;
import cargo.Hazard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import management.WarehouseManagementSystem;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddCargoController
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<String> cbCustomers;

    @FXML
    private CheckBox cbExplosive;

    @FXML
    private CheckBox cbFlammable;

    @FXML
    private CheckBox cbRadioactive;

    @FXML
    private CheckBox cbToxic;

    @FXML
    private ChoiceBox<CargoType> cbTypes;

    @FXML
    private RadioButton rbFragile;

    @FXML
    private RadioButton rbPressurised;

    @FXML
    private TextField txtGrainSize;

    @FXML
    private TextField txtValue;

    private WarehouseManagementSystem system;

    private GUIController parentController;

    public void setParentController(GUIController parentController)
    {
        this.parentController = parentController;
    }

    @FXML
    void btnCancel(MouseEvent event)
    {
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

    @FXML
    void btnInsert(MouseEvent event)
    {
        String name = cbCustomers.getSelectionModel().getSelectedItem();
        CargoType type = cbTypes.getSelectionModel().getSelectedItem();
        BigDecimal value = txtValue.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtValue.getText());
        List<Hazard> hazards = new ArrayList<>();
        boolean fragile = rbFragile.isSelected();
        boolean pressurized = rbPressurised.isSelected();
        int grainSize = Optional.ofNullable(txtGrainSize.getText()).filter(s -> !s.isEmpty() && !s.isBlank()).map(Integer::parseInt).orElse(0);

        if (cbExplosive.isSelected())
        {
            hazards.add(Hazard.valueOf(cbExplosive.getText().toLowerCase()));
        }
        if (cbFlammable.isSelected())
        {
            hazards.add(Hazard.valueOf(cbFlammable.getText().toLowerCase()));
        }
        if (cbRadioactive.isSelected())
        {
            hazards.add(Hazard.valueOf(cbRadioactive.getText().toLowerCase()));
        }
        if (cbToxic.isSelected())
        {
            hazards.add(Hazard.valueOf(cbToxic.getText().toLowerCase()));
        }

        if (name.isEmpty() || name.isBlank())
        {
            parentController.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Kindly enter proper information");
            return;
        }
        if (type == null)
        {
            parentController.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Kindly enter proper information");
            return;
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0)
        {
            parentController.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Kindly enter proper information");
            return;
        }

        system.insertCargoItem(type, name, value, hazards, fragile, pressurized, grainSize);
        parentController.showAlert(Alert.AlertType.INFORMATION, "Cargo Inserted", "Cargo item has been inserted successfully");

        parentController.loadData();
        clearFields();
    }

    @FXML
    void initialize()
    {
        this.system = parentController.getSystem();

        txtGrainSize.setDisable(true);
        rbFragile.setDisable(true);
        rbPressurised.setDisable(true);

        List<String> items = new ArrayList<>();
        for (Customer customer : system.getCustomers())
        {
            items.add(customer.getName());
        }
        cbCustomers.getItems().addAll(items);

        ObservableList<CargoType> enumValues = FXCollections.observableArrayList(CargoType.values());
        cbTypes.setItems(enumValues);

        txtGrainSize.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0,
                change ->
                {
                    if (change.getControlNewText().matches("-?\\d*"))
                    {
                        return change;
                    }
                    return null;
                }));
        txtValue.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), 0.0,
                change ->
                {
                    if (change.getControlNewText().matches("-?\\d*(\\.\\d*)?"))
                    {
                        return change;
                    }
                    return null;
                }));

        cbTypes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue != null)
            {
                switch (newValue)
                {
                    case DryBulkAndUnitisedCargo ->
                    {
                        txtGrainSize.setDisable(false);
                        rbFragile.setDisable(false);
                        rbPressurised.setDisable(true);
                    }
                    case DryBulkCargo ->
                    {
                        txtGrainSize.setDisable(false);
                        rbFragile.setDisable(true);
                        rbPressurised.setDisable(true);
                    }
                    case LiquidAndDryBulkCargo ->
                    {
                        txtGrainSize.setDisable(false);
                        rbFragile.setDisable(true);
                        rbPressurised.setDisable(false);
                    }
                    case LiquidBulkAndUnitisedCargo ->
                    {
                        txtGrainSize.setDisable(true);
                        rbFragile.setDisable(false);
                        rbPressurised.setDisable(false);
                    }
                    case LiquidBulkCargo ->
                    {
                        txtGrainSize.setDisable(true);
                        rbFragile.setDisable(true);
                        rbPressurised.setDisable(false);
                    }
                    case UnitisedCargo ->
                    {
                        txtGrainSize.setDisable(true);
                        rbFragile.setDisable(false);
                        rbPressurised.setDisable(true);
                    }
                }
            }
        });
    }

    private void clearFields()
    {
        cbCustomers.getSelectionModel().clearSelection();
        cbTypes.getSelectionModel().clearSelection();

        cbExplosive.setSelected(false);
        cbFlammable.setSelected(false);
        cbRadioactive.setSelected(false);
        cbToxic.setSelected(false);

        rbFragile.setSelected(false);
        rbPressurised.setSelected(false);

        txtValue.clear();
        txtGrainSize.clear();
    }

}
