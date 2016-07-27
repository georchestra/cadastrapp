FROM jetty:9.3-jre8

ADD . /

RUN java -jar "$JETTY_HOME/start.jar" --add-to-startd=jmx,jmx-remote,stats

RUN  unzip -d /var/lib/jetty/webapps/cadastrapp /var/lib/jetty/webapps/cadastrapp.war && \
  rm -f /var/lib/jetty/webapps/cadastrapp.war &&                                         \
  cp /etc/georchestra/cadastrapp/jetty-env.xml /var/lib/jetty/webapps/cadastrapp/WEB-INF/

CMD ["sh", "-c", "exec java -Djava.io.tmpdir=/tmp/jetty -Dgeorchestra.datadir=/etc/georchestra \
 -Xmx${XMX:-512m} -Xms${XMX:-512m}                                                             \
 $TRUSTORE_PASSWORD $TRUSTSTORE_PATH                                                           \
 -jar /usr/local/jetty/start.jar"]

