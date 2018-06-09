### ezLogger
Java library, provide an easy logging interface.

#### What ?
A customizable logging class suitable to your needs.
What can be configured right now :

Parameter|Type|Default|Side Note
---------|----|-------|---------
name|String|`""`|the text wich is printed in brackets at the beginning of each line
header|String|`""`|the lines wich are printed at the initialisation of the Logger object (first line)
out|PrintStream|`System.out`|where to print 'info' and 'debug' messages
err|PrintStream|`System.err`|where to print 'error' messages
printdebug|boolean|`false`|shall we print debug messages ?(may be changed at runtime)
printname|boolean|`true`|shall we print the name at the beginning of each line ?
printheader|boolean|`true`|shall we print the header ?
printdate|boolean|`true`|shall we print the time ?
printlinedescriptor|boolean|`true`|shall we print the entire line descriptor ? (include name, date and possible log level)
printconsole|boolean|`true`|shall we print messages to console ?
printfile|boolean|`true`|shall we print messages to log file ?
logfile|File|`logs/name_dd-MM-yyyy_HH-mm-ss.log`|if the above is true, specify a log file destination

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
 Logger(String name, String header, PrintStream out, PrintStream err, boolean doPrintDebug, boolean doPrintTime, boolean doPrintToConsole, boolean doPrintToFile, boolean doPrintLineHeading, boolean printHeader)
*/

private static Logger log = new Logger("LoggerTest", "LoggerTest - v0.1");

public static void main(String[] args) {
	log.info("Will be printed");
	log.debug("Will not be printed");
}
```

As you can see, it's dirty / messy and this initialization should not be used in big projects. On the other hand, it is very useful for a fast instantiation of the class with the default values. The prefered way is :
```java
/** Better initialisation
* @since 2.0
*/
Logger log;

public static void main(String[] args) {
    // LogParams go in this hashmap
	HashMap<LogParams, Object> logparams = new HashMap<LogParams, Object>();
    // Values you put in overwrite default values
	logparams.put(LogParams.DEBUG, true);
	
	log = new Logger("LoggerTest", "LoggerTest - v0.1", logparams);
	
	log.info("Will be printed");
	log.debug("Will also be printed");
}
```
