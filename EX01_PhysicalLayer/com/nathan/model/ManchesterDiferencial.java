package com.nathan.model;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 29/07/2021
 * Ultima alteracao: 13/01/2022
 * Nome: Manchester
 * Funcao: codificar e decodificar bits
 * ************************************************************** */
public class ManchesterDiferencial extends Protocolo {
  @Override
  public int[] codificar(int[] bits) {
    int[] cod = new int[bits.length * 2];
    for (int i = 0, j = 0; i < bits.length; i++) {
      // Caso inicial, baixo-alto
      if (i == 0) {
        if (bits[i] == 0) {
          cod[j] = 0;
          cod[j + 1] = 1;
        } else {
          cod[j] = 1;
          cod[j + 1] = 0;
        }
      } else {
        if (bits[i] == bits[i - 1]) {
          cod[j] = cod[j - 1];
          cod[j + 1] = cod[j - 2];
        } else {
          cod[j] = cod[j - 2];
          cod[j + 1] = cod[j - 1];
        }
      }
      j += 2;
    }
    return cod;
  }

  @Override
  public int[] decodificar(int[] bits) {
    int[] decod = new int[bits.length / 2];
    for (int i = 0, j = 0; i < bits.length; i += 2) {
      if (i == 0) {
        if (bits[i] == 0 && bits[i + 1] == 1) {
          decod[0] = 0;
        }
        if (bits[i] == 1 && bits[i + 1] == 0) {
          decod[0] = 1;
        }
      } else {
        if (bits[i] == bits[i - 1]) {
          decod[j] = decod[j - 1];
        } else {
          if (decod[j - 1] == 1) {
            decod[j] = 0;
          } else {
            decod[j] = 1;
          }
        }
      }
      j++;
    }
    return decod;
  }
}
