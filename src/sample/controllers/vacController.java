package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.models.Vacation;

import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;

public class vacController{

    public TableView vacTable;
    public TableColumn empCol;
    public TableColumn bDateCol;
    public TableColumn eDateCol;
    public TableColumn typeCol;
    public TextField idTF;
    public ComboBox empCB;
    public DatePicker bDateDP;
    public DatePicker eDateDP;
    public RadioButton pRB;
    public RadioButton upRB;
    ToggleGroup group;
    Connection vDatabaseConnection;
    private ObservableList<Vacation> vacData;
    public void initialize() throws SQLException {
        idTF.setDisable(true);
        group = new ToggleGroup();
        pRB.setToggleGroup(group);
        upRB.setToggleGroup(group);
        // connect
        vDatabaseConnection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:orcl",
                "uni",
                "oracle"
        );
        empCB.setItems(getEmployeeNames());
        vacData = FXCollections.observableArrayList();
        empCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
        bDateCol.setCellValueFactory(new PropertyValueFactory<>("bDate"));
        eDateCol.setCellValueFactory(new PropertyValueFactory<>("eDate"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        buildTableData();
        vacTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Vacation vacation = (Vacation) newSelection;
                clearData(false);
                idTF.setText(String.valueOf(vacation.getId()));
                LocalDate date1 = LocalDate.parse(vacation.getbDate().split(" ")[0]);
                bDateDP.setValue(date1);
                LocalDate date2 = LocalDate.parse(vacation.geteDate().split(" ")[0]);
                eDateDP.setValue(date2);
                empCB.setValue(vacation.getEmployee());
                if (Objects.equals(vacation.getType(), "PAID")) {
                    pRB.setSelected(true);
                } else if (Objects.equals(vacation.getType(), "UNPAID")) {
                    upRB.setSelected(true);
                }
            }
        });
    }
    @SuppressWarnings("Duplicates")
    private void buildTableData() throws SQLException {
        Statement stmt = vDatabaseConnection.createStatement();
        String query = "Select * from VACATION";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String id = rs.getString("id");
            int type = rs.getInt("TYPE");
            String bDate = rs.getString("DATEBEGIN");
            String eDate = rs.getString("DATEEND");
            String employee = getEmpName(rs.getString("EMPLOYEEID"));
            Vacation vacation = new Vacation(id, employee, bDate, eDate, getTypeName(type));
            vacData.add(vacation);
        }
        vacTable.setItems(vacData);
    }

    private String  getTypeName(int type) {
        if (type == 1) {
            return "PAID";
        }
        return "UNPAID";
    }

    private int getTypeNumber(String typeName) {
        if (Objects.equals(typeName, "PAID")) {
            return 1;
        }
        return 2;
    }
    private void clearData(boolean allData) {
        if (allData) {
            vacData.clear();
        }
        idTF.clear();
        bDateDP.getEditor().clear();
        eDateDP.getEditor().clear();
        pRB.setSelected(false);
        upRB.setSelected(false);
        empCB.getEditor().clear();
    }

    public static boolean isNullOrWhiteSpace(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean dataValid() {
        return !(isNullOrWhiteSpace(bDateDP.getEditor().getText()) ||
                isNullOrWhiteSpace(eDateDP.getEditor().getText()) || empCB.getSelectionModel().isEmpty()
                || group.getSelectedToggle() == null);
    }
    public void handleAddVacation(ActionEvent actionEvent) throws SQLException {
        // add Diploma
        if (isNullOrWhiteSpace(idTF.getText()) && dataValid()) {
            String query = "BEGIN ADDVACATION('%s',date '%s',date '%s',%d); end;";
            query = String.format(query, getEmployeeID("h"),
                    bDateDP.getValue(), eDateDP.getValue(), getTypeNumber(((RadioButton) group.getSelectedToggle()).getText()));
            CallableStatement callStmt = vDatabaseConnection.prepareCall(query);
            callStmt.execute();
//        } else if (!isNullOrWhiteSpace(idTF.getText()) && dataValid()) {
//            String query = "BEGIN MODIFYDIPLOMA('%s','%s',%d,date '%s'); end;";
//            query = String.format(query, idTF.getText(), titleTF.getText(), getDiplomaLevel(((RadioButton) group.getSelectedToggle()).getText()),
//                    dodDP.getValue());
//            CallableStatement callStmt = vDatabaseConnection.prepareCall(query);
//            callStmt.execute();
//        }
            clearData(true);
            buildTableData();
        }
    }

    public void handleDeleteVacation(ActionEvent actionEvent) {
    }

    public void handleClearFields(ActionEvent actionEvent) {
    }
    @SuppressWarnings("Duplicates")
    private ObservableList<String> getEmployeeNames() throws SQLException {
        ObservableList<String> names = FXCollections.observableArrayList();
        String query = "select FIRSTNAME,LASTNAME from EMPLOYEE ";
        Statement stmt = vDatabaseConnection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String firstName = rs.getString("FIRSTNAME");
            String lastName = rs.getString("LASTNAME");
            names.add(firstName + " " + lastName);
        }
        return names;
    }
    @SuppressWarnings("Duplicates")
    private String getEmployeeID(String empName) throws SQLException {
        String[] fullname = empName.split(" ");
        String query = "select  ID from EMPLOYEE WHERE FIRSTNAME = '%s' AND LASTNAME = '%s' ";
        query = String.format(query, fullname[0], fullname[1]);
        Statement stmt = vDatabaseConnection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String id = null;
        while (rs.next()) {
            id = rs.getString("ID");
        }
        return id;
    }

    @SuppressWarnings("Duplicates")
    private String getEmpName(String empID) throws SQLException {
        String query = "select FIRSTNAME, LASTNAME from EMPLOYEE WHERE ID = " + empID;
        Statement stmt = vDatabaseConnection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String lastName = null;
        String firstName = null;
        while (rs.next()) {
            firstName = rs.getString("FIRSTNAME");
            lastName = rs.getString("LASTNAME");
        }
        return firstName + " " + lastName;
    }
}