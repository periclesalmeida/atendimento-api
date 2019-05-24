FROM openjdk:8-jdk-alpine
MAINTAINER periclesalmeida
VOLUME /tmp
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS=""
RUN apk --no-cache add tzdata && \
    cp /usr/share/zoneinfo/America/Maceio /etc/localtime && \
    fc-cache -f
ADD entrypoint.sh entrypoint.sh
ADD ./classes/static /static
RUN chmod 755 entrypoint.sh
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["./entrypoint.sh"]
