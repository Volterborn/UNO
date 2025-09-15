package UNO;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Menu interactif pour sélectionner le nombre de joueurs
        System.out.println("Bienvenue dans le jeu UNO !");
        int nombreJoueurs;
        do {
            System.out.print("Entrez le nombre de joueurs (2 à 4) : ");
            nombreJoueurs = scanner.nextInt();
        } while (nombreJoueurs < 2 || nombreJoueurs > 4);

        // Saisie des noms des joueurs
        List<String> joueurs = new ArrayList<>();
        for (int i = 1; i <= nombreJoueurs; i++) {
            System.out.print("Entrez le nom du joueur " + i + " : ");
            String nom = scanner.next();
            joueurs.add(nom);
        }

        // Créer la partie
        Partie partie = new Partie(joueurs);

        // Simulation d'une partie dans le terminal
        while (!partie.estTerminee()) {
            Joueur joueurCourant = partie.getJoueurCourant();
            System.out.println("\nC'est le tour de " + joueurCourant);
            System.out.println("Carte actuelle sur la pile : " + partie.getCarteCourante());
            boolean carteJouee = false;

            while (!carteJouee) {
                System.out.println("Votre main : ");
                List<Carte> mainJoueur = joueurCourant.getMain();
                for (int i = 0; i < mainJoueur.size(); i++) {
                    System.out.println(i + ": " + mainJoueur.get(i));
                }

                System.out.println("Choisissez une carte à jouer (numéro) ou tapez -1 pour piocher une carte : ");
                int choix = scanner.nextInt();

                if (choix == -1) {
                    Carte cartePiochee = partie.getPaquet().piocher();
                    if (cartePiochee != null) {
                        joueurCourant.ajouterCarte(cartePiochee);
                        System.out.println("Vous avez pioché : " + cartePiochee);
                    } else {
                        System.out.println("Le paquet est vide !");
                    }
                } else if (choix >= 0 && choix < joueurCourant.getMain().size()) {
                    Carte carteChoisie = joueurCourant.getMain().get(choix);
                    if (partie.peutJouer(carteChoisie)) {
                        joueurCourant.retirerCarte(carteChoisie);
                        partie.jouerCarte(carteChoisie);
                        System.out.println("Vous avez joué : " + carteChoisie);

                        // Gestion de la carte "Switch couleur"
                        if (carteChoisie.getValeur().equals("switch couleur")) {
                            String[] couleurs = {"rouge", "bleu", "jaune", "vert"};
                            int choixCouleur;
                            do {
                                System.out.println("Choisissez une nouvelle couleur : ");
                                for (int i = 0; i < couleurs.length; i++) {
                                    System.out.println((i + 1) + ": " + couleurs[i]);
                                }
                                choixCouleur = scanner.nextInt();
                            } while (choixCouleur < 1 || choixCouleur > 4);

                            partie.getCarteCourante().setCouleur(couleurs[choixCouleur - 1]);
                            System.out.println("La nouvelle couleur est : " + couleurs[choixCouleur - 1]);
                        }

                        carteJouee = true;
                    } else {
                        System.out.println("Vous ne pouvez pas jouer cette carte !");
                    }
                }
            }
            partie.passerTour();
        }

        System.out.println(partie.obtenirGagnant() + " a gagné la partie !");
        scanner.close();
    }
}