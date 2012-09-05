# vertx-lang-spring

Spring language support extension for vert.x.

## Download 

A download is available for testing: [vertx-lang-spring-1.0-SNAPSHOT.zip](https://github.com/swilliams-vmw/vertx-lang-spring/downloads "vertx-lang-spring-1.0-SNAPSHOT.zip — vert.x language support module for Spring applications")

## HowTo use vertx-lang-spring

1. Download the current package and unzip it in $VERTX_HOME/lib.

2. Using the prefix 'spring:', specify a Spring configuration file in your verticle or module's mod.json:

    {
      "main": "spring:mySpringConfig.xml"
    }

3. Alternatively, if you provide cglib in your application, you can specify an annotated @Configuration class in your verticle or module's mod.json:
    {
      "main": "spring:my.example.ConfigClass"
    }


