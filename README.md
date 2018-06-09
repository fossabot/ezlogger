### ezLogger
An highly customizable java logging library

#### Get Started
Create a logger object in one line :
```java

private static Logger log = Logger.getBuilder().name("Example").doPrintDebug(false).build();

public static void main(String[] args) {
	log.info("Will be printed");
	log.debug("Will not be printed");
}
```

#### Documentation
Javadoc is available online [here](https://limeilon.github.io/ezlogger/javadoc) (latest library version) or can be downloaded [here](https://github.com/LimeiloN/ezlogger/releases).
