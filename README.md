# Openshiftdemo


## Build & Deploy

This project requires **JDK 11** to build and run the application. It also requires **cf** CLI if you want to deploy to PCF. You can visit [Dev Enablement's OnBoarding](http://x.ford.com/dev-onboard) page for links to download these resources or sign-up for a free-trial PCF foundation.

>If your workstation is not already setup to develop with Java 11, then you can refer to [Java 11 Workstation Setup](https://github.ford.com/DevEnablement/pcfdev-guides/blob/master/migrations/Java-11.md#workstation-setup).


#### CLI

To build, run `./gradlew build`

To start and run the application, run `./gradlew bootRun`. Your local running app is served on *http://localhost:8080/...*. Press `^C` (control-C) to stop your app.

To deploy your built application:

```bash
# login into your PCF space
# cf login -a https://[foundation-api-host] ...

# generate manifest based on 'dev-edc1' settings
./gradlew -PcfManifestTarget=dev-edc1 cfManifest

# push generated manifest
cf push -f manifest-generated.yml
```

#### IDE

Your IDE version must support Java 11 &mdash; i.e. Eclipse (>= 2018-09), IntelliJ (>= 2018.2).

**Your must install and enable use of Lombok for your IDE.** Lombok setup instructions can be found [here](https://github.ford.com/DevEnablement/pcfdev-guides/blob/master/base-service/README.md#lombok).

<br/>

## EcoBoost Project Features
This [**EcoBoost Project**](http://x.ford.com/spring-ecoboost) was originally generated with the following features:

- [**Core Features**](https://github.ford.com/DevEnablement/pcfdev-guides/blob/master/base-service/README.md#core-features) &mdash; 
  Spring Boot 2
, Gradle 5
, Java 11
, [Gradle Boost Plugin](https://github.ford.com/DevEnablement/gradle-boost-plugin)
, [Lombok](https://projectlombok.org/)
, Firewall Friendly

- [**Recommended Web Features**](https://github.ford.com/DevEnablement/pcfdev-guides/blob/master/base-service/README.md#recommended-web-features) &mdash; 
Swagger
, Actuator
, Sleuth
, X-Application-Info
, X-Request-Info
, Common Error Handling

- Other Features & Reference Code &mdash; 
[Simple REST Controller](https://github.ford.com/DevEnablement/pcfdev-guides/tree/master/rest-controller)
, [PCF Dev Jenkins Pipeline](https://github.ford.com/DevEnablement/pcfdev-guides/tree/master/pipeline-jenkins)
