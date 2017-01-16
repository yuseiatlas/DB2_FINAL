package sample.models;

import javafx.beans.property.SimpleStringProperty;

public class child{
    int id;
    SimpleStringProperty fName;
    SimpleStringProperty dob;
    SimpleStringProperty finishedStudies;
    SimpleStringProperty employeeid;
    SimpleStringProperty fullName;

    public String getFullName() {
        return fullName.get();
    }

    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public child(int id, String fName, String dob, String finishedStudies, String employeeid) {
        this.id = id;
        this.fName = new SimpleStringProperty(fName);
        this.dob = new SimpleStringProperty(dob);
        this.finishedStudies = new SimpleStringProperty(finishedStudies);
        this.employeeid = new SimpleStringProperty(employeeid);
    }
    public child(int id, String fullName, String fName, String dob, String finishedStudies, String employeeid) {
        this.fullName = new SimpleStringProperty(fullName);
        this.fName = new SimpleStringProperty(fName);
        this.dob = new SimpleStringProperty(dob);
        this.finishedStudies = new SimpleStringProperty(finishedStudies);
        this.employeeid = new SimpleStringProperty(employeeid);
        this.id=id;
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

    public String getDob() {
        return dob.get();
    }

    public SimpleStringProperty dobProperty() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob.set(dob);
    }

    public String getFinishedStudies() {
        return finishedStudies.get();
    }

    public SimpleStringProperty finishedStudiesProperty() {
        return finishedStudies;
    }

    public void setFinishedStudies(String finishedStudies) {
        this.finishedStudies.set(finishedStudies);
    }

    public String getEmployeeid() {
        return employeeid.get();
    }

    public SimpleStringProperty employeeidProperty() {
        return employeeid;
    }

    public void setEmployeeid(String employeeid) {
        this.employeeid.set(employeeid);
    }
}