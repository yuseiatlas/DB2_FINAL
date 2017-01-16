package sample.models;

import javafx.beans.property.SimpleStringProperty;

public class report{
   // SimpleStringProperty id;
    SimpleStringProperty employee;
    SimpleStringProperty salary;

    public report(String employee, String salary) {
        //this.id=new SimpleStringProperty(Integer.toString(id));
        this.employee = new SimpleStringProperty(employee);
        this.salary = new SimpleStringProperty(salary);
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

    public String getSalary() {
        return salary.get();
    }

    public SimpleStringProperty salaryProperty() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary.set(salary);
    }
}