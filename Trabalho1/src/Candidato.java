import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

public class Candidato implements Comparable<Candidato> {
    private int ID;
    private int cargo;
    private String nomeUrna;
    private LocalDate nasc;
    private boolean eleito;
    private char genero; // M = Masculino, F = Feminino, I = Indefinido, N = Nao Binario
    private Integer qntVotos;
    private Partido partidao;

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

    public char getGenero() {
        return genero;
    }

    public void addQntVotos(Integer qntVotos) {
        this.qntVotos += qntVotos;
    }

    public int getQntVotos() {
        return qntVotos;
    }

    public Partido getPartidao() {
        return partidao;
    }

    public void setPartidao(Partido partidao) {
        this.partidao = partidao;
        partidao.InsereCand(this);
    }

    @Override
    public int compareTo(Candidato arg0) {
        return this.qntVotos.compareTo(arg0.qntVotos);
    }

    @Override
    public String toString() {

        Locale ptbr = Locale.forLanguageTag("pt-BR");
        NumberFormat nf = NumberFormat.getInstance(ptbr);
        if (partidao != null)
            return nomeUrna + " (" +
                    partidao.getSiglaPartido() + ", " + nf.format(qntVotos) + " votos)";
        else
            return nomeUrna + " (" + nf.format(qntVotos) + " votos)";
    }

}
