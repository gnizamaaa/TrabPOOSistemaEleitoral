import java.util.ArrayList;

public class Federacao {
    private int ID;
    private ArrayList<Partido> partidos = new ArrayList<>();

    public ArrayList<Partido> getPartidos() {
        return partidos;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public void inserePartido(Partido novo) {
        if(!partidos.contains(novo)){
            partidos.add(novo);
            novo.setFed(this);
        }
    }
}
