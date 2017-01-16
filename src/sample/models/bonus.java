package sample.models;

import javafx.beans.property.SimpleStringProperty;

public class bonus {
    SimpleStringProperty employee;
    SimpleStringProperty amount;
    SimpleStringProperty date;
    int employeeId;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public bonus(int employeeId, String employee, String amount, String date) {
        this.employeeId=employeeId;
        this.employee = new SimpleStringProperty(employee);
        this.amount = new SimpleStringProperty(amount);
        this.date = new SimpleStringProperty(date);
    }
    public bonus(int employeeId, String employee, String amount, String date, int id) {
        this.employeeId=employeeId;
        this.employee = new SimpleStringProperty(employee);
        this.amount = new SimpleStringProperty(amount);
        this.date = new SimpleStringProperty(date);
        this.id=id;
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

    public String getAmount() {
        return amount.get();
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }
}