import java.util.ArrayList;

public class Federacao {
    private int ID;
    private final ArrayList<Partido> partidos = new ArrayList<>();

    public ArrayList<Partido> getPartidos() {
        return partidos;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    // Insere o partido na lista de partidos da federacao mantendo o link (Partido
    // possui uma referencia para a federacao que participa)
    public void inserePartido(Partido novo) {
        if (!partidos.contains(novo)) {
            partidos.add(novo);
            novo.setFed(this);
        }
    }
}
