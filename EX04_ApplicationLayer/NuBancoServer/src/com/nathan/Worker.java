package com.nathan;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class Worker extends Thread {

  private Socket s;
  private Conta conta;

  public Worker(Socket s) {
    this.s = s;
  }

  public void cadastrarConta(String numero, String senha) {
    BancoServer.getContas().add(new Conta(numero, senha));
  }
  public Conta getContaByName(String numero) {
    for (Conta conta1 : BancoServer.getContas()) {
      if (conta1.getNumero().equals(numero))
        return conta1;
    }
    return null;
  }
  public Conta getConta(String numero, String senha) {
    for (Conta conta : BancoServer.getContas()) {
      if (conta.getNumero().equals(numero) && conta.getSenha().equals(senha))
        return conta;
    }
    return null;
  }
  public void atualizarDados(Conta conta){
    for (int i = 0; i < BancoServer.getContas().size(); i++) {
      if (BancoServer.getContas().get(i).getNumero().equals(conta.getNumero()) &&
          BancoServer.getContas().get(i).getSenha().equals(conta.getSenha())){
          BancoServer.getContas().get(i).setSaldo(conta.getSaldo());
          BancoServer.getContas().get(i).setExtrato(conta.getExtrato());
      }
    }
    System.out.println("Dados atualizados");
  }

  public void run() {
    try {
      Scanner clientRecieve = new Scanner(s.getInputStream());
      PrintWriter clienteSend = new PrintWriter(s.getOutputStream(), true);
      int cod;

      do {
        cod = Integer.parseInt(clientRecieve.nextLine());
        System.out.println("CODIGO ATUAL: " + cod);
        switch (cod) {
          case 1: // LOGIN
            String login = clientRecieve.nextLine();
            String senha = clientRecieve.nextLine();
            System.out.println("\tLogin: " + login);
            System.out.println("\tSenha: " + senha);
            conta = getConta(login,senha);
            // TESTANDO //
            if (conta!=null) {
              clienteSend.println("Conta logada");
              clienteSend.println(2);
              System.out.println("Servidor enviou: Conta logada");
            } else {
              clienteSend.println("Conta nao existente");
              clienteSend.println(1);
              System.out.println("Servidor enviou: Conta nao existente");
            }
            break;
          case 2: // SAQUE
            double saque = Double.parseDouble(clientRecieve.nextLine());
            saque = conta.sacar(saque);
            clienteSend.println("Saque efetuado no valor de " + saque);
            clienteSend.println(2);
            break;
          case 3: // DEPOSITO
            double deposito = Double.parseDouble(clientRecieve.nextLine());
            conta.depositar(deposito);
            clienteSend.println("Depósito efetuado");
            clienteSend.println(2);
            System.out.println("Servidor enviou: Depósito efetuado");
            System.out.println("Servidor enviou: 2");
            break;
          case 4: // CONSULTA
            clienteSend.println(conta.consultar());
            clienteSend.println("Consulta efetuada");
            clienteSend.println(2);
            break;
          case 5: // TRANSFERENCIA
            String contaPraReceber = clientRecieve.nextLine();
            double trans = Double.parseDouble(clientRecieve.nextLine());
            Conta recebedora = getContaByName(contaPraReceber);
            if (recebedora != null) {
              conta.transferir(recebedora, trans);
              clienteSend.println("Transferencia efetuada");
              clienteSend.println(2);
            }else{
              clienteSend.println("Conta nao encontrada");
              clienteSend.println(0);
            }
            break;
          case 6: // EXTRATO
            clienteSend.println(conta.getExtrato().size());
            clienteSend.print(conta.extratoToString());
            clienteSend.println("Extrado pego");
            clienteSend.println(2);
            break;
          case 7: // CADASTRAR
            String login2 = clientRecieve.nextLine();
            String senha2 = clientRecieve.nextLine();
            cadastrarConta(login2, senha2);
            clienteSend.println("Cadastro Efetuado");
            clienteSend.println(2);
            break;
          default:
            cod = 0;
            clienteSend.println("Saindo...");
            clienteSend.println(0);
        }
        if(cod!=7 && cod!=1)
          atualizarDados(conta);
        BancoServer.serializeDataOut(BancoServer.getContas());
      } while (cod != 0);

      s.close();
      clienteSend.println("Operação concluida");
      System.out.println("PROCESSO ENCERRADO");
    } catch (Exception e) {
    }
  }

}
