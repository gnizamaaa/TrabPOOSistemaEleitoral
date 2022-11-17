import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Relatorio {
    public static void estatisticasEleitos(List<Candidato> eleitos) {

        double sub30 = 0, sub40 = 0, sub50 = 0, sub60 = 0, up60 = 0;
        int fem = 0, masc = 0, nb = 0;
        for (Candidato e : eleitos) {

            char genero = e.getGenero();
            LocalDate nasc = e.getNasc();
            int idade = Period.between(nasc, LocalDate.now()).getYears();

            if (genero == 'F')
                fem++;
            else if (genero == 'M')
                masc++;
            else
                nb++;

            if (idade < 30)
                sub30++;
            else if (idade < 40)
                sub40++;
            else if (idade < 50)
                sub50++;
            else if (idade < 60)
                sub60++;
            else
                up60++;

        }

        Locale ptbr = Locale.forLanguageTag("pt-BR");
        NumberFormat format = NumberFormat.getPercentInstance(ptbr);
        NumberFormat nf = NumberFormat.getInstance(ptbr);

        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);

        System.out.println("\nEleitos, por faixa etária (na data da eleição):");
        System.out.println(
                "\t  Idade < 30: " + nf.format(sub30) + " (" + format.format((sub30 / eleitos.size())) + ")");
        System.out
                .println("30 <= Idade < 40: " + nf.format(sub40) + " (" + format.format(sub40 / eleitos.size()) + ")");
        System.out
                .println("40 <= Idade < 50: " + nf.format(sub50) + " (" + format.format(sub50 / eleitos.size()) + ")");
        System.out
                .println("50 <= Idade < 60: " + nf.format(sub60) + " (" + format.format(sub60 / eleitos.size()) + ")");
        System.out.println("60 <= Idade\t: " + nf.format(up60) + " (" + format.format(up60 / eleitos.size()) + ")");

        System.out.println("\nEleitos, por gênero:");
        System.out.println("Feminino:  " + fem + " (" + format.format(fem / (double) eleitos.size()) + ")");
        System.out.println("Masculino: " + masc + " (" + format.format(masc / (double) eleitos.size()) + ")");
        if (nb > 0)
            System.out.println("Nao binario: " + masc + " (" + format.format(masc / (double) eleitos.size()) + ")");

    }

    public static void estatisticasVotos(List<Candidato> candidatos, List<Partido> listaPartido) {

        Locale ptbr = Locale.forLanguageTag("pt-BR");
        NumberFormat format = NumberFormat.getPercentInstance(ptbr);
        NumberFormat nf = NumberFormat.getInstance(ptbr);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);

        int votosNominais = 0, votosLegenda = 0;

        for (Candidato e : candidatos)
            votosNominais += e.getQntVotos();

        for (Partido c : listaPartido)
            votosLegenda += c.getVotosPartidarios();

        System.out.println("\nTotal de votos válidos:       " + nf.format(votosLegenda + votosNominais));
        System.out.println("Total de votos nominais:      " + nf.format(votosNominais) + " ("
                + format.format(votosNominais / (double) (votosLegenda + votosNominais)) + ")");
        System.out.println("Total de votos de legenda:    " + nf.format(votosLegenda) + " ("
                + format.format(votosLegenda / (double) (votosLegenda + votosNominais)) + ")");
    }

    public static void estatisticasParts(List<Candidato> candidatos, Map<Integer, Partido> partidos) {

        HashMap<Integer, Partido> impressos = new HashMap<>();
        System.out.println("\nPrimeiro e último colocados de cada partido:");
        Integer i = 1;
        for (Candidato e : candidatos) {
            if (impressos.containsValue(e.getPartidao())) {
                if (impressos.size() == partidos.size())
                    break;
            } else {
                System.out.println(i + " - " + e.getPartidao().estatisticasCands());
                impressos.put(e.getPartidao().getNumeroUrna(), e.getPartidao());
                i++;
            }
        }
    }

    public static void RelatorioEleitos(int filtroCargo, List<Candidato> eleitos) {
        if (filtroCargo == 7)
            System.out.println("Número de vagas: " + eleitos.size() + "\n\nDeputados estaduais eleitos:");
        else if (filtroCargo == 6)
            System.out.println("Número de vagas: " + eleitos.size() + "\n\nDeputados federais eleitos:");
        Candidato.imprimeListaCand(eleitos);
    }

    public static void RelatorioMaisVotados(List<Candidato> eleicaoQntVotos) {
        System.out
                .println("\nCandidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        Candidato.imprimeListaCand(eleicaoQntVotos);
    }

    public static void RelatorioDif_MajEle(List<Candidato> eleMaj, List<Candidato> candidatos) {
        System.out
                .println(
                        "\nTeriam sido eleitos se a votação fosse majoritária, e não foram eleitos:\n(com sua posição no ranking de mais votados)");
        for (Candidato e : eleMaj) {
            System.out.println((candidatos.indexOf(e) + 1) + " - " + e);
        }
    }

    public static void RelatorioDif_EleMaj(List<Candidato> eleMaj, List<Candidato> candidatos) {
        System.out
                .println(
                        "\nEleitos, que se beneficiaram do sistema proporcional:\n(com sua posição no ranking de mais votados)");
        for (Candidato e : eleMaj) {
            System.out.println((candidatos.indexOf(e) + 1) + " - " + e);
        }
    }

    public static void RelatorioListaPart(List<Partido> listaPartido) {
        System.out.println("\nVotação dos partidos e número de candidatos eleitos:");
        Partido.imprimeListaPartido(listaPartido);
    }

}
