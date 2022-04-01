FROM adoptopenjdk:11.0.6_10-jre-hotspot

WORKDIR /
COPY target/backend-*.jar ./backend.jar
COPY target/lib/ /lib/
COPY src/script/start.sh ./start.sh
RUN chmod +x ./start.sh
CMD ./start.sh