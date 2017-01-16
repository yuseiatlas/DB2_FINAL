package sample.models;

import javafx.beans.property.SimpleStringProperty;

public class Vacation {
    SimpleStringProperty id;
    SimpleStringProperty employee;
    SimpleStringProperty bDate;
    SimpleStringProperty eDate;
    SimpleStringProperty type;

    public Vacation() {
    }

    public Vacation(String id, String employee, String bDate, String eDate, String type) {
        this.id = new SimpleStringProperty(id);
        this.employee = new SimpleStringProperty(employee);
        this.bDate = new SimpleStringProperty(bDate);
        this.eDate = new SimpleStringProperty(eDate);
        this.type = new SimpleStringProperty(type);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getEmployee() {
        return employee.get();
    }

    public SimpleStringProperty employeeProperty() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee.set(employee);
    }

    public String getbDate() {
        return bDate.get();
    }

    public SimpleStringProperty bDateProperty() {
        return bDate;
    }

    public void setbDate(String bDate) {
        this.bDate.set(bDate);
    }

    public String geteDate() {
        return eDate.get();
    }

    public SimpleStringProperty eDateProperty() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate.set(eDate);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }
}