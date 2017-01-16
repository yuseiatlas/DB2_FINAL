package sample.models;

import javafx.beans.property.SimpleStringProperty;

public class report{
    SimpleStringProperty id;
    SimpleStringProperty employee;
    SimpleStringProperty salary;

    public report(int id,String employee, String salary) {
        this.id=new SimpleStringProperty(Integer.toString(id));
        this.employee = new SimpleStringProperty(employee);
        this.salary = new SimpleStringProperty(salary);
    }
}