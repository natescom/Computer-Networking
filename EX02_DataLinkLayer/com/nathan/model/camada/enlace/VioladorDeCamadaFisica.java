package com.nathan.model.camada.enlace;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 20/03/2022
 * Ultima alteracao: 21/03/2022
 * Nome: VioladorDeCamadaFisica
 * Funcao: Aplica a tecnica de violacao da camada fisica para
 * utilizar um par Alto-Alto da codificacao Manchester para
 * delimitar o tamanho dos quadros
 * ************************************************************** */
public class VioladorDeCamadaFisica extends Enquadramento {
  int quadroSize = 10;

  /**
   * Nao utilizado pois eh chamado na camada de enlace
   * @param bits
   * @return
   */
  @Override
  public int[] enquadrar(int[] bits) {
    return bits;
  }

  /**
   * Nao utilizado pois eh chamado na camada de enlace
   * @param bits
   * @return
   */
  @Override
  public int[] desenquadrar(int[] bits) {
    return bits;
  }

  /**
   * Aplica o enquadramento na camada fisica usando um
   * par Alto-Alto para delimitar o tamanho do quadro
   * @param bits
   * @return
   */
  public int[] enquadrarNaCamadaFisica(int[] bits) {
    int qtsDeQuadros = bits.length / quadroSize;
    int[] novoBits = new int[bits.length + (qtsDeQuadros * 2)];
    for (int i = 0, j = 0; i < bits.length; i++, j++) {
      if (i % quadroSize == 0) {
        novoBits[j] = 1;
        novoBits[j + 1] = 1;
        j++;
      }
      novoBits[j] = bits[i];
    }

    System.out.println("\nCAMADA DE ENLACE DE DADOS EMISSORA");
    System.out.print("\tOriginal: " + arraytoString(bits));
    System.out.println("\n\tEnquadra: " + arraytoString(novoBits));

    return bits;
  }

  /**
   * Retira o enquadramento da mensagem na camada fisica
   * @param bits
   * @return
   */
  public int[] desenquadrarNaCamadaFisica(int[] bits) {
    int qtsDeQuadros = bits.length / quadroSize;
    int[] novoBits = new int[bits.length - (qtsDeQuadros * 2)];
    for (int i = 0, j = 0; i < bits.length; i++, j++) {
      if (i % quadroSize == 0) {
        i += 2;
      }
      novoBits[j] = bits[i];
    }

    System.out.println("\n\nCAMADA DE ENLACE DE DADOS RECEPTORA");
    System.out.print("\tEnquadra: " + arraytoString(bits));
    System.out.print("\n\tOriginal: " + arraytoString(novoBits) + "\n");

    return bits;
  }

  /**
   * Transforma vetor numa string
   * @param bits
   * @return
   */
  private String arraytoString(int[] bits) {
    String s = "";
    for (int bit : bits) {
      s += bit;
    }
    return s;
  }

}
