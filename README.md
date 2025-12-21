# RoboEscape

## Description

RoboEscape est un mini-jeu en Java où le joueur contrôle un petit robot coincé dans
un labyrinthe généré aléatoirement. L’objectif est d’atteindre la sortie tout en
évitant les pièges, en gérant sa vie, et en profitant de power-ups temporaires
(bouclier, vitesse, vision).

Le jeu inclut plusieurs états (Menu, Playing, Pause, Game Over, Win), un système de
logging avancé, une interface graphique JavaFX, une grille dynamique représentant
le labyrinthe, et éventuellement des ennemis dotés d’une stratégie de déplacement.

L’accent est mis sur l’architecture logicielle, l’application de plusieurs Design
Patterns et la maintenabilité du code, conformément aux exigences du module.

## Membres du Groupe

- Ons Souissi
- Ameny Hamzaoui
- Jasser Gorsia
- Mohamed Aziz Sandid

## Technologies Utilisées

- Langage : Java 17
- Framework GUI : JavaFX
- Logging : Log4j2
- Build : Maven

## Design Patterns Implémentés

1. **State Pattern**  
   Gestion des différents états du jeu (Menu, Playing, Pause, Game Over, Win) ainsi
   que des états du joueur (Idle, Moving, Stunned, Boosted).

2. **Decorator Pattern**  
   Système de power-ups appliqués dynamiquement au joueur (SpeedBoost, Shield,
   VisionEnhancer). Chaque décorateur modifie temporairement le comportement du
   personnage.

3. **Composite Pattern**  
   Structure hiérarchique du labyrinthe : une grille composée de cellules
   (WallCell, TrapCell, FloorCell, ExitCell). Le labyrinthe est traité comme un
   ensemble d’éléments pouvant être parcourus et rendus de manière uniforme.

4. **Factory Pattern**  
   Création des cellules du labyrinthe et/ou des ennemis via une factory dédiée,
   permettant d’isoler la logique de construction et de simplifier la génération
   du niveau.

## Installation

### Prérequis

- JDK 17 ou supérieur
- Maven 3.6 ou supérieur

### Étapes

1. Cloner le dépôt :  
   `git clone https://github.com/souissi-ons/RoboEscape.git`
2. Compiler le projet :  
   `mvn clean install`
3. Exécuter l’application :  
   `java -jar target/game.jar`

## Utilisation

- **Flèches directionnelles** : Déplacement dans la grille
- **Espace** : Action / Saut
- **Échap** : Pause du jeu (retour au Menu)

