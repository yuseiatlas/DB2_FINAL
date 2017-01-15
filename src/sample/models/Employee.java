package sample.models;

import javafx.beans.property.SimpleStringProperty;

public class Employee {
    int id;
    SimpleStringProperty fName;
    SimpleStringProperty lName;
    SimpleStringProperty dob;
    int salary;
    SimpleStringProperty married;

    public Employee() {
    }

    public Employee(int id, String fName, String lName, String dob, int  salary, String married) {
        this.id = id;
        this.fName = new SimpleStringProperty(fName);
        this.lName = new SimpleStringProperty(lName);
        this.dob = new SimpleStringProperty(dob);
        this.salary =  salary;
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

    public int getSalary() {
        return salary;
    }
    public void setSalary(int salary) {
        this.salary= salary;
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

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", fName=" + fName +
                ", lName=" + lName +
                ", dob=" + dob +
                ", salary=" + salary +
                ", married=" + married +
                '}';
    }
}