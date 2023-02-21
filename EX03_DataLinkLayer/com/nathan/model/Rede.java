package com.nathan.model;

import com.nathan.model.camada.enlace.ctrlerro.ControleDeErro;
import com.nathan.model.camada.enlace.enquadramento.Enquadramento;
import com.nathan.model.camada.fisica.Protocolo;
import com.nathan.model.util.FormatFactory;
import com.nathan.model.util.RedeGUI;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Random;

import static com.nathan.model.util.FormatFactory.arrayToString;
import static com.nathan.model.util.FormatFactory.converterEmChar;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 29/07/2021
 * Ultima alteracao: 13/04/2022
 * Nome: Rede
 * Funcao: Executa os passos de transmissao de mensagem na rede
 * ************************************************************** */
public class Rede extends Thread {
  private final RedeGUI gui;
  private final Protocolo protocolo;
  private final Enquadramento enquadramento;
  private final ControleDeErro controleDeErro;

  public Rede(RedeGUI gui, Protocolo protocolo, Enquadramento enquadramento, ControleDeErro controleDeErro) {
    this.gui = gui;
    this.protocolo = protocolo;
    this.enquadramento = enquadramento;
    this.controleDeErro = controleDeErro;
  }

  @Override
  public void run() {
    ArrayList<int[]> quadrosParaEnviar;
    ArrayList<int[]> quadrosRecebidos = new ArrayList<>();

    String mensagem   = aplicacaoTransmissora();
    int[] bits        = camadaDeAplicacaoTransmissora(mensagem);
    int[] fluxo       = camadaEnlaceDadosTransmissora(bits);
    quadrosParaEnviar = camadaEnlaceDadosTransmissoraEnquadramento(fluxo);

    while (!quadrosParaEnviar.isEmpty()) {
      int[] quadro = quadrosParaEnviar.get(0);
      quadrosParaEnviar.remove(0);
      VBox vBox = new VBox();
      gui.publishAck(20, new String(converterEmChar(quadro)), vBox);
      ProgressIndicator ack = (ProgressIndicator) vBox.getChildren().get(1); //todo: ADD COMENTARIO PRA NAO USAR ACK NO MODO DEV

      int[] qd                = camadaEnlaceDadosTransmissoraControleDeErro(quadro);
      int[] fluxoBrutoDeBits  = camadaFisicaTransmissora(qd);
      int[] fluxoRecebido     = meioDeComunicacao(fluxoBrutoDeBits);
      int[] qd2               = camadaFisicaReceptora(fluxoRecebido);
      int[] quadroRecebido    = camadaEnlaceDadosReceptoraControleDeErro(qd2);

      if (quadroRecebido == null || ack.getProgress() >= 1) {      // CONDICAO DE ERRO todo: REMOVER CONDICAO || ack.getProgress() >= 1 PARA USAR NO MODO DEV
        quadrosParaEnviar.add(quadro);
      } else {
        quadrosRecebidos.add(quadroRecebido);
      }
      gui.removeAck(vBox);
    }

    fluxo     = camadaEnlaceDadosReceptoraEnquadramento(quadrosRecebidos);
    bits      = camadaEnlaceDadosReceptora(fluxo);
    mensagem  = camadaDeAplicacaoReceptora(bits);
    aplicacaoReceptora(mensagem);
  }


  /**
   * Pega o conteudo da caixa de texto e manda pra camada de aplicaxao transmissora
   */
  private String aplicacaoTransmissora() {
    gui.publishIndicador(0, true);
    return gui.getInput();
  }

