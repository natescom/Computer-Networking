package com.nathan;

import java.io.Serializable;

public class Transacao implements Serializable {
    private final int tipo;
    private final String nome;
    private final double valor;
    private final String dataHora;

    public Transacao(int tipo, String nome, double valor, String dataHora) {
        this.tipo = tipo;
        this.nome = nome;
        this.valor = valor;
        this.dataHora = dataHora;
    }

    public String getNome() {
        return nome;
    }

    public int getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public String getDataHora() {
        return dataHora;
    }
    
    
}
