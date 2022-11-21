package rhuangarcia;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class Leitora {

    // Conversao de inteiro (usado no arquivo) para char utilizado no programa
    private static char converteGenero(int entrada) {
        if (entrada == 2)
            return 'M';
        else if (entrada == 4)
            return 'F';
        return 'I';
    }

    public static void leituraArqCand(List<Candidato> eleitos, List<Candidato> candidatos,
            Map<Integer, Candidato> votacao, List<Partido> listaPartido,
            Map<Integer, Partido> partidos, Map<Integer, Federacao> feds,
            String arq, int filtroCargo) throws IOException {
        try (
                // Arquivo de candidatos
                FileInputStream isCand = new FileInputStream(arq)) {
            InputStreamReader rsCand = new InputStreamReader(isCand, "ISO8859-1");
            BufferedReader arqCand = new BufferedReader(rsCand);
            String temp = arqCand.readLine();
            temp = arqCand.readLine();

            while (temp != null) {
                String[] partes = temp.split("\";\"");

                // Tratamento dos partidos
                // Se o partido ja existe
                if (!partidos.containsKey(Integer.parseInt(partes[27]))) {
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

                // Se o candidato concorre para o cargo desejado e possui candidatura valida
                if ((Integer.parseInt(partes[13]) == filtroCargo)
                        & (Integer.parseInt(partes[24]) == 2 | Integer.parseInt(partes[24]) == 16)) {

                    // Cria candidato lido
                    LocalDate nasc = LocalDate.parse(partes[42], DateTimeFormatter.ofPattern("d/MM/yyyy"));
                    Candidato cand = new Candidato(Integer.parseInt(partes[16]), Integer.parseInt(partes[13]),
                            partes[18],
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
        } catch (NumberFormatException e) {
            // parse deu erro (Formatacao incorreta)
            System.out.println("O arquivo de candidatos fornecido ndao esta com a devida formatacao");
            System.exit(0);

        } catch (IOException e) {
            throw e;
        } catch (ArrayIndexOutOfBoundsException e) {
            // O split nao gerou tantos campos (Formatacao incorreta)
            System.out.println("O arquivo de candidatos fornecido ndao esta com a devida formatacao");
            System.exit(0);
        }

    }

    public static void leituraArqVot(String arq,
            Map<Integer, Candidato> votacao,
            Map<Integer, Partido> partidos, int filtroCargo) throws IOException {
        try (FileInputStream isSec = new FileInputStream(arq)) {
            InputStreamReader rsSec = new InputStreamReader(isSec, "ISO8859-1");
            BufferedReader ArqSec = new BufferedReader(rsSec);
            String temp = ArqSec.readLine();
            temp = ArqSec.readLine();

            while (temp != null) {
                // Divide em campos, tirando aspas
                String[] partes = temp.split("\";\"");
                // if (Integer.parseInt(partes[19]) == 95 || Integer.parseInt(partes[19]) == 96
                // || Integer.parseInt(partes[19]) == 97) {
                // } else {

                // Se e o cargo procurado, processe o voto
                if (Integer.parseInt(partes[17]) == filtroCargo) {

                    Candidato cand = votacao.get(Integer.parseInt(partes[19]));
                    if (cand != null) {
                        // Se foi voto nomeavel
                        cand.addQntVotos(Integer.parseInt(partes[21]));
                    }
                    // Foi voto por partido
                    Partido part = partidos.get(Integer.parseInt(partes[19]));
                    if (part != null) // Se nao for um branco/nulo/anulado
                        part.addVotosPartidarios(Integer.parseInt(partes[21]));
                }
                temp = ArqSec.readLine();
                // }
            }
        } catch (NumberFormatException e) {
            // parse deu erro (Formatacao incorreta)
            System.out.println("O arquivo de votacao fornecido nao esta com a devida formatacao");
            System.exit(0);
        } catch (IOException e) {
            throw e;
        } catch (ArrayIndexOutOfBoundsException e) {
            // O split nao gerou tantos campos (Formatacao incorreta)
            System.out.println("O arquivo de votacao fornecido nao esta com a devida formatacao");
            System.exit(0);
        }
    }
}
