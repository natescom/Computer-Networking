package com.nathan.model.camada.enlace.enquadramento;

import com.nathan.model.util.FormatFactory;

import java.util.ArrayList;
import static com.nathan.model.util.FormatFactory.*;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 20/03/2022
 * Ultima alteracao: 21/03/2022
 * Nome: BitStufing
 * Funcao: Aplica a tecnica de enquadramento Bit Stufing que consiste
 * em adicionar um bit 0 a cada sequencia de 5 bits 1
 * ************************************************************** */
public class BitStufing extends Enquadramento {

  @Override
  public ArrayList<int[]> enquadrar(int[] bits) {
    ArrayList<int[]> quadros = colocarInformacaoDeControle(bits);
    int[] bitsComIC = juntarQuadro(quadros);

    mensagemOriginal = arrayToString(bits);
    mensagEnquadrada = formatQuadroBitStufing(bitsComIC);

    System.out.print("\tOriginal: " + mensagemOriginal);
    System.out.println("\n\tEnquadra: " + mensagEnquadrada);

    return quadros;
  }

  @Override
  public int[] desenquadrar(ArrayList<int[]> quadros) {
    int[] bits = juntarQuadro(quadros);
    int[] bitsSemIC = removerInformacaoDeControle(bits);

    mensagEnquadrada = formatQuadroBitStufing(bits);
    mensagemOriginal = arrayToString(bitsSemIC);

    System.out.print("\tEnquadra: " + mensagEnquadrada);
    System.out.print("\n\tOriginal: " + mensagemOriginal + "\n");

    return bitsSemIC;
  }

  /**
   * Coloca a Informacao de cotrole no quadro
   *
   * @param bits
   * @return
   */
  private ArrayList<int[]> colocarInformacaoDeControle(int[] bits) {
    String bitsComIC = "";
    ArrayList<int[]> mensagem = new ArrayList<>();
    int cont = 0;         // Conta os 1 consecutivos
    for (int i = 0; i < bits.length; i++) {
      bitsComIC += bits[i];
      if (bits[i] == 1)
        cont++;
      else
        cont = 0;
      if (cont == 5) {
        bitsComIC += 0;
        cont = 0;
        int[] quadro = new int[bitsComIC.length()];
        char[] bitsComICChar = bitsComIC.toCharArray();
        for (int j = 0; j < quadro.length; j++) {
          quadro[j] = Integer.parseInt(""+bitsComICChar[j], 10);
        }
        mensagem.add(quadro);
        bitsComIC = "";
      }else if(i == bits.length-1){
        int[] quadro = new int[bitsComIC.length()];
        char[] bitsComICChar = bitsComIC.toCharArray();
        for (int j = 0; j < quadro.length; j++) {
          quadro[j] = Integer.parseInt(""+bitsComICChar[j], 10);
        }
        mensagem.add(quadro);
      }

    }
    return mensagem;
  }

  /**
   * Remove a Informacao de cotrole no quadro
   *
   * @param bits
   * @return
   */
  private int[] removerInformacaoDeControle(int[] bits) {
    String bitsSemIC = "";
    int cont = 0;         // Conta os 1 consecutivos
    for (int i = 0; i < bits.length; i++) {
      if (cont == 5) {
        cont = 0;
        i++;
      }
      if (bits[i] == 1)
        cont++;
      else
        cont = 0;
      bitsSemIC += bits[i];
    }
    char[] bitsSemICArray = bitsSemIC.toCharArray();
    bits = new int[bitsSemICArray.length];
    for (int i = 0; i < bits.length; i++) {
      bits[i] = Character.getNumericValue(bitsSemICArray[i]);
    }
    return bits;
  }
}
