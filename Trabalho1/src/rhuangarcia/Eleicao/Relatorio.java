package rhuangarcia.Eleicao;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Relatorio {

    /**
     * Saida do relatorio sobre estatisticas,dados sobre eleitos (idade, genero)
     * 
     * @param eleitos - Lista de candidatos eleitos ordenada em ordem crescente
     */
    public static void estatisticasEleitos(List<Candidato> eleitos, LocalDate datEle) {

        double sub30 = 0, sub40 = 0, sub50 = 0, sub60 = 0, up60 = 0;
        int fem = 0, masc = 0, nb = 0;
        for (Candidato e : eleitos) {

            char genero = e.getGenero();
            LocalDate nasc = e.getNasc();
            int idade = Period.between(nasc, datEle).getYears();

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
                "      Idade < 30: " + nf.format(sub30) + " (" + format.format((sub30 / eleitos.size())) + ")");
        System.out
                .println("30 <= Idade < 40: " + nf.format(sub40) + " (" + format.format(sub40 / eleitos.size()) + ")");
        System.out
                .println("40 <= Idade < 50: " + nf.format(sub50) + " (" + format.format(sub50 / eleitos.size()) + ")");
        System.out
                .println("50 <= Idade < 60: " + nf.format(sub60) + " (" + format.format(sub60 / eleitos.size()) + ")");
        System.out.println("60 <= Idade     : " + nf.format(up60) + " (" + format.format(up60 / eleitos.size()) + ")");

        System.out.println("\nEleitos, por gênero:");
        System.out.println("Feminino:  " + fem + " (" + format.format(fem / (double) eleitos.size()) + ")");
        System.out.println("Masculino: " + masc + " (" + format.format(masc / (double) eleitos.size()) + ")");
        if (nb > 0)
            System.out.println("Nao binario: " + masc + " (" + format.format(masc / (double) eleitos.size()) + ")");

    }

    /**
     * Saida do relatorio sobre os votos (validos, nominais e legenda)
     * 
     * @param candidatos   - Lista de Candidatos ordenada em ordem crescente
     * @param listaPartido - Lista de Partidos
     */
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

        System.out.println("\nTotal de votos válidos:    " + nf.format(votosLegenda + votosNominais));
        System.out.println("Total de votos nominais:   " + nf.format(votosNominais) + " ("
                + format.format(votosNominais / (double) (votosLegenda + votosNominais)) + ")");
        System.out.println("Total de votos de legenda: " + nf.format(votosLegenda) + " ("
                + format.format(votosLegenda / (double) (votosLegenda + votosNominais)) + ")");
    }

    /**
     * Saida do relatorio sobre os partidos(Estatisticas sobre os candidatos do
     * partido)
     * 
     * @param candidatos - Lista de candidatos ordenada em ordem crescente
     * @param partidos   - Mapa(tabela) de Numero de partido - Partido
     */
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

    /**
     * Saida do relatorio sobre os Eleitos (Cargo e dados do candidato)
     * 
     * @param filtroCargo - Cargo sendo concorrido
     * @param eleitos     - Lista de eleitos ordenada em ordem crescente
     */
    public static void RelatorioEleitos(int filtroCargo, List<Candidato> eleitos) {
        if (filtroCargo == 7)
            System.out.println("Número de vagas: " + eleitos.size() + "\n\nDeputados estaduais eleitos:");
        else if (filtroCargo == 6)
            System.out.println("Número de vagas: " + eleitos.size() + "\n\nDeputados federais eleitos:");
        Candidato.imprimeListaCand(eleitos);
    }

    /**
     * Saida do relatorio sobre os mais votados (Cargo e dados do candidato)
     * 
     * @param eleicaoQntVotos - Lista dos candidatos mais votados em ordem crescente
     *                        (de
     *                        tamanho igual a quantidade de vagas)
     */
    public static void RelatorioMaisVotados(List<Candidato> eleicaoQntVotos) {
        System.out
                .println("\nCandidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        Candidato.imprimeListaCand(eleicaoQntVotos);
    }

    /**
     * Saida do relatorio sobre a diferenca entre os mais votados e os eleitos (Os
     * que receberam maior quantidade de votos mas nao foram eleitos)
     * 
     * @param eleMaj     - Lista de candidatos que seriam eleitos caso fosse uma
     *                   eleicao majoritaria
     * @param candidatos - Lista de todos os candidatos ordenada em ordem crescente
     */
    public static void RelatorioDif_MajEle(List<Candidato> eleMaj, List<Candidato> candidatos) {
        System.out
                .println(
                        "\nTeriam sido eleitos se a votação fosse majoritária, e não foram eleitos:\n(com sua posição no ranking de mais votados)");
        for (Candidato e : eleMaj) {
            System.out.println((candidatos.indexOf(e) + 1) + " - " + e);
        }
    }

    /**
     * Saida do relatorio sobre a diferenca entre os eleitos e os mais votados(Os
     * que receberam menor quantidade de votos mas foram eleitos)
     * 
     * @param eleMaj     - Lista de candidatos que nao seriam eleitos caso fosse uma
     *                   eleicao majoritaria
     * @param candidatos - Lista de todos os candidatos ordenada em ordem crescente
     */
    public static void RelatorioDif_EleMaj(List<Candidato> eleMaj, List<Candidato> candidatos) {
        System.out
                .println(
                        "\nEleitos, que se beneficiaram do sistema proporcional:\n(com sua posição no ranking de mais votados)");
        for (Candidato e : eleMaj) {
            System.out.println((candidatos.indexOf(e) + 1) + " - " + e);
        }
    }

    /**
     * Saida do relatorio sobre os partidos (quantidade de votos e candidatos
     * eleitos)
     * 
     * @param listaPartido - Lista de partidos ordenada em ordem crescente
     */
    public static void RelatorioListaPart(List<Partido> listaPartido) {
        System.out.println("\nVotação dos partidos e número de candidatos eleitos:");
        Partido.imprimeListaPartido(listaPartido);
    }

}
