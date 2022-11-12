import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;

public class App {
    private static char converteGenero(int entrada) {
        if (entrada == 2)
            return 'M';
        else if (entrada == 4)
            return 'F';
        return 'I';
    }

    private static void leituraArqCand(ArrayList<Candidato> eleitos, ArrayList<Candidato> candidatos,
            Hashtable<Integer, Candidato> votacao, ArrayList<Partido> listaPartido,
            Hashtable<Integer, Partido> partidos, Hashtable<Integer, Federacao> feds,
            String arq, int filtroCargo)
            throws Exception {
        // Arquivo de candidatos
        FileInputStream isCand = new FileInputStream(arq);
        InputStreamReader rsCand = new InputStreamReader(isCand, "ISO8859-1");
        BufferedReader arqCand = new BufferedReader(rsCand);
        String temp = arqCand.readLine();
        temp = arqCand.readLine();

        while (temp != null) {
            String[] partes = temp.split("\";\"");

            // System.out.println(partes[13] + " " + partes[24] + " " + partes[16] + " " +
            // partes[18] + " " +
            // partes[27] + " " + partes[28] + " " + partes[30] + " " +
            // partes[42] + " " + partes[45] + " " + partes[56]);

            // Tratamento dos partidos
            // Se o partido ja existe
            if (partidos.containsKey(Integer.parseInt(partes[27]))) {
            } else {
                // caso nao exista, cria partido, insere na tabela, linka
                Partido novo = new Partido(Integer.parseInt(partes[27]), partes[28]);
                partidos.put(novo.getNumeroUrna(), novo);
                listaPartido.add(novo);

                // Tratamento das federacoes
                if (Integer.parseInt(partes[30]) != -1)
                    if (feds.containsKey(Integer.parseInt(partes[30]))) {
                        novo.setFed(feds.get(Integer.parseInt(partes[30])));
                    } else {
                        Federacao nova = new Federacao();
                        nova.setID(Integer.parseInt(partes[30]));
                        feds.put(nova.getID(), nova);
                        novo.setFed(nova);
                    }
            }

            if ((Integer.parseInt(partes[13]) == filtroCargo)
                    & (Integer.parseInt(partes[24]) == 2 | Integer.parseInt(partes[24]) == 16)) {

                // Cria candidato lido
                LocalDate nasc = LocalDate.parse(partes[42], DateTimeFormatter.ofPattern("d/MM/yyyy"));
                Candidato cand = new Candidato(Integer.parseInt(partes[16]), Integer.parseInt(partes[13]), partes[18],
                        nasc,
                        (Integer.parseInt(partes[56]) == 2 || Integer.parseInt(partes[56]) == 3),
                        converteGenero(Integer.parseInt(partes[45])), 0);

                cand.setPartidao(partidos.get(Integer.parseInt(partes[27])));
                // Add cand em candidatos
                candidatos.add(cand);
                votacao.put(cand.getID(), cand);
                // Add cand em eleitos
                if (cand.isEleito()) {
                    eleitos.add(cand);
                    cand.setEleito(true);
                }
            }
            temp = arqCand.readLine();
        }
        arqCand.close();
    }

    private static void leituraArqVot(String arq,
            Hashtable<Integer, Candidato> votacao,
            Hashtable<Integer, Partido> partidos, int filtroCargo) throws Exception {
        FileInputStream isSec = new FileInputStream(arq);
        InputStreamReader rsSec = new InputStreamReader(isSec, "ISO8859-1");
        BufferedReader ArqSec = new BufferedReader(rsSec);
        String temp = ArqSec.readLine();
        temp = ArqSec.readLine();

        while (temp != null) {
            String[] partes = temp.split("\";\"");
            // System.out.println(partes[17] + " " + partes[19] + " " + partes[21]);
            if (Integer.parseInt(partes[17]) == filtroCargo) {
                Candidato cand = votacao.get(Integer.parseInt(partes[19]));
                if (cand != null) {// Se foi voto nomeavel
                    cand.addQntVotos(Integer.parseInt(partes[21]));
                } else { // Foi voto por partido
                    Partido part = partidos.get(Integer.parseInt(partes[19]));
                    if (part != null) // Se nao for um branco/nulo/anulado
                        part.addVotosPartidarios(Integer.parseInt(partes[21]));
                }
            }
            temp = ArqSec.readLine();
        }
        ArqSec.close();
    }

    private static List<Candidato> difereLista(int tamMax, List<Candidato> e1, List<Candidato> e2) {
        ArrayList<Candidato> saida = new ArrayList<>(tamMax);

        for (Candidato e : e1) {
            if (!e2.contains(e)) {
                saida.add(e);
            }
        }

        return saida;
    }

    public static void main(String[] args) throws Exception {
        // System.out.println("Hello, World!");

        int filtroCargo = 0;

        if (args[0].equals("--estadual")) {
            filtroCargo = 7;
        }
        if (args[0].equals("--federal")) {
            filtroCargo = 6;

        }

        ArrayList<Candidato> eleitos = new ArrayList<>();
        ArrayList<Candidato> candidatos = new ArrayList<>();
        ArrayList<Partido> listaPartido = new ArrayList<>();

        // Dados com frequente acesso tambem serao colocados em uma Hashtable para maior
        // agilidade
        Hashtable<Integer, Candidato> votacao = new Hashtable<>();
        Hashtable<Integer, Partido> partidos = new Hashtable<>();
        Hashtable<Integer, Federacao> feds = new Hashtable<>();

        leituraArqCand(eleitos, candidatos, votacao, listaPartido, partidos, feds, args[1], filtroCargo);

        leituraArqVot(args[2], votacao, partidos, filtroCargo);

        Integer i = 1;
        Collections.sort(candidatos);
        Collections.sort(eleitos);
        System.out.println("Número de vagas: " + eleitos.size() + "\n\nDeputados estaduais eleitos:");
        for (Candidato e : eleitos) {
            System.out.println(i.toString() + " - " + e);
            i++;
        }

        i = 1;
        System.out
                .println("\nCandidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        for (Candidato e : candidatos) {
            System.out.println(i.toString() + " - " + e);
            i++;
            if (i > eleitos.size() - 1)
                break;
        }

        List<Candidato> eleicaoQntVotos = candidatos.subList(0, eleitos.size());

        for (Candidato e : eleicaoQntVotos) {
            System.out.println(i.toString() + " - " + e);
            i++;
            if (i > eleitos.size())
                break;
        }
        i = 1;
        List<Candidato> eleMaj = difereLista(eleitos.size(), eleicaoQntVotos, eleitos);
        System.out
                .println(
                        "\nTeriam sido eleitos se a votação fosse majoritária, e não foram eleitos:\n(com sua posição no ranking de mais votados)");
        for (Candidato e : eleMaj) {
            System.out.println(candidatos.indexOf(e) + " - " + e);
        }

        eleMaj = difereLista(eleitos.size(), eleitos, eleicaoQntVotos);
        System.out
                .println(
                        "\nEleitos, que se beneficiaram do sistema proporcional:\n(com sua posição no ranking de mais votados)");
        for (Candidato e : eleMaj) {
            System.out.println(candidatos.indexOf(e) + " - " + e);
        }

        Collections.sort(listaPartido);
        System.out.println("\nVotação dos partidos e número de candidatos eleitos:");
        for (Partido e : listaPartido) {
            System.out.println(i + " - " + e);
            i++;
        }

    }
}
