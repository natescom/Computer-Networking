package com.nathan.model.camada.enlace;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 20/03/2022
 * Ultima alteracao: 21/03/2022
 * Nome: BitStufing
 * Funcao: Aplica a tecnica de enquadramento Bit Stufing que consiste
 * em adicionar um bit 0 a cada sequencia de 5 bits 1
 * ************************************************************** */
public class BitStufing extends Enquadramento {

  @Override
  public int[] enquadrar(int[] bits) {
    int[] bitsComIC = colocarInformacaoDeControle(bits);

    mensagemOriginal = simpleArray(bits);
    mensagEnquadrada = formatQuadro(bitsComIC);

    System.out.println("\nCAMADA DE ENLACE DE DADOS EMISSORA");
    System.out.print("\tOriginal: " + mensagemOriginal);
    System.out.println("\n\tEnquadra: " + mensagEnquadrada);

    return bitsComIC;
  }

  @Override
  public int[] desenquadrar(int[] bits) {
    int[] bitsSemIC = removerInformacaoDeControle(bits);

    mensagEnquadrada = formatQuadro(bits);
    mensagemOriginal = simpleArray(bitsSemIC);

    System.out.println("\n\nCAMADA DE ENLACE DE DADOS RECEPTORA");
    System.out.print("\tEnquadra: " + mensagEnquadrada);
    System.out.print("\n\tOriginal: " + mensagemOriginal + "\n");

    return bitsSemIC;
  }

  /**
   * Coloca a Informacao de cotrole no quadro
   *
   * @param mensagem
   * @return
   */
  private int[] colocarInformacaoDeControle(int[] mensagem) {
    String bitsComIC = "";
    int cont = 0;         // Conta os 1 consecutivos
    for (int i = 0; i < mensagem.length; i++) {
      bitsComIC += mensagem[i];
      if (mensagem[i] == 1)
        cont++;
      else
        cont = 0;
      if (cont == 5) {
        cont = 0;
        bitsComIC += 0;
      }
    }
    char[] bitsComICArray = bitsComIC.toCharArray();
    mensagem = new int[bitsComICArray.length];
    for (int i = 0; i < mensagem.length; i++) {
      mensagem[i] = Character.getNumericValue(bitsComICArray[i]);
    }
    return mensagem;
  }

  /**
   * Remove a Informacao de cotrole no quadro
   *
   * @param mensagem
   * @return
   */
  private int[] removerInformacaoDeControle(int[] mensagem) {
    String bitsSemIC = "";
    int cont = 0;         // Conta os 1 consecutivos
    for (int i = 0; i < mensagem.length; i++) {
      if (cont == 5) {
        cont = 0;
        i++;
      }
      if (mensagem[i] == 1)
        cont++;
      else
        cont = 0;
      bitsSemIC += mensagem[i];
    }
    char[] bitsSemICArray = bitsSemIC.toCharArray();
    mensagem = new int[bitsSemICArray.length];
    for (int i = 0; i < mensagem.length; i++) {
      mensagem[i] = Character.getNumericValue(bitsSemICArray[i]);
    }
    return mensagem;
  }

  protected String formatQuadro(int[] bits) {
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

  private String simpleArray(int[] bits) {
    String s = "";
    for (int i = 0; i < bits.length; i++) {
      s += bits[i];
    }
    return s;
  }
}
