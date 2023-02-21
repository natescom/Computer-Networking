package com.nathan;

import com.nathan.controller.Controller_TelaPrincipal;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 29/07/2021
 * Ultima alteracao: 29/07/2021
 * Nome: Principal
 * Funcao: Inicializa o programa
 * ************************************************************** */
public class Principal extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller_TelaPrincipal controller_telaPrincipal = new Controller_TelaPrincipal();
        Parent root = FXMLLoader.load(getClass().getResource("view/screen/telaprincipal.fxml"));
        primaryStage.setTitle("Redes I - Trab 02 CAMADA DE ENLACE DE DADOS");
        primaryStage.getIcons().add(new Image(Principal.class.getResourceAsStream("/com/nathan/view/img/network_cool_two_pcs-5.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        }); // Ao fechar a janela todos os processos sao fechados tambem
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
