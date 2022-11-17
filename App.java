import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class App {
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

        int filtroCargo = 0;
        if (args[0].equals("--estadual")) {
            filtroCargo = 7;
        } else if (args[0].equals("--federal")) {
            filtroCargo = 6;
        }

        if (filtroCargo != 6 & filtroCargo != 7) {
            System.out.println("Votacao pedida nao esta prevista!");
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
            Leitora.leituraArqCand(eleitos, candidatos, votacao, listaPartido, partidos, feds, args[1], filtroCargo);
            Leitora.leituraArqVot(args[2], votacao, partidos, filtroCargo);

        } catch (FileNotFoundException e1) {
            System.out.println("Arquivo(s) indisponiveis ou nao acessiveis");
        } catch (IOException e1) {
            System.out.println("Erro de IO nao previsto, forneça o stack abaixo ao dev:");
            e1.printStackTrace();
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

        // Saida - Estatisticas eleitos
        Relatorio.estatisticasEleitos(eleitos);

        // Saida - votos totais
        Relatorio.estatisticasVotos(candidatos, listaPartido);
    }
}
