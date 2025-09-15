package UNO;

import java.util.ArrayList;
import java.util.List;

public class Joueur {
    private String nom;
    private List<Carte> main;


    public Joueur(String nom) {
        this.nom = nom;
        this.main = new ArrayList<>();
    }

    public void ajouterCarte(Carte carte){
        main.add(carte);
    }

    public void retirerCarte(Carte carte){
        main.remove(carte);
    }

    public List<Carte> getMain(){
        return main;
    }

    public boolean aGagner(){
        return main.isEmpty();
    }

    public boolean aUno(){
        return main.size() == 1;
    }

    public Carte jouerCarte(int index){
        if(index >= 0 && index < main.size()){
            return main.remove(index);
        }
        return null;
    }


    @Override
    public String toString() {
        return nom;
    }
}
