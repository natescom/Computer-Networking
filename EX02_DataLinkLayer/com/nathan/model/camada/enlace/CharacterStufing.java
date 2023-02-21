package com.nathan.model.camada.enlace;

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
  public int[] enquadrar(int[] bits) {
    char[] mensSemIC = converterEmChar(bits);
    char[] mensComIC = colocarInformacaoDeControle(mensSemIC);
    bits = converterEmBits(mensComIC);

    mensagemOriginal = new String(mensSemIC);
    mensagEnquadrada = new String(mensComIC);

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

    mensagEnquadrada = new String(mensComIC);
    mensagemOriginal = new String(mensSemIC);

    System.out.println("\n\nCAMADA DE ENLACE DE DADOS RECEPTORA");
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
  private char[] colocarInformacaoDeControle(char[] mensagem) {
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
      }
    }
    return mensagemNova.toCharArray();
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

  protected int[] converterEmBits(char[] chars) {
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
      binaryStrings[i] = String.format("%07d", Integer.valueOf(binaryString));
      mensagemCodificada += binaryStrings[i];
    }

    char[] bitsChar = mensagemCodificada.toCharArray();
    for (int i = 0; i < bits.length; i++) {
      bits[i] = Character.getNumericValue(bitsChar[i]);
    }
    return bits;
  }

  protected char[] converterEmChar(int[] bits) {
    String mensagem = "";
    String letra = "";
    for (int i = 0; i < bits.length; i++) {
      letra += bits[i];
      if (letra.length() % 7 == 0) {
        int ascii = Integer.parseInt(letra, 2);
        mensagem += ((char) ascii);
        letra = "";
      }
    }
    return mensagem.toCharArray();
  }
}
