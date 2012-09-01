# vertx-lang-spring

Spring language support extension for vert.x.

## HowTo use vertx-lang-spring

1. Using the prefix 'spring:', specify a Spring configuration file in your verticle or module's mod.json:

    {
      "main": "spring:mySpringConfig.xml"
    }

2. Alternatively, if you provide cglib in your application, you can specify an annotated @Configuration class in your verticle or module's mod.json:
    {
      "main": "spring:my.example.ConfigClass"
    }


