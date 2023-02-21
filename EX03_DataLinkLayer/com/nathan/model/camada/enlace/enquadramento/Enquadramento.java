package com.nathan.model.camada.enlace.enquadramento;

import java.util.ArrayList;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 20/03/2022
 * Ultima alteracao: 27/03/2022
 * Nome: Enquadramento
 * Funcao: Abstrai as funcionalidades de enquadramento da camda de
 * enlace de dados
 * ************************************************************** */
public abstract class Enquadramento {
  protected static final int quadroSize = 5;
  protected String mensagemOriginal;
  protected String mensagEnquadrada;

  /**
   * Aplica o algoritmo de enquadramento
   * @param bits
   * @return
   */
  public abstract ArrayList<int[]> enquadrar(int[] bits);

  /**
   * Retira o enquadramento da mensagem
   * @param bits
   * @return
   */
  public abstract int[] desenquadrar(ArrayList<int[]> bits);

  public String getMensagemOriginal() {
    return mensagemOriginal;
  }

  public String getMensagEnquadrada() {
    return mensagEnquadrada;
  }

  protected int[] juntarQuadro(ArrayList<int[]> quadros){
    String men = "";
    for(int[] quadro: quadros) {
      for (int i : quadro) {
        men += (i + "");
      }
    }
    int[] bitsComIC = new int[men.length()];
    char[] bitsComICChar = men.toCharArray();
    for (int i = 0; i < bitsComIC.length; i++) {
      bitsComIC[i] = Integer.parseInt(bitsComICChar[i]+"",10);
    }
    return bitsComIC;
  }
}
