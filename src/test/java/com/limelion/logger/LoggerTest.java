package com.limelion.logger;

public class LoggerTest {

    public static void main(String[] args) {

        long startTime = System.nanoTime();

        Logger log = Logger.builder().name("EzLoggerTest").header("EzLoggerTest v4.0.0%n© LimeiloN 2018").doPrintToFile(false).build();

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

        log.log("test" + i, LogLevel.INFO);
        i++;
        log.log("test" + i, LogLevel.ERR);
        i++;
        log.log("test" + i, LogLevel.DEBUG);
        i++;

        log.log("test%d", LogLevel.INFO, i);
        i++;
        log.log("test%d", LogLevel.ERR, i);
        i++;
        log.log("test%d", LogLevel.DEBUG, i);
        i++;

        System.out.println("Elapsed time : " + String.valueOf((System.nanoTime() - startTime) / 1000) + " µs");
    }
}
