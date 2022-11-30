package rhuangarcia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import rhuangarcia.Eleicao.Candidato;
import rhuangarcia.Eleicao.Federacao;
import rhuangarcia.Eleicao.Leitora;
import rhuangarcia.Eleicao.Partido;
import rhuangarcia.Eleicao.Relatorio;

public class App {
    /**
     * Retorna uma lista com a diferença entre a lista de candidatos e1 e e2
     * Os elementos da lista retornada serão os presentes em e1 que não estão
     * presentes em e2
     * 
     * @param tamMax
     * @param e1
     * @param e2
     * @return
     */
    private static List<Candidato> difereLista(int tamMax, List<Candidato> e1, List<Candidato> e2) {
        ArrayList<Candidato> saida = new ArrayList<>(tamMax);

        for (Candidato e : e1) {
            if (!e2.contains(e)) {
                saida.add(e);
            }
        }

        return saida;
    }

    public static void main(String[] args) {

        try {
            if (args.length < 4) {
                System.out.println("Faltaram parametros!");
                System.exit(0);
            }

            int filtroCargo = 0;
            if (args[0].equals("--estadual")) {
                filtroCargo = 7;
            } else if (args[0].equals("--federal")) {
                filtroCargo = 6;
            } else {
                System.out.println("Votacao pedida nao esta prevista!");
                System.exit(0);
            }

            ArrayList<Candidato> eleitos = new ArrayList<>();
            ArrayList<Candidato> candidatos = new ArrayList<>();
            ArrayList<Partido> listaPartido = new ArrayList<>();

            // Dados com frequente acesso tambem serao colocados em uma HashMap para maior
            // agilidade
            HashMap<Integer, Candidato> votacao = new HashMap<>();
            HashMap<Integer, Partido> partidos = new HashMap<>();
            HashMap<Integer, Federacao> feds = new HashMap<>();

            try {
                Leitora.leituraArqCand(eleitos, candidatos, votacao, listaPartido, partidos, feds, args[1],
                        filtroCargo);
                Leitora.leituraArqVot(args[2], votacao, partidos, filtroCargo);

            } catch (FileNotFoundException e1) {
                System.out.println("Arquivo(s) indisponiveis ou nao acessiveis");
                System.exit(0);
            } catch (IOException e1) {
                System.out.println("Erro de IO não previsto, forneça o stack abaixo ao dev:");
                e1.printStackTrace();
                System.exit(0);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Nome para o arquivo nao foi passado");
                System.exit(0);
            }

            Collections.sort(candidatos);
            Collections.sort(eleitos);
            // Saidas

            // Primeira saida - Eleitos
            Relatorio.RelatorioEleitos(filtroCargo, eleitos);

            // Saida - Mais votados
            List<Candidato> eleicaoQntVotos = candidatos.subList(0, eleitos.size());
            Relatorio.RelatorioMaisVotados(eleicaoQntVotos);

            // Saida - Diferencas (maj - eleitos)
            List<Candidato> eleMaj = difereLista(eleitos.size(), eleicaoQntVotos, eleitos);
            Relatorio.RelatorioDif_MajEle(eleMaj, candidatos);

            // Saida - Diferencas pt2 (eleitos - maj)
            eleMaj = difereLista(eleitos.size(), eleitos, eleicaoQntVotos);
            Relatorio.RelatorioDif_EleMaj(eleMaj, candidatos);

            // Saida - Partidos
            Collections.sort(listaPartido);
            Relatorio.RelatorioListaPart(listaPartido);

            // Saida - Estatisticas de cada partido
            Relatorio.estatisticasParts(candidatos, partidos);

            try {
                // Saida - Estatisticas eleitos
                LocalDate datEle = LocalDate.parse(args[3], DateTimeFormatter.ofPattern("d/MM/yyyy"));
                Relatorio.estatisticasEleitos(eleitos, datEle);
            } catch (DateTimeParseException e) {
                System.out.println("Data para comparação não está corretamente formatada, use Dia/Mes/Ano");
                System.exit(0);
            } catch (Exception e) {
                System.out.println("Erro relacionado a data não previsto, forneça o texto abaixo ao dev:");
                e.printStackTrace();
                System.exit(0);
            }

            // Saida - votos totais
            Relatorio.estatisticasVotos(candidatos, listaPartido);

            System.out.println();

        } catch (Exception e) {
            System.out.println("Ops, erro nao previsto. Por favor forneça o texto abaixo ao dev:");
            e.printStackTrace();
            System.exit(0);
        }

    }
}
