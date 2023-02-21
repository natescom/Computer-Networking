package com.nathan.controller;

import com.nathan.model.Binaria;
import com.nathan.model.Manchester;
import com.nathan.model.ManchesterDiferencial;
import com.nathan.model.Protocolo;
import com.nathan.model.Rede;
import com.nathan.util.RedeGUI;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller_TelaPrincipal implements Initializable {

  public HBox hbox_led;
  public Label lbl_men;
  public Button btn_enviar;
  public TextField txt_men;
  public MenuButton menu_cod;
  public CheckMenuItem menuItemBinario;
  public CheckMenuItem menuItemManchester;
  public CheckMenuItem menuItemManchesterDiferencial;
  public Label lbl_protocolo;
  public Slider sld_speed;
  public TextArea txt_disp01;
  public TextArea txt_disp02;
  public HBox hboxEmAp;
  public HBox hboxEmApr;
  public HBox hboxEmSe;
  public HBox hboxEmTr;
  public HBox hboxEmRe;
  public HBox hboxEmEn;
  public HBox hboxEmFi;
  public HBox hboxReAp;
  public HBox hboxReApr;
  public HBox hboxReSe;
  public HBox hboxReTr;
  public HBox hboxReRe;
  public HBox hboxReEn;
  public HBox hboxReFi;
  public HBox[] camadasEmissor;
  public HBox[] camadasReceptor;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    menuItemBinario.setSelected(true);
    camadasEmissor  = new HBox[]{hboxEmAp, hboxEmApr, hboxEmSe, hboxEmTr, hboxEmRe, hboxEmEn, hboxEmFi};
    camadasReceptor = new HBox[]{hboxReAp, hboxReApr, hboxReSe, hboxReTr, hboxReRe, hboxReEn, hboxReFi};
  }

  /**
   * Pega o protocolo do menu
   * @return
   */
  private Protocolo getProtocoloFromMenu(){
    if(menuItemBinario.isSelected())
      return new Binaria();
    if(menuItemManchester.isSelected())
      return new Manchester();
    if(menuItemManchesterDiferencial.isSelected())
      return new ManchesterDiferencial();
    return null;
  }

  public void onClick(ActionEvent e) {

    if(e.getSource().equals(btn_enviar)) {
      if(txt_men.getText().equals("")) return;

      lbl_men.setText("Aguardando Mensagem");
      lbl_men.setTextFill(Paint.valueOf("#868686"));

      RedeGUI redeGUI = new RedeGUI(txt_men,lbl_men,hbox_led,btn_enviar, sld_speed, txt_disp01,
          txt_disp02, camadasEmissor, camadasReceptor);
      Rede rede = new Rede(redeGUI,getProtocoloFromMenu());
      rede.aplicacaoTransmissora();
      btn_enviar.setDisable(true);
      return;
    }

    if(e.getSource().equals(menuItemBinario)){
      menuItemBinario.setSelected(true);
      menuItemManchester.setSelected(false);
      menuItemManchesterDiferencial.setSelected(false);
      lbl_protocolo.setText("Binario");
      return;
    }
    if(e.getSource().equals(menuItemManchester)){
      menuItemBinario.setSelected(false);
      menuItemManchester.setSelected(true);
      menuItemManchesterDiferencial.setSelected(false);
      lbl_protocolo.setText("Manchester");
      return;
    }
    if(e.getSource().equals(menuItemManchesterDiferencial)){
      menuItemBinario.setSelected(false);
      menuItemManchester.setSelected(false);
      menuItemManchesterDiferencial.setSelected(true);
      lbl_protocolo.setText("Manchester-Diferencial");
      return;
    }
  }
}
