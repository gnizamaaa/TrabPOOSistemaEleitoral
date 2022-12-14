package rhuangarcia.Eleicao;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Partido implements Comparable<Partido> {
    private final ArrayList<Candidato> candidatos = new ArrayList<>(); // Lista de candidatos associados ao partido
    private final int numeroUrna; // Numero de urna do partido
    private final String siglaPartido; // Sigla/Nome de Urna do Partido
    private Federacao fed; // Federacao que participa
    private int votosPartidarios; // Contador de votos partidarios

    public Partido(int numeroUrna, String siglaPartido) {
        this.numeroUrna = numeroUrna;
        this.siglaPartido = siglaPartido;
        fed = null;
    }

    public ArrayList<Candidato> getCandidatos() {
        return candidatos;
    }

    public int getNumeroUrna() {
        return numeroUrna;
    }

    public String getSiglaPartido() {
        return siglaPartido;
    }

    public Federacao getFed() {
        return fed;
    }

    public Integer getVotos() {
        Integer saida = 0;
        for (Candidato c : candidatos) {
            saida += c.getQntVotos();
        }
        return saida + votosPartidarios;
    }

    public int getVotosPartidarios() {
        return votosPartidarios;
    }

    /**
     * Adiciona [votosPartidarios] votos ao contador de votos partidarios
     * 
     * @param votosPartidarios
     */
    public void addVotosPartidarios(int votosPartidarios) {
        this.votosPartidarios += votosPartidarios;
    }

    public void setFed(Federacao fed) {
        this.fed = fed;
        fed.inserePartido(this);
    }

    /**
     * Verifica se o partido está associado a uma federação
     * 
     * @return
     */
    public Boolean isInFed() {
        return (fed != null);
    }

    /**
     * Insere o candidato na lista de candidatos do partido mantendo o link
     * (Candidato possui uma referencia para o Partido que participa)
     * 
     * @param novo
     */
    public void InsereCand(Candidato novo) {
        if (!candidatos.contains(novo)) {
            candidatos.add(novo);
            novo.setPartidao(this);
        }
    }

    @Override
    public int compareTo(Partido arg0) {

        if (arg0.getVotos().compareTo(this.getVotos()) != 0)
            return arg0.getVotos().compareTo(this.getVotos());
        else
            return (this.getNumeroUrna() - arg0.getNumeroUrna());
    }

    @Override
    public String toString() {
        int votosNominais = 0;
        int qntEleitos = 0;

        for (Candidato cand : candidatos) {

            votosNominais += cand.getQntVotos();
            if (cand.isEleito())
                qntEleitos++;
        }

        Locale ptbr = Locale.forLanguageTag("pt-BR");
        NumberFormat nf = NumberFormat.getInstance(ptbr);
        if ((votosNominais + votosPartidarios) > 1)
            if (votosNominais > 1)
                if (qntEleitos > 1)
                    return siglaPartido + " - " + numeroUrna + ", " + nf.format(votosNominais + votosPartidarios) +
                            " votos (" + nf.format(votosNominais) + " nominais e " + nf.format(votosPartidarios)
                            + " de legenda), "
                            + nf.format(qntEleitos) + " candidatos eleitos";
                else
                    return siglaPartido + " - " + numeroUrna + ", " + nf.format(votosNominais + votosPartidarios) +
                            " votos (" + nf.format(votosNominais) + " nominais e " + nf.format(votosPartidarios)
                            + " de legenda), "
                            + nf.format(qntEleitos) + " candidato eleito";
            else {
                if (qntEleitos > 1)
                    return siglaPartido + " - " + numeroUrna + ", " + nf.format(votosNominais + votosPartidarios) +
                            " votod (" + nf.format(votosNominais) + " nominal e " + nf.format(votosPartidarios)
                            + " de legenda), "
                            + nf.format(qntEleitos) + " candidatos eleitos";
                else
                    return siglaPartido + " - " + numeroUrna + ", " + nf.format(votosNominais + votosPartidarios) +
                            " votos (" + nf.format(votosNominais) + " nominal e " + nf.format(votosPartidarios)
                            + " de legenda), "
                            + nf.format(qntEleitos) + " candidato eleito";
            }
        else {
            if (qntEleitos > 1)
                return siglaPartido + " - " + numeroUrna + ", " + nf.format(votosNominais + votosPartidarios) +
                        " voto (" + nf.format(votosNominais) + " nominal e " + nf.format(votosPartidarios)
                        + " de legenda), "
                        + nf.format(qntEleitos) + " candidatos eleitos";
            else
                return siglaPartido + " - " + numeroUrna + ", " + nf.format(votosNominais + votosPartidarios) +
                        " voto (" + nf.format(votosNominais) + " nominal e " + nf.format(votosPartidarios)
                        + " de legenda), "
                        + nf.format(qntEleitos) + " candidato eleito";
        }
    }

    /**
     * Retorna uma String para impressão das estatisticas do Partido sobre seus
     * candidatos (mais votado, menos votado e votos deles)
     * 
     * @return
     */
    public String estatisticasCands() {
        Collections.sort(candidatos);
        Candidato primeiro = candidatos.get(0);
        Candidato ultimo = candidatos.get(candidatos.size() - 1);

        Locale ptbr = Locale.forLanguageTag("pt-BR");
        NumberFormat nf = NumberFormat.getInstance(ptbr);

        String vot1, vot2;
        if (primeiro.getQntVotos() > 1)
            vot1 = "votos";
        else
            vot1 = "voto";

        if (ultimo.getQntVotos() > 1)
            vot2 = "votos";
        else
            vot2 = "voto";

        if (candidatos.size() > 0) {
            return siglaPartido + " - " + numeroUrna + ", " +
                    primeiro.getNomeUrna() + " (" + primeiro.getID() + ", " + nf.format(primeiro.getQntVotos())
                    + " " + vot1 + ") / " +
                    ultimo.getNomeUrna() + " (" + ultimo.getID() + ", " + nf.format(ultimo.getQntVotos()) + " " + vot2
                    + ")";
        } else
            return "";
    }

    /**
     * Faz a impressao de uma lista de partidos com a posicao a esquerda
     * 
     * @param listaPartido
     */
    public static void imprimeListaPartido(List<Partido> listaPartido) {
        Integer i = 1;
        for (Partido e : listaPartido) {
            System.out.println(i + " - " + e);
            i++;
        }
    }
}
