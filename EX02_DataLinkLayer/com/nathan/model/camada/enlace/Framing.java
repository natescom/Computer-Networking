package com.nathan.model.camada.enlace;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 20/03/2022
 * Ultima alteracao: 21/03/2022
 * Nome: Framing
 * Funcao: Aplica a tecnica de enquadramento Framing que consiste
 * em adicionar uma informcao de controle que significa o tamanho
 * do quadro
 * ************************************************************** */
public class Framing extends Enquadramento{

  @Override
  public int[] enquadrar(int[] bits) {
    char[] mensSemIC = converterEmChar(bits);
    char[] mensComIC = colocarInformacaoDeControle(mensSemIC);;
    bits = converterEmBits(mensComIC);

    mensagemOriginal = new String(mensSemIC);
    mensagEnquadrada = formatQuadro(mensComIC);

    System.out.println("\nCAMADA DE ENLACE DE DADOS EMISSORA");
    System.out.print("\tOriginal: " + mensagemOriginal);
    System.out.println("\n\tEnquadra: " + mensagEnquadrada);

    return bits;
  }

  @Override
  public int[] desenquadrar(int[] bits) {
    char[] mensComIC = converterEmChar(bits);
    char[] mensSemIC = removerInformacaoDeControle(mensComIC);
    bits = converterEmBits(mensSemIC);

    mensagEnquadrada = formatQuadro(mensComIC);
    mensagemOriginal = new String(mensSemIC);

    System.out.println("\n\nCAMADA DE ENLACE DE DADOS RECEPTORA");
    System.out.print("\tEnquadra: " + mensagEnquadrada);
    System.out.print("\n\tOriginal: " + mensagemOriginal+"\n");

    return bits;
  }

  /**
   * Coloca a Informacao de cotrole no quadro
   * @param mensagem
   * @return
   */
  private char[] colocarInformacaoDeControle(char[] mensagem){
    // CRIA UM VETOR COM As IC - INFORMACAO DE CONTROLE //
    int[] ic;
    int qts = mensagem.length / (quadroSize - 1);
    int restante = mensagem.length % (quadroSize - 1);
    if(restante > 0){
      ic = new int[qts+1];
      ic[qts] = restante + 1;
    }else
      ic = new int[qts];
    for (int i = 0; i < qts; i++) {
      ic[i] = quadroSize;
    }

    // COLOCA A INFORMACAO DE CONTROLE E SEU RESPECTIVO INDICE //
    char[] novaMensagem = new char[mensagem.length+ic.length];
    int i_ic = 0;
    int i_bits = 0;
    for (int i = 0; i < novaMensagem.length; i++) {
      if(i % quadroSize == 0){
        novaMensagem[i] = Character.forDigit(ic[i_ic],10);
        i_ic++;
      }
      else{
        novaMensagem[i] = mensagem[i_bits];
        i_bits++;
      }
    }
    return novaMensagem;
  }

  /**
   * Remove a informacao de controle do quadro
   * @param mensagem
   * @return
   */
  private char[] removerInformacaoDeControle(char[] mensagem){
    // CRIA UM VETOR COM A IC - INFORMACAO DE CONTROLE //
    int qtsDeQuadros = contarQuadros(mensagem.length, quadroSize);

    // TIRA A INFORMACAO DE CONTROLE DE SEU RESPECTIVO INDICE //
    char[] novaMensagem = new char[mensagem.length-qtsDeQuadros];
    int i_bits = 0;
    for (int i = 0; i < mensagem.length; i++) {
      if(i % quadroSize != 0) {
        novaMensagem[i_bits] = mensagem[i];
        i_bits++;
      }
    }
    return novaMensagem;
  }

  /**
   * Formata a mensagem em quadros dentro de uma string
   * @param mensagem
   * @return
   */
  private String formatQuadro(char[] mensagem){
    String quadro = "";
    for (int i = 0; i < mensagem.length; i++) {
      if(i % quadroSize == 0)
        quadro+="[";
      quadro+=mensagem[i];
      if(i == mensagem.length-1 || i % quadroSize == 4)
        quadro+="]";
    }
    return quadro;
  }

  /**
   * Conta o tamanho do quadro
   * @param tam         Tamanho da mensagem
   * @param tamquadro   Tamanho do quadro
   * @return
   */
  private static int contarQuadros(int tam, int tamquadro){
    int qts = tam / tamquadro ;
    int restante = tam % tamquadro;
    if(restante > 0)
      qts+=1;
    return qts;
  }


  protected int[] converterEmBits(char[] chars){
    int[] ascii = new int[chars.length];
    String[] binaryStrings = new String[chars.length];
    int[] bits = new int[chars.length * 7];

    // Converte cada caractere em seu valor da tabela ASCII
    for (int i = 0; i < chars.length; i++)
      ascii[i] = chars[i];

    // Converte cada valor ASCII em Binario - SAO NECESSARIO 7 CASAS DECIMAIS PARA CADA CHAR
    String mensagemCodificada = "";
    for (int i = 0; i < ascii.length; i++) {
      String binaryString = Integer.toBinaryString(ascii[i]);
      binaryStrings[i] = String.format("%07d",Integer.valueOf(binaryString));
      mensagemCodificada += binaryStrings[i];
    }

    char[] bitsChar = mensagemCodificada.toCharArray();
    for (int i = 0; i < bits.length; i++) {
      bits[i] = Character.getNumericValue(bitsChar[i]);
    }
    return bits;
  }
  protected char[] converterEmChar(int[] bits){
    String mensagem = "";
    String letra = "";
    for (int i = 0; i < bits.length; i++) {
      letra += bits[i];
      if(letra.length() % 7 == 0){
        int ascii = Integer.parseInt(letra,2);
        mensagem += ((char) ascii);
        letra = "";
      }
    }
    return mensagem.toCharArray();
  }
}
