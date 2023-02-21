package com.nathan.model.camada.enlace.ctrlerro;

import java.util.ArrayList;

import static com.nathan.model.util.FormatFactory.arrayToString;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 20/03/2022
 * Ultima alteracao: 27/03/2022
 * Nome: Hamming
 * Funcao: Aplica a tecnica de Hamming para encontrar uma informa
 * cao de controle
 * ************************************************************** */
public class Hamming extends ControleDeErro {


  /**
   * Descobre a quantidade de bits de controle
   *
   * @param tamQuadro
   * @return
   */
  public int qtdDeBitsDeControle(int tamQuadro) {
    int p = 0;
    while (Math.pow(2, p) < tamQuadro + p + 1)
      p++;
    return p;
  }

  /**
   * Calcula o log de um numero em uma base qualquer
   * uso pra descobrir quantos bits uso pra representar
   * a informacao de controle
   *
   * @param base
   * @param lognumber
   * @return
   */
  public double log(double base, double lognumber) {
    return Math.log(lognumber) / Math.log(base);
  }

  /**
   * Decompoe um numero em potencias de 2
   *
   * @param i
   * @return
   */
  public int[] decompoe(int i) {
    int[] numero = {};
    int j = 0;
    while (somaVetor(numero) < i) {
      int[] array2 = {(int) Math.pow(2, j)};
      int[] concate = new int[numero.length + array2.length];
      System.arraycopy(numero, 0, concate, 0, numero.length);
      System.arraycopy(array2, 0, concate, numero.length, array2.length);
      numero = concate;
      j++;
    }
    return numero;
  }

  /**
   * Concatena arrays
   *
   * @param a
   * @param b
   * @return
   */
  public int[] juntaArray(int[] a, int[] b) {
    int[] concate = new int[a.length + b.length];
    System.arraycopy(a, 0, concate, 0, a.length);
    System.arraycopy(b, 0, concate, a.length, b.length);
    return concate;
  }

  /**
   * Verifica se um numero esta contido em um vetor
   *
   * @param vetor
   * @param a
   * @return
   */
  public boolean contem(int[] vetor, int a) {
    for (int i = 0; i < vetor.length; i++) {
      if (a == vetor[i])
        return true;
    }
    return false;
  }

  /**
   * Soma todos os elementos de um vetos
   *
   * @param vetor
   * @return
   */
  public int somaVetor(int[] vetor) {
    int i = 0;
    for (int j = 0; j < vetor.length; j++)
      i += vetor[j];
    return i;
  }

  /**
   * Descobre qual a informacao de controle de um certo indice
   *
   * @param indice
   * @param quadro
   * @param indicesDeM
   * @return
   */
  public int calcularInformacaoDeControle(int indice, int[] quadro, int[] indicesDeM) {
    // SE M1 CONTEM INDICE DE X1 ENTAO M1 VAI SER USADO NO CALCULO DE X1
    int resultado = -1;
    for (int i = 0; i < indicesDeM.length; i++) {
      int mxIndice = indicesDeM[i];
      int[] mx = decompoe(mxIndice);
      int mxValor = quadro[i];
      if (contem(mx, indice)) {
        resultado = (resultado < 0) ? mxValor : xor(resultado, mxValor);
      }
    }
    return resultado;
  }

  int[] t;

  /**
   * Operacao xor
   *
   * @param a
   * @param b
   * @return
   */
  public int xor(int a, int b) {
    if (a == b)
      return 0;
    else
      return 1;
  }


  @Override
  public int[] colocarInformacaoDeControle(int[] quadro) {
    int icDeControleTamanho = qtdDeBitsDeControle(quadro.length);
    int[] novoQuadro = new int[quadro.length + icDeControleTamanho];
    int[] indicedeM = {};
    t = quadro;

    for (int i = 0, j = 0; i < novoQuadro.length; i++, j++) {
      double potencia = log(2, i + 1);
      double resto = potencia % 1;
      if (resto > 0) {
        novoQuadro[i] = quadro[j];
      } else {
        indicedeM = juntaArray(new int[]{(i + 1)}, indicedeM);
        j--;
      }
    }

    for (int i = 0; i < novoQuadro.length; i++) {
      double potencia = log(2, i + 1);
      double resto = potencia % 1;
      if (resto == 0) {       // EH POTENCIA DE DOIS
        novoQuadro[i] = calcularInformacaoDeControle(i + 1, quadro, indicedeM);
      }
    }

    System.out.println("\tOriginal: " + arrayToString(quadro));
    System.out.println("\tC/ Inf C: " + arrayToString(novoQuadro));
    System.out.println("\tCorreto? " + verificarInformacaoDeControle(quadro));


    return novoQuadro;
  }

  @Override
  public int[] removerInformacaoDeControle(int[] quadro) {
    ArrayList<Integer> novoQuadro = new ArrayList<>();
    System.out.print("Removendo: ");
    for (int i = 0; i < quadro.length; i++) {
      double potencia = log(2, i + 1);
      double resto = potencia % 1;
      if (resto > 0) {
        novoQuadro.add(quadro[i]);
      }else{
        System.out.print((i+1)+" ");
      }
    }
    System.out.println();

    int[] novoQuadroArray = new int[novoQuadro.size()];

    for (int i = 0; i < novoQuadroArray.length; i++) {
      novoQuadroArray[i] = novoQuadro.get(i);
    }

    System.out.println("\tOriginal: " + arrayToString(quadro));
    System.out.println("\tS/ Inf C: " + arrayToString(novoQuadroArray));

    return novoQuadroArray;
  }

  @Override
  public boolean verificarInformacaoDeControle(int[] quadro) {
    ArrayList<Integer> ms = new ArrayList<>();
    ArrayList<Integer> xs = new ArrayList<>();
    for (int i = 0; i < quadro.length; i++) {
      double potencia = log(2, i+1);
      double resto = potencia % 1;
      if (resto > 0) {
        xs.add(quadro[i]);
      }else {
        ms.add(quadro[i]);
      }
    }
    quadro = removerInformacaoDeControle(quadro);
    for (int i = 0; i < quadro.length; i++)
      if(quadro[i]!=t[i]) {
        return false;
      }

    return true;
  }
}
