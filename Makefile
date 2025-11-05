GAME_DIR = game

all: compile run

compile:
	cd $(GAME_DIR) && mvn clean compile

run:
	cd $(GAME_DIR) && mvn javafx:run

# Compile et lance
start: compile run

# Package l'application
package:
	cd $(GAME_DIR) && mvn clean package

# Nettoie les fichiers générés
clean:
	cd $(GAME_DIR) && mvn clean

# Tests
test:
	cd $(GAME_DIR) && mvn test

# Affiche les dépendances
deps:
	cd $(GAME_DIR) && mvn dependency:tree

# Debug Maven
debug:
	cd $(GAME_DIR) && mvn clean compile -X

# Aide
help:
	@echo "Commandes disponibles:"
	@echo "  make compile    - Compile le projet"
	@echo "  make run        - Lance l'application"
	@echo "  make start      - Compile et lance"
	@echo "  make package    - Package l'application"
	@echo "  make clean      - Nettoie les fichiers générés"
	@echo "  make test       - Lance les tests"
	@echo "  make deps       - Affiche les dépendances"
	@echo "  make install    - Installation complète"
	@echo "  make help       - Affiche cette aide"

.PHONY: all compile run start package clean test deps docker-build docker-run debug install help