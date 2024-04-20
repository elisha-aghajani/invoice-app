package controllers;

import database.SQLiteDatabase;
import invoice.Invoice;
import invoice.InvoiceRow;
import invoice.Payment;
import invoice.PDFBuilder;
import invoice.BusinessEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import tablerows.CustomersTableRow;
import tablerows.InvoiceTableRow;
import tablerows.CompaniesTableRow;
import utils.Constants;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppController extends BaseController implements Initializable {

    @FXML
    private TableView<InvoiceTableRow> invoiceTable;

    @FXML
    private TableColumn<InvoiceTableRow, String> descriptionCol;

    @FXML
    private TableColumn<InvoiceTableRow, String> priceCol;

    @FXML
    private TableView<CompaniesTableRow> companiesTable;

    @FXML
    private TableColumn<CompaniesTableRow, String> companiesNameCol;

    @FXML
    private TableColumn<CompaniesTableRow, String> companiesStreetCol;

    @FXML
    private TableColumn<CompaniesTableRow, String> companiesStateCol;

    @FXML
    private TableColumn<CompaniesTableRow, String> companiesCityCol;

    @FXML
    private TableColumn<CompaniesTableRow, String> companiesZipcodeCol;

    @FXML
    private TableView<CustomersTableRow> customersTable;

    @FXML
    private TableColumn<CompaniesTableRow, String> customersNameCol;

    @FXML
    private TableColumn<CompaniesTableRow, String> customersStreetCol;

    @FXML
    private TableColumn<CompaniesTableRow, String> customersStateCol;

    @FXML
    private TableColumn<CompaniesTableRow, String> customersCityCol;

    @FXML
    private TableColumn<CompaniesTableRow, String> customersZipcodeCol;

    @FXML
    private Button insertInvoiceButton;

    @FXML
    private Button updateInvoiceButton;

    @FXML
    private Button deleteInvoiceButton;

    @FXML
    private Button insertCompanyButton;

    @FXML
    private Button updateCompanyButton;

    @FXML
    private Button deleteCompanyButton;

    @FXML
    private Button insertCustomerButton;

    @FXML
    private Button updateCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField companyNameField;

    @FXML
    private TextField companyStreetField;

    @FXML
    private TextField companyCityField;

    @FXML
    private ComboBox<String> companyStateBox;

    @FXML
    private TextField companyZipcodeField;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField customerStreetField;

    @FXML
    private TextField customerCityField;

    @FXML
    private ComboBox<String> customerStateBox;

    @FXML
    private TextField customerZipcodeField;

    @FXML
    private TextField subtotalField;

    @FXML
    private TextField paidField;

    @FXML
    private TextField totalField;

    private final ObservableList<InvoiceTableRow> invoiceTableRows = FXCollections.observableArrayList();
    private final ObservableList<CompaniesTableRow> companiesTableRows = FXCollections.observableArrayList();
    private final ObservableList<CustomersTableRow> customersTableRows = FXCollections.observableArrayList();

    private final ObservableList<String> states = FXCollections.observableArrayList(Constants.STATES);

    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        invoiceTable.setItems(invoiceTableRows);

        companiesNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        companiesStreetCol.setCellValueFactory(new PropertyValueFactory<>("Street"));
        companiesCityCol.setCellValueFactory(new PropertyValueFactory<>("City"));
        companiesStateCol.setCellValueFactory(new PropertyValueFactory<>("State"));
        companiesZipcodeCol.setCellValueFactory(new PropertyValueFactory<>("Zipcode"));
        companiesTable.setItems(companiesTableRows);

        customersNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        customersStreetCol.setCellValueFactory(new PropertyValueFactory<>("Street"));
        customersCityCol.setCellValueFactory(new PropertyValueFactory<>("City"));
        customersStateCol.setCellValueFactory(new PropertyValueFactory<>("State"));
        customersZipcodeCol.setCellValueFactory(new PropertyValueFactory<>("Zipcode"));
        customersTable.setItems(customersTableRows);

        companyStateBox.setItems(states);
        customerStateBox.setItems(states);
    }

    public void setup() {
        ArrayList<CompaniesTableRow> companies = SQLiteDatabase.getCompanies(userId);
        companiesTableRows.addAll(companies);

        ArrayList<CustomersTableRow> customers = SQLiteDatabase.getCustomers(userId);
        customersTableRows.addAll(customers);
    }

    @FXML
    public void invoiceTableMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                ObservableList<InvoiceTableRow> selectedRows = invoiceTable.getSelectionModel().getSelectedItems();
                InvoiceTableRow selectedRow = selectedRows.get(0);
                setInvoiceFields(selectedRow.getDescription(), selectedRow.getPrice());
                disableInvoiceButtons(true, false, false);
            } catch (IndexOutOfBoundsException ignored) {

            }
        } else if (event.getButton() == MouseButton.SECONDARY) {
            invoiceTable.getSelectionModel().select(null);
            clearInvoiceFields();
            disableInvoiceButtons(false, true, true);
        }
    }

    @FXML
    public void companiesTableMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                ObservableList<CompaniesTableRow> selectedRows = companiesTable.getSelectionModel().getSelectedItems();
                CompaniesTableRow selectedRow = selectedRows.get(0);
                setCompanyFields(selectedRow.getName(), selectedRow.getStreet(), selectedRow.getCity(), selectedRow.getState(), selectedRow.getZipcode());
                disableCompanyButtons(true, false, false);
            } catch (IndexOutOfBoundsException ignored) {

            }
        } else if (event.getButton() == MouseButton.SECONDARY) {
            companiesTable.getSelectionModel().select(null);
            clearCompanyFields();
            disableCompanyButtons(false, true, true);
        }
    }

    @FXML
    public void customersTableMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                ObservableList<CustomersTableRow> selectedRows = customersTable.getSelectionModel().getSelectedItems();
                CustomersTableRow selectedRow = selectedRows.get(0);
                setCustomerFields(selectedRow.getName(), selectedRow.getStreet(), selectedRow.getCity(), selectedRow.getState(), selectedRow.getZipcode());
                disableCustomerButtons(true, false, false);
            } catch (IndexOutOfBoundsException ignored) {

            }
        } else if (event.getButton() == MouseButton.SECONDARY) {
            customersTable.getSelectionModel().select(null);
            clearCustomerFields();
            disableCustomerButtons(false, true, true);
        }
    }

    @FXML
    public void insertInvoiceButtonAction() {
        if (!invoiceFieldsValid()) {
            return;
        }

        invoiceTableRows.add(new InvoiceTableRow(descriptionField.getText(), priceField.getText()));
        clearInvoiceFields();
    }

    @FXML
    public void updateInvoiceButtonAction() {
        if (!invoiceFieldsValid()) {
            return;
        }

        ObservableList<InvoiceTableRow> selectedRows = invoiceTable.getSelectionModel().getSelectedItems();
        InvoiceTableRow selectedRow = selectedRows.get(0);
        selectedRow.setDescription(descriptionField.getText());
        selectedRow.setPrice(priceField.getText());
        invoiceTable.refresh();

        clearInvoiceFields();
        disableInvoiceButtons(false, true, true);
    }

    @FXML
    public void deleteInvoiceButtonAction() {
        ObservableList<InvoiceTableRow> selectedRows = invoiceTable.getSelectionModel().getSelectedItems();
        invoiceTable.getItems().removeAll(selectedRows);

        clearInvoiceFields();
        disableInvoiceButtons(false, true, true);
    }

    @FXML
    public void insertCompanyButtonAction() {
        if (!companyFieldsValid()) {
            showErrorBox("Missing and/or incorrect company fields");
            return;
        }

        if (SQLiteDatabase.isCompany(companyNameField.getText(), userId)) {
            showErrorBox(String.format("Company with name: \"%s\" already exists", companyNameField.getText()));
            return;
        }

        if (!SQLiteDatabase.insertCompany(getCompany(), userId)) {
            showErrorBox("Error inserting company. Please try again");
            return;
        }

        companiesTableRows.add(new CompaniesTableRow(companyNameField.getText(), companyStreetField.getText(), companyCityField.getText(), companyStateBox.getValue(), companyZipcodeField.getText()));
        clearCompanyFields();
    }

    @FXML
    public void updateCompanyButtonAction() {
        if (!companyFieldsValid()) {
            showErrorBox("Missing and/or incorrect company fields");
            return;
        }

        if (!SQLiteDatabase.updateCompany(getCompany(), userId)) {
            showErrorBox("Error updating company. Please try again");
            return;
        }

        ObservableList<CompaniesTableRow> selectedRows = companiesTable.getSelectionModel().getSelectedItems();
        CompaniesTableRow selectedRow = selectedRows.get(0);
        selectedRow.setName(companyNameField.getText());
        selectedRow.setStreet(companyStreetField.getText());
        selectedRow.setCity(companyCityField.getText());
        selectedRow.setState(companyStateBox.getValue());
        selectedRow.setZipcode(companyZipcodeField.getText());
        companiesTable.refresh();

        clearCompanyFields();
        disableCompanyButtons(false, true, true);
    }

    @FXML
    public void deleteCompanyButtonAction() {
        ObservableList<CompaniesTableRow> selectedRows = companiesTable.getSelectionModel().getSelectedItems();
        companiesTable.getItems().removeAll(selectedRows);

        if (!SQLiteDatabase.deleteCompany(getCompany(), userId)) {
            showErrorBox("Error deleting company. Please try again");
            return;
        }

        companiesTable.getSelectionModel().select(null);
        clearCompanyFields();
        disableCompanyButtons(false, true, true);
    }

    @FXML
    public void insertCustomerButtonAction() {
        if (!customerFieldsValid()) {
            showErrorBox("Missing and/or incorrect customer fields");
            return;
        }

        if (SQLiteDatabase.isCustomer(customerNameField.getText(), userId)) {
            showErrorBox(String.format("Customer with name: \"%s\" already exists", customerNameField.getText()));
            return;
        }

        if (!SQLiteDatabase.insertCustomer(getCustomer(), userId)) {
            showErrorBox("Error inserting customer. Please try again");
            return;
        }

        customersTableRows.add(new CustomersTableRow(customerNameField.getText(), customerStreetField.getText(), customerCityField.getText(), customerStateBox.getValue(), customerZipcodeField.getText()));
        clearCustomerFields();
    }

    @FXML
    public void updateCustomerButtonAction() {
        if (!customerFieldsValid()) {
            showErrorBox("Missing and/or incorrect customer fields");
            return;
        }

        if (!SQLiteDatabase.updateCustomer(getCustomer(), userId)) {
            showErrorBox("Error updating customer. Please try again");
            return;
        }

        ObservableList<CustomersTableRow> selectedRows = customersTable.getSelectionModel().getSelectedItems();
        CustomersTableRow selectedRow = selectedRows.get(0);
        selectedRow.setName(customerNameField.getText());
        selectedRow.setStreet(customerStreetField.getText());
        selectedRow.setCity(customerCityField.getText());
        selectedRow.setState(customerStateBox.getValue());
        selectedRow.setZipcode(customerZipcodeField.getText());
        customersTable.refresh();

        clearCustomerFields();
        disableCustomerButtons(false, true, true);
    }

    @FXML
    public void deleteCustomerButtonAction() {
        ObservableList<CustomersTableRow> selectedRows = customersTable.getSelectionModel().getSelectedItems();
        customersTable.getItems().removeAll(selectedRows);

        if (!SQLiteDatabase.deleteCustomer(getCustomer(), userId)) {
            showErrorBox("Error deleting customer. Please try again");
            return;
        }

        customersTable.getSelectionModel().select(null);
        clearCustomerFields();
        disableCustomerButtons(false, true, true);
    }

    @FXML
    public void downloadAction() {
        if (!companyFieldsValid() || !customerFieldsValid() || !paymentFieldsValid()) {
            showErrorBox("Missing and/or incorrect fields");
            return;
        }
        downloadPDF();
    }

    @FXML
    public void logoutAction() {
        Optional<ButtonType> result = showConfirmationBox("Are you sure you want to logout?");
        if (result.get() != ButtonType.OK) {
            return;
        }

        reset();
        Scene scene = sceneManager.loadScene("/scenes/login.fxml");
        sceneManager.switchScene(scene);
    }

    private void downloadPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        File file = fileChooser.showSaveDialog(this.sceneManager.getPrimaryStage());
        if (file == null) {
            return;
        }
        String path = file.getAbsolutePath();

        Invoice invoice = getInvoice();
        PDFBuilder pdfBuilder = new PDFBuilder(invoice, path);
        if (pdfBuilder.openPDF()) {
            pdfBuilder.generatePDF();
            showInfoBox("PDF downloaded successfully!");
        } else {
            showErrorBox("Error downloading PDF. Please try again");
        }
    }

    private Invoice getInvoice() {
        return new Invoice(getInvoiceRows(), getCompany(), getCustomer(), getPayment());
    }

    private List<InvoiceRow> getInvoiceRows() {
        List<InvoiceRow> invoiceRows = new ArrayList<>();
        for (InvoiceTableRow row : invoiceTableRows) {
            invoiceRows.add(new InvoiceRow(row.getDescription(), row.getPrice().isEmpty() ? 0 : Integer.parseInt(row.getPrice())));
        }
        return invoiceRows;
    }

    private BusinessEntity getCompany() {
        return new BusinessEntity(companyNameField.getText(),
                companyStreetField.getText(),
                companyCityField.getText(),
                companyStateBox.getValue(),
                companyZipcodeField.getText());
    }

    private BusinessEntity getCustomer() {
        return new BusinessEntity(customerNameField.getText(),
                customerStreetField.getText(),
                customerCityField.getText(),
                customerStateBox.getValue(),
                customerZipcodeField.getText());
    }

    private Payment getPayment() {
        return new Payment(Integer.parseInt(subtotalField.getText()),
                Integer.parseInt(paidField.getText()),
                Integer.parseInt(totalField.getText()));
    }

    private void showErrorBox(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(text);
        alert.showAndWait();
    }

    private void showInfoBox(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(text);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmationBox(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(text);
        return alert.showAndWait();
    }

    private boolean invoiceFieldsValid() {
        String description = descriptionField.getText();
        String price = priceField.getText();

        if (description.isEmpty()) {
            showErrorBox("Missing description field");
            return false;
        }
        if (!price.isEmpty() && !isNum(price)) {
            showErrorBox("Price field must be a number");
            return false;
        }
        return true;
    }

    private boolean companyFieldsValid() {
        return !companyNameField.getText().isEmpty()
                && !companyStreetField.getText().isEmpty()
                && !companyCityField.getText().isEmpty()
                && !companyStateBox.getSelectionModel().isEmpty()
                && !companyZipcodeField.getText().isEmpty()
                && isNum(companyZipcodeField.getText());
    }

    private boolean customerFieldsValid() {
        return !customerNameField.getText().isEmpty()
                && !customerStreetField.getText().isEmpty()
                && !customerCityField.getText().isEmpty()
                && !customerStateBox.getSelectionModel().isEmpty()
                && !customerZipcodeField.getText().isEmpty()
                && isNum(customerZipcodeField.getText());
    }

    private boolean paymentFieldsValid() {
        return !subtotalField.getText().isEmpty()
                && !paidField.getText().isEmpty()
                && !totalField.getText().isEmpty()
                && isNum(subtotalField.getText())
                && isNum(paidField.getText())
                && isNum(totalField.getText());
    }

    private void setInvoiceFields(String description, String price) {
        descriptionField.setText(description);
        priceField.setText(price);
    }

    private void setCompanyFields(String name, String street, String city, String state, String zipcode) {
        companyNameField.setText(name);
        companyStreetField.setText(street);
        companyCityField.setText(city);
        companyStateBox.setValue(state);
        companyZipcodeField.setText(zipcode);
    }

    private void setCustomerFields(String name, String street, String city, String state, String zipcode) {
        customerNameField.setText(name);
        customerStreetField.setText(street);
        customerCityField.setText(city);
        customerStateBox.setValue(state);
        customerZipcodeField.setText(zipcode);
    }

    private void reset() {
        clearAllTables();
        clearInvoiceFields();
        clearCompanyFields();
        clearCustomerFields();
        clearPaymentFields();
        invoiceTable.requestFocus();
    }

    private void clearAllTables() {
        invoiceTable.getItems().clear();
        companiesTable.getItems().clear();
        customersTable.getItems().clear();
    }

    private void clearInvoiceFields() {
        descriptionField.clear();
        priceField.clear();
    }

    private void clearCompanyFields() {
        companyNameField.setText("");
        companyStreetField.setText("");
        companyCityField.setText("");
        companyStateBox.getSelectionModel().clearSelection();
        companyZipcodeField.setText("");
    }

    private void clearCustomerFields() {
        customerNameField.setText("");
        customerStreetField.setText("");
        customerCityField.setText("");
        customerStateBox.getSelectionModel().clearSelection();
        customerZipcodeField.setText("");
    }

    private void clearPaymentFields() {
        subtotalField.setText("");
        paidField.setText("");
        totalField.setText("");
    }

    private void disableInvoiceButtons(boolean insertButtonValue, boolean updateButtonValue, boolean deleteButtonValue) {
        insertInvoiceButton.setDisable(insertButtonValue);
        updateInvoiceButton.setDisable(updateButtonValue);
        deleteInvoiceButton.setDisable(deleteButtonValue);
    }

    private void disableCompanyButtons(boolean insertButtonValue, boolean updateButtonValue, boolean deleteButtonValue) {
        insertCompanyButton.setDisable(insertButtonValue);
        updateCompanyButton.setDisable(updateButtonValue);
        deleteCompanyButton.setDisable(deleteButtonValue);
    }

    private void disableCustomerButtons(boolean insertButtonValue, boolean updateButtonValue, boolean deleteButtonValue) {
        insertCustomerButton.setDisable(insertButtonValue);
        updateCustomerButton.setDisable(updateButtonValue);
        deleteCustomerButton.setDisable(deleteButtonValue);
    }

    private boolean isNum(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
