# mod-lang-spring

Language support implementation for Spring Framework applications on vert.x 2.0.

[![Build Status](https://travis-ci.org/vert-x/vertx-junit-annotations.png?branch=master)](https://travis-ci.org/swilliams-vmw/vertx-lang-spring)



## HowTo use vertx-lang-spring


1. Clone the repo and build the distribution package (`./gradlew dist`).  You'll find it in: build/distributions. Unzip it in `$VERTX_HOME/lib`.


2. Add the following to `vertx/conf/langs.properties`:

    spring=org.vertx.java.deploy.impl.spring.SpringVerticleFactory


3. Using the prefix 'spring:', specify a Spring configuration file in your verticle or module's `mod.json`:

    {
      "main": "spring:mySpringConfig.xml"
    }


4. Alternatively, if you provide cglib in your application, you can specify an annotated @Configuration class in your verticle or module's `mod.json`:

    {
      "main": "spring:my.example.ConfigClass"
    }


