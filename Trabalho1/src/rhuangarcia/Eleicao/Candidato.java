package rhuangarcia.Eleicao;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class Candidato implements Comparable<Candidato> {
    private final int ID; // Numero de urna
    private final int cargo; // Deputado federal ou estadual
    private final String nomeUrna; // Nome de urna do candidato
    private final LocalDate nasc; // Nascimento do candidato
    private boolean eleito;
    private final char genero; // M = Masculino, F = Feminino, I = Indefinido, N = Nao Binario
    private Integer qntVotos; // Contador de votos
    private Partido partidao; // Partido que o canditado faz parte

    public Candidato(int iD, int cargo, String nomeUrna, LocalDate nasc, boolean eleito, char genero, int qntVotos) {
        ID = iD;
        this.cargo = cargo;
        this.nomeUrna = nomeUrna;
        this.nasc = nasc;
        this.eleito = eleito;
        this.genero = genero;
        this.qntVotos = qntVotos;
    }

    public int getID() {
        return ID;
    }

    public int getCargo() {
        return cargo;
    }

    public String getNomeUrna() {
        return nomeUrna;
    }

    public LocalDate getNasc() {
        return nasc;
    }

    public boolean isEleito() {
        return eleito;
    }

    public void setEleito(boolean eleito) {
        this.eleito = eleito;
    }

    public char getGenero() {
        return genero;
    }

    /**
     * Adiciona [qntVotos] votos ao contador de votos
     * 
     * @param qntVotos - quantidade de votos a serem adicionados
     */
    public void addQntVotos(Integer qntVotos) {
        this.qntVotos += qntVotos;
    }

    public int getQntVotos() {
        return qntVotos;
    }

    public Partido getPartidao() {
        return partidao;
    }

    // Faz o set mantendo o link com o partido (que possui uma lista de candidatos)
    public void setPartidao(Partido partidao) {
        this.partidao = partidao;
        partidao.InsereCand(this);
    }

    @Override
    public int compareTo(Candidato arg0) {
        if (arg0.qntVotos.compareTo(qntVotos) != 0)
            return arg0.qntVotos.compareTo(qntVotos);
        else {
            return this.nasc.compareTo(arg0.nasc);
        }
    }

    @Override
    public String toString() {

        Locale ptbr = Locale.forLanguageTag("pt-BR");
        NumberFormat nf = NumberFormat.getInstance(ptbr);
        if (partidao != null) {
            if (partidao.getFed() != null) {
                return '*' + nomeUrna + " (" +
                        partidao.getSiglaPartido() + ", " + nf.format(qntVotos) + " votos)";
            } else
                return nomeUrna + " (" +
                        partidao.getSiglaPartido() + ", " + nf.format(qntVotos) + " votos)";
        } else
            return nomeUrna + " (" + nf.format(qntVotos) + " votos)";
    }

    /**
     * Faz a impressao de uma lista de candidatos com a posicao na lista a esquerda
     * 
     * @param candidatos - Lista a ser impressa
     */
    public static void imprimeListaCand(List<Candidato> candidatos) {
        Integer i = 1;
        for (Candidato e : candidatos) {
            System.out.println(i + " - " + e);
            i++;
        }
    }

}
