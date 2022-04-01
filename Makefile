DOCKER_TAG = latest

clean:
	cd backend && mvn clean;

build:  clean
	cd backend && mvn install;

docker: build
	cd backend && docker build -t reveal-control/backend:$(DOCKER_TAG) .

publish: docker
	cd backend && docker push reveal-control/backend:$(DOCKKER_TAG)

.PHONY: clean build docker publish
