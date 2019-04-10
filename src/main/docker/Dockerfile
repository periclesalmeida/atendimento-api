FROM tomcat:9.0.14-jre8
RUN apt-get update && \
ln -fs /usr/share/zoneinfo/America/Maceio /etc/localtime && dpkg-reconfigure -f noninteractive tzdata && \
rm -rf /usr/local/tomcat/webapps/ && \
apt-get clean
ENV CATALINA_OPTS="-Dkey=value"
ARG WAR_FILE
COPY ${WAR_FILE} /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]
