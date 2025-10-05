# Projet JavaFX - UNO

## Description
Ce projet est une application JavaFX qui implémente un jeu interactif UNO.  
L’objectif est de fournir une interface graphique intuitive tout en respectant les règles du jeu et en permettant une interaction fluide entre l’utilisateur et le programme.

L’application est développée en **Java 21** et utilise **JavaFX 21+** pour l’interface graphique.

---
## Installation et Configuration
Pour pouvoir installer et exécuter ce projet, vous devez avoir les éléments suivants installés sur votre machine :
- **Java Development Kit (JDK) 21 ou supérieur** : Assurez-vous que Java est correctement installé et configuré sur votre système.
- **JavaFX 21+** 
- **Gradle** : Utilisé pour la gestion des dépendances et la construction du projet.
---
## Clonage du Répertoire
Clonez ce répertoire sur votre machine locale en utilisant la commande suivante :
```bash
git clone https://github.com/Volterborn/UNO.git

```

## Règles du jeu (simplifiées)

### Objectif
Être le premier joueur à **se débarrasser de toutes ses cartes**.

### Cartes et effets

| Carte                                                                                       | Effet                                                                                                    |
|---------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| **Numérique (0-9)**                                                                         | Peut être jouée si elle correspond à la **même couleur** ou au **même chiffre** que la carte précédente. |
| **+2**                                                                                      | Le joueur suivant pioche **2 cartes** et **passe son tour**.                                             |
| **Inversion**                                                                               | Change le **sens du jeu** (horaire ↔ antihoraire).                                                       |
| **Passe ton tour**                                                                          | Le joueur suivant **saute son tour**.                                                                    |
| **Joker (Wild)**                                                                            | Permet de **changer la couleur** à jouer.                                                                |
| **+4 Joker (Wild Draw Four)**                                                               | Permet de **changer la couleur** et le joueur suivant pioche **4 cartes**.                               |
| - À jouer seulement si aucune carte de la couleur précédente n’est disponible dans la main. |                                                                                                          |

### Déroulement d’un tour
1. Le joueur doit poser une carte correspondant à la couleur ou au chiffre/symbole de la carte précédente, ou jouer un Joker.
2. Si le joueur ne peut pas jouer, il doit **piocher une carte**.
    - Si cette carte peut être posée immédiatement, il peut la jouer.
    - Sinon, le tour passe au joueur suivant.

### Fin du jeu
- La partie se termine lorsqu’un joueur n’a **plus de cartes**.
- Ce joueur est déclaré **vainqueur**.

---
