---
layout: default
---
### Introduction
EzLogger is An highly customizable java logging library.

#### Documentation
Javadoc can be found [here](https://limeilon.github.io/ezlogger/javadoc/) online. A jar is also available [here]({{ site.github.releases_url }}).

#### Get Started
Create a logger object in one line :
```java

private static Logger log = Logger.getBuilder().name("Example").doPrintDebug(false).build();

public static void main(String[] args) {
	log.info("Will be printed");
	log.debug("Will not be printed");
}
```
