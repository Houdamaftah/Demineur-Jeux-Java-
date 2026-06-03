# Démineur — Miniprojet PPO ISIA3

Implémentation Java du jeu **Démineur** (Minesweeper), réalisée dans le cadre du miniprojet de Programmation Par Objets (PPO). Le projet comprend un noyau fonctionnel complet (`lib/`) et une interface graphique Swing (`gui/`).

---

## Lancer le jeu

### Prérequis

- Java 11 ou supérieur

### Compilation

Depuis la racine du projet :

```bash
javac lib/*.java gui/*.java exceptions/*.java
```

### Exécution

```bash
java gui.DemineurUI
```

Une fenêtre de configuration s'ouvre au démarrage pour choisir la taille de la grille et le nombre de mines.

---

## Structure du projet

```
ppo_demineur/
├── exceptions/
│   ├── InvalidMoveException.java   # Exception de base pour les coups invalides
│   ├── GameLostException.java      # Lancée quand le joueur clique sur une bombe
│   └── GameWonException.java       # Lancée quand toutes les cases sûres sont révélées
├── lib/
│   ├── AbstractCell.java           # Classe abstraite représentant une case
│   ├── SafeCell.java               # Case sûre (stocke le nombre de bombes voisines)
│   ├── BombCell.java               # Case bombe
│   └── Demineur.java               # Noyau du jeu : grille, règles, logique
├── gui/
│   ├── DemineurUI.java             # Fenêtre principale (JFrame), point d'entrée
│   ├── GamePanel.java              # Grille de boutons cliquables
│   ├── TopPanel.java               # Barre du haut (compteurs, boutons)
│   └── SetupDialog.java            # Dialogue de configuration initiale
└── README.md
```

---

## Fonctionnalités implémentées

### Noyau fonctionnel (`lib/`)

**Hiérarchie de classes pour les cases**
- `AbstractCell` définit l'état commun (révélé/non révélé) et les méthodes abstraites `isBomb()` et `getAdjacentBombs()`.
- `SafeCell` étend `AbstractCell` et stocke le nombre de bombes adjacentes (entre 0 et 8).
- `BombCell` étend `AbstractCell` et retourne toujours `true` pour `isBomb()`.

Ce choix de conception par héritage permet à la GUI de manipuler toutes les cases de façon uniforme via `AbstractCell`, sans avoir à connaître leur type concret.

**Placement différé des bombes**
Les bombes sont placées aléatoirement *au premier clic*, en excluant systématiquement la case cliquée. Cela garantit que le joueur ne peut jamais perdre dès son premier coup.

**Propagation automatique (flood fill)**
Lorsqu'une case révélée a 0 bombe adjacente, la révélation se propage récursivement à ses 8 voisins. La récursion s'arrête naturellement sur les cases déjà révélées ou les cases ayant au moins une bombe voisine.

**Détection de fin de partie**
- **Défaite** : `revealCell()` lance une `GameLostException` si la case est une bombe.
- **Victoire** : après chaque révélation, `checkWin()` vérifie si toutes les cases non-bombe sont découvertes ; si oui, une `GameWonException` est lancée.

Les deux exceptions héritent de `InvalidMoveException`, ce qui simplifie la gestion dans la GUI.

### Interface graphique (`gui/`)

- **`SetupDialog`** : dialogue de configuration permettant de paramétrer le nombre de lignes, colonnes et mines. Valide les saisies (valeurs positives, nombre de mines < total de cases) et redemande en cas d'erreur.
- **`GamePanel`** : grille de `JButton` représentant les cases. Chaque clic appelle `revealCell()` et met à jour l'affichage. Les cases révélées affichent leur chiffre en couleur (bleu pour 1, vert pour 2, rouge pour 3, etc.).
- **`TopPanel`** : affiche le nombre de mines et le compteur de clics. Propose trois boutons : *Règles*, *Suggérer une case*, et *Recommencer*.
- **`DemineurUI`** : fenêtre principale qui orchestre les composants. Le bouton "Suggérer une case" met en surbrillance jaune la première case sûre non révélée trouvée dans la grille.

---

## Choix d'implémentation notables

| Choix | Justification |
|---|---|
| Héritage `SafeCell` / `BombCell` | Polymorphisme : la GUI appelle `isBomb()` et `getAdjacentBombs()` sans `instanceof`. |
| Placement des bombes au premier clic | Garantit une première case sûre, améliore l'expérience de jeu. |
| Propagation récursive | Mise en œuvre naturelle du flood fill ; la vérification `isRevealed()` avant chaque appel récursif évite les boucles infinies. |
| Exceptions pour gérer la fin de partie | Évite de polluer la valeur de retour de `revealCell()` avec des codes d'état ; le flux d'exception remonte proprement jusqu'au gestionnaire dans `GamePanel`. |
| `updateAllButtons()` plutôt que `updateButton()` | Après propagation, plusieurs cases sont révélées simultanément ; mettre à jour toute la grille en une passe est plus simple et sans surcoût notable pour des tailles classiques. |
