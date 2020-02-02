FROM openjdk:8-jre-alpine
MAINTAINER periclesalmeida
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS=""
RUN apk --no-cache add tzdata && \
    cp /usr/share/zoneinfo/America/Maceio /etc/localtime && \
    apk --no-cache add fontconfig  msttcorefonts-installer && \
    update-ms-fonts && \
    fc-cache -f
ADD entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["./entrypoint.sh"]
