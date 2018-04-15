package com.limelion.logger;

import java.io.File;
import java.util.HashMap;

import com.limelion.logger.Logger.LogParams;

public class LoggerTest {

	public static void main(String[] args) {
		
		HashMap<LogParams, Object> logparams = new HashMap<LogParams, Object>();
		logparams.put(LogParams.PRINTFILE, false);
		logparams.put(LogParams.PRINTDEBUG, true);
		logparams.put(LogParams.LOGFILE, new File("data\\bruh.txt"));
		
		Logger log = Logger.newLogger("LoggerTest", "This header will be printed !", logparams);
		log.info("test");
		log.debug("test");
		
		log.debug("Sample text : %s", "coucou");
	}
}
