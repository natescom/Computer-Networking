package com.nathan.model.camada.enlace.enquadramento;

import java.util.ArrayList;

import static com.nathan.model.util.FormatFactory.*;

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
  public ArrayList<int[]> enquadrar(int[] bits) {
    mensagEnquadrada="";
    char[] mensSemIC = converterEmChar(bits);
    ArrayList<char[]> mensComICCharArray = colocarInformacaoDeControle(mensSemIC);
    ArrayList<int[]> bitsArray = new ArrayList<>();
    for (int i = 0; i < mensComICCharArray.size(); i++)  bitsArray.add(converterEmBits(mensComICCharArray.get(i)));
    for (char[] chars : mensComICCharArray) {
      String quadro = new String(chars);
      mensagEnquadrada += String.format("[%s] ",quadro);
    }

    mensagemOriginal = new String(mensSemIC);

    System.out.println("\tOriginal: " + mensagemOriginal);
    System.out.println("\tEnquadra: " + mensagEnquadrada);

    return bitsArray;
  }

  @Override
  public int[] desenquadrar(ArrayList<int[]> quadros) {
    int[] bits = juntarQuadro(quadros);
    char[] mensComIC = converterEmChar(bits);
    char[] mensSemIC = removerInformacaoDeControle(mensComIC);
    bits = converterEmBits(mensSemIC);

    mensagEnquadrada = formatQuadro(mensComIC);
    mensagemOriginal = new String(mensSemIC);

    System.out.print("\tEnquadra: " + mensagEnquadrada);
    System.out.print("\n\tOriginal: " + mensagemOriginal+"\n");

    return bits;
  }

  /**
   * Coloca a Informacao de cotrole no quadro
   * @param mensagem
   * @return
   */
  private ArrayList<char[]> colocarInformacaoDeControle(char[] mensagem){
    ArrayList<char[]> quadros = new ArrayList<>();
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
    String novaMen = "";
    char[] novaMensagem = new char[mensagem.length+ic.length];
    int i_ic = 0;
    int i_bits = 0;
    for (int i = 0; i < novaMensagem.length; i++) {
      if(i % quadroSize == 0){
        novaMensagem[i] = Character.forDigit(ic[i_ic],10);
        novaMen += novaMensagem[i];
        i_ic++;
      }else{
        novaMensagem[i] = mensagem[i_bits];
        novaMen += novaMensagem[i];
        i_bits++;
        if(i % quadroSize == quadroSize-1 || i == novaMensagem.length-1){
          quadros.add(novaMen.toCharArray());
          novaMen = "";
        }
      }
    }
    return quadros;
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


}
