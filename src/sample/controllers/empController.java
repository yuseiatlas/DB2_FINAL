package sample.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.*;

public class empController{

    public TableView employeesTable;

    public TableColumn fNameCol;
    public TableColumn lNameCol;
    public TableColumn dobCol;
    public TableColumn salaryCol;
    public TableColumn marriedCol;
    public TextField idTF;
    public TextField fNameTF;
    public TextField lNameTF;
    public DatePicker dobDP;
    Connection vDatabaseConnection;

    public void initalize() throws SQLException {
        // connect
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        vDatabaseConnection = DriverManager.getConnection (
                "jdbc:oracle:thin:@localhost:1521:orcl",
                "uni",
                "oracle"
        );
        buildTableData();
    }

    @SuppressWarnings("Duplicates")
    private void buildTableData() throws SQLException {
        Statement stmt = vDatabaseConnection.createStatement();
        String query = "Select * from Employee";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {

        }
    }

    public void handleAddEmployee(ActionEvent actionEvent) {
    }

    public void handleDeleteEmployee(ActionEvent actionEvent) {
    }

    public void handleClearFields(ActionEvent actionEvent) {

    }
}