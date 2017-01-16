package sample.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import oracle.sql.DATE;
import sample.models.Employee;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Objects;

public class empController {

    public TableView<Employee> employeesTable;

    public TableColumn<Object, Object> fNameCol;
    public TableColumn<Object, Object> lNameCol;
    public TableColumn<Object, Object> dobCol;
    public TableColumn<Object, Object> salaryCol;
    public TableColumn<Object, Object> marriedCol;
    public TextField idTF;
    public TextField fNameTF;
    public TextField lNameTF;
    public DatePicker dobDP;
    public RadioButton yesRB;
    public RadioButton noRB;
    public TextField dtitleTF;
    public ComboBox dlevelCB;
    public DatePicker ddateDP;
    ToggleGroup group;

    Connection vDatabaseConnection;
    private ObservableList<Employee> empData;

    public void initialize() throws SQLException {
        idTF.setDisable(true);
        ObservableList<String> dipLevels =
                FXCollections.observableArrayList(
                        "baccalaureate",
                        "university",
                        "higher studies"
                );
        dlevelCB.setItems(dipLevels);
        group = new ToggleGroup();
        yesRB.setToggleGroup(group);
        noRB.setToggleGroup(group);
        empData = FXCollections.observableArrayList();
        fNameCol.setCellValueFactory(new PropertyValueFactory<>("fName"));
        lNameCol.setCellValueFactory(new PropertyValueFactory<>("lName"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));
        marriedCol.setCellValueFactory(new PropertyValueFactory<>("married"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        // connect
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        vDatabaseConnection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:orcl",
                "uni",
                "oracle"
        );
        buildTableData();
        employeesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                clearDate(false);
                idTF.setText(String.valueOf(newSelection.getId()));
                fNameTF.setText(newSelection.getfName());
                lNameTF.setText(newSelection.getlName());
                LocalDate date = LocalDate.parse(newSelection.getDob().split(" ")[0]);
                dobDP.setValue(date);
                if (Objects.equals(newSelection.getMarried(), "Y")) {
                    yesRB.setSelected(true);
                } else {
                    noRB.setSelected(false);
                }
            }
        });

    }

    private void clearDate(boolean allDate) {
        if (allDate) {
            empData.clear();
        }
        idTF.clear();
        fNameTF.clear();
        lNameTF.clear();
        dobDP.getEditor().clear();
        yesRB.setSelected(false);
        noRB.setSelected(false);
        dlevelCB.getEditor().clear();
        dtitleTF.clear();
        ddateDP.getEditor().clear();
    }

    @SuppressWarnings("Duplicates")
    private void buildTableData() throws SQLException {
        Statement stmt = vDatabaseConnection.createStatement();
        String query = "Select * from Employee";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            int id = rs.getInt("id");
            String fName = rs.getString("FIRSTNAME");
            String lName = rs.getString("LASTNAME");
            String dob = rs.getString("DATEOFBIRTH");
            int baseSalary = rs.getInt("BASESALARY");
            String married = rs.getString("MARRIED");
            Employee emp = new Employee(id, fName, lName, dob, baseSalary, married);
            empData.add(emp);
        }
        employeesTable.setItems(empData);
    }

    public void handleAddEmployee(ActionEvent actionEvent) throws SQLException, ParseException {
        // adding user
        if (isNullOrWhiteSpace(idTF.getText()) && addDataValid()) {
            String query = "BEGIN ADDEMPLOYEE('%s','%s',date '%s','%s','%s',%d,date '%s'); end;";
            query = String.format(query, fNameTF.getText(), lNameTF.getText(), dobDP.getValue(),
                    ((RadioButton) group.getSelectedToggle()).getText().substring(0, 1),
                    dtitleTF.getText(),
                    getDiplomaLevel(dlevelCB.getSelectionModel().getSelectedItem().toString()),
                    ddateDP.getValue());
            CallableStatement callStmt = vDatabaseConnection.prepareCall(query);
            callStmt.execute();
        }
        // Updating an existing employee => id not empty
        else {
            if (!editDataValid()) {
                return;
            }
            String query = "BEGIN MODIFYEMPLOYEE('%s','%s','%s',date '%s', '%s'); end;";
            query = String.format(query, idTF.getText(), fNameTF.getText(), lNameTF.getText(),
                    dobDP.getValue(),
                    ((RadioButton) group.getSelectedToggle()).getText().substring(0, 1));
            CallableStatement callStmt = vDatabaseConnection.prepareCall(query);
            callStmt.execute();
        }
        clearDate(true);
        buildTableData();
    }

    private boolean addDataValid() {
        return !(isNullOrWhiteSpace(fNameTF.getText()) || isNullOrWhiteSpace(lNameTF.getText()) ||
                isNullOrWhiteSpace(dobDP.getEditor().getText()) ||
                isNullOrWhiteSpace(dtitleTF.getText()) || dlevelCB.getSelectionModel().isEmpty() ||
                isNullOrWhiteSpace(ddateDP.getEditor().getText())
                || group.getSelectedToggle() == null);
    }

    private boolean editDataValid() {
        return !(isNullOrWhiteSpace(fNameTF.getText()) || isNullOrWhiteSpace(lNameTF.getText()) ||
                isNullOrWhiteSpace(dobDP.getEditor().getText()) || isNullOrWhiteSpace(idTF.getText())
                || group.getSelectedToggle() == null);
    }

    public void handleDeleteEmployee(ActionEvent actionEvent) throws SQLException {
        if (!editDataValid()) {
            return;
        }
        String query = "BEGIN DELETEMPLOYEE('%s'); end;";
        query = String.format(query, idTF.getText());
        CallableStatement callStmt = vDatabaseConnection.prepareCall(query);
        callStmt.execute();
        clearDate(true);
        buildTableData();
    }

    public void handleClearFields(ActionEvent actionEvent) {
        clearDate(false);
    }

    public static boolean isNullOrWhiteSpace(String value) {
        return value == null || value.trim().isEmpty();
    }

    private int getDiplomaLevel(String level) {
        if (Objects.equals(level, "baccalaureate")) {
            return 1;
        } else if (Objects.equals(level, "university")) {
            return 2;
        } else {
            return 3;
        }
    }

}