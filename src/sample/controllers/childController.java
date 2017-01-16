package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.models.child;

import java.sql.*;
import java.util.ArrayList;

public class childController {
    public TableView childTable;
    public TableColumn idCol;
    public TableColumn fNameCol;
    public TableColumn dobCol;
    public TableColumn empCol;
    public TableColumn studyCol;
    public TextField idTF;
    public TextField fNameTF;
    public ComboBox parentCB;
    public DatePicker dobDP;
    public RadioButton yes;
    public RadioButton no;
    public ToggleGroup group;
    //two lists to get the value of the id based on the combo box selection
    public ArrayList<Integer> idList;
    public ArrayList<String> stringList;
    //three variables to hold the current (selected) child ID, date of birth and first name
    public int childID;
    public String DateOfBirth;
    public String FirstName;
    private ObservableList<child> childData;
    Connection vDatabaseConnection;

    public void initialize() throws SQLException {
        System.out.println("Hello");
        // connect
        System.out.println("blah");
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        vDatabaseConnection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:orcl",
                "uni",
                "oracle"
        );
        idList=new ArrayList<>();
        stringList=new ArrayList<>();

        childData = FXCollections.observableArrayList();
        group=new ToggleGroup();//to group the radio buttons together
        yes.setToggleGroup(group);
        no.setToggleGroup(group);
        yes.setSelected(true);
        buildComboBox(); //builds the combo box according to the name of the employees
        idCol = new TableColumn();
        //(int id, String fName, String dob, String finishedStudies, String employeeid)
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        fNameCol.setCellValueFactory(new PropertyValueFactory<>("fName"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));
        studyCol.setCellValueFactory(new PropertyValueFactory<>("finishedStudies"));
        empCol.setCellValueFactory(new PropertyValueFactory<>("employeeid"));

        buildTableData();

        childTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            childID=((child)newSelection).getId();
            DateOfBirth=((child)newSelection).getDob();
            FirstName=((child)newSelection).getfName();
            System.out.println(childID);
        });
    }

    public void buildTableData() throws SQLException {
        Statement stmt = vDatabaseConnection.createStatement();
        String query = "Select * from child";
        ResultSet rs = stmt.executeQuery(query);
        childData.clear();
        while (rs.next()) {
            int id = rs.getInt("id");
            String fname = rs.getString("FIRSTNAME");
            String date = rs.getDate("DATEOFBIRTH").toString();
            String finished = rs.getString("FINISHEDSTUDIES");
            int eid = rs.getInt("EMPLOYEEID");
            childData.add(new child(id, fname, date, finished, Integer.toString(eid)));
        }
        childTable.setItems(childData);
        System.out.println(childTable);
    }
    //puts the full name of the employees in the combo box and adds the correct ids to the
    //stringList and idList to match the id and the correct name
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
        parentCB.setItems(list);
        parentCB.setPromptText("Parent name");
    }
    public void handleAddChild(ActionEvent actionEvent) throws SQLException {
        if(childTable.getSelectionModel().getSelectedItem()!=null){ //if a row is selected go to edit child
            handleEditChild(actionEvent);
                }else{//else add a new child
            Statement statement = vDatabaseConnection.createStatement();
            String choice = (group.getSelectedToggle() == yes) ? "Y" : "N";
            System.out.println(idList.get(stringList.indexOf(parentCB.getValue())));
            String query = "begin" +
                    " ADDCHILD(" +
                    "\'" + fNameTF.getText() + "\'" +
                    ",\'" + dobDP.getValue() + "\'," +
                    "\'" + choice + "\'," +
                    "" + idList.get(stringList.indexOf(parentCB.getValue())) + "); " +
                    "end;";
            statement.execute(query);
            buildTableData();
            System.out.println("add child!");
        }
    }
    public void handleEditChild(ActionEvent actionEvent) throws SQLException {
        Statement statement = vDatabaseConnection.createStatement();
        //three variable that holds the inputted date or the previously saved data depending on the user input
        String dob=(dobDP.getValue()==null)?DateOfBirth:dobDP.getValue().toString();
        String name=(fNameTF.getText()==null)?FirstName:fNameTF.getText();
        String choice = (group.getSelectedToggle() == yes) ? "Y" : "N";
        String query="begin MODIFYCHILD(" +
                "\'"+fNameTF.getText() +"\'"+
                ",TO_DATE(\'"+dob +"\',\'YYYY-MM-DD\')"+
                ",\'"+choice+"\'" +
                ","+childID+
                "); end;";
        statement.execute(query);
        buildTableData();
    }

    public void handleDeleteChild(ActionEvent actionEvent) throws SQLException {
        handleClearFields(actionEvent);
        Statement statement=vDatabaseConnection.createStatement();
        String query="begin DELETECHILD(" +
                ""+childID +
                ");  end;";
        statement.execute(query);
        buildTableData();
    }

    public void handleClearFields(ActionEvent actionEvent) {
        idTF.setText("");
        fNameTF.setText("");
        dobDP.getEditor().clear();
        parentCB.getSelectionModel().clearSelection();
        group.selectToggle(yes);
        childTable.getSelectionModel().clearSelection();

    }
}