package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.StringUtils;
import sample.models.bonus;
import sample.models.child;

import java.sql.*;
import java.util.*;
import java.util.Date;

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
    //list that holds the IDs of the all bonuses using idList an stringList
    public ArrayList<Integer> bonusID;
    //four variables to hold the current (selected) employee full name employee ID, date of bonues and amount of bonus
    public String employeeName;
    public String DateOfBonus;
    public int Amount;
    public int EmployeeID;
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
            if(newSelection!=null) {
                employeeName = ((bonus) newSelection).getEmployee();
                DateOfBonus = ((bonus) newSelection).getDate();
                Amount = Integer.parseInt(((bonus) newSelection).getAmount());
                EmployeeID = ((bonus) newSelection).getEmployeeId();
                System.out.println(((bonus) newSelection).getEmployeeId());
            }
        });
    }
    public void buildTableData() throws SQLException {
        Statement stmt = vDatabaseConnection.createStatement();
        String query = "Select * from bonus";
        ResultSet rs = stmt.executeQuery(query);
        bonusData.clear();
        bonusID.clear();
        while (rs.next()) {
            bonusID.add(rs.getInt("id"));
            int employeeId = rs.getInt("employeeid");
            ResultSet employeeRS=vDatabaseConnection.createStatement()
                    .executeQuery("select * from employee where id="+employeeId);
            employeeRS.next();
            String amount = rs.getString("AMOUNT");
            String date = rs.getDate("DATEOFBONUS").toString();
            bonusData.add(new bonus(employeeId,employeeRS.getString("FIRSTNAME")+' '+employeeRS.getString("LASTNAME"),amount,date));
        }
        bonusTable.setItems(bonusData);
    }


    public void handleAddBonus(ActionEvent actionEvent) throws SQLException {
        if(bonusTable.getSelectionModel().getSelectedItem()!=null){ //if a row is selected go to edit bonus
            handleEditBonus(actionEvent);
            //System.out.println("edit bonus");
        }else{//else add a new bonus
            if(StringUtils.isBlank(amountTF.getText())){
                System.out.println("field left empty");
                return;
            }
            if(empCB.getSelectionModel().getSelectedIndex()==-1){
                System.out.println("no employee selects");
                return;
            }
            if(dateDP.getValue()==null){
                System.out.println("date is null");
                return;
            }
            System.out.println(dateDP.getValue());
            Statement statement = vDatabaseConnection.createStatement();
            System.out.println(idList.get(stringList.indexOf(empCB.getValue())));
            String query = "begin" +
                    " ADDBONUS(" +
                     +idList.get(stringList.indexOf(empCB.getValue()))+
                    "," +amountTF.getText() + "," +
                    "TO_DATE(\'" +dateDP.getValue()+ "\',\'YYYY-MM-DD\')" +
                    "); " +
                    "end;";
            System.out.println(query);
            statement.execute(query);
            buildTableData();
            System.out.println("add child!");
        }
    }

    public void handleEditBonus(ActionEvent actionEvent) throws SQLException {
        Statement statement = vDatabaseConnection.createStatement();

        //three variable that holds data of the chosen bonus is no data is inputted
        if(dateDP.getValue()!=null)
            System.out.println(dateDP.getValue().toString());
        String dob=(dateDP.getValue()==null)?DateOfBonus:dateDP.getValue().toString();
        int amount=0;
        System.out.println("get text value "+amountTF.getText());
        if(amountTF.getText().equals("")){
            System.out.println("in if");
            amount=Amount;
        }else{
            System.out.println("in else");
            amount=Integer.parseInt(amountTF.getText());
        }
        //if the user entered a new id take the new id else take the row's id
        int id=(empCB.getSelectionModel().getSelectedIndex()==-1)?EmployeeID:idList.get(stringList.indexOf(empCB.getValue()));

        System.out.println("-----------------------------");
        System.out.println(dob);
        System.out.println(amount);
        System.out.println(bonusID.get(bonusTable.getSelectionModel().getSelectedIndex()));
        System.out.println(id);
        System.out.println("-----------------------------");

        String query="begin MODIFYBONUS(" +
                ""+ bonusID.get(bonusTable.getSelectionModel().getSelectedIndex())+
                ","+amount +
                ",TO_DATE(\'" +dob+ "\',\'YYYY-MM-DD\'),"+
                    id+");" +
                "end;";
        System.out.println(query);
        statement.execute(query);
        buildTableData();
    }


    public void handleDeleteBonus(ActionEvent actionEvent) throws SQLException {
        Statement statement=vDatabaseConnection.createStatement();
        String query="begin DELETEBONUS(" +
                ""+bonusID.get(bonusTable.getSelectionModel().getSelectedIndex()) +
                ");  end;";
        statement.execute(query);
        buildTableData();
        handleClearFields(actionEvent);
    }

    public void handleClearFields(ActionEvent actionEvent) {
        bonusTable.getSelectionModel().clearSelection();
        idTF.setText("");
        dateDP.getEditor().clear();
        empCB.getSelectionModel().clearSelection();
        amountTF.setText("");
        //group.selectToggle(yes);
    }
    //builds combobox with the employee full name
    public void buildComboBox() throws SQLException {
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

    public void handleRefresh(ActionEvent actionEvent) throws SQLException {
        buildTableData();
        buildComboBox();
    }
}