package com.limelion.logger;

public class LoggerTest {

	public static void main(String[] args) {

	    long startTime = System.nanoTime();

		Builder b = Logger.getBuilder().name("EzLoggerTest").header("EzLoggerTest v3.0%n© LimeiloN 2018").baseLogFileName("logs\\EzLoggerTest");
		Logger log = b.build();

		int i = 0;

		log.info("test" + i);
		i++;

		log.i("test" + i);
        i++;

        log.info("test%d", i);
        i++;

        log.i("test%d", i);
        i++;

        log.debug("test" + i);
        i++;

        log.d("test" + i);
        i++;

        log.debug("test%d", i);
        i++;

        log.d("test%d", i);
        i++;

        log.err("test" + i);
        i++;

        log.e("test" + i);
        i++;

        log.err("test%d", i);
        i++;

        log.e("test%d", i);
        i++;

        log.log("test" + i, Logger.LogLevel.INFO);
        i++;
        log.log("test" + i, Logger.LogLevel.ERR);
        i++;
        log.log("test" + i, Logger.LogLevel.DEBUG);
        i++;

        log.log("test%d", Logger.LogLevel.INFO, i);
        i++;
        log.log("test%d", Logger.LogLevel.ERR, i);
        i++;
        log.log("test%d", Logger.LogLevel.DEBUG, i);
        i++;

        System.out.println("Elapsed time :" + String.valueOf((System.nanoTime() - startTime) / 1000) + " µs");
	}
}
