package com.nathan.model.util;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 15/01/2022
 * Ultima alteracao: 19/02/2022
 * Nome: RedeGUI
 * Funcao: Fornece os metodos necessarios para fazer alteracoes na
 * interface
 * ***************************************************************/
public class RedeGUI {
  protected final TextField txtInput;
  protected final Label lblDisp02;
  protected final HBox hboxLed;
  protected final HBox hboxAcks;
  protected final Button btnEnviar;
  protected final Slider sldSpeed;
  protected final Slider sldErro;
  protected final TextArea txtEmissor;
  protected final TextArea txtReceptor;
  protected final HBox[] camadasEmissor;
  protected final HBox[] camadasReceptor;
  protected final ImageView imageViewIndicador;
  protected boolean ack_Status;

  /**
   * Construtor do grafico
   *
   * @param txtInput        Campo de texto para digitar a mensagem
   * @param lblDisp02       Label receptora da mensagem
   * @param hboxLed         Hbox para imprimir o grafico
   * @param hboxAcks
   * @param btnEnviar       Botao enviar
   * @param sldSpeed        Medidor de velocidade da transmissao
   * @param sldErro
   * @param txtEmissor      Caixa de texto com informacoes do emissor
   * @param txtReceptor     Caixa de texto com informacoes do receptor
   * @param camadasEmissor  Todas as camadas do disp emissor
   * @param camadasReceptor Todas as camadas do disp receptor
   */
  public RedeGUI(TextField txtInput, Label lblDisp02, HBox hboxLed, HBox hboxAcks, Button btnEnviar,
                 Slider sldSpeed, Slider sldErro, TextArea txtEmissor, TextArea txtReceptor, HBox[] camadasEmissor,
                 HBox[] camadasReceptor) {
    this.txtInput = txtInput;
    this.lblDisp02 = lblDisp02;
    this.hboxLed = hboxLed;
    this.hboxAcks = hboxAcks;
    this.btnEnviar = btnEnviar;
    this.sldSpeed = sldSpeed;
    this.sldErro = sldErro;
    this.txtEmissor = txtEmissor;
    this.txtReceptor = txtReceptor;
    this.camadasEmissor = camadasEmissor;
    this.camadasReceptor = camadasReceptor;
    this.imageViewIndicador = new ImageView();
    Platform.runLater(() -> this.imageViewIndicador.setImage(Gallery.INDICADOR));
    this.ack_Status = false;
  }

  /**
   * @return Slider de velocidade da transmissao
   */
  public Slider getSld_speed() {
    return sldSpeed;
  }

  /**
   * @return Slider com propabilidade de erro
   */
  public Slider getSldErro() {
    return sldErro;
  }

  /**
   * Retorna a entrada do simulador
   *
   * @return
   */
  public String getInput() {
    return txtInput.getText();
  }

  /**
   * Publica o sinal no grafico
   *
   * @param bits
   */
  public void publishSignal(int[] bits) {
    DisplayLed.show(bits, hboxLed, sldSpeed);
  }

  /**
   * Publica texto na caixa de texto do emissor
   *
   * @param s Texto a ser publicado
   */
  public void publishTxtEmissor(String s) {
    Platform.runLater(() -> {
      txtEmissor.setText(txtEmissor.getText() + s);
      txtEmissor.positionCaret(txtEmissor.getText().length());
    });
  }

  /**
   * Sobrecarga, serve para apagar o texto na caixa
   * do emissor para simular o envio das informacoes
   */
  public void publishTxtEmissor() {
    Platform.runLater(() -> {
      txtEmissor.setText(txtEmissor.getText().substring(0, txtEmissor.getText().length() - 2));
      txtEmissor.positionCaret(txtEmissor.getText().length());
    });
  }

  /**
   * Publica informacao na caixa de texto do receptor
   *
   * @param s
   */
  public void publishTxtReceptor(String s) {
    Platform.runLater(() -> {
      txtReceptor.setText(txtReceptor.getText() + s);
      txtReceptor.positionCaret(txtReceptor.getText().length());
    });
  }

  /**
   * Publica texto no campo de mensagem do receptor
   *
   * @param s
   */
  public void publishResult(String s) {
    Platform.runLater(() -> {
      lblDisp02.setText(s);
      lblDisp02.setTextFill(Paint.valueOf("#000000"));
      btnEnviar.setDisable(false);
    });
  }

  /**
   * Limpa a caixa de texto do emissor e do recepetor
   */
  public void clearTxt() {
    Platform.runLater(() -> {
      txtEmissor.setText("");
      txtReceptor.setText("");
    });
  }


  /**
   * Publica o indicador de camada
   *
   * @param i       Indice da camada
   * @param emissor True: Camada do dispositivo emissor,
   *                False: Camada do dispositivo Receptor
   */
  public void publishIndicador(int i, boolean emissor) {
    HBox[] camadas;
    if (emissor)
      camadas = camadasEmissor;
    else
      camadas = camadasReceptor;

    for (HBox camada : camadas) {
      ObservableList observableList = camada.getChildren();
      if (observableList.contains(imageViewIndicador)) {
        Platform.runLater(() -> observableList.remove(imageViewIndicador));
        break;
      }
    }
    Platform.runLater(() -> camadas[i].getChildren().add(0, imageViewIndicador));
  }

  /**
   * Remove o indicador de camada
   */
  public void removeIdicador() {
    for (HBox camada : camadasEmissor) {
      ObservableList observableList = camada.getChildren();
      if (observableList.contains(imageViewIndicador)) {
        Platform.runLater(() -> observableList.remove(imageViewIndicador));
        break;
      }
    }
    for (HBox camada : camadasReceptor) {
      ObservableList observableList = camada.getChildren();
      if (observableList.contains(imageViewIndicador)) {
        Platform.runLater(() -> observableList.remove(imageViewIndicador));
        break;
      }
    }
  }

  /**
   * Publica um contador na interface
   *
   * @param duration
   * @param quadro
   * @param vBox
   */
  public void publishAck(int duration,String quadro, VBox vBox) {
    ObservableList observableList = hboxAcks.getChildren();
    vBox.setPrefHeight(150);
    Label label = new Label();
    label.setText("["+quadro+"]");
    ProgressIndicator progressIndicator = new ProgressIndicator();
    progressIndicator.setPrefHeight(150);
    progressIndicator.setProgress(0);
    vBox.setAlignment(Pos.CENTER);
    vBox.getChildren().add(label);
    vBox.getChildren().add(progressIndicator);
    Platform.runLater(() -> observableList.add(vBox));
    new Thread(() -> {
      while (progressIndicator.getProgress() < 1) {
        Platform.runLater(() -> progressIndicator.setProgress(progressIndicator.getProgress() + 0.01));
        try {
          Thread.sleep((long) (0.01 * (duration * 1000L)));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      removeAck(vBox);
    }).start();
  }

  public void removeAck(VBox vBox) {
    ObservableList observableList = hboxAcks.getChildren();
    if (observableList.contains(vBox))
      Platform.runLater(() -> observableList.remove(vBox));
  }


}
