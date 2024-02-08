# Quick reference

-    **Maintained by**:  
      [georchestra.org](https://www.georchestra.org/)

-    **Where to get help**:  
     the [geOrchestra Github repo](https://github.com/georchestra/georchestra), [IRC chat](https://kiwiirc.com/nextclient/irc.libera.chat/georchestra), Stack Overflow

# Featured tags

- `latest`

# Quick reference

-	**Where to file issues**:  
     [https://github.com/georchestra/georchestra/issues](https://github.com/georchestra/georchestra/issues)

-	**Supported architectures**:   
     [`amd64`](https://hub.docker.com/r/amd64/docker/)

-	**Source of this description**:  
     [docs repo's directory](https://github.com/georchestra/cadastrapp/blob/master/DOCKER_HUB.md)

# What is `georchestra/cadastrapp`

**Cadastrapp** is the tool for consulting cadastral data provided by the French Directorate General of Public Finances (DGFiP) for geOrchestra 

# How to use this image

As for every other geOrchestra webapp, its configuration resides in the data directory ([datadir](https://github.com/georchestra/datadir)), typically something like /etc/georchestra, where it expects to find a cadstrapp/ sub-directory.

It is recommended to use the official docker composition: https://github.com/georchestra/docker.

Example : 
```
  cadastrapp:
    image: georchestra/cadastrapp:latest
    environment:
      XMX: 512m
      JAVA_OPTIONS: -Duser.language=fr -Duser.country=FR
      CADASTRAPP_JDBC_URL: jdbc:postgresql://database:5432/cadastrapp
      CADASTRAPP_JDBC_USER: cadastrapp
      CADASTRAPP_JDBC_PASSWORD: cadastrapp
```

## Where is it built

This image is built using maven : `mvn package docker:build -pl cadastrapp -Pdocker` in the repo https://github.com/georchestra/cadastrapp/.

# License

View [license information](https://www.georchestra.org/software.html) for the software contained in this image.

As with all Docker images, these likely also contain other software which may be under other licenses (such as Bash, etc from the base distribution, along with any direct or indirect dependencies of the primary software being contained).

[//]: # (Some additional license information which was able to be auto-detected might be found in [the `repo-info` repository's georchestra/ directory]&#40;&#41;.)

As for any docker image, it is the user's responsibility to ensure that usages of this image comply with any relevant licenses for all software contained within.