  /**
   * Pega a mensagem e transforma em vetor de bit
   */
  private int[] camadaDeAplicacaoTransmissora(String s) {
    // Transforma a string da mensagem em binario em vetor de inteiro //
    System.out.println(FormatFactory.paint(FormatFactory.YELLOW_BOLD, "CAMADA DE APLICACAO TRANSMISSORA"));

    char[] chars = s.toCharArray();
    int[] ascii = new int[chars.length];
    String[] binaryStrings = new String[chars.length];
    int[] bits = new int[chars.length * 7];

    // Converte cada caractere em seu valor da tabela ASCII
    for (int i = 0; i < chars.length; i++)
      ascii[i] = chars[i];

    // Converte cada valor ASCII em Binario - SAO NECESSARIO 7 CASAS DECIMAIS PARA CADA CHAR
    String mensagemCodificada = "";
    for (int i = 0; i < ascii.length; i++) {
      String binaryString = Integer.toBinaryString(ascii[i]);
      binaryStrings[i] = String.format("%07d", Integer.valueOf(binaryString));
      mensagemCodificada += binaryStrings[i];
    }

    char[] bitsChar = mensagemCodificada.toCharArray();
    for (int i = 0; i < bits.length; i++) {
      bits[i] = Character.getNumericValue(bitsChar[i]);
    }

    gui.clearTxt();
    gui.publishTxtEmissor("CAMADA DE APLICACAO TRANSMISSORA\n");
    int time = 300;     // 300

    for (int i = 0; i < chars.length; i++) {
      System.out.printf("\t[%s] -> ", chars[i]);
      gui.publishTxtEmissor(String.format("\t[%s] -> ", chars[i]));
      pausarThread(time);

      System.out.printf("[%03d] -> ", ascii[i]);
      gui.publishTxtEmissor(String.format("[%03d] -> ", ascii[i]));
      pausarThread(time);

      System.out.printf("[%s]%n", binaryStrings[i]);
      gui.publishTxtEmissor(String.format("[%s]%n", binaryStrings[i]));
      pausarThread(time);
    }

    return bits;
  }

  private int[] camadaEnlaceDadosTransmissora(int[] bits) {
    gui.publishIndicador(5, true);
    return bits;
  }

  /**
   * Divide a informacao em quadros
   *
   * @param quadro
   */
  private ArrayList<int[]> camadaEnlaceDadosTransmissoraEnquadramento(int[] quadro) {
    System.out.println(FormatFactory.paint(FormatFactory.YELLOW_BOLD, "\n\nCAMADA DE ENLACE DE DADOS TRANSMISSORA - ENQUADRAMENTO"));
    ArrayList<int[]> quadros = enquadramento.enquadrar(quadro);
    int time = 200; // 200
    gui.publishTxtEmissor("\nCAMADA DE ENLACE DE DADOS");
    String menOr = enquadramento.getMensagemOriginal();
    String menEn = enquadramento.getMensagEnquadrada();
    if (menOr == null)
      menOr = "";
    if (menEn == null)
      menEn = "";
    gui.publishTxtEmissor("\n\tOriginal:   ");
    for (int i = 0; i < menOr.length(); i++) {
      gui.publishTxtEmissor("" + menOr.toCharArray()[i]);
      pausarThread(time);
    }
    gui.publishTxtEmissor("\n\tQuadros : ");
    for (int i = 0; i < menEn.length(); i++) {
      gui.publishTxtEmissor("" + menEn.toCharArray()[i]);
      pausarThread(time);
    }

    if (menOr == "" && menEn == "") {
      gui.publishTxtEmissor("\nEnquadramento via Camada Fisica");
    }
    gui.publishTxtEmissor("\n");

    return quadros;
  }

  /**
   * Aplica os algorimtos de controle de erro no quadro
   * @param quadro
   * @return
   */
  private int[] camadaEnlaceDadosTransmissoraControleDeErro(int[] quadro) {
    System.out.println(FormatFactory.paint(FormatFactory.YELLOW_BOLD, "\nCAMADA DE ENLANCE DE DADOS TRANSMISSORA - CONTROLE DE ERRO"));
    gui.publishTxtEmissor("\nCONTROLE DE ERRO TRANSMISSORA\n");
    gui.publishTxtEmissor(String.format("\tQuadro: [%s]\n",new String(converterEmChar(quadro))));
    gui.publishTxtEmissor(String.format("\tBits: %s\n",arrayToString(quadro)));
    quadro = controleDeErro.colocarInformacaoDeControle(quadro);
    gui.publishTxtEmissor(String.format("\tBits com IC: %s\n",arrayToString(quadro)));
    return quadro;
  }

