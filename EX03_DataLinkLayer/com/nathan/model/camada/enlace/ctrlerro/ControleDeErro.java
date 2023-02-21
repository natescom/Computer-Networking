package com.nathan.model.camada.enlace.ctrlerro;


/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 20/03/2022
 * Ultima alteracao: 27/03/2022
 * Nome: Controle de erro
 * Funcao: Abstrai as funcionalidades do controle de erro da camda de
 * enlace de dados
 * ************************************************************** */
public abstract class ControleDeErro {
  /**
   * Aplica o algoritmo de enquadramento no quadro
   * @param quadro
   * @return
   */
  public abstract int[] colocarInformacaoDeControle(int[] quadro);

  /**
   * Remove a informacao de controle do quadro
   * @param quadro
   * @return
   */
  public abstract int[] removerInformacaoDeControle(int[] quadro);

  /**
   * Verifica se o quadro esta correto
   * @param quadro
   * @return
   */
  public abstract boolean verificarInformacaoDeControle(int[] quadro);
}
