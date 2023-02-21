package com.nathan.model;

import com.nathan.model.camada.enlace.Enquadramento;
import com.nathan.model.camada.fisica.Protocolo;
import com.nathan.model.util.RedeGUI;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 29/07/2021
 * Ultima alteracao: 20/02/2022
 * Nome: Rede
 * Funcao: Executa os passos de transmissao de mensagem na rede
 * ************************************************************** */
public class Rede {
  private final RedeGUI gui;
  private final Protocolo protocolo;
  private final Enquadramento enquadramento;

  public Rede(RedeGUI gui, Protocolo protocolo, Enquadramento enquadramento) {
    this.gui = gui;
    this.protocolo = protocolo;
    this.enquadramento = enquadramento;
  }

  /**
   * Pega o conteudo da caixa de texto e manda pra camada de aplicaxao transmissora
   */
  public void aplicacaoTransmissora() {
    camadaDeAplicacaoTransmissora(gui.getInput());
    gui.publishIndicador(0, true);
  }

  /**
   * Pega a mensagem e transforma em vetor de bit
   */
  private void camadaDeAplicacaoTransmissora(String s) {
    // Transforma a string da mensagem em binario em vetor de inteiro //
    System.out.println("CAMADA DE APLICACAO TRANSMISSORA");

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

    new Thread(() -> {
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

      camadaEnlaceDadosTransmissora(bits);
    }).start();

  }

  public void camadaEnlaceDadosTransmissora(int[] bits) {
    gui.publishIndicador(5, true);
    camadaEnlaceDadosTransmissoraEnquadramento(bits);
  }

  /**
   * Divide a informacao em quadros
   *
   * @param quadro
   */
  public void camadaEnlaceDadosTransmissoraEnquadramento(int[] quadro) {
    int[] quadro2 = enquadramento.enquadrar(quadro);
    int time = 200; // 200
    new Thread(() -> {
      gui.publishTxtEmissor("\nCAMADA DE ENLACE DE DADOS");
      String menOr = enquadramento.getMensagemOriginal();
      String menEn = enquadramento.getMensagEnquadrada();
      if(menOr == null)
        menOr = "";
      if(menEn == null)
        menEn = "";
      gui.publishTxtEmissor("\n\tOriginal:   ");
      for (int i = 0; i < menOr.length(); i++) {
        gui.publishTxtEmissor("" + menOr.toCharArray()[i]);
        pausarThread(time);
      }
      gui.publishTxtEmissor("\n\tEnquadrada: ");
      for (int i = 0; i < menEn.length(); i++) {
        gui.publishTxtEmissor("" + menEn.toCharArray()[i]);
        pausarThread(time);
      }

      if(menOr == "" && menEn == ""){
        gui.publishTxtEmissor("\nEnquadramento via Camada Fisica");
      }

      gui.publishTxtEmissor("\n");

      camadaEnlaceDadosTransmissoraControleDeErro(quadro2);
    }).start();
  }

  public void camadaEnlaceDadosTransmissoraControleDeErro(int[] quadro) {
    camadaEnlaceDadosTransmissoraControleDeFluxo(quadro);
  }

  public void camadaEnlaceDadosTransmissoraControleDeFluxo(int[] quadro) {
    camadaFisicaTransmissora(quadro);
  }

  /**
   * Aplica alguma codificacao na mensagem e envia
   */
  private void camadaFisicaTransmissora(int[] quadro) {
    gui.publishIndicador(6, true);
    int[] fluxoBrutoDeBits = protocolo.codificar(quadro);

    int time = 100; // 100
    new Thread(() -> {
      System.out.println("\nCAMADA FISICA TRANSMISSORA");
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

      meioDeComunicacao(fluxoBrutoDeBits);
    }).start();

  }

  /**
   * Ativa o envio
   *
   * @param fluxoBrutoDeBits
   */
  private void meioDeComunicacao(int[] fluxoBrutoDeBits) {
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

    new Thread(() -> {
      /**
       * Essa rotina envia os dados de um ponto ao outro
       */
      for (int i = 0; i < pontoA.length; i++) {
        pontoB[i] = pontoA[i];
        gui.publishTxtReceptor(pontoB[i] + " ");
        gui.publishTxtEmissor();
        pausarThread(400); // 400
      }
      camadaFisicaReceptora(fluxoBrutoDeBits);
    }).start();
  }

  /**
   * Pega o fluxo bruto de bits e decodifica
   *
   * @param fluxoBrutoDeBits
   */
  private void camadaFisicaReceptora(int[] fluxoBrutoDeBits) {
    gui.publishIndicador(6, false);
    int[] quadro = protocolo.decodificar(fluxoBrutoDeBits);


    int time = 100; // 100
    new Thread(() -> {
      System.out.println("\nCAMADA FISICA RECEPTORA");
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

      camadaEnlaceDadosReceptora(quadro);
    }).start();
  }

  public void camadaEnlaceDadosReceptora(int[] quadro) {
    gui.publishIndicador(5, false);
    camadaEnlaceDadosReceptoraEnquadramento(quadro);
  }

  /**
   * Divide a informacao em quadros
   *
   * @param quadro
   */
  public void camadaEnlaceDadosReceptoraEnquadramento(int[] quadro) {
    int[] quadro2 = enquadramento.desenquadrar(quadro);
    int time = 200; // 200
    new Thread(() -> {
      gui.publishTxtReceptor("\n\nCAMADA DE ENLACE DE DADOS");
      String menOr = enquadramento.getMensagemOriginal();
      String menEn = enquadramento.getMensagEnquadrada();
      if(menOr == null)
        menOr = "";
      if(menEn == null)
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

      if(menOr == "" && menEn == ""){
        gui.publishTxtReceptor("\nEnquadramento via Camada Fisica");
      }

      camadaEnlaceDadosReceptoraControleDeErro(quadro2);
    }).start();
  }

  public void camadaEnlaceDadosReceptoraControleDeErro(int[] quadro) {
    camadaEnlaceDadosReceptoraControleDeFluxo(quadro);
  }

  public void camadaEnlaceDadosReceptoraControleDeFluxo(int[] quadro) {
    camadaDeAplicacaoReceptora(quadro);
  }

  /**
   * Pega os bits recebidos e transforma em mensagem
   */
  private void camadaDeAplicacaoReceptora(int[] quadro) {
    gui.publishIndicador(0, false);
    System.out.println("\nCAMADA DE APLICACAO RECEPTORA");
    gui.publishTxtReceptor("\n\nCAMADA DE APLICACAO RECEPTORA\n");
    int time = 300; // 300
    new Thread(() -> {
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
      aplicacaoReceptora(mensagem);
    }).start();

  }

  /**
   * Publica o texto recebido
   *
   * @param s
   */
  private void aplicacaoReceptora(String s) {
    System.out.println("\nAPLICACAO RECEPTORA");
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
  public void pausarThread(int max) {
    try {
      Thread.sleep((long) (max * gui.getSld_speed().getValue()));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


}
