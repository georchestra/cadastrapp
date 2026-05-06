FROM jetty:9-jre8

ADD var/ /var
ADD jetty-env.xml /var/lib/jetty/webapps/cadastrapp/WEB-INF/jetty-env.xml

RUN java -jar "$JETTY_HOME/start.jar" --create-startd --add-to-start=jmx,jmx-remote,stats,http-forwarded

VOLUME [ "/tmp", "/run/jetty" ]

CMD ["sh", "-c", "exec java \
-Djava.io.tmpdir=/tmp/jetty \
-Dgeorchestra.datadir=/etc/georchestra \
-XX:MaxRAMPercentage=80 -XX:+UseParallelGC \
-Duser.language=fr -Duser.country=FR \
-XX:-UsePerfData \
${JAVA_OPTIONS} \
-Djetty.jmxremote.rmiregistryhost=${JMX_HOST:-127.0.0.1} \
-Djetty.jmxremote.rmiserverhost=${JMX_HOST:-127.0.0.1} \
-Dcom.sun.management.jmxremote=${JMX_ENABLE:-false} \
-Dcom.sun.management.jmxremote.host=${JMX_HOST:-127.0.0.1} \
-Dcom.sun.management.jmxremote.ssl=${JMX_SSL:-false} \
-Dcom.sun.management.jmxremote.authenticate=${JMX_AUTHENTICATE:-false} \
-jar /usr/local/jetty/start.jar"]
