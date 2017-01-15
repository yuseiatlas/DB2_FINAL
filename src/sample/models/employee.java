package sample.models;

import javafx.beans.property.SimpleStringProperty;

public class employee{
    int id;
    SimpleStringProperty fName;
    SimpleStringProperty lName;
    SimpleStringProperty dob;
    SimpleStringProperty salary;
    SimpleStringProperty married;

    public employee() {
    }

    public employee(int id, String fName, String lName, String dob, String  salary, String married) {
        this.id = id;
        this.fName = new SimpleStringProperty(fName);
        this.lName = new SimpleStringProperty(lName);
        this.dob = new SimpleStringProperty(dob);
        this.salary = new SimpleStringProperty(salary);
        this.married = new SimpleStringProperty(married);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfName() {
        return fName.get();
    }

    public SimpleStringProperty fNameProperty() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName.set(fName);
    }

    public String getlName() {
        return lName.get();
    }

    public SimpleStringProperty lNameProperty() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName.set(lName);
    }

    public String getDob() {
        return dob.get();
    }

    public SimpleStringProperty dobProperty() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob.set(dob);
    }

    public String getSalary() {
        return salary.get();
    }

    public SimpleStringProperty salaryProperty() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary.set(salary);
    }

    public String getMarried() {
        return married.get();
    }

    public SimpleStringProperty marriedProperty() {
        return married;
    }

    public void setMarried(String married) {
        this.married.set(married);
    }
}