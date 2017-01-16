package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.models.report;

import java.sql.*;

public class reportController{

    public DatePicker searchField;
    public TableView reportTable;
    public TableColumn empCol;
    public TableColumn salaryCol;
    public ObservableList<report> reportData;
    Connection vDatabaseConnection;

    public void initialize() throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        vDatabaseConnection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:orcl",
                "uni",
                "oracle"
        );
        reportData= FXCollections.observableArrayList();
        empCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
    }

    public void buildTableData() throws SQLException {
        Statement stmt = vDatabaseConnection.createStatement();
        String query = "Select * from bonus";
        ResultSet rs = stmt.executeQuery(query);
        reportData.clear();
        while (rs.next()) {
            int employeeId = rs.getInt("employeeid");
            ResultSet employeeRS=vDatabaseConnection.createStatement()
                    .executeQuery("select * from employee where id="+employeeId);
            employeeRS.next();
            String salary = "";
            reportData.add(new report(employeeId,employeeRS.getString("FIRSTNAME")+' '+employeeRS.getString("LASTNAME"),salary));
        }
        reportTable.setItems(reportData);
    }
    public void handleSearch(ActionEvent actionEvent) throws SQLException {
        String query="SALARYSUM(11)";
        CallableStatement statement=vDatabaseConnection.prepareCall(query);
        statement.registerOutParameter(1, Types.INTEGER);
        statement.execute();
        System.out.println(statement.getInt(1));
        //buildTableData();
    }
}