package com.nathan.model.util;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 20/03/2022
 * Ultima alteracao: 27/03/2022
 * Nome: FormatFactory
 * Funcao: Oferece atributos e metodos para formartar string
 * ************************************************************** */
public abstract class FormatFactory {
  // Reset
  public static final String RESET = "\033[0m";  // Text Reset

  public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
  public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
  public static final String RED_BACKGROUND = "\033[41m";    // RED
  public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN

  public static String formatQuadroBitStufing(int[] bits) {
    String quadroFormatado = "[";
    int cont = 0;
    for (int i = 0; i < bits.length; i++) {
      if (bits[i] == 1) {
        cont++;
      } else {
        cont = 0;
      }
      quadroFormatado += bits[i];
      if (cont == 5) {
        quadroFormatado += "] [";
      }
    }
    quadroFormatado += "]";
    return quadroFormatado;
  }

  /**
   * Transforma vetor numa string
   * @param bits
   * @return
   */
  public static String arrayToString(int[] bits) {
    String s = "";
    for (int i = 0; i < bits.length; i++) {
      s += bits[i];
    }
    return s;
  }

  public static int[] converterEmBits(char[] chars){
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
      binaryStrings[i] = String.format("%07d",Integer.valueOf(binaryString));
      mensagemCodificada += binaryStrings[i];
    }

    char[] bitsChar = mensagemCodificada.toCharArray();
    for (int i = 0; i < bits.length; i++) {
      bits[i] = Character.getNumericValue(bitsChar[i]);
    }
    return bits;
  }

  public static char[] converterEmChar(int[] bits){
    String mensagem = "";
    String letra = "";
    for (int i = 0; i < bits.length; i++) {
      letra += bits[i];
      if(letra.length() % 7 == 0){
        int ascii = Integer.parseInt(letra,2);
        mensagem += ((char) ascii);
        letra = "";
      }
    }
    return mensagem.toCharArray();
  }

  public static String paint(String color, String text){
    return color + text + RESET;
  }
}
