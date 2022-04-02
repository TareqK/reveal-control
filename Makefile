DOCKER_TAG = latest
DOCKER_REPOSITORY = revealcontrol/backend
clean:  
	rm -rf *.sentinel
	cd backend && mvn clean;

build: 
	cd backend && mvn install;

docker: build
	cd backend && docker build -t $(DOCKER_REPOSITORY):$(DOCKER_TAG) .

publish: docker
	cd backend && docker push $(DOCKER_REPOSITORY):$(DOCKER_TAG)

run: docker
	docker run -it -p 8080:8080 $(DOCKER_REPOSITORY):$(DOCKER_TAG)

.PHONY: clean build docker publish run
