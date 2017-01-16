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
import java.util.Calendar;

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
//        Statement stmt = vDatabaseConnection.createStatement();
//        String query = "Select * from bonus";
//        ResultSet rs = stmt.executeQuery(query);
//        reportData.clear();
//        while (rs.next()) {
//            int employeeId = rs.getInt("employeeid");
//            ResultSet employeeRS=vDatabaseConnection.createStatement()
//                    .executeQuery("select * from employee where id="+employeeId);
//            employeeRS.next();
//            String salary = "";
//            reportData.add(new report(employeeId,employeeRS.getString("FIRSTNAME")+' '+employeeRS.getString("LASTNAME"),salary));
//        }
        reportTable.setItems(reportData);
    }
    public void handleSearch(ActionEvent actionEvent) throws SQLException {
        String query="{?=call GETSALARY(?,?)}";
        CallableStatement statement=vDatabaseConnection.prepareCall(query);
        ResultSet rs=vDatabaseConnection.createStatement().executeQuery("select * from employee");
        if(searchField.getValue()==null){
            System.out.println("search field is null");
            return;
        }
            reportData.clear();
        statement.setInt(2,searchField.getValue().getMonthValue());
        while(rs.next()){
            statement.setInt(3,rs.getInt("id"));
            statement.registerOutParameter(1, Types.INTEGER);
            statement.execute();
            reportData.add(new report(rs.getString("FIRSTNAME")+' '+rs.getString("LASTNAME"),Integer.toString(statement.getInt(1))));
        }
       // System.out.println(reportData.get(1));
        //buildTableData();
        reportTable.setItems(reportData);


        //System.out.println(statement.getInt(1));

//        ResultSet rs=vDatabaseConnection.createStatement().executeQuery("select * from employee");
//        while(rs.next()){
//            statement=vDatabaseConnection.prepareCall(query);
//            statement.registerOutParameter(1, Types.INTEGER);
//            statement.setInt(2,11);
//            statement.setInt(3, rs.getInt("id"));
//            String salary = Integer.toString(statement.getInt(1));
//            String fullName=rs.getString("FIRSTNAME")+' '+rs.getString("LASTNAME");
//            reportData.add(new report(rs.getInt("id"),fullName, salary));
//        }
//        statement.execute();
//        //System.out.println(statement.getInt(1));
//        buildTableData();
    }
}