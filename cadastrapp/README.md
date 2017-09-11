Cadastrapp - WebApplication 
=========================== 

This is a simple maven project, mvn clean install will build a war to be deployed in tomcat6.

This application is made to be installed behind GeOrchestra security proxy. To install georchestra please check :  https://github.com/georchestra/georchestra/


##  Setup middleware (tomcat6, apache) :
	- once georchestra is up and working you have to choise:
		1 - Create a new instance of tomcat6 for this additional webapplication
		2 - Add this application in existing tomcat6 instance ( for exemple tomcat-georchestra)
	- where are explaining here case 1 : 
	
	###  Create instance
	-- As for gerochestra tomcat instance, you will have to  create a new instance
		(choose a port not already use here we choose 8480 for http endpoint and 8405 to stop
	```
		sudo tomcat6-instance-create -p 8480 -c 8405 /var/lib/tomcat-cadastrapp
	```
	###  Init policy and service
		sudo mkdir /var/lib/tomcat-cadastrapp/conf/policy.d
		sudo touch /var/lib/tomcat-cadastrapp/conf/policy.d/empty.policy
		sudo chown -R tomcat6:tomcat6 /var/lib/tomcat-cadastrapp
		sudo cp /etc/init.d/tomcat6 /etc/init.d/tomcat-cadastrapp
		sudo cp /etc/default/tomcat6 /etc/default/tomcat-cadastrapp

Finally, edit the ```/etc/init.d/tomcat-cadastrapp``` script, find the following line:
```
# Provides:          tomcat6
```
... and replace it with:
```
# Provides:          tomcat-cadastrapp
```

### Customize Java options

In ```/etc/default/tomcat-cadastrapp```, add or replace : 
```
JAVA_OPTS="-Djava.awt.headless=true -XX:+UseConcMarkSweepGC"
```

And later add these lines (change the ```STOREPASSWORD``` string):
```
JAVA_OPTS="$JAVA_OPTS \
              -Xms1024m \
              -Xmx1024m \
              -XX:MaxPermSize=256m"

JAVA_OPTS="$JAVA_OPTS \
              -Djavax.net.ssl.trustStore=/etc/tomcat6/keystore \
              -Djavax.net.ssl.trustStorePassword=STOREPASSWORD"
```
### Configure connectors 

In ```/var/lib/tomcat-cadastrapp/conf/server.xml```, find the place where the HTTP connector is defined, and change it into:
```
    <Connector port="8480" protocol="HTTP/1.1" 
               connectionTimeout="20000" 
               URIEncoding="UTF-8"
               redirectPort="8443" />
```

#### Add datasource
In the same file (server.xml) add a new Resource at the end of ```<GlobalNamingResources>``` part
```
<Resource name="jdbc/cadastrapp" auth="Container" type="javax.sql.DataSource">
          username="username"
          password="password"
          driverClassName="org.postgresql.Driver"
          url="jdbc:postgresql://databaseHost:databasePort/databaseName"
          maxActive="20"
          maxIdle="10"
          validationQuery="select 1" />
```

In ```/var/lib/tomcat-cadastrapp/conf/context.xml```, add a new ResourceLink at the end of ```<Context>``` part :

```
<ResourceLink name="jdbc/cadastrapp"
   global="jdbc/cadastrapp"
   type="javax.sql.DataSource"/>
```

#### Add jar require for this datasource

To be able to connect to postgresql database, tomcat will have to get postgresql client librairie in his classloader.
In tomcat6 instance, you will have to add ```postgresql-9.1-901-1.jdbc4.jar``` to ```/var/lib/tomcat-cadastrapp/lib``` folder (with a symlink)

### Start the instance

```
sudo insserv tomcat-cadastrapp
sudo service tomcat-cadastrapp start
```

### Add proxy-cas information

Now configure proxy-cas to point to cadastrapp , add the following in ```/var/lib/tomcat-proxycas/webapps/ROOT/WEB-INF/proxy-servlet.xml```


```
 <entry key="cadastrapp"    value="http://localhost:8480/cadastrapp/" />
```

Then restart proxy-cas instance

```
sudo service tomcat-proxycas restart
```

##  Configure application : 

Only two files can be configure :

The spring configuration file

```src/main/webapp/WEB-INF/beans.xml``` 

The application database configuration file :

```src/main/resources/cadastrapp.properties```
In this file replace schema.name depending on server script installation option
for exemple : ```schema.name=cadastreapp```

##  Build application : 

Note that you'll need ```virtualenv``` to build/minify the addon javascript - on debian derivatives,
it's provided by the ```python-virtualenv``` package.

```
mvn clean install
```

##  Build a debian package :

You'll need ```fakeroot``` installed for this task. To ease deployment and use the georchestra datadir,
you can also build and deploy a debian package :

```
mvn clean package deb:package -pl cadastrapp -PdebianPackage
```

This will produce a .deb file under ```cadastrapp/target/```

##  Deploy application : 

Copy war build previously in tomcat-cadastrapp webapps folder

```
sudo cp  ./target/cadastrapp-0.0.1-SNAPSHOT.war  /var/lib/tomcat-cadastre/webapps/cadastrapp.war
```

### Restart the instance

```
sudo service tomcat-cadastrapp restart
```

