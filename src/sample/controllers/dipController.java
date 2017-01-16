package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.models.Diploma;

import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;

public class dipController {
    public TableView dipTable;
    public TableColumn<Object, Object> titleCol;
    public TableColumn<Object, Object> levelCol;
    public TableColumn dodCol;
    public TableColumn empCol;
    public TextField idTF;
    public TextField titleTF;
    public ComboBox<String> empCB;
    public DatePicker dodDP;
    public RadioButton bacRB;
    public RadioButton uniRB;
    public RadioButton hsRB;
    ToggleGroup group;
    Connection vDatabaseConnection;
    private ObservableList<Diploma> dipData;

    public void initialize() throws SQLException {
        idTF.setDisable(true);
        group = new ToggleGroup();
        bacRB.setToggleGroup(group);
        uniRB.setToggleGroup(group);
        hsRB.setToggleGroup(group);
        // connect
        vDatabaseConnection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:orcl",
                "uni",
                "oracle"
        );
        empCB.setItems(getEmployeeNames());
        dipData = FXCollections.observableArrayList();
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
        dodCol.setCellValueFactory(new PropertyValueFactory<>("dod"));
        empCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
        buildTableData();
        dipTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Diploma diploma = (Diploma) newSelection;
                clearData(false);
                idTF.setText(String.valueOf(diploma.getId()));
                titleTF.setText(diploma.getTitle());
                LocalDate date = LocalDate.parse(diploma.getDod().split(" ")[0]);
                dodDP.setValue(date);
                empCB.setValue(diploma.getEmployee());
                if (Objects.equals(diploma.getLevel(), "baccalaureate")) {
                    bacRB.setSelected(true);
                } else if (Objects.equals(diploma.getLevel(), "university")) {
                    uniRB.setSelected(true);
                } else {
                    hsRB.setSelected(true);
                }
            }
        });
    }

    @SuppressWarnings("Duplicates")
    private void buildTableData() throws SQLException {
        Statement stmt = vDatabaseConnection.createStatement();
        String query = "Select * from Diploma";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String id = rs.getString("id");
            String title = rs.getString("TITLE");
            int level = rs.getInt("DLEVEL");
            String dod = rs.getString("DATEOFDIPLOMA");
            String employee = getEmpName(rs.getString("EMPID"));
            Diploma diploma = new Diploma(id, title, getLevelName(level), dod, employee);
            dipData.add(diploma);
        }
        dipTable.setItems(dipData);
    }

    public void handleAddDiploma(ActionEvent actionEvent) throws SQLException {
        // add Diploma
        if (isNullOrWhiteSpace(idTF.getText()) && dataValid()) {
            String query = "BEGIN ADDIPLOMA('%s',%d,date '%s','%s'); end;";
            query = String.format(query, titleTF.getText(), getDiplomaLevel(((RadioButton) group.getSelectedToggle()).getText()),
                    dodDP.getValue(), getEmployeeID(empCB.getSelectionModel().getSelectedItem()));
            CallableStatement callStmt = vDatabaseConnection.prepareCall(query);
            callStmt.execute();
        } else if (!isNullOrWhiteSpace(idTF.getText()) && dataValid()) {
            String query = "BEGIN MODIFYDIPLOMA('%s','%s',%d,date '%s'); end;";
            query = String.format(query, idTF.getText(), titleTF.getText(), getDiplomaLevel(((RadioButton) group.getSelectedToggle()).getText()),
                    dodDP.getValue());
            CallableStatement callStmt = vDatabaseConnection.prepareCall(query);
            callStmt.execute();
        }
        clearData(true);
        buildTableData();
    }

    public void handleDeleteDiploma(ActionEvent actionEvent) throws SQLException {
        if (isNullOrWhiteSpace(idTF.getText()) || !dataValid()) {
            return;
        }
        if (getDiplomasCount(getEmployeeID(empCB.getSelectionModel().getSelectedItem())) <= 1) {
            return;
        }
        String query = "BEGIN DELETEDIPLOMA('%s'); end;";
        query = String.format(query, idTF.getText());
        CallableStatement callStmt = vDatabaseConnection.prepareCall(query);
        callStmt.execute();
        clearData(true);
        buildTableData();
    }

    public void handleClearFields(ActionEvent actionEvent) {
        clearData(false);
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

    private String getLevelName(int level) {
        String levelName = null;
        switch (level) {
            case 1:
                levelName = "baccalaureate";
                break;
            case 2:
                levelName = "university";
                break;
            case 3:
                levelName = "higher studies";
                break;
            default:
                break;
        }
        return levelName;
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

    private void clearData(boolean allData) {
        if (allData) {
            dipData.clear();
        }
        idTF.clear();
        titleTF.clear();
        dodDP.getEditor().clear();
        bacRB.setSelected(false);
        hsRB.setSelected(false);
        uniRB.setSelected(false);
        empCB.getEditor().clear();
    }

    public static boolean isNullOrWhiteSpace(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean dataValid() {
        return !(isNullOrWhiteSpace(titleTF.getText()) ||
                isNullOrWhiteSpace(dodDP.getEditor().getText()) || empCB.getSelectionModel().isEmpty()
                || group.getSelectedToggle() == null);
    }

    @SuppressWarnings("Duplicates")
    private int getDiplomaLevel(String level) {
        if (Objects.equals(level, "baccalaureate")) {
            return 1;
        } else if (Objects.equals(level, "university")) {
            return 2;
        } else {
            return 3;
        }
    }

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

    public int getDiplomasCount(String empID) throws SQLException {
        String query = "select  count(*) as DIPCOUNT from DIPLOMA WHERE EMPID = " + empID;
        Statement stmt = vDatabaseConnection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String id = null;
        while (rs.next()) {
            id = rs.getString("DIPCOUNT");
        }
        return Integer.parseInt(id);
    }
}
