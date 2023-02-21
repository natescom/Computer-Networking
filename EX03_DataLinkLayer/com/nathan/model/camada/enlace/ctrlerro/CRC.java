package com.nathan.model.camada.enlace.ctrlerro;

import static com.nathan.model.util.FormatFactory.converterEmChar;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 20/03/2022
 * Ultima alteracao: 27/03/2022
 * Nome: CRC
 * Funcao: Aplica a tecnica da redundancia clicica 32
 * ************************************************************** */
public class CRC extends ControleDeErro {

  private final int[] gerador;    // CRC 32
  private final int qtsDeBits;    // qts de bits de controle

  public CRC() {
    gerador = new int[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1};
    qtsDeBits = gerador.length-1;
  }

  @Override
  public int[] colocarInformacaoDeControle(int[] quadro) {
    int [] novoQuadro = criarNovoQuadro(quadro, qtsDeBits); // cria um quadro maior alocando o espaco da informacao de controle
    int[] ic = dividirbinario(novoQuadro,gerador);

    // ADICIONANDO INFORMACAO DE CONTROLE NO FINAL NOVO QUADRO
    for(int i = quadro.length, j = 1; j < ic.length; i++, j++){
      novoQuadro[i] = ic[j];           // acrescenta o resto da divisao
    }

    System.out.println("\tQuadro  : " + new String(converterEmChar(quadro)));
    System.out.println("\tOriginal: " + array2String(quadro));
    System.out.println("\tC/ Inf C: " + array2String(novoQuadro));

    return novoQuadro;
  }

  @Override
  public int[] removerInformacaoDeControle(int[] quadro) {
    int[] novoQuadro = new int[quadro.length-qtsDeBits];
    for (int i = 0; i < novoQuadro.length; i++) {
      novoQuadro[i] = quadro[i];
    }

    System.out.println("\tOriginal: " + array2String(quadro));
    System.out.println("\tS/ Inf C: " + array2String(novoQuadro));
    System.out.println("\tCorreto? " + verificarInformacaoDeControle(quadro));

    return novoQuadro;
  }

  @Override
  public boolean verificarInformacaoDeControle(int[] quadro) {
    int[] result = dividirbinario(quadro,gerador);
    System.out.println("\tDivisao: "+ array2String(result));
    for (int i = 1; i < result.length; i++) {
      if(result[i]!=0)
        return false;
    }
    return true;
  }

  private String array2String (int[] bits){
    String s = "";
    for (int i = 0; i < bits.length; i++) {
      s+=bits[i];
    }
    return s;
  }

  /**
   * Pega a primeira parte do divisor para executar a divisao binaria
   * @param vet
   * @param start
   * @param end
   * @return
   */
  private static int [] getSubArray(int vet[], int start, int end){
    int [] retorno = new int[(end-start)];
    int count = 0;
    for(int i = 0; i<vet.length; i++){
      if(i>=start && i< end){
        retorno[count] = vet[i];
        count++;
      }
    }
    return retorno;
  }

  /**
   * Xor para dois valores de bit
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

  /**
   * xor para bits colocados em array
   * @param m
   * @param g
   * @return
   */
  public int[] xor(int [] m, int [] g){
    int [] gMenor = new int[gerador.length];
    int [] resultado = new int [m.length];

    if(m[0] == 0)
      g = gMenor;
    for(int i = 1; i < m.length; i++){
      resultado[i-1] = xor(m[i],g[i]);
    }
    return resultado;
  }

  /**
   * Realiza a operacao de modulo, isso e
   * divite e retorna o resto da divisao
   * @param dividendo   Deve ser o quadro
   * @param divisor     Deve ser a funcao geradora
   * @return            CRC - Resto da Divisao
   */
  public int[] dividirbinario(int[] dividendo, int[] divisor){
    int[] dividendoParcial = getSubArray(dividendo, 0, divisor.length);

    for(int i = divisor.length; i < dividendo.length; i++){
      dividendoParcial = xor(dividendoParcial, divisor);                     // realiza xor com gerador e resto
      dividendoParcial[divisor.length-1] = dividendo[i];                     // desce o proximo bit da divisao
    }

    return dividendoParcial;
  }

  /**
   * Cria um quadro com todos os bits do quadro original
   * e o espaco da informacao de controle
   * @param quadro
   * @param extraSize
   * @return
   */
  public static int [] criarNovoQuadro(int [] quadro, int extraSize){
    int [] m = new int[quadro.length + extraSize];  // cria vetor com tamanho extra
    for(int i = 0; i < quadro.length; i++){         // o laco vai percorrer ate o tamanho quadro
      m[i] = quadro[i];
    }
    return m;
  }



}
