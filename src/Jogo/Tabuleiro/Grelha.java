/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jogo.Tabuleiro;

import Jogo.Alvo.Arma;
import Jogo.Alvo.Borda;
import Jogo.Alvo.Terra;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author romulo
 */
public class Grelha implements Serializable {

    private Campo[][] campos;
    private int dimensao;

    /**
     * Inicializa a grelha. New Terra() pode ser excesso de lixo de memória.
     * Instancia todos os campos e os acopla à grelha Inicializa todos os campos
     * com terra e os acopla
     *
     * @param dimensao corresponde ao tamanho da grelha
     */
    public Grelha(int dimensao) {
        Terra terra;
        List<Campo> camposObjeto;
        Campo campo;

        setDimensao(dimensao);
        campos = new Campo[dimensao][dimensao];
        for (int i = 0; i < dimensao; i++) {
            for (int j = 0; j < dimensao; j++) {
                terra = new Terra();
                camposObjeto = new ArrayList<>();
                campo = new Campo(new Coordenada(i, j), terra);
                campos[i][j] = campo;
                campo.setGrelha(this);

                camposObjeto.add(campo);
                terra.setCampos(camposObjeto);
            }
        }
    }

    public Campo[][] getCampos() {
        return campos;
    }

    public int getDimensao() {
        return dimensao;
    }

    private void setDimensao(int dimensao) {
        if (dimensao > 5) {
            this.dimensao = dimensao;
        }
    }

    public Campo getCampo(Coordenada coordenada) {
        if (coordenada.getX() < dimensao && coordenada.getY() < dimensao) {
            return campos[coordenada.getX()][coordenada.getY()];
        }
        return null;
    }

    private void addBorda(Campo campo) {
        if (campo != null && campo.getObjeto() instanceof Terra) {
            campo.setObjeto(new Borda());
        }
    }

    /**
     * Adiciona uma arma na grelha, estabelecendo a relação bi-direcional
     * Verifica a possibilidade de inserção na grelha Insere os campos de borda
     * ao redor da arma
     *
     * @param arma
     * @param campos devem ser passados de maneira contígua
     * @return
     */
    public boolean addArma(Arma arma, List<Campo> campos) {
        if (campos == null) {
            return false;
        }

        for (Campo campo : campos) {
            if (!(campo.getObjeto() instanceof Terra)) {
                return false;
            }
        }

        Coordenada coordenada;
        arma.setCampos(campos);
        for (Campo campo : campos) {
            campo.setObjeto(arma);

            coordenada = campo.getCoordenada().north();
            addBorda(getCampo(coordenada));

            coordenada = campo.getCoordenada().northwest();
            addBorda(getCampo(coordenada));

            coordenada = campo.getCoordenada().northeast();
            addBorda(getCampo(coordenada));

            coordenada = campo.getCoordenada().west();
            addBorda(getCampo(coordenada));

            coordenada = campo.getCoordenada().east();
            addBorda(getCampo(coordenada));

            coordenada = campo.getCoordenada().south();
            addBorda(getCampo(coordenada));

            coordenada = campo.getCoordenada().southwest();
            addBorda(getCampo(coordenada));

            coordenada = campo.getCoordenada().southeast();
            addBorda(getCampo(coordenada));
        }

        arma.setAtiva(true);

        //coleta lixo de todas as terras removidas
        System.gc();
        return true;
    }

    public void setInativo(Campo campo) {
        if (campo != null) {
            campo.setAtiravel(false);
            if (campo.getObjeto() instanceof Borda) {
                campo.getObjeto().setImagem("80_borda.png");
            }
        }
    }

    public void setBordasInativas(List<Campo> listaCampos) {
        for (Campo campo : listaCampos) {

            Coordenada coordenada;

            coordenada = campo.getCoordenada().north();
            setInativo(getCampo(coordenada));

            coordenada = campo.getCoordenada().northwest();
            setInativo(getCampo(coordenada));

            coordenada = campo.getCoordenada().northeast();
            setInativo(getCampo(coordenada));

            coordenada = campo.getCoordenada().west();
            setInativo(getCampo(coordenada));

            coordenada = campo.getCoordenada().east();
            setInativo(getCampo(coordenada));

            coordenada = campo.getCoordenada().south();
            setInativo(getCampo(coordenada));

            coordenada = campo.getCoordenada().southwest();
            setInativo(getCampo(coordenada));

            coordenada = campo.getCoordenada().southeast();
            setInativo(getCampo(coordenada));
        }

    }
}
