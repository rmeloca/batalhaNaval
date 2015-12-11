/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jogo;

import Jogo.Alvo.Arma;
import Jogo.Alvo.Astros2020;
import Jogo.Alvo.CarroCombate;
import Jogo.Alvo.Explosivo;
import Jogo.Tabuleiro.Campo;
import Jogo.Tabuleiro.Coordenada;
import Jogo.Tabuleiro.Grelha;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author romulo
 */
public class Estrategia implements Serializable {

    private Jogador jogador;
    private Grelha grelha;
    private List<Arma> armas;

    public Estrategia(Jogador jogador, Grelha grelha) {
        setJogador(jogador);
        this.grelha = grelha;
        this.armas = new ArrayList<>();
    }
    
    public Estrategia(Jogador jogador) {
        setJogador(jogador);
        this.grelha = new Grelha(10);
        this.armas = new ArrayList<>();
    }

    public int calcTamanhoArmas() {
        int tamanho = 0;
        for (Arma a : armas) {
            tamanho += a.getTamanho();
        }
        return tamanho;
    }

    public Grelha getGrelha() {
        return grelha;
    }

    public List<Arma> getArmas() {
        return armas;
    }

    public List<CarroCombate> getCarrosCombate() {
        List<CarroCombate> carroCombates = new ArrayList<>();
        for (Arma arma : armas) {
            if (arma instanceof CarroCombate) {
                carroCombates.add((CarroCombate) arma);
            }
        }
        return carroCombates;
    }

    public List<Explosivo> getExplosivos() {
        List<Explosivo> explosivos = new ArrayList<>();
        for (Arma arma : armas) {
            if (arma instanceof Explosivo) {
                explosivos.add((Explosivo) arma);
            }
        }
        return explosivos;
    }

    public boolean isAllArmasInativas() {
        for (Arma arma : armas) {
            if (arma instanceof CarroCombate && arma.isAtiva()) {
                return false;
            }
        }
        return true;
    }

    public void addArma(Arma arma) {
        this.armas.add(arma);
    }

    public Jogador getJogador() {
        return jogador;
    }

    private void setJogador(Jogador jogador) {
        if (jogador != null) {
            this.jogador = jogador;
        }
    }

    /**
     * Sorteia posições e solicita a inserção na grelha Ao encontrar uma mina,
     * não adiciona-se à grelha uma vez que adicioná-la-á na grelha inimiga
     */
    public void dispoeArmas() {
        List<Campo> campos;
        boolean deployed;

        for (Arma arma : armas) {
            deployed = false;
            while (!deployed) {
                campos = calcularPosicao(arma);
                if (arma instanceof Explosivo) {
                    //associação unidirecional
                    //redundância controlada
                    //ausência de bordas
                    arma.setCampos(campos);
                    deployed = true;
                } else {
                    deployed = grelha.addArma(arma, campos);
                }
            }
        }
    }

    private List<Campo> calcularPosicao(Arma arma) {
        List<Campo> campos;
        Campo campo, base;
        Coordenada coordenada;
        Random random;

        campos = new ArrayList<>();
        random = new Random();

        coordenada = new Coordenada(random.nextInt(grelha.getDimensao()), random.nextInt(grelha.getDimensao()));
        base = grelha.getCampo(coordenada);
        campo = base;
        //instrução não é perigosa, devivo aos limites de random [0, dimensao[
        campos.add(campo);

        if (arma instanceof Astros2020) {
            campo = grelha.getCampo(base.getCoordenada().south());
            if (campo == null || campo.getCoordenada().equals(base.getCoordenada())) {
                return null;
            }
            campos.add(campo);

            campo = grelha.getCampo(base.getCoordenada().east());
            if (campo == null || campo.getCoordenada().equals(base.getCoordenada())) {
                return null;
            }
            campos.add(campo);

            //se tem south e east, então também possui southeast
            campo = grelha.getCampo(base.getCoordenada().southeast());
            campos.add(campo);
        } else {
            for (int i = 1; i < arma.getTamanho(); i++) {
                campo = grelha.getCampo(campo.getCoordenada().east());
                if (campo == null) {
                    return null;
                }
                campos.add(campo);
                campo = campos.get(i);
            }
        }
        return campos;
    }
}
