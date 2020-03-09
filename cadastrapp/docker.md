# Notes for docker users

One can generate a docker image using the following command from the current
directory:

```
$ mvn clean package docker:build -Pdocker
```

If you are using this docker image in your setup, you will need to be aware of
2 main things.

# Servlet container

The docker image is based on Jetty, not on Tomcat.

# JNDI connection

A JNDI datasource will be defined in a `jetty-env.xml` nested into the webapp.
This one is based on [c3p0](https://www.mchange.com/projects/c3p0/).

C3P0 is not a direct dependency from Cadastrapp, but is transitively required
because of Quartz Scheduler:

```
[INFO] +- org.quartz-scheduler:quartz:jar:2.3.0:compile
[INFO] |  +- com.mchange:c3p0:jar:0.9.5.2:compile
[INFO] |  +- com.mchange:mchange-commons-java:jar:0.2.11:compile
```

# JNDI configuration

Some environment variables are needed at runtime to correctly configure the JNDI datasource:

* `CADASTRAPP_JDBC_URL`, e.g. "jdbc:postgresql://external-db.georchestra-database:5432/cadastrapp"
* `CADASTRAPP_JDBC_USER`, e.g. "cadastrapp"
* `CADASTRAPP_JDBC_PASSWORD`, e.g. "cadastrapp"
