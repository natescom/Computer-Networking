package com.nathan;

import java.io.Serializable;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class Conta implements Serializable {
  private final String numero;
  private final String senha;
  private double saldo;
  private ArrayList<Transacao> extrato;

  public Conta(String numero, String senha) {
    this.numero = numero;
    this.senha = senha;
    extrato = new ArrayList<>();
  }

  public String getNumero() {
    return numero;
  }

  public String getSenha() {
    return senha;
  }

  public ArrayList<Transacao> getExtrato(){
    return extrato;
  }

  public void setExtrato(ArrayList<Transacao> extrato) {
    this.extrato = extrato;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

  public double getSaldo() {
    return saldo;
  }


  public void depositar(double deposito) {
    saldo += deposito;
    extrato.add(new Transacao(1, "Depósito", deposito, getTimeNow()));
  }

  // todo: ADICIONAR EXCEPTION
  public double sacar(double saque) {
    if (saque > saldo) {
      return 0;
    } else {
      saldo -= saque;
      extrato.add(new Transacao(2, "Saque", saque, getTimeNow()));
      return saque;
    }
  }

  public String consultar() {
    return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(saldo);
  }

  // todo: ADICIONAR EXCEPTION
  public void transferir(Conta conta, double valor) {
//    conta.depositar(sacar(valor));
    if (valor <= saldo) {
      saldo -= valor;
      conta.receber(valor,numero);
    }
    extrato.add(new Transacao(3, "Enviou para "+conta.getNumero(), valor, getTimeNow()));
  }

  public void receber(double valor, String de){
    saldo+=valor;
    extrato.add(new Transacao(3, "Recebeu de "+de, valor, getTimeNow()));
  }


  public String extratoToString() {
    String s = "";
    for (int i = extrato.size()-1; i >= 0 ; i--) {
      s += String.format("%d\n%s\n%s\n%s\n",
          extrato.get(i).getTipo(),
          extrato.get(i).getNome(),
          extrato.get(i).getDataHora(),
          NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(extrato.get(i).getValor()));
    }
    return s;
  }

  private String getTimeNow() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    return dtf.format(now);
  }
}
