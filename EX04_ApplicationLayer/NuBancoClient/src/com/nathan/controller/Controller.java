package com.nathan.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {

  public Button btnSacar;
  public Button btnDepositar;
  public Button btnTransferir;
  public Button btnSair;
  public Button btnBack;
  public Button btnBack2;
  public Label lblUser;
  public Label lblSaldo;
  public VBox vboxExtrato;

  public TextField fldValorSD;
  public Button btnEfetuarSD;
  public Label lblOpera;
  public Label lblSaldoOp;
  public Label lblSaldoTr;

  public Label lblBanco;
  public Label lblConta;
  public Label lblSenha;
  public Label lblServer;
  public Button btnEntrar;
  public Button btnOkTransferir;
  public TextField fldConta;
  public PasswordField fldSenha;
  public ToggleButton btnCriarConta;
  public VBox vboxLoginTop;
  public VBox vboxLoginCenter;
  public VBox vboxLoginBottom;
  public Label[] labelsLogin;
  public VBox[] boxesLogin;

  public TextField fldContaTr;
  public TextField fldValorTr;

  private static Socket socket;
  private static PrintWriter serverSend;
  private static Scanner serverRecieve;
  public TabPane tabPane;


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      serverConnect();
      lblServer.setText("Servidor: Online");
    } catch (IOException e) {
      e.printStackTrace();
      lblServer.setText("Servidor: Offline");
      btnEntrar.setDisable(true);
    }

    labelsLogin = new Label[]{lblBanco,lblConta,lblSenha,lblServer};
    boxesLogin = new VBox[]{vboxLoginCenter,vboxLoginBottom,vboxLoginTop};

  }

  public void onClick(ActionEvent e) {
    if(e.getSource().equals(btnCriarConta)){
      if(btnCriarConta.isSelected()){
        loginTema1();
      }else{
        loginTema2();
      }
    }

    if (e.getSource().equals(btnEntrar)) {
      if (!fldConta.getText().isEmpty() && !fldSenha.getText().isEmpty()) {
        if(btnCriarConta.isSelected()){
          int i = cadastrar(fldConta.getText(), fldSenha.getText());
          loginTema2();
          lblServer.setText("Conta criada com sucesso");
          btnCriarConta.setSelected(false);
        }else {
          int i = login(fldConta.getText(), fldSenha.getText());
          if (i == 2) {
            tabPane.getSelectionModel().select(1);
            lblServer.setText("");
            consultar();
            extrato();
          } else {
            lblServer.setText("Conta ou senha errada, verifique e tente novamente.");
          }
        }
        fldConta.setText("");
        fldSenha.setText("");
      } else {
        // todo:PREENCHA OS CAMPOS
      }
    }

    if (e.getSource().equals(btnSacar)) {
      tabPane.getSelectionModel().select(2);
      lblOpera.setText("Sacar");
      lblSaldoOp.setText("Saldo: "+ lblSaldo.getText());
    }

    if (e.getSource().equals(btnDepositar)) {
      tabPane.getSelectionModel().select(2);
      lblOpera.setText("Depositar");
      lblSaldoOp.setText("Saldo: "+ lblSaldo.getText());
    }

    if (e.getSource().equals(btnTransferir)) {
      tabPane.getSelectionModel().select(3);
      lblSaldoTr.setText("Saldo: "+ lblSaldo.getText());
    }

    if(e.getSource().equals(btnOkTransferir)){
      int i = transferir(fldContaTr.getText(),Double.parseDouble(fldValorTr.getText()));
      if(i == 0){
        fldContaTr.setText("Conta não encontrada");
      }else{
        fldContaTr.setText("");
        fldValorTr.setText("");
        tabPane.getSelectionModel().select(1);
        extrato();
        consultar();
      }
    }

    if (e.getSource().equals(btnSair)) {
      tabPane.getSelectionModel().select(0);
      sair();
      try {
        serverConnect();
        lblServer.setText("Servidor: Online");
      } catch (IOException ex) {
        ex.printStackTrace();
        lblServer.setText("Servidor: Offline");
        btnEntrar.setDisable(true);
      }
    }

    if (e.getSource().equals(btnBack) || e.getSource().equals(btnBack2)) {
      tabPane.getSelectionModel().select(1);
    }

    if (e.getSource().equals(btnEfetuarSD)) {
      double valor = Double.parseDouble(fldValorSD.getText());
      if (lblOpera.getText().equals("Depositar"))
        depositar(valor);
      else
        sacar(valor);
      consultar();
      tabPane.getSelectionModel().select(1);
      extrato();
    }

  }

  public int login(String conta, String senha) {
    serverSend.println(1);
    serverSend.println(conta);
    serverSend.println(senha);
    return serverResposta();
  }

  public int cadastrar(String conta, String senha){
    serverSend.println(7);
    serverSend.println(conta);
    serverSend.println(senha);
    return serverResposta();
  }

  public int sacar(double valor) {
    serverSend.println(2);
    serverSend.println(valor);
    return serverResposta();
  }

  public int depositar(double valor) {
    serverSend.println(3);
    serverSend.println(valor);
    return serverResposta();
  }

  public int consultar() {
    serverSend.println(4);
    String valor = serverRecieve.nextLine();
    Platform.runLater(() -> lblSaldo.setText(valor));
    return serverResposta();
  }

  public int transferir(String conta, double valor) {
    serverSend.println(5);
    serverSend.println(conta);
    serverSend.println(valor);
    return serverResposta();
  }

  public int extrato() {
    serverSend.println(6);
    ObservableList box = vboxExtrato.getChildren();
    Object a1 = box.get(0);
    Object a2 = box.get(1);
    box.clear();
    Platform.runLater(() -> {
      box.add(a1);
      box.add(a2);
    });
    int tam = Integer.parseInt(serverRecieve.nextLine());
    for (int i = 0; i < tam; i++) {
      String idtipo = serverRecieve.nextLine();
      String tipo = serverRecieve.nextLine();
      String data = serverRecieve.nextLine();
      String valor = serverRecieve.nextLine();
      Platform.runLater(() -> {
        box.add(extratoElemento(valor, data, tipo, idtipo));
      });
    }

    return serverResposta();
  }

  public int sair() {
    serverSend.println(0);
    return serverResposta();
  }

  public int serverResposta() {
    String mensagemDoServidor = serverRecieve.nextLine();
    String nextState = serverRecieve.nextLine();
    return Integer.parseInt(nextState);
  }

  public void serverConnect() throws IOException {
    socket = new Socket("localhost", 60000);
    serverSend = new PrintWriter(socket.getOutputStream(), true);
    serverRecieve = new Scanner(socket.getInputStream());
  }

  public HBox extratoElemento(String valor, String data, String tipo, String idTipo) {
    HBox hBox = new HBox();
    VBox vBox = new VBox();
    Circle circle = new Circle();
    Label vl = new Label(valor);
    Label dt = new Label(data);
    Label tp = new Label(tipo);
    int i = Integer.parseInt(idTipo);

    switch (i) {
      case 1:
        vl.setTextFill(Paint.valueOf("#00a42c"));
        break;
      case 2:
        vl.setTextFill(Paint.valueOf("#da0000"));
        break;
      case 3:
        vl.setTextFill(Paint.valueOf("#1e00d9"));
    }

    vl.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));

    ObservableList vbList = vBox.getChildren();
    vbList.add(vl);
    vbList.add(tp);
    vBox.setMinWidth(226);

    circle.setFill(Paint.valueOf("#9017bb"));
    circle.setStroke(Paint.valueOf("#FFFFFF00"));
    circle.setRadius(4);

    hBox.getChildren().add(circle);
    hBox.getChildren().add(vBox);
    hBox.getChildren().add(dt);

    hBox.setAlignment(Pos.CENTER);
    hBox.setSpacing(10);

    return hBox;
  }

  public void loginTema1(){
    btnEntrar.setText("Criar");
    lblBanco.setText("Cadastrar");
    btnCriarConta.setText("Fazer login");

    btnCriarConta.setTextFill(Paint.valueOf("#9017BB"));
    btnEntrar.setTextFill(Paint.valueOf("FFFFFF"));
    btnEntrar.setStyle("-fx-background-color: #9017BB;-fx-background-radius: 90;");

    for (Label label : labelsLogin) {
      label.setTextFill(Paint.valueOf("#9017BB"));
    }
    for (VBox vBox : boxesLogin) {
      vBox.setStyle("-fx-background-color: #FFFFFF");
    }
  }

  public void loginTema2(){
    btnEntrar.setText("Entrar");
    lblBanco.setText("Banco");
    btnCriarConta.setText("Criar uma conta");

    btnEntrar.setTextFill(Paint.valueOf("000000"));
    btnCriarConta.setTextFill(Paint.valueOf("FFFFFF"));
    btnEntrar.setStyle("-fx-background-color: FFFFFF;-fx-background-radius: 90;");


    for (Label label : labelsLogin) {
      label.setTextFill(Paint.valueOf("FFFFFF"));
    }
    for (VBox vBox : boxesLogin) {
      vBox.setStyle("-fx-background-color: #9017BB");
    }
  }

}
