# Architecture du Projet Java 2D

## Structure des dossiers

```
game/src/main/java/com/main/
│
├── App.java                    # Point d'entrée de l'application
├── EventHandler.java           # Gestion des événements (clavier)
│
├── entities/                   # Toutes les entités du jeu
│   ├── Unit.java              # Classe abstraite de base pour toutes les unités
│   │
│   ├── player/                # Personnages jouables
│   │   ├── Hero.java
│   │   └── Ghost.java         # Personnage contrôlable par le joueur
│   │
│   ├── units/                 # Soldats alliés
│   │   ├── Soldier.java       # Classe de base des soldats
│   │   ├── Tank.java          # Tank (HP: 500, ATK: 10, SPD: 3)
│   │   ├── Sniper.java        # Sniper (HP: 150, ATK: 40, SPD: 4)
│   │   └── Melee.java         # Corps à corps (HP: 200, ATK: 20, SPD: 5)
│   │
│   └── enemies/               # Ennemis
│       └── Zombie.java        # Zombie (HP: 100, ATK: 15, SPD: 2)
│
├── weapons/                    # Système d'armes
│   ├── Weapon.java            # Classe abstraite de base
│   ├── Pistol.java            # Pistolet
│   ├── AssaultRifle.java      # Fusil d'assaut
│   ├── SniperRifle.java       # Fusil de sniper
│   ├── Shotgun.java           # Fusil à pompe
│   └── SMG.java               # Mitraillette
│
├── map/                        # Éléments de la carte
│   ├── WarMap.java            # Gestion de la carte de jeu
│   ├── Obstacle.java          # Obstacles sur la carte
│   └── Base.java              # Base (spawn d'unités)
│
└── utils/                      # Classes utilitaires
    └── Position.java          # Gestion des positions (x, y)
```

## Hiérarchie des classes

### Entités
- `Unit` (abstraite)
  - `Ghost` (joueur)
  - `Soldier` (soldats alliés)
    - `Tank`
    - `Sniper`
    - `Melee`
  - `Zombie` (ennemis)

### Armes
- `Weapon` (abstraite)
  - `Pistol`
  - `AssaultRifle`
  - `SniperRifle`
  - `Shotgun`
  - `SMG`

## Packages

- `com.main` : Point d'entrée et gestionnaires principaux
- `com.main.entities` : Toutes les entités du jeu
- `com.main.entities.player` : Personnages jouables
- `com.main.entities.units` : Soldats alliés
- `com.main.entities.enemies` : Ennemis
- `com.main.weapons` : Système d'armes
- `com.main.map` : Éléments de carte
- `com.main.utils` : Classes utilitaires

## Notes importantes

1. Tous les fichiers ont été déplacés dans les dossiers appropriés
2. Les packages ont été mis à jour pour refléter la nouvelle structure
3. Les imports ont été corrigés dans tous les fichiers
4. Un getter `getSprite()` a été ajouté à la classe `Unit` pour accéder au sprite

## Prochaines étapes suggérées

- Implémenter les méthodes `attack()` et `specialAbility()` dans les sous-classes
- Ajouter des getters/setters pour les attributs non accessibles
- Créer des tests unitaires pour valider le comportement
- Documenter les classes avec Javadoc
