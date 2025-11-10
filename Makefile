all: compile run

compile:
	./gradlew build

run:
	./gradlew lwjgl3:run

test:
	./gradlew test jacocoTestReport