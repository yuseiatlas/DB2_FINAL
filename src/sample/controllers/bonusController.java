package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.models.bonus;
import sample.models.child;

import java.sql.*;
import java.util.ArrayList;

public class bonusController {

    public TableView bonusTable;
    public TableColumn empCol;
    public TableColumn amountCol;
    public TableColumn dateCol;
    public TextField idTF;
    public TextField amountTF;
    public ComboBox empCB;
    public DatePicker dateDP;
    public ObservableList<bonus> bonusData;
    //two lists to get the value of the id based on the combo box selection
    public ArrayList<Integer> idList;
    public ArrayList<String> stringList;
    public ArrayList<Integer> bonusID;
    //three variables to hold the current (selected) child ID, date of birth and first name
    public String employeeName;
    public String DateOfBonus;
    public int Amount;
    Connection vDatabaseConnection;

    public void initialize() throws SQLException {
        System.out.println("blah");
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        vDatabaseConnection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:orcl",
                "uni",
                "oracle"
        );
        idList=new ArrayList<>();
        stringList=new ArrayList<>();
        bonusID=new ArrayList<>();
        bonusData=FXCollections.observableArrayList();
        empCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        buildComboBox();
        buildTableData();
        bonusTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            employeeName=((bonus)newSelection).getEmployee();
            DateOfBonus=((bonus)newSelection).getDate();
            Amount=Integer.parseInt(((bonus)newSelection).getAmount());
            System.out.println(Amount);
        });
    }
    public void buildTableData() throws SQLException {
        Statement stmt = vDatabaseConnection.createStatement();
        String query = "Select * from bonus";
        ResultSet rs = stmt.executeQuery(query);
        bonusData.clear();
        while (rs.next()) {
            bonusID.add(rs.getInt("id"));
            int employeeId = rs.getInt("employeeid");
            ResultSet employeeRS=vDatabaseConnection.createStatement()
                    .executeQuery("select * from employee where id="+employeeId);
            employeeRS.next();
            String amount = rs.getString("AMOUNT");
            String date = rs.getDate("DATEOFBONUS").toString();
            bonusData.add(new bonus(employeeRS.getString("FIRSTNAME")+' '+employeeRS.getString("LASTNAME"),amount,date));
        }
        bonusTable.setItems(bonusData);
    }


    public void handleAddBonus(ActionEvent actionEvent) throws SQLException {
        if(bonusTable.getSelectionModel().getSelectedItem()!=null){ //if a row is selected go to edit child
            handleEditBonus(actionEvent);
            //System.out.println("edit bonus");
        }else{//else add a new child
            Statement statement = vDatabaseConnection.createStatement();
            //String choice = (group.getSelectedToggle() == yes) ? "Y" : "N";
            System.out.println(idList.get(stringList.indexOf(empCB.getValue())));
            String query = "begin" +
                    " ADDBONUS(" +
                     +idList.get(stringList.indexOf(empCB.getValue()))+
                    "," +amountTF.getText() + "," +
                    "TO_DATE(\'" +dateDP.getValue()+ "\',\'YYYY-MM-DD\')); " +
                    "end;";
            statement.execute(query);
            buildTableData();
            System.out.println("add child!");
        }
    }

    public void handleEditBonus(ActionEvent actionEvent) throws SQLException {
        amountTF.setText(null);
        Statement statement = vDatabaseConnection.createStatement();
        //three variable that holds the inptt
        String dob=(dateDP.getValue()==null)?DateOfBonus:dateDP.getValue().toString();
        int amount=0; //(amountTF.getText()!=null)?Integer.parseInt(amountTF.getText()):Amount;
        if(amountTF.getText()!=null){
            System.out.println("in if");
            amount=Integer.parseInt(amountTF.getText());
        }else{
            System.out.println("in else");
            amount=Amount;
        }
        System.out.println("-----------------------------");
        System.out.println(dob);
        System.out.println(amount);
        System.out.println(bonusID.get(bonusTable.getSelectionModel().getSelectedIndex()));
        //String choice = (group.getSelectedToggle() == yes) ? "Y" : "N";
        String query="begin MODIFYBONUS(" +
                ""+ bonusID.get(bonusTable.getSelectionModel().getSelectedIndex())+
                ","+amount +
                ",TO_DATE(\'" +dob+ "\',\'YYYY-MM-DD\'));" +
                "end;";
        statement.execute(query);
        buildTableData();
    }


    public void handleDeleteBonus(ActionEvent actionEvent) {
    }

    public void handleClearFields(ActionEvent actionEvent) {
        idTF.setText("");
        dateDP.getEditor().clear();
        empCB.getSelectionModel().clearSelection();
        amountTF.setText("");
        //group.selectToggle(yes);
        bonusTable.getSelectionModel().clearSelection();
    }
    public void buildComboBox() throws SQLException {
        //parentCB=new ComboBox();
        Statement stmt = vDatabaseConnection.createStatement();
        String query = "Select * from employee";
        ResultSet rs = stmt.executeQuery(query);
        ObservableList<String> list=FXCollections.observableArrayList();
        while (rs.next()) {
            int id = rs.getInt("id");
            String fname = rs.getString("FIRSTNAME");
            String lname=rs.getString("LASTNAME");
            list.add(fname+' '+lname);

            idList.add(id);
            stringList.add(fname+' '+lname);
        }
        empCB.setItems(list);
        empCB.setPromptText("Employee name");
    }
}