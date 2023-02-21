package com.nathan.controller;

import com.nathan.model.camada.enlace.*;
import com.nathan.model.camada.fisica.Binaria;
import com.nathan.model.camada.fisica.Manchester;
import com.nathan.model.camada.fisica.ManchesterDiferencial;
import com.nathan.model.camada.fisica.Protocolo;
import com.nathan.model.Rede;
import com.nathan.model.util.RedeGUI;
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
  public Label lbl_enquad;
  public Button btn_enviar;
  public TextField txt_men;
  public MenuButton menu_cod;
  public CheckMenuItem menuItemBinario;
  public CheckMenuItem menuItemManchester;
  public CheckMenuItem menuItemManchesterDiferencial;
  public CheckMenuItem menuItemEnFraming;
  public CheckMenuItem menuItemEnBit;
  public CheckMenuItem menuItemEnByte;
  public CheckMenuItem menuItemEnVF;
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

  private HBox[] camadasEmissor;
  private HBox[] camadasReceptor;

  private CheckMenuItem[] menuCamadaFisica;
  private CheckMenuItem[] menuEnlaceDeDadosEnquadramento;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    menuItemBinario.setSelected(true);
    menuItemEnFraming.setSelected(true);
    camadasEmissor  = new HBox[]{hboxEmAp, hboxEmApr, hboxEmSe, hboxEmTr, hboxEmRe, hboxEmEn, hboxEmFi};
    camadasReceptor = new HBox[]{hboxReAp, hboxReApr, hboxReSe, hboxReTr, hboxReRe, hboxReEn, hboxReFi};
    menuCamadaFisica = new CheckMenuItem[]{menuItemBinario,menuItemManchester,menuItemManchesterDiferencial};
    menuEnlaceDeDadosEnquadramento = new CheckMenuItem[]{menuItemEnFraming, menuItemEnBit, menuItemEnByte, menuItemEnVF};

  }

  /**
   * Pega o protocolo do menu
   * @return
   */
  private Protocolo getProtocoloFromMenu(){
    VioladorDeCamadaFisica violador = null;
    if(menuItemEnVF.isSelected())
      violador = new VioladorDeCamadaFisica();
    if(menuItemBinario.isSelected())
      return new Binaria();
    if(menuItemManchester.isSelected())
      return new Manchester(violador);
    if(menuItemManchesterDiferencial.isSelected())
      return new ManchesterDiferencial(violador);
    return null;
  }

  private Enquadramento getEnquadramentoFromMenu(){
    if(menuItemEnBit.isSelected())
      return new BitStufing();
    if(menuItemEnByte.isSelected())
      return new CharacterStufing();
    if(menuItemEnFraming.isSelected())
      return new Framing();
    if(menuItemEnVF.isSelected())
      return new VioladorDeCamadaFisica();
    return null;
  }

  public void onClick(ActionEvent e) {

    if(e.getSource().equals(btn_enviar)) {
      if(txt_men.getText().equals("")) return;

      lbl_men.setText("Aguardando Mensagem");
      lbl_men.setTextFill(Paint.valueOf("#868686"));

      RedeGUI redeGUI = new RedeGUI(txt_men,lbl_men,hbox_led,btn_enviar, sld_speed, txt_disp01,
          txt_disp02, camadasEmissor, camadasReceptor);
      Rede rede = new Rede(redeGUI,getProtocoloFromMenu(), getEnquadramentoFromMenu());
      rede.aplicacaoTransmissora();
      btn_enviar.setDisable(true);
      return;
    }

    clickItemMenu(e, menuCamadaFisica, menuItemBinario,lbl_protocolo);
    clickItemMenu(e, menuCamadaFisica, menuItemManchester,lbl_protocolo);
    clickItemMenu(e, menuCamadaFisica, menuItemManchesterDiferencial,lbl_protocolo);

    clickItemMenu(e, menuEnlaceDeDadosEnquadramento, menuItemEnFraming, lbl_enquad);
    clickItemMenu(e, menuEnlaceDeDadosEnquadramento, menuItemEnBit, lbl_enquad);
    clickItemMenu(e, menuEnlaceDeDadosEnquadramento, menuItemEnByte, lbl_enquad);
    clickItemMenu(e, menuEnlaceDeDadosEnquadramento, menuItemEnVF, lbl_enquad);

    if(e.getSource().equals(menuItemEnVF)){
      menuItemBinario.setDisable(true);
      lbl_protocolo.setText(menuItemManchester.getText());
      menuItemBinario.setSelected(false);
      menuItemManchester.setSelected(true);
    }

    if(e.getSource().equals(menuItemEnBit) || e.getSource().equals(menuItemEnFraming) || e.getSource().equals(menuItemEnByte)){
      menuItemBinario.setDisable(false);
    }
  }

  public void clickItemMenu(ActionEvent e, CheckMenuItem[] menu, CheckMenuItem item, Label label){
    if(e.getSource().equals(item)){
      for (CheckMenuItem checkMenuItem : menu) {
        checkMenuItem.setSelected(false);
      }
      item.setSelected(true);
      label.setText(item.getText());
    }
  }
}
