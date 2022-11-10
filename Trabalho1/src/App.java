import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Hashtable;

public class App {
    private static char converteGenero(int entrada) {
        if (entrada == 2)
            return 'M';
        else if (entrada == 4)
            return 'F';
        return 'I';
    }

    private static void leituraArqCand(ArrayList<Candidato> eleitos, ArrayList<Candidato> candidatos,
            Hashtable<Integer, Partido> partidos, String arq, int filtroCargo) throws Exception {
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

            if ((Integer.parseInt(partes[13]) == filtroCargo)
                    & (Integer.parseInt(partes[24]) == 2 | Integer.parseInt(partes[24]) == 16)) {

                // Cria candidato lido
                LocalDate nasc = LocalDate.parse(partes[42], DateTimeFormatter.ofPattern("d/MM/yyyy"));
                Candidato cand = new Candidato(Integer.parseInt(partes[16]), Integer.parseInt(partes[13]), partes[18],
                        nasc,
                        (partes[56] == "2" || partes[56] == "3"), converteGenero(Integer.parseInt(partes[45])), 0);

                // Se o partido ja existe
                if (partidos.containsKey(Integer.parseInt(partes[27]))) {
                    cand.setPartidao(partidos.get(Integer.parseInt(partes[27])));
                } else {
                    // caso nao exista, cria partido, insere na tabela, linka
                    Partido novo = new Partido(Integer.parseInt(partes[27]), partes[28]);
                    partidos.put(novo.getNumeroUrna(), novo);
                    novo.InsereCand(cand);
                }

                // Add cand em candidatos
                candidatos.add(cand);
                // Add cand em eleitos
                if (cand.isEleito())
                    eleitos.add(cand);
            }
            temp = arqCand.readLine();
        }
        arqCand.close();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        int filtroCargo = 0;

        if (args[0].equals("--estadual")) {
            filtroCargo = 6;
        }
        if (args[0].equals("--federal")) {
            filtroCargo = 7;

        }

        ArrayList<Candidato> eleitos = new ArrayList<>();
        ArrayList<Candidato> candidatos = new ArrayList<>();

        // Tabela hash para agilidade, na saida serao colocados em um array e ordenados
        // A chave sera a numero de urna do partido
        Hashtable<Integer, Partido> partidos = new Hashtable<>();

        leituraArqCand(eleitos, candidatos, partidos, args[1], filtroCargo);

        System.out.println("Candidatos:");
        for (Candidato e : candidatos) {
            System.out.println(e);
        }

        // Arquivo de secoes AINDA ESTA EM DEV
        // TODO: terminar aq
        FileInputStream isSec = new FileInputStream(args[2]);
        InputStreamReader rsSec = new InputStreamReader(isSec, "ISO8859-1");
        BufferedReader ArqSec = new BufferedReader(rsSec);
        String temp = ArqSec.readLine();
        while (temp != null) {
            String[] partes = temp.split("\";\"");
            System.out.println(partes[17] + " " + partes[19] + " " + partes[21]);
            temp = ArqSec.readLine();
        }
        ArqSec.close();

    }
}
