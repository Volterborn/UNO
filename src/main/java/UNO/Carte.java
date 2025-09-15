package UNO;

public class Carte {
        private String couleur;
        private String valeur;


    public Carte(String couleur, String valeur) {
        this.couleur = couleur;
        this.valeur = valeur;
    }


    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }


    public String getValeur() {
        return valeur;
    }


    @Override
    public String toString() {
        return couleur + " " + valeur;
    }
}
