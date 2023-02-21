package com.nathan.model.camada.enlace.ctrlerro;

import static com.nathan.model.util.FormatFactory.arrayToString;
import static com.nathan.model.util.FormatFactory.converterEmChar;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 20/03/2022
 * Ultima alteracao: 27/03/2022
 * Nome: BitParidadePar
 * Funcao: Aplica a tecnica de Bit de Paridade para encontrar uma
 * informacao de controle
 * ************************************************************** */
public class BitParidadePar extends ControleDeErro{

  @Override
  public int[] colocarInformacaoDeControle(int[] quadro) {
    int[] novoQuadro = new int[quadro.length+1];
    int paridade = 0;

    for (int i = 0; i < quadro.length; i++) {
      if(quadro[i]==1)
        paridade++;
      novoQuadro[i] = quadro[i];
    }
    if(paridade%2==0){
      novoQuadro[novoQuadro.length-1] = 0;
    }else{
      novoQuadro[novoQuadro.length-1] = 1;
    }

    System.out.println("\tQuadro  : " + new String(converterEmChar(quadro)));
    System.out.println("\tOriginal: " + arrayToString(quadro));
    System.out.println("\tC/ Inf C: " + arrayToString(novoQuadro));

    return novoQuadro;
  }

  @Override
  public int[] removerInformacaoDeControle(int[] quadro) {
    int[] novoQuadro = new int[quadro.length-1];
    for (int i = 0; i < novoQuadro.length; i++) {
      novoQuadro[i] = quadro[i];
    }

    System.out.println("\tOriginal: " + arrayToString(quadro));
    System.out.println("\tS/ Inf C: " + arrayToString(novoQuadro));
    System.out.println("\tCorreto? " + verificarInformacaoDeControle(quadro));

    return novoQuadro;
  }

  @Override
  public boolean verificarInformacaoDeControle(int[] quadro) {
    int paridade = 0;
    for (int i = 0; i < quadro.length-1; i++) {
      if(quadro[i]==1)
        paridade++;
    }

    return (paridade%2 == quadro[quadro.length-1]);
  }


}
