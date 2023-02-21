package com.nathan;

import com.nathan.controller.Controller_TelaPrincipal;
import com.nathan.model.Rede;
import com.nathan.model.camada.enlace.ctrlerro.Hamming;
import com.nathan.model.camada.enlace.enquadramento.CharacterStufing;
import com.nathan.model.camada.fisica.Binaria;
import com.nathan.model.util.RedeGUIModoDev;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Scanner;

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
    primaryStage.setTitle("Redes I - Trab 03 CAMADA DE ENLACE DE DADOS - Controle de Erro");
    primaryStage.getIcons().add(new Image(Principal.class.getResourceAsStream("/com/nathan/view/img/network_cool_two_pcs-5.png")));
    primaryStage.setScene(new Scene(root));
    primaryStage.setOnCloseRequest(t -> {
      Platform.exit();
      System.exit(0);
    }); // Ao fechar a janela todos os processos sao fechados tambem
    primaryStage.show();

  }

  public static void main(String[] args) throws InterruptedException {
    launch(args);

    // MODO DEV //
//        System.out.print("Digite o texto: ");
//        Scanner scanner = new Scanner(System.in);
//        RedeGUIModoDev redeGUI = new RedeGUIModoDev(scanner.nextLine(),0);
//        Rede rede = new Rede(redeGUI,new Binaria(), new CharacterStufing(), new Hamming());
//        rede.start();
//
//        rede.join();
//        System.exit(0);

  }

}
