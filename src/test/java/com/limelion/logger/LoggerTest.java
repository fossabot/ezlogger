package com.limelion.logger;

import java.util.HashMap;

import com.limelion.logger.Logger.LogParams;

public class LoggerTest {

	public static void main(String[] args) {
		
		HashMap<LogParams, Object> logparams = new HashMap<LogParams, Object>();
		logparams.put(LogParams.PRINTFILE, false);
		logparams.put(LogParams.PRINTDEBUG, true);
		
		Logger log = Logger.newLogger("LoggerTest", "This header will be printed !", logparams);
		log.info("test");
		log.debug("test");
	}
}
