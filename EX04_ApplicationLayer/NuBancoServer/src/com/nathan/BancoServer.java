package com.nathan;

import java.io.*;
import java.net.*;
import java.util.*;

public class BancoServer {

  private static ArrayList<Conta> contas;

  public static void main(String args[]) throws IOException {
    contas = serializeDataIn();

    ServerSocket servidor = new ServerSocket(60000);
    while (true) {
      System.out.println("Servidor rodando...");
      Socket s = servidor.accept();
      Worker w = new Worker(s);
      w.start();
    }
  }

  public static ArrayList<Conta> getContas() {
    return contas;
  }

  public static void serializeDataOut(ArrayList<Conta> contas) throws IOException {
    String fileName = "src/com/nathan/Test.txt";
    FileOutputStream fos = new FileOutputStream(fileName);
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(contas);
    oos.close();
    System.out.println("Dados Salvos");
  }

  public static ArrayList<Conta> serializeDataIn() {
    String fileName = "src/com/nathan/Test.txt";
    FileInputStream fin = null;
    ArrayList<Conta> contas;
    try {
      fin = new FileInputStream(fileName);
      ObjectInputStream ois = new ObjectInputStream(fin);
      contas = (ArrayList<Conta>) ois.readObject();
      ois.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      contas = new ArrayList<>();
    }
    return contas;
  }
}

