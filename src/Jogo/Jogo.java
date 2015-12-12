/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jogo;

import Jogo.Alvo.Explosivo;
import Jogo.Tabuleiro.Grelha;
import java.io.Serializable;

/**
 *
 * @author a1137131
 */
public class Jogo implements Serializable {

    private Estrategia estrategia1;
    private Estrategia estrategia2;
    private int dimensao;
    private Jogador jogadorRodada;
    private Jogador vencedor;
    public boolean bloqueado;

    public Jogo(int dimensao, Jogador jogador1, Jogador jogador2) {
        setDimensao(dimensao);
        this.jogadorRodada = jogador1;
        estrategia1 = new Estrategia(jogador1, new Grelha(dimensao));
        estrategia2 = new Estrategia(jogador2, new Grelha(dimensao));
    }

    public Jogo(int dimensao) {
        setDimensao(dimensao);
        bloqueado = false;
    }

    public void setEstrategia2(Estrategia estrategia2) {
        this.estrategia2 = estrategia2;
    }

    public void setEstrategia1(Estrategia estrategia1) {
        this.estrategia1 = estrategia1;
    }

    public int getDimensao() {
        return dimensao;
    }

    private void setDimensao(int dimensao) {
        if (dimensao > 5) {
            this.dimensao = dimensao;
        }
    }

    public boolean haVencedor() {
        if (estrategia1.isAllArmasInativas()) {
            vencedor = estrategia1.getJogador();
        } else if (estrategia2.isAllArmasInativas()) {
            vencedor = estrategia2.getJogador();
        } else {
            vencedor = null;
            return false;
        }
        return true;
    }

    public Jogador getVencedor() {
        return vencedor;
    }

    public Estrategia getEstrategiaMinha() {
        return estrategia1;
    }

    public Estrategia getEstrategiaInimiga() {
        return estrategia2;
    }

    public Jogador getJogadorProximaRodada(boolean acertou) {
        if (jogadorRodada == null) {
            jogadorRodada = estrategia1.getJogador();
            return jogadorRodada;
        }
        if (acertou) {
            return jogadorRodada;
        }
        if (this.jogadorRodada.equals(estrategia1.getJogador())) {
            jogadorRodada = estrategia2.getJogador();
        } else {
            jogadorRodada = estrategia1.getJogador();
        }
        return jogadorRodada;
    }

    public Estrategia getEstrategia(Jogador jogador) {
        if (estrategia1.getJogador().equals(jogador)) {
            return estrategia1;
        }
        return estrategia2;
    }

    /**
     * Realiza a explosão das bombas, considerando a tradução dos campos Utiliza
     * a orientação a objetos, mas não é mais lento?
     */
    public void inicializar() {
        for (Explosivo explosivo : estrategia1.getExplosivos()) {
            explosivo.explodir(estrategia2.getGrelha().getCampo(explosivo.getCampos().get(0).getCoordenada()));
        }
        for (Explosivo explosivo : estrategia2.getExplosivos()) {
            explosivo.explodir(estrategia1.getGrelha().getCampo(explosivo.getCampos().get(0).getCoordenada()));
        }
    }
}
