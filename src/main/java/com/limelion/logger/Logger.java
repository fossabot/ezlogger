/*******************************************************************************
 * Copyright (C) 2018 LimelioN
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.limelion.logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * <p>A customizable logging class suitable to your needs.
			What can be configured right now :</p>
		<ul>
			<li>header</li>
			<li>name (in brackets : [name])</li>
			<li>do print datetime ?</li>
			<li>print header ?</li>
			<li>log level (info, debug, err)</li>
			<li>do print debug messages ?</li>
			<li>do print to console ?</li>
			<li>do print to file ?</li>
			<li>do print line descriptor ?</li>
		</ul>
 * <p>
 * Available contructors :</p>
 * 	<ul>
 * 		<li><code>Logger(String name, String header)</code></li>
 * 		<li><code>Logger(String name, String header, PrintStream out, PrintStream err)</code></li>
 * 		<li><code>Logger(String name, String header, PrintStream out, PrintStream err, boolean printDebug, boolean printDate, boolean printToConsole, boolean printToFile, boolean printLineDescriptor, boolean printHeader)</code></li>
 * 		<li><code>Logger.newLogger(String name, String header, HashMap&lt;LogParams, Object&gt; params)</code></li>
 * 	</ul>
 * 
 * @author LimelioN/LimeiloN
 * @version 2.0
 * @see LogParams
 */
public class Logger {

	/**
	 * <p>An enum containing all the possible parameters.
	 * Use it like this :
	 * <code>
	 * HashMap&lt;LogParams, Object&gt; lp = new HashMap&lt;LogParams, Object&gt;();
	 * lp.put(LogParams.DEBUG, true);
	 * Logger log = new Logger("LoggerTest", "LoggerTest - v0.1", lp);
	 * </code>
	 * </p>
	 * @see Logger
	 * @author LimelioN/LimeiloN
	 * @since 2.0
	 */
	public enum LogParams {
		PRINTLINEDESCRIPTOR,
		OUT, 
		ERR, 
		PRINTDEBUG, 
		PRINTDATE, 
		PRINTCONSOLE, 
		PRINTFILE,
		PRINTHEADER;
	}

	private PrintWriter pw;
	private PrintStream out = System.out;
	private PrintStream err = System.err;
	
	private String name = "";
	private String header = "";
	
	private final String baseLineDescriptor = "%s%s%s%s%n";
	
	private String baseLogName;
	private String dateMarker;
	private String finalLogName;
	
	private boolean printDate = true;
	private boolean printDebug = false;
	private boolean printToConsole = true;
	private boolean printToFile = true;
	private boolean printLineDescriptor = true;

	private Date d;
	private SimpleDateFormat sdf;
	/**
	 * Cleaner way to instantiate Logger with a HashMap containing parameters
	 * @param name String
	 * @param header String - a header to show at start
	 * @param params HashMap&lt;LogParams, Object&gt; containing parameters values
	 * @return a new Logger instance
	 * @see Logger
	 * @see LogParams
	 * @since 2.0
	 */
	public static Logger newLogger(String name, String header, HashMap<LogParams, Object> params) {
		PrintStream psout_ = System.out;
		PrintStream pserr_ = System.err;
		boolean printDebug_ = false;
		boolean printDate_ = true;
		boolean printConsole_ = true;
		boolean printFile_ = true;
		boolean printHeader_ = true;
		boolean printLineDescriptor_ = true;
		
		for (LogParams key : params.keySet()) {
			switch (key) {
			case OUT:
				psout_ = (PrintStream) params.get(key);
				break;
			case ERR:
				pserr_ = (PrintStream) params.get(key);
				break;
			case PRINTDEBUG:
				printDebug_ = (boolean) params.get(key);
				break;
			case PRINTDATE:
				printDate_ = (boolean) params.get(key);
				break;
			case PRINTCONSOLE:
				printConsole_ = (boolean) params.get(key);
				break;
			case PRINTFILE:
				printFile_ = (boolean) params.get(key);
				break;
			case PRINTLINEDESCRIPTOR:
				printLineDescriptor_ = (boolean) params.get(key);
				break;
			case PRINTHEADER:
				printHeader_ = (boolean) params.get(key);
				break;
			default:
				System.err.println(
						"EzLogger : Unrecognized key or unimplemented ! Logger.java @ Logger::Logger(HashMap<Keys, Object> params)");
				break;
			}
		}
		return new Logger(name, header, psout_, pserr_, printDebug_, printDate_, printConsole_, printFile_, printLineDescriptor_, printHeader_);
	}
	
