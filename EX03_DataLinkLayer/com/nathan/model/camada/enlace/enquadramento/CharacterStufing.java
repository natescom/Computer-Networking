package com.nathan.model.camada.enlace.enquadramento;

import java.util.ArrayList;

import static com.nathan.model.util.FormatFactory.*;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 20/03/2022
 * Ultima alteracao: 21/03/2022
 * Nome: CharacterStufing
 * Funcao: Aplica a tecnica de enquadramento Character Stufing, ou
 * Insercao de Caracter (Insercao de Byte),  que consiste em
 * delimitar o tamanho do quadro atraves de um Caracter Escape, um
 * Caracter para delimitar o inicio e fim da mensagem
 * ************************************************************** */
public class CharacterStufing extends Enquadramento {
  char DLE = '\\'; // DLE = DATA LINK SCAPE
  char STX = 'S';  // STX = START OF TEXT
  char ETX = 'E';  // ETX = END OF TEXT

  @Override
  public ArrayList<int[]> enquadrar(int[] bits) {
    char[] mensSemIC = converterEmChar(bits);
    ArrayList<char[]> mensComICCharArray = colocarInformacaoDeControle(mensSemIC);
    ArrayList<int[]> bitsArray = new ArrayList<>();
    mensagEnquadrada = "";
    for (int i = 0; i < mensComICCharArray.size(); i++) bitsArray.add(converterEmBits(mensComICCharArray.get(i)));
    for (char[] chars : mensComICCharArray) {
      String quadro = new String(chars);
      mensagEnquadrada += String.format("%-10s [%s]\n\t\t\t  ",quadro,arrayToString(converterEmBits(chars)));
    }

    mensagemOriginal = new String(mensSemIC);

    System.out.println("\tOriginal: " + mensagemOriginal);
    System.out.print("\tEnquadra: " + mensagEnquadrada);

    return bitsArray;
  }

  @Override
  public int[] desenquadrar(ArrayList<int[]> quadros) {
    int[] bits = juntarQuadro(quadros);
    char[] mensComIC = converterEmChar(bits);
    char[] mensSemIC = removerInformacaoDeControle(mensComIC);
    bits = converterEmBits(mensSemIC);

    mensagEnquadrada = new String(mensComIC);
    mensagemOriginal = new String(mensSemIC);

    System.out.print("\tEnquadra: " + mensagEnquadrada);
    System.out.print("\n\tOriginal: " + mensagemOriginal + "\n");

    return bits;
  }

  /**
   * Coloca a Informacao de cotrole no quadro
   *
   * @param mensagem
   * @return
   */
  private ArrayList<char[]> colocarInformacaoDeControle(char[] mensagem) {
    ArrayList<char[]> quadros = new ArrayList<>();
    String mensagemNova = "";
    for (int i = 0; i < mensagem.length; i++) {
      if (i % quadroSize == 0) {
        mensagemNova += DLE;
        mensagemNova += STX;
      }
      mensagemNova += mensagem[i];
      if (i % quadroSize == quadroSize - 1 || i == mensagem.length - 1) {
        mensagemNova += DLE;
        mensagemNova += ETX;
        quadros.add(mensagemNova.toCharArray());
        mensagemNova = "";
      }
    }
    return quadros;
  }

  /**
   * Remove a Informacao de cotrole no quadro
   *
   * @param mensagem
   * @return
   */
  private char[] removerInformacaoDeControle(char[] mensagem) {
    String mensagemNova = "";
    for (int i = 0; i < mensagem.length; i++) {
      if (mensagem[i] == DLE && mensagem[i + 1] == STX)
        i += 2;
      mensagemNova += mensagem[i];
      if (mensagem[i + 1] == DLE && mensagem[i + 2] == ETX)
        i += 2;
    }
    return mensagemNova.toCharArray();
  }

}