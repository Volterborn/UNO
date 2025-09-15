package UNO;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Partie {
    private Paquet paquet;
    private List<Joueur> joueurs;
    private Carte carteCourante;
    private int joueurCourant;
    private boolean sensHoraire;


    public Partie(List<String> nomJoueurs) {
        paquet = new Paquet();
        joueurs = new ArrayList<>();

        for (String nom : nomJoueurs){
            joueurs.add(new Joueur(nom));
        }
        joueurCourant = 0;
        sensHoraire = true;
        debuterPartie();
    }
    private void debuterPartie(){
        for (Joueur joueur : joueurs){
            for (int i = 0; i < 7; i++) {
                joueur.ajouterCarte(paquet.piocher());
            }
        }
        do{
            carteCourante = paquet.piocher();
        }while (carteCourante.getCouleur().equals("noir"));
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    public boolean isSensHoraire() {
        return sensHoraire;
    }

    public Paquet getPaquet() {
        return paquet;
    }

    public Joueur getJoueurCourant(){
        return joueurs.get(joueurCourant);
    }

    public void changerSens() {
        List<Joueur> joueursInverses = new ArrayList<>();
        for (int i = joueurs.size() - 1; i >= 0; i--) {
            joueursInverses.add(joueurs.get(i));
        }
        joueurs = joueursInverses;
        joueurCourant = joueurs.size() - 1 - joueurCourant;
        System.out.println("Le sens du jeu a été inversé !");
    }

    public void passerTour() {
        if (sensHoraire) {
            joueurCourant = (joueurCourant + 1) % joueurs.size();
        } else {
            joueurCourant = (joueurCourant - 1 + joueurs.size()) % joueurs.size();
        }
        System.out.println("Tour passé. Nouveau joueur courant : " + joueurs.get(joueurCourant));
    }

    public boolean peutJouer(Carte carte){
        return carte.getCouleur().equals(carteCourante.getCouleur()) || carte.getValeur().equals(carteCourante.getValeur()) || carte.getCouleur().equals("noir");
    }

    public boolean jouerCarte(Carte carte) {
        if (peutJouer(carte)) {
            carteCourante = carte;
            paquet.remettreCarte(carte);


            switch (carte.getValeur()) {
                case "inversion":
                    changerSens();
                    break;
                case "passe":
                    passerTour();
                    break;
                case "+2":
                    joueurSuivant().ajouterCarte(paquet.piocher());
                    joueurSuivant().ajouterCarte(paquet.piocher());
                    break;
                case "+4":
                    joueurSuivant().ajouterCarte(paquet.piocher());
                    joueurSuivant().ajouterCarte(paquet.piocher());
                    joueurSuivant().ajouterCarte(paquet.piocher());
                    joueurSuivant().ajouterCarte(paquet.piocher());
                    break;
                case "switch couleur":
                    choisirCouleur();
                    break;
            }
            return true;
        }
        return false;
    }

    public void choisirCouleur() {
        Scanner scanner = new Scanner(System.in);
        String[] couleurs = {"rouge", "bleu", "jaune", "vert"};
        int choix;

        do {
            System.out.println("Choisissez une nouvelle couleur : ");
            for (int i = 0; i < couleurs.length; i++) {
                System.out.println((i + 1) + ": " + couleurs[i]);
            }
            choix = scanner.nextInt();
        } while (choix < 1 || choix > 4);

        carteCourante = new Carte(couleurs[choix - 1], "switch couleur");
        System.out.println("La nouvelle couleur est : " + couleurs[choix - 1]);
    }

    public Joueur joueurSuivant(){
        int index;
        if(sensHoraire){
            index = (joueurCourant + 1) % joueurs.size();
        } else {
            index = (joueurCourant - 1 + joueurs.size()) % joueurs.size();
        }
        return joueurs.get(index);
    }

    public boolean estTerminee(){
        for (Joueur joueur : joueurs){
            if (joueur.aGagner()){
                return true;
            }
        }
        return false;
    }
    public Joueur obtenirGagnant(){
        for (Joueur joueur : joueurs){
            if (joueur.aGagner()){
                return joueur;
            }
        }
        return null;
    }

    public Carte getCarteCourante() {
        return carteCourante;
    }
}
