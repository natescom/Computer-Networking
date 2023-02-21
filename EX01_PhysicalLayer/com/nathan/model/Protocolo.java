package com.nathan.model;
/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 29/07/2021
 * Ultima alteracao: 13/01/2022
 * Nome: Protocolo
 * Funcao: Define as funcoes dos codificadores
 * ************************************************************** */
public abstract class Protocolo {
  /**
   *
   * @param bits
   * @return
   */
  public abstract int[] codificar(int[] bits);

  /**
   *
   * @param bits
   * @return
   */
  public abstract int[] decodificar(int[] bits);
}
