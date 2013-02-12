# mod-lang-spring

Language support implementation for Spring Framework applications on vert.x 2.0.

[![Build Status](https://travis-ci.org/vert-x/vertx-junit-annotations.png?branch=master)](https://travis-ci.org/swilliams-vmw/vertx-lang-spring)



## HowTo use vertx-lang-spring

### Module Configuration

1. Clone the repo and build the distribution package (`./gradlew dist`).  You'll find it in: build/distributions. Unzip it in `$VERTX_HOME/lib`.

2. Using the prefix 'spring:', specify a Spring configuration file in your verticle or module's `mod.json`:

    {
      "main": "spring:mySpringConfig.xml",
      "worker": true
    }


3. Alternatively, if you provide cglib in your application, you can specify an annotated @Configuration class in your verticle or module's `mod.json`:

    {
      "main": "spring:my.example.ConfigClass",
      "worker": true
    }


### Application Components

In a bean annotated with `@Component`, created in an `@Configuration` class, or declared in an Spring Framework XML configuration file, you can annotate a method with the `@EventBusHandler` annotation.  The application context will automatically register a vert.x event bus Handler that delegates to this method, on the address specified in the annotation.

If the method returns a value, then it is sent as a reply to the sender of the event.

    @Component
    public class ExampleReplyingEventBusHandler {

      @EventBusHandler("some.address.to.listen")
      public Object anyMethod(Object payload) {
        return payload;
      }
    }

TODO: Return a Callable and do a conversion to a Handler?


