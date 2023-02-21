package com.nathan.util;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 13/01/2022
 * Ultima alteracao: 13/01/2022
 * Nome: DisplayLed
 * Funcao: Transforma uma Hbox do JavaFX num simples display de LED
 * para desenhar a transformacao dos bits em onda na camada fisica.
 * Para funcionar corretamente e' necessario que o Hbox esteja
 * dentro de um ScrollPane com os scroll desativados.
 * Nathan -> 0011110101
 *
 * ************************************************************** */
public abstract class DisplayLed {

  /**
   * Converte a string de sinal em representacao grafica
   * @param bits
   * @param ledDisplay
   * @param sldTime
   */
  public static void show(int[] bits, HBox ledDisplay, Slider sldTime) {
    /* FAZ A ONDA NA ORDEM DOS NUMEROS */
    Platform.runLater(() ->
        ledDisplay.getChildren().clear()
    );
    /*
    new Thread(() -> {
      for (int i = bits.length - 1; i >= 0; i--) {
        if (i == bits.length - 1)
          addSignal(ledDisplay,bits[i], 'n');
        else
          addSignal(ledDisplay,bits[i], bits[i + 1]);
        try {
          Thread.sleep((long) sldTime.getValue());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
    */

    //ADICIONA A ONDA NA ORDEM QUE RECEBE OS NUMEROS
    new Thread(() -> {
      for (int i = 0; i < bits.length; i++) {
        if (i == 0)
          addSignal(ledDisplay,bits[i], 'n');
        else
          addSignal(ledDisplay,bits[i], bits[i - 1]);
        try {
          Thread.sleep((long) (sldTime.getValue()*400));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();

  }

  /**
   * O sinal atual e' adicionado no grafico enquanto que o anterior
   * e' utilizado para verificar a necessidade de adicionar uma barra
   * conectando a troca de sinal _|-
   * @param current Sinal atual
   * @param prev    Sinal anterior
   */
  private static void addSignal(HBox ledDisplay, int current, int prev) {
    Platform.runLater(() -> {
      ObservableList led = ledDisplay.getChildren();
      VBox vBox = new VBox();
      if (current == 0)
        vBox.setAlignment(Pos.BOTTOM_LEFT);
      else
        vBox.setAlignment(Pos.TOP_LEFT);
      if (current != prev && !led.isEmpty())
        led.add(0,new Line(0, 0, 0, ledDisplay.getHeight() - ledDisplay.getPadding().getTop() - ledDisplay.getPadding().getBottom()-1));

      vBox.getChildren().add(new Line(0, 0, 50, 0));
      led.add(0,vBox);
    });
  }


}
