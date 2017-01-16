package sample.models;

import javafx.beans.property.SimpleStringProperty;

public class Diploma {
    SimpleStringProperty id;
    SimpleStringProperty title;
    SimpleStringProperty level;
    SimpleStringProperty dod;
    SimpleStringProperty employee;

    public Diploma() {
    }

    public Diploma(String id, String title, String level, String dod, String employee) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.level = new SimpleStringProperty(level);
        this.dod = new SimpleStringProperty(dod);
        this.employee = new SimpleStringProperty(employee);
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

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getLevel() {
        return level.get();
    }

    public SimpleStringProperty levelProperty() {
        return level;
    }

    public void setLevel(String level) {
        this.level.set(level);
    }

    public String getDod() {
        return dod.get();
    }

    public SimpleStringProperty dodProperty() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod.set(dod);
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
}