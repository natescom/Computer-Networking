package com.nathan.model;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 29/07/2021
 * Ultima alteracao: 13/01/2022
 * Nome: Manchester
 * Funcao: codificar e decodificar bits
 * ************************************************************** */
public class Manchester extends Protocolo{
  @Override
  public int[] codificar(int[] bits) {
    int[] cod = new int[bits.length*2];
    for (int i = 0, j = 0; i < bits.length; i++) {
      if(bits[i] == 0){
        cod[j] = 0;
        cod[j+1] = 1;
      }else{
        cod[j] = 1;
        cod[j+1] = 0;
      }
      j+=2;
    }
    return cod;
  }

  @Override
  public int[] decodificar(int[] bits) {
    int[] decod = new int[bits.length/2];
    for (int i = 0, j = 0; i < bits.length; i+=2) {
      if(bits[i] == 0 && bits[i+1] == 1){
        decod[j] = 0;
      }
      if(bits[i] == 1 && bits[i+1] == 0){
        decod[j] = 1;
      }
      j++;
    }
    return decod;
  }
}
