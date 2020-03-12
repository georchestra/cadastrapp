FROM jetty:9-jre8

ADD var/ /var
ADD jetty-env.xml /var/lib/jetty/webapps/cadastrapp/WEB-INF/jetty-env.xml

RUN java -jar "$JETTY_HOME/start.jar" --create-startd --add-to-start=jmx,jmx-remote,stats,http-forwarded

VOLUME [ "/tmp", "/run/jetty" ]

CMD ["sh", "-c", "exec java \
-Djava.io.tmpdir=/tmp/jetty \
-Dgeorchestra.datadir=/etc/georchestra \
-Xmx${XMX:-512m} -Xms${XMX:-512m} \
-Duser.language=fr -Duser.country=FR \
-XX:-UsePerfData \
${JAVA_OPTIONS} \
-Djetty.jmxremote.rmiregistryhost=0.0.0.0 \
-Djetty.jmxremote.rmiserverhost=0.0.0.0 \
-jar /usr/local/jetty/start.jar"]
