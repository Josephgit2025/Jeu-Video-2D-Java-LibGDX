IMAGE_NAME = mon-app-java

build:
	docker build -t $(IMAGE_NAME) .

run:
	build
	docker run -p 8080:8080 $(IMAGE_NAME)

clean:
	docker rm -f $(IMAGE_NAME)
	docker rmi $(IMAGE_NAME)

.PHONY: build run clean