	/**
	 * 
	 * @param name
	 * @param header
	 * @param out
	 * @param err
	 * @param printDebug
	 * @param printDate
	 * @param printToConsole
	 * @param printToFile
	 * @param printLineDescriptor
	 * @param printHeader
	 * @since 1.0
	 * @see Logger
	 */
	public Logger(String name, String header, PrintStream out, PrintStream err, boolean printDebug, boolean printDate,
			boolean printToConsole, boolean printToFile, boolean printLineDescriptor, boolean printHeader) {

		this.printDebug = printDebug;
		this.name = name;
		this.out = out;
		this.err = err;
		this.printDate = printDate;
		this.header = header;
		this.printToConsole = printToConsole;
		this.printToFile = printToFile;
		this.printLineDescriptor = printLineDescriptor;
		
		if (!printLineDescriptor) {
			this.printDate = false;
		}

		if (printToFile) {
			baseLogName = "logs\\" + name;
			File dir = new File("logs");
			dir.mkdir();

			sdf = new SimpleDateFormat("_dd-MM-yyyy_HH-mm-ss");
			d = new Date();
			dateMarker = sdf.format(d);
			finalLogName = baseLogName + dateMarker + ".log";

			File log = new File(finalLogName);

			System.out.println(finalLogName);

			try {
				log.createNewFile();
				pw = new PrintWriter(log);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (printHeader)
			pw.printf(this.header + "%n");
		}

		if (printToConsole) {
			if (printHeader)
			out.printf(this.header + "%n");
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param header
	 * @param out
	 * @param err
	 * 
	 * @since 1.0
	 * @see Logger
	 */
	public Logger(String name, String header, PrintStream out, PrintStream err) {
		this(name, header, out, err, false, true, true, true, true, true);
	}

	/**
	 * 
	 * @param name
	 * @param header
	 * 
	 * @since 1.0
	 * @see Logger
	 */
	public Logger(String name, String header) {
		this(name, header, System.out, System.err);
	}

	/**
	 * Print given message to out PrintStream specified at initialization
	 * @param info
	 * @see Logger#out
	 */
	public void info(String info) {
		if (printToConsole) {
			out.printf(baseLineDescriptor, getPrintedName(), getPrintedDate(), getPrintedLevel(""), info);
			out.flush();
		}
		if (printToFile) {
			pw.printf(baseLineDescriptor, getPrintedName(), getPrintedDate(), getPrintedLevel(""), info);
			pw.flush();
		}
	}

	/**
	 * Print given message to err PrintStream specified at initialization, preceded by [Error]
	 * @param error
	 * @see Logger#err
	 */
	public void err(String error) {
		if (printToConsole) {
			err.printf(baseLineDescriptor, getPrintedName(), getPrintedDate(), getPrintedLevel("[Error]"), error);
			err.flush();
		}
		if (printToFile) {
			pw.printf(baseLineDescriptor, getPrintedName(), getPrintedDate(), getPrintedLevel("[Error]"), error);
			pw.flush();
		}
	}

	/**
	 * Print given message to out PrintStream specified at initialzation, preceded by [Debug]. Only prints if printDebug has been set to true.
	 * @param debug
	 * @see Logger#out
	 * @see Logger#printDebug
	 */
	public void debug(String debug) {
		if (printDebug) {
			if (printToConsole) {
				out.printf(baseLineDescriptor, getPrintedName(), getPrintedDate(), getPrintedLevel("[Debug]"), debug);
				out.flush();
			}
			if (printToFile) {
				pw.printf(baseLineDescriptor, getPrintedName(), getPrintedDate(), getPrintedLevel("[Debug]"), debug);
				pw.flush();
			}
		}
	}

	private String getFDate() {
		d = new Date();
		sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(d);
	}
	
	private String getPrintedDate() {
		if (printLineDescriptor) {
			if (printDate) {
				return " - " + getFDate() + " ";
			} else {
				return "";
			}
		} else {
			return "";
		}
	}
	
	private String getPrintedLevel(String from) {
		if (printLineDescriptor) {
			return from + ": ";
		} else {
			return ": ";
		}
	}
	
	private String getPrintedName() {
		if (printLineDescriptor) {
			return "[" + name + "]";
		} else {
			return "";
		}
	}

	/**
	 * Use this method to show or hide debug messages.
	 * @param printDebug
	 * @see Logger#printDebug
	 */
	public void setDebug(boolean printDebug) {
		this.printDebug = printDebug;
	}

	/**
	 * Use this to close this Logger instance.
	 * Will throw some exceptions if you reuse the closed object.
	 */
	public void close() {
		pw.close();
		pw = null;
		out.close();
		out = null;
	}
}
