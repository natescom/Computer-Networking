package com.nathan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("view/layout/pag_principal.fxml"));
    primaryStage.setTitle("Banco");
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }


  public static void main(String[] args) throws Exception {
    launch(args);
  }


}
