package gui.controller;

import administration.Customer;
import cargo.AbstractCargo;
import cargo.Hazard;
import gui.GraphicalUserInterface;
import gui.model.CargoModel;
import gui.model.CustomerModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import management.WarehouseManagementSystem;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class GUIController
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnDeleteCargo;

    @FXML
    private Button btnDeleteCustomer;

    @FXML
    private Button btnInspectCargo;

    @FXML
    private TableColumn<CustomerModel, Integer> colCount;

    @FXML
    private TableColumn<CustomerModel, String> colCustomerName;

    @FXML
    private TableColumn<CargoModel, String> colDuration;

    @FXML
    private TableColumn<CargoModel, String> colInsDate;

    @FXML
    private TableColumn<CargoModel, Integer> colLocation;

    @FXML
    private TableColumn<CargoModel, String> colType;

    @FXML
    private ListView<String> listHazards;

    @FXML
    private RadioButton rbAbsent;

    @FXML
    private RadioButton rbPresent;

    @FXML
    private TableView<CargoModel> tbCargo;

    @FXML
    private TableView<CustomerModel> tbCustomer;

    @FXML
    private TextField txtLoadFilename;

    @FXML
    private TextField txtSaveFilename;

    @FXML
    public TextField txtCustomerName;

    private WarehouseManagementSystem system;
    private static CustomerModel selectedCustomer;
    private static CargoModel selectedCargo;

    public WarehouseManagementSystem getSystem()
    {
        return system;
    }

    @FXML
    void btnDeleteCargo(MouseEvent event)
    {
        if (selectedCargo != null)
        {
            system.removeCargoItem(selectedCargo.getLocation());
            showAlert(AlertType.INFORMATION, "Cargo Deleted", "Cargo Item has been deleted successfully");
            tbCargo.getSelectionModel().clearSelection();
            loadData();
        }
        btnDeleteCargo.setDisable(true);
    }

    @FXML
    void btnDeleteCustomer(MouseEvent event)
    {
        if (selectedCustomer != null)
        {
            system.deleteCustomer(selectedCustomer.getName());
            showAlert(AlertType.INFORMATION, "Customer Deleted", "Customer has been deleted successfully");
            tbCustomer.getSelectionModel().clearSelection();
            loadData();
        }
        btnDeleteCustomer.setDisable(true);
    }

    @FXML
    void btnInsertCargo(MouseEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/gui/view/add_cargo.fxml"));
            loader.setControllerFactory(c ->
            {
                AddCargoController controller = new AddCargoController();
                controller.setParentController(this);
                return controller;
            });
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Cargo Insertion");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception exception)
        {
            exception.printStackTrace();
            showAlert(AlertType.ERROR, "Exception", exception.getMessage());
        }
    }

    @FXML
    void btnInsertCustomer(MouseEvent event)
    {
        String name = txtCustomerName.getText().trim();

        for (Customer customer : system.getCustomers())
        {
            if (name.equals(customer.getName()))
            {
                showAlert(AlertType.ERROR, "Customer Insertion", "Customer is already exist");
                txtCustomerName.clear();
                return;
            }
        }

        if (name.isEmpty() || name.isBlank())
        {
            showAlert(AlertType.ERROR, "Invalid Input", "Kindly enter proper information");
            return;
        }

        system.createCustomer(name);
        txtCustomerName.clear();
        showAlert(AlertType.INFORMATION, "Customer Insertion", "Customer has been inserted successfully");

        loadData();
    }

    @FXML
    void btnInspectCargo(MouseEvent event)
    {
        if (selectedCargo == null) return;

        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton rbNow = new RadioButton("Inspect Now");
        RadioButton rbDate = new RadioButton("Pick Inspection Date");
        DatePicker datePicker = new DatePicker();

        rbNow.setToggleGroup(toggleGroup);
        rbDate.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue.equals(rbNow))
            {
                rbNow.setDisable(false);
                datePicker.setDisable(true);

            } else if (newValue.equals(rbDate))
            {
                rbNow.setDisable(true);
                datePicker.setDisable(false);
            }
        });

        LocalDate currentDate = LocalDate.now();
        datePicker.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue != null && newValue.isAfter(currentDate))
            {
                datePicker.setValue(currentDate);
                showAlert(AlertType.ERROR, "Invalid Date", "Selected date cannot be after today.");
            }
        });

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(rbNow, rbDate, datePicker);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cargo Inspection");
        alert.getDialogPane().setContent(vbox);

        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().remove(ButtonType.CANCEL);

        ButtonType btnInspect = new ButtonType("Inspect Cargo");
        alert.getButtonTypes().add(btnInspect);
        ButtonType btnCancel = new ButtonType("Cancel");
        alert.getButtonTypes().add(btnCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btnInspect)
        {
            if (!rbNow.isSelected() && !rbDate.isSelected()) return;

            Date inspectionDate = null;
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            if (rbNow.isSelected())
            {
                inspectionDate = parseDate(formatter.format(Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant())));
            } else if (rbDate.isSelected())
            {
                inspectionDate = parseDate(formatter.format(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())));
            }
            system.setInspectionDate(selectedCargo.getLocation(), inspectionDate);
        } else if (result.isPresent() && result.get() == btnCancel)
        {
            alert.close();
        }
        loadData();
    }

    @FXML
    void btnLoadInstance(MouseEvent event)
    {
        String filename = txtLoadFilename.getText().trim();

        if (filename.isEmpty() || filename.isBlank())
        {
            showAlert(AlertType.ERROR, "Invalid Input", "Kindly enter proper information");
            return;
        }

        system.loadJOS(filename);
        loadData();
        txtLoadFilename.clear();
        showAlert(AlertType.INFORMATION, "Instance Loaded", "Instance has been loaded successfully");
    }

    @FXML
    void btnSaveInstance(MouseEvent event)
    {
        String filename = txtSaveFilename.getText().trim();

        if (filename.isEmpty() || filename.isBlank())
        {
            showAlert(AlertType.ERROR, "Invalid Input", "Kindly enter proper information");
            return;
        }

        system.saveJOS(filename);
        txtSaveFilename.clear();
        showAlert(AlertType.INFORMATION, "Instance Saved", "Instance has been saved successfully");
    }

    @FXML
    void btnShowHazards(MouseEvent event)
    {
        if (!rbPresent.isSelected() && !rbAbsent.isSelected())
            showAlert(AlertType.ERROR, "Invalid Input", "Kindly select one present / absent");

        Set<Hazard> hazards = system.getHazards();
        ObservableList<String> list = FXCollections.observableArrayList();

        if (rbPresent.isSelected())
        {
            if (!hazards.isEmpty())
            {
                list.add("****** Present Hazards ******");
                for (Hazard hazard : hazards)
                {
                    list.add(hazard.name());
                }
            } else
            {
                list.add("****** No present hazards ******");
            }
        } else if (rbAbsent.isSelected())
        {
            EnumSet<Hazard> absentHazards;

            if (hazards.isEmpty())
            {
                absentHazards = EnumSet.allOf(Hazard.class);
            } else
            {
                absentHazards = EnumSet.complementOf(EnumSet.copyOf(hazards));
            }

            if (!absentHazards.isEmpty())
            {
                list.add("****** Absent Hazards ******");
                for (Hazard hazard : absentHazards)
                {
                    list.add(hazard.name());
                }
            } else
            {
                list.add("****** No absent hazards ******");
            }
        }
        listHazards.setItems(list);
    }

    @FXML
    void getSelectedCustomer(MouseEvent mouseEvent)
    {
        selectedCustomer = tbCustomer.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null)
        {
            btnDeleteCustomer.setDisable(false);
        }
    }

    @FXML
    void getSelectedCargo(MouseEvent mouseEvent)
    {
        selectedCargo = tbCargo.getSelectionModel().getSelectedItem();
        if (selectedCargo != null)
        {
            btnInspectCargo.setDisable(false);
            btnDeleteCargo.setDisable(false);
        }
    }

    @FXML
    void initialize()
    {
        system = new WarehouseManagementSystem(1000);

        btnDeleteCustomer.setDisable(true);
        btnInspectCargo.setDisable(true);
        btnDeleteCargo.setDisable(true);

        ToggleGroup toggleGroup = new ToggleGroup();
        rbPresent.setToggleGroup(toggleGroup);
        rbAbsent.setToggleGroup(toggleGroup);

        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCount.setCellValueFactory(new PropertyValueFactory<>("itemsCount"));

        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colInsDate.setCellValueFactory(new PropertyValueFactory<>("insDate"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));

        loadData();
    }

    public void loadData()
    {
        populateCustomers();
        populateCargos();

        listHazards.setItems(null);
    }

    private void populateCustomers()
    {
        List<Customer> customers = system.getCustomers();
        List<CustomerModel> models = new ArrayList<>();

        for (Customer customer : customers)
        {
            CustomerModel customerModel = new CustomerModel(customer.getName(), system.getCargoItemCount(customer));
            models.add(customerModel);
        }
        ObservableList<CustomerModel> list = FXCollections.observableArrayList(models);

        tbCustomer.setItems(list);
    }

    private void populateCargos()
    {
        List<AbstractCargo> cargos = system.getCargos();
        List<CargoModel> models = new ArrayList<>();

        for (AbstractCargo cargo : cargos)
        {
            CargoModel model = new CargoModel(cargo.getClass().getSimpleName(), cargo.getStorageLocation(), cargo.getLastInspectionDate(), cargo.getDurationOfStorage());
            models.add(model);
        }
        ObservableList<CargoModel> list = FXCollections.observableArrayList(models);

        tbCargo.setItems(list);
    }

    public void showAlert(Alert.AlertType type, String title, String message)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private Date parseDate(String dateString)
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            return format.parse(dateString);
        } catch (ParseException e)
        {
            return null;
        }
    }
}
