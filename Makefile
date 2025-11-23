all: compile run

compile:
	./gradlew build -x test

run:
	./gradlew lwjgl3:run

test:
	./gradlew test jacocoTestReport

exe:
	./gradlew clean jar
	./gradlew lwjgl3:packageWinX64