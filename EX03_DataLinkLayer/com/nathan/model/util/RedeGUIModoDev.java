package com.nathan.model.util;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class RedeGUIModoDev extends RedeGUI{


  /**
   * Construtor do grafico
   *
   */
  public RedeGUIModoDev(String input, double erroRatio) {
    super(new TextField(), null, null, null, null, new Slider(), new Slider(), null, null, null, null);
    sldSpeed.setValue(0);
    sldErro.setValue(erroRatio);
    txtInput.setText(input);
  }

  @Override
  public void publishSignal(int[] bits) {
  }

  @Override
  public void publishTxtEmissor(String s) {
  }

  @Override
  public void publishTxtEmissor() {
  }

  @Override
  public void publishTxtReceptor(String s) {
  }

  @Override
  public void publishResult(String s) {
  }

  @Override
  public void clearTxt() {
  }

  @Override
  public void publishIndicador(int i, boolean emissor) {
  }

  @Override
  public void removeIdicador() {
  }

  @Override
  public void publishAck(int duration, String quadro, VBox vBox) {

  }

  @Override
  public void removeAck(VBox vBox) {
  }
}
