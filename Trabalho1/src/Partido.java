import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Partido implements Comparable<Partido> {
    private ArrayList<Candidato> candidatos = new ArrayList<Candidato>();
    private int numeroUrna;
    private String siglaPartido;
    private Federacao fed;
    private int votosPartidarios;

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

    public void addVotosPartidarios(int votosPartidarios) {
        this.votosPartidarios += votosPartidarios;
    }

    public void setFed(Federacao fed) {
        this.fed = fed;
        fed.inserePartido(this);
    }

    public Boolean isInFed() {
        return (fed != null);
    }

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
        if (qntEleitos > 1)
            return siglaPartido + " - " + numeroUrna + ", " + nf.format(votosNominais + votosPartidarios) +
                    " votos (" + nf.format(votosNominais) + " e " + nf.format(votosPartidarios) + "), "
                    + nf.format(qntEleitos) + " candidatos eleitos";
        else
            return siglaPartido + " - " + numeroUrna + ", " + nf.format(votosNominais + votosPartidarios) +
                    " votos (" + nf.format(votosNominais) + " nominais e " + nf.format(votosPartidarios)
                    + " de legenda), "
                    + nf.format(qntEleitos) + " candidato eleito";
    }

    public String estatisticasCands() {
        Collections.sort(candidatos);
        Candidato primeiro = candidatos.get(0);
        Candidato ultimo = candidatos.get(candidatos.size() - 1);

        Locale ptbr = Locale.forLanguageTag("pt-BR");
        NumberFormat nf = NumberFormat.getInstance(ptbr);

        if (candidatos.size() > 0)
            return siglaPartido + " - " + numeroUrna + ", " +
                    primeiro.getNomeUrna() + " (" + primeiro.getID() + ", " + nf.format(primeiro.getQntVotos())
                    + " votos) / " +
                    ultimo.getNomeUrna() + " (" + ultimo.getID() + ", " + nf.format(ultimo.getQntVotos()) + " votos)";
        else
            return "";
    }

    public static void imprimeListaPartido(List<Partido> listaPartido) {
        Integer i = 1;
        for (Partido e : listaPartido) {
            System.out.println(i + " - " + e);
            i++;
        }
    }
}
