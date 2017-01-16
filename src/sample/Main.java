package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("views/home.fxml"));
        Parent empFile = FXMLLoader.load(getClass().getResource("views/emp.fxml"));
        Parent bonusFile = FXMLLoader.load(getClass().getResource("views/bonus.fxml"));
        Parent childFile = FXMLLoader.load(getClass().getResource("views/child.fxml"));
        Parent reportFile = FXMLLoader.load(getClass().getResource("views/report.fxml"));
        Parent vacFile = FXMLLoader.load(getClass().getResource("views/vacation.fxml"));
        Parent dipFile = FXMLLoader.load(getClass().getResource("views/diploma.fxml"));
        Scene homeScene = new Scene(root, 800, 430);
        Scene empScene = new Scene(empFile, 800, 430);
        Scene childScene = new Scene(childFile, 600, 450);
        Scene bonusScene = new Scene(bonusFile, 600, 450);
        Scene reportScene = new Scene(reportFile, 600, 450);
        Scene vacScene = new Scene(vacFile, 600, 450);
        Scene dipScene = new Scene(dipFile, 600, 450);
        primaryStage.setTitle("HR SYSTEM");
        primaryStage.setScene(dipScene);
        Button addEditEmp = (Button) root.lookup("#employeeBTN");
        Button addEditChild = (Button) root.lookup("#childBTN");
        Button addEditVac = (Button) root.lookup("#vacationBTN");
        Button addEditBonus = (Button) root.lookup("#bonusBTN");
        Button addEditReport = (Button) root.lookup("#reportBTN");
        Button addEditDiploma = (Button) root.lookup("#diplomaBTN");
        ImageView empIV = new ImageView("/sample/images/eCard.png");
        ImageView childIV = new ImageView("/sample/images/boy.png");
        ImageView bonusIV = new ImageView("/sample/images/bonus.png");
        ImageView vacIV = new ImageView("/sample/images/beach.png");
        ImageView reportIV = new ImageView("/sample/images/report.png");
        ImageView diplomaIV = new ImageView("/sample/images/diploma.png");
        // ======================
        // NAV DECLARATIONS
        // ======================
        ImageView empNav = (ImageView) empFile.lookup("#home_nav");
        ImageView childNav = (ImageView) childFile.lookup("#home_nav");
        ImageView bonusNav = (ImageView) bonusFile.lookup("#home_nav");
        ImageView vacNav = (ImageView) vacFile.lookup("#home_nav");
        ImageView reportNav = (ImageView) reportFile.lookup("#home_nav");
        ImageView dipNav = (ImageView) dipFile.lookup("#home_nav");
        // ======================
        addEditEmp.setFocusTraversable(false);
        addEditEmp.setStyle("-fx-base: #8D6E63;");
        addEditEmp.setContentDisplay(ContentDisplay.TOP);
        addEditEmp.setGraphic(empIV);
        // ======================
        addEditChild.setFocusTraversable(false);
        addEditChild.setStyle("-fx-base: #8D6E63;");
        addEditChild.setContentDisplay(ContentDisplay.TOP);
        addEditChild.setGraphic(childIV);
        // ======================
        addEditBonus.setFocusTraversable(false);
        addEditBonus.setStyle("-fx-base: #8D6E63;");
        addEditBonus.setContentDisplay(ContentDisplay.TOP);
        addEditBonus.setGraphic(bonusIV);
        // ======================
        addEditVac.setFocusTraversable(false);
        addEditVac.setStyle("-fx-base: #8D6E63;");
        addEditVac.setContentDisplay(ContentDisplay.TOP);
        addEditVac.setGraphic(vacIV);
        // ======================
        addEditReport.setFocusTraversable(false);
        addEditReport.setStyle("-fx-base: #8D6E63;");
        addEditReport.setContentDisplay(ContentDisplay.TOP);
        addEditReport.setGraphic(reportIV);
        // =======================
        addEditDiploma.setFocusTraversable(false);
        addEditDiploma.setStyle("-fx-base: #8D6E63;");
        addEditDiploma.setContentDisplay(ContentDisplay.TOP);
        addEditDiploma.setGraphic(diplomaIV);
        // =======================
        // Action Listeners
        // =======================
        addEditEmp.setOnAction(event -> {
            primaryStage.setScene(empScene);
        });
        addEditChild.setOnAction(event -> {
            primaryStage.setScene(childScene);
        });
        addEditBonus.setOnAction(event -> {
            primaryStage.setScene(bonusScene);
        });
        addEditReport.setOnAction(event -> {
            primaryStage.setScene(reportScene);
        });
        addEditVac.setOnAction(event -> {
            primaryStage.setScene(vacScene);
        });
        addEditDiploma.setOnAction(event -> {
            primaryStage.setScene(dipScene);
        });
        // =======================
        // NAV Action Listeners
        // =======================
        empNav.setOnMouseClicked(event -> {
            primaryStage.setScene(homeScene);
        });
        bonusNav.setOnMouseClicked(event -> {
            primaryStage.setScene(homeScene);
        });
        reportNav.setOnMouseClicked(event -> {
            primaryStage.setScene(homeScene);
        });
        childNav.setOnMouseClicked(event -> {
            primaryStage.setScene(homeScene);
        });
        vacNav.setOnMouseClicked(event -> {
            primaryStage.setScene(homeScene);
        });
        dipNav.setOnMouseClicked(event -> {
            primaryStage.setScene(homeScene);
        });
        // =======================
        // APP LAUNCH
        // =======================
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