  /**
   * Aplica alguma codificacao na mensagem e envia
   */
  private int[] camadaFisicaTransmissora(int[] quadro) {
    gui.publishIndicador(6, true);
    int[] fluxoBrutoDeBits = protocolo.codificar(quadro);

    int time = 100; // 100
    System.out.println(FormatFactory.paint(FormatFactory.YELLOW_BOLD, "\nCAMADA FISICA TRANSMISSORA"));
    gui.publishTxtEmissor("\nCAMADA FISICA TRANSMISSORA\n");

    System.out.printf("\t %-15s", "Quadro:");
    gui.publishTxtEmissor(String.format("%s", "Quadro: "));

    for (int i = 0; i < quadro.length; i++) {
      System.out.print(quadro[i]);
      gui.publishTxtEmissor(String.valueOf(quadro[i]));
      pausarThread(time);
    }

    System.out.print("\n\t Fluxo de bits: ");
    gui.publishTxtEmissor("\nFluxo de bits:\n");

    for (int i = 0; i < fluxoBrutoDeBits.length; i++) {
      System.out.print(fluxoBrutoDeBits[i]);
      gui.publishTxtEmissor(String.valueOf(fluxoBrutoDeBits[i]));
      pausarThread(time);
    }
    System.out.println();
    gui.publishTxtEmissor("\n");

    return fluxoBrutoDeBits;
  }

  /**
   * Ativa o envio
   *
   * @param fluxoBrutoDeBits
   */
  private int[] meioDeComunicacao(int[] fluxoBrutoDeBits) {
    gui.removeIdicador();
    System.out.println("\nMEIO DE COMUNICACAO");
    int[] pontoA = fluxoBrutoDeBits;
    int[] pontoB = new int[pontoA.length];

    gui.publishSignal(fluxoBrutoDeBits);        // Ativa o DisplayGrafico de transimssao de Bits
    gui.publishTxtEmissor("\nENVIANDO DADOS\n");
    gui.publishTxtReceptor("RECEBENDO DADOS\n");

    for (int i = fluxoBrutoDeBits.length - 1; i >= 0; i--) {
      gui.publishTxtEmissor(fluxoBrutoDeBits[i] + " ");
    }

    for (int i = 0; i < pontoA.length; i++) {
      if (i == pontoA.length / 2) {// PROVOCANDO ERRO
        Random random = new Random();
        int rand = random.nextInt(10);
        int sorter = (int) (10 * gui.getSldErro().getValue());
        if (rand < sorter) {
          System.out.println(FormatFactory.paint(FormatFactory.RED_BACKGROUND, "ERRO"));
          if (pontoA[i] == 0) {
            pontoB[i] = 1;
          } else {
            pontoB[i] = 0;
          }
        } else {
          System.out.println(FormatFactory.paint(FormatFactory.GREEN_BACKGROUND, "TUDO CERTO"));
          pontoB[i] = pontoA[i];
        }
      } else {
        pontoB[i] = pontoA[i];
      }
      gui.publishTxtReceptor(pontoB[i] + " ");
      gui.publishTxtEmissor();
      pausarThread(400); // 400
    }
    return pontoB;
  }

  /**
   * Pega o fluxo bruto de bits e decodifica
   *
   * @param fluxoBrutoDeBits
   */
  private int[] camadaFisicaReceptora(int[] fluxoBrutoDeBits) {
    gui.publishIndicador(6, false);
    int[] quadro = protocolo.decodificar(fluxoBrutoDeBits);


    int time = 100; // 100
    System.out.println(FormatFactory.paint(FormatFactory.BLUE_BOLD, "\nCAMADA FISICA RECEPTORA"));
    gui.publishTxtReceptor("\n\nCAMADA FISICA RECEPTORA");

    System.out.print("\t Fluxo de bits: ");
    gui.publishTxtReceptor("\nFluxo de bits:\n");


    for (int i = 0; i < fluxoBrutoDeBits.length; i++) {
      System.out.print(fluxoBrutoDeBits[i]);
      gui.publishTxtReceptor(fluxoBrutoDeBits[i] + "");

      pausarThread(time);
    }

    System.out.printf("\n\t %-15s", "Quadro:");
    gui.publishTxtReceptor(String.format("%s", "\nQuadro: \n"));

    for (int i = 0; i < quadro.length; i++) {
      System.out.print(quadro[i]);
      gui.publishTxtReceptor(quadro[i] + "");
      pausarThread(time);
    }

    gui.publishTxtReceptor("\n");
    System.out.println("");
    return quadro;
  }

  /**
   * Pega o quadro e manda pro controle de erro
   * @param quadro
   * @return
   */
  private int[] camadaEnlaceDadosReceptora(int[] quadro) {
    gui.publishIndicador(5, false);
    return quadro;
  }

