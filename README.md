### ezLogger
Java library, provide an easy logging interface.

#### What ?
A customizable logging class suitable to your needs.
What can be configured right now :
 - header
 - name (in brackets : [name])
 - do print datetime ?
 - print header ?
 - log level (info, debug, err)
 - do print debug messages ?
 - do print to console ?
 - do print to file ?
 - do print line descriptor ?

Coming soon :
 - log location

#### Get Started
Create a logger object in one line :
```java
/* Base constructors
 Logger(String name, String header)
  || (name, header, System.out, System.err)
  \/
 Logger(String name, String header, PrintStream out, PrintStream err)
  || (name, header, out, err, false, true, true, true, true, true)
  \/
 Logger(String name, String header, PrintStream out, PrintStream err, boolean printDebug, boolean printDate, boolean printToConsole, boolean printToFile, boolean printLineDescriptor, boolean printHeader)
*/

private static Logger log = new Logger("LoggerTest", "LoggerTest - v0.1");

public static void main(String[] args) {
	log.info("Will be printed");
	log.debug("Will not be printed");
}
```

As you can see, it's dirty / messy and this initialization should not be used in big projects. On the other hand, it is very useful for a fast instantiation of the class. The prefered way is :
```java
/** Better initialisation
* @since 2.0
*/
Logger log;

public static void main(String[] args) {
	HashMap<LogParams, Object> logparams = new HashMap<LogParams, Object>();
	logparams.put(LogParams.DEBUG, true);
	
	log = new Logger("LoggerTest", "LoggerTest - v0.1", logparams);
	
	log.info("Will be printed");
	log.debug("Will also be printed");
}
```