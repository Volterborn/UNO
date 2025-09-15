package UNO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Paquet {
    private List<Carte> cartes;
    private List<Carte> cartesPiochees;

    public Paquet() {
        cartes = new ArrayList<>();
        cartesPiochees = new ArrayList<>();
        initialiserPaquet();
        melanger();
    }


    private void initialiserPaquet(){
        String[] couleurs = {"rouge", "bleu", "jaune", "vert"};
        String[] valeurs = {"0","1","2","3","4","5","6","7","8","9","+2","inversion", "passe"};

        for(String couleur : couleurs){
            for (String valeur : valeurs){
                cartes.add(new Carte(couleur, valeur));
                if (!valeur.equals("0")){
                    cartes.add(new Carte(couleur, valeur));
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            cartes.add(new Carte("noir", "+4"));
            cartes.add(new Carte("noir", "switch couleur"));
        }
    }
        public void melanger(){
            Collections.shuffle(cartes);
        }

        public Carte piocher(){
        if(cartes.isEmpty()){
            reinitialiserPaquet();
        }
        if(!cartes.isEmpty()){
                return cartes.remove(0);
            }
            return null;
        }

        public void remettreCarte(Carte carte) {
            cartesPiochees.add(carte);
        }
        private void reinitialiserPaquet(){
            cartes.addAll(cartesPiochees);
            cartesPiochees.clear();
            melanger();
        }

        public boolean estVide(){
            return cartes.isEmpty();
    }
}
