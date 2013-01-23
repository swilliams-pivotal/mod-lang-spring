# vertx-lang-spring

Spring language support extension for vert.x.

[![Build Status](https://travis-ci.org/vert-x/vertx-junit-annotations.png?branch=master)](https://travis-ci.org/swilliams-vmw/vertx-lang-spring)


### This implementation will only work with a build of vert.x that is newer than 1.3.1.final

vert.x 1.3.1.final will not support this language impl.

The current master contains a fix to permit prefix's in module or verticle main class/script names.


## HowTo use vertx-lang-spring


1. Clone the repo and build the distribution package (./gradlew dist).  You'll find it in: build/distributions. Unzip it in $VERTX_HOME/lib.

2. Add the following to vertx/conf/langs.properties:

    spring=org.vertx.java.deploy.impl.spring.SpringVerticleFactory

3. Using the prefix 'spring:', specify a Spring configuration file in your verticle or module's mod.json:

    {
      "main": "spring:mySpringConfig.xml"
    }

4. Alternatively, if you provide cglib in your application, you can specify an annotated @Configuration class in your verticle or module's mod.json:
    {
      "main": "spring:my.example.ConfigClass"
    }