  /**
   * Pega o quadro e envia para o enquadramento
   * @param quadro
   * @return
   */
  private int[] camadaEnlaceDadosReceptoraControleDeErro(int[] quadro) {
    System.out.println(FormatFactory.paint(FormatFactory.BLUE_BOLD, "\nCAMADA DE ENLANCE DE DADOS RECEPTORA - CONTROLE DE ERRO"));
    int time = 200;
    gui.publishTxtReceptor("\nCONTROLE DE ERRO RECEPTORA\n");
    gui.publishTxtReceptor(String.format("\tBits: %s\n",arrayToString(quadro)));
    pausarThread(time);
    boolean correto = controleDeErro.verificarInformacaoDeControle(quadro);
    quadro = controleDeErro.removerInformacaoDeControle(quadro);
    gui.publishTxtReceptor(String.format("\tQuadro: [%s]\n",new String(converterEmChar(quadro))));
    pausarThread(time);
    gui.publishTxtReceptor(String.format("\tCorreto? %b\n\n",correto));
    pausarThread(time);

    if (correto)
      return quadro;
    else
      return null;
  }

  /**
   * Reune os quadros da informacao
   *
   * @param quadros
   */
  private int[] camadaEnlaceDadosReceptoraEnquadramento(ArrayList<int[]> quadros) {
    System.out.println(FormatFactory.paint(FormatFactory.BLUE_BOLD, "CAMADA DE ENLACE DE DADOS RECEPTORA - ENQUADRAMENTO"));
    int[] quadro = enquadramento.desenquadrar(quadros);

    int time = 200; // 200
    gui.publishTxtReceptor("CAMADA DE ENLACE DE DADOS");
    String menOr = enquadramento.getMensagemOriginal();
    String menEn = enquadramento.getMensagEnquadrada();
    if (menOr == null)
      menOr = "";
    if (menEn == null)
      menEn = "";
    gui.publishTxtReceptor("\n\tEnquadrada: ");
    for (int i = 0; i < menEn.length(); i++) {
      gui.publishTxtReceptor("" + menEn.toCharArray()[i]);
      pausarThread(time);
    }
    gui.publishTxtReceptor("\n\tOriginal:   ");
    for (int i = 0; i < menOr.length(); i++) {
      gui.publishTxtReceptor("" + menOr.toCharArray()[i]);
      pausarThread(time);
    }

    if (menOr == "" && menEn == "") {
      gui.publishTxtReceptor("\nEnquadramento via Camada Fisica");
    }

    return quadro;
  }

  /**
   * Pega os bits recebidos e transforma em mensagem
   */
  private String camadaDeAplicacaoReceptora(int[] quadro) {
    gui.publishIndicador(0, false);
    System.out.println(FormatFactory.paint(FormatFactory.BLUE_BOLD, "\nCAMADA DE APLICACAO RECEPTORA"));
    gui.publishTxtReceptor("\n\nCAMADA DE APLICACAO RECEPTORA\n");
    int time = 300; // 300
//    new Thread(() -> {
    String mensagem = "";
    String letra = "";
    for (int i = 0; i < quadro.length; i++) {
      letra += quadro[i];
      if (letra.length() % 7 == 0) {
        int ascii = Integer.parseInt(letra, 2);
        gui.publishTxtReceptor(String.format("\t[%s] -> ", letra));
        pausarThread(time);
        gui.publishTxtReceptor(String.format("[%03d] -> ", ascii));
        pausarThread(time);
        gui.publishTxtReceptor(String.format("[%s] %n", ((char) ascii)));
        System.out.printf("\tBin: %s  ASCII: %03d  Char:%s %n", letra, ascii, ((char) ascii));
        mensagem += ((char) ascii);
        letra = "";
        pausarThread(time);
      }
    }
//      aplicacaoReceptora(mensagem);
//    }).start();
    return mensagem;
  }

  /**
   * Publica o texto recebido
   *
   * @param s
   */
  private void aplicacaoReceptora(String s) {
    System.out.println(FormatFactory.paint(FormatFactory.BLUE_BOLD, "\nAPLICACAO RECEPTORA"));
    gui.publishResult(s);
    System.out.println("\t Mensagem recebida:" + s);
    System.out.println("-----------------------------");
    gui.removeIdicador();
  }

  /**
   * Bloco pra pausar a Thread que chamar essa funcao
   * Serve apenas para deixar o codigo mais legivel
   *
   * @param max Tempo maximo de pausa da Thread, o
   *            valor real eh definido pelo produto
   *            do valor do slide pelo tempo maximo
   */
  private void pausarThread(int max) {
    try {
      Thread.sleep((long) (max * gui.getSld_speed().getValue()));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


}
