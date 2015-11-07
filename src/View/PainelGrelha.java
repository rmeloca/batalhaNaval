/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Jogo.Alvo.Guarani;
import Jogo.Alvo.Terra;
import Jogo.Jogador;
import Jogo.Tabuleiro.Campo;
import Jogo.Tabuleiro.Coordenada;
import Jogo.Tabuleiro.Grelha;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author romulo
 */
public class PainelGrelha extends JPanel {

    JButton[][] campos;
    ActionListener campoActionListener;
    Grelha grelha;
    PainelConfronto painelConfronto;

    public PainelGrelha(Grelha grelha) {
        this.grelha = grelha;
        int dimensao = grelha.getDimensao();
        setLayout(new GridLayout(dimensao, dimensao));
        campos = new JButton[dimensao][dimensao];

        campoActionListener = new ActionListener() {
            JButton btnCampo;
            Campo campo;

            @Override
            public void actionPerformed(ActionEvent e) {
                String[] action;
                List<Campo> camposSelecionados;
                Guarani guarani = new Guarani();
                Coordenada coordenada;

                camposSelecionados = new ArrayList<>();

                action = e.getActionCommand().split(",");
                coordenada = new Coordenada(Integer.parseInt(action[0]), Integer.parseInt(action[1]));
                campo = grelha.getCampo(coordenada);
                camposSelecionados.add(campo);

                boolean acertou = guarani.atirar(camposSelecionados) > 0;

                Jogador jogador = painelConfronto.jogo.getJogadorProximaRodada(acertou);
                btnCampo = campos[coordenada.getX()][coordenada.getY()];
                btnCampo.setEnabled(false);
                btnCampo.setBackground(Color.red);
                btnCampo.setText(campo.getObjeto().toString());
                btnCampo.setIcon(new ImageIcon(campo.getObjeto().getImagem()));

                if (painelConfronto.jogo.haVencedor()) {
                    painelConfronto.painelGrelha1.desabilitarGrelha();
                    painelConfronto.painelGrelha2.desabilitarGrelha();
                } else if (jogador.equals(painelConfronto.jogo.getEstrategia1().getJogador())) {
                    painelConfronto.painelGrelha1.atualizarGrelha();
                    painelConfronto.painelGrelha2.desabilitarGrelha();
                } else {
                    painelConfronto.painelGrelha2.atualizarGrelha();
                    painelConfronto.painelGrelha1.desabilitarGrelha();
                }
                painelConfronto.atualizarToolBar(jogador);
            }
        };

        Terra terra = new Terra();
        Campo campo;
        JButton btnCampo;
        for (int i = 0; i < grelha.getDimensao(); i++) {
            for (int j = 0; j < grelha.getDimensao(); j++) {
                btnCampo = new JButton();
                campos[i][j] = btnCampo;
                campo = grelha.getCampos()[i][j];
                btnCampo.addActionListener(campoActionListener);
                btnCampo.setActionCommand(i + "," + j);
                btnCampo.setIcon(null);
                if (campo.foiAtirado()) {
                    btnCampo.setText(campo.getObjeto().toString());
                    btnCampo.setEnabled(false);
                    btnCampo.setBackground(Color.BLUE);
                    btnCampo.setIcon(new ImageIcon(campo.getObjeto().getImagem()));
                } else {
                    btnCampo.setText(terra.toString());
                    btnCampo.setBackground(Color.GRAY);
                    btnCampo.setIcon(new ImageIcon(terra.getImagem()));
                }
                add(btnCampo);
            }
        }
    }

    public void setPainelConfronto(PainelConfronto painelConfronto) {
        this.painelConfronto = painelConfronto;
    }

    protected void atualizarGrelha() {
        Terra terra = new Terra();
        JButton btnCampo;
        Campo campo;
        for (int i = 0; i < grelha.getDimensao(); i++) {
            for (int j = 0; j < grelha.getDimensao(); j++) {
                btnCampo = campos[i][j];
                campo = grelha.getCampos()[i][j];
                if (campo.isAtiravel()) {
                    btnCampo.setEnabled(true);
                    btnCampo.setIcon(new ImageIcon(terra.getImagem()));
                } else {
                    btnCampo.setEnabled(false);
                    btnCampo.setIcon(new ImageIcon(campo.getObjeto().getImagem()));
                }
            }
        }
    }

    protected void desabilitarGrelha() {
        JButton btnCampo;
        for (int i = 0; i < grelha.getDimensao(); i++) {
            for (int j = 0; j < grelha.getDimensao(); j++) {
                btnCampo = campos[i][j];
                btnCampo.setEnabled(false);
            }
        }
    }
}
