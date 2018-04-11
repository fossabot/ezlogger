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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * <p>
 * A customizable logging class suitable to your needs. What can be configured
 * right now :
 * </p>
 *
 * <table>
 * <tr>
 * <th>Parameter</th>
 * <th>Type</th>
 * <th>Default</th>
 * </tr>
 * <tr>
 * <td>name</td>
 * <td>String</td>
 * <td>""</td>
 * </tr>
 * <tr>
 * <td>header</td>
 * <td>String</td>
 * <td>""</td>
 * </tr>
 * <tr>
 * <td>OUT</td>
 * <td>PrintStream</td>
 * <td>System.out</td>
 * </tr>
 * <tr>
 * <td>ERR</td>
 * <td>PrintStream</td>
 * <td>System.err</td>
 * </tr>
 * <tr>
 * <td>PRINTDEBUG</td>
 * <td>boolean</td>
 * <td>false</td>
 * </tr>
 * <tr>
 * <td>PRINTNAME</td>
 * <td>boolean</td>
 * <td>true</td>
 * </tr>
 * <tr>
 * <td>PRINTHEADER</td>
 * <td>boolean</td>
 * <td>true</td>
 * </tr>
 * <tr>
 * <td>PRINTDATE</td>
 * <td>boolean</td>
 * <td>true</td>
 * </tr>
 * <tr>
 * <td>PRINTLINEDESCRIPTOR</td>
 * <td>boolean</td>
 * <td>true</td>
 * </tr>
 * <tr>
 * <td>PRINTCONSOLE</td>
 * <td>boolean</td>
 * <td>true</td>
 * </tr>
 * <tr>
 * <td>PRINTFILE</td>
 * <td>boolean</td>
 * <td>true</td>
 * </tr>
 * <tr>
 * <td>LOGFILE</td>
 * <td>File</td>
 * <td>"{rootdir}/logs/{name}_dd-MM-yyyy_HH-mm-ss.log"</td>
 * </tr>
 * </table>
 *
 * @author LimelioN/LimeiloN
 * @version 2.2
 * @see LogParams
 */
public class Logger {

	/**
	 * <p>
	 * An enum containing all the possible parameters. Use it like this :
	 * <pre>
	 * {@code
	 * 	HashMap<LogParams, Object> lp = new HashMap<LogParams, Object>();
	 * 	lp.put(LogParams.DEBUG, true);
	 * 	Logger log = new Logger("LoggerTest", "LoggerTest - v0.1", lp);
	 * }
	 * </pre>
	 * </p>
	 * 
	 * @see Logger
	 * @author LimelioN/LimeiloN
	 * @since 2.0
	 */
	public enum LogParams {
		PRINTLINEDESCRIPTOR, OUT, ERR, PRINTDEBUG, PRINTDATE, PRINTCONSOLE, PRINTFILE, PRINTHEADER, PRINTNAME, LOGFILE;
	}
	
	/**
	 * <p>All possible log level values</p>
	 * @author LimelioN/LimeiloN
	 * @since 2.2
	 * @see Logger#log(String, LogLevel)
	 */
	public enum LogLevel {
		INFO, DEBUG, ERR;
	}

	private PrintWriter pw;
	private PrintStream out = System.out;
	private PrintStream err = System.err;

	private String name = "";
	private String header = "";

	private final String baseLineDescriptor = "%s%s%s%s%n";
	private final String formatLineDescriptor = "%s%s%s";

	private String baseLogName;
	private String dateMarker;
	private String finalLogName;
	private File logfile;

	private boolean printDate = true;
	private boolean printName = true;
	private boolean printDebug = false;
	private boolean printToConsole = true;
	private boolean printToFile = true;
	private boolean printLineDescriptor = true;

	private Date d;
	private SimpleDateFormat sdf;

	/**
	 * <p>
	 * Cleaner way to instantiate Logger with a HashMap containing parameters
	 * </p>
	 * <p>
	 * If 'params' parameter is null, the default parameters are used, see
	 * {@link Logger}
	 * </p>
	 * 
	 * @param name
	 *            String
	 * @param header
	 *            String - a header to show at start
	 * @param params
	 *            HashMap&lt;LogParams, Object&gt; containing parameters values
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
		boolean printName_ = true;
		File logfile_ = null;

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
			case LOGFILE:
				logfile_ = (File) params.get(key);
				break;
			case PRINTNAME:
				printName_ = (boolean) params.get(key);
				break;
			default:
				System.err.println(
						"EzLogger : Unrecognized key or unimplemented ! Logger.java @ Logger::Logger(HashMap<Keys, Object> params)");
				break;
			}
		}
		return new Logger(name, header, psout_, pserr_, printDebug_, printDate_, printConsole_, printFile_,
				printLineDescriptor_, printHeader_, printName_, logfile_);
	}

	/**
	 * Create a new logger with some default values
	 * @param name String
	 * @param header String
	 * @param out PrintStream
	 * @param err PrintStream
	 *
	 * @since 1.0
	 * @see Logger
	 */
	public Logger(String name, String header, PrintStream out, PrintStream err) {
		this(name, header, out, err, false, true, true, true, true, true, true, null);
	}

	/**
	 * Create a new logger with some default values
	 * @param name String
	 * @param header String
	 *
	 * @since 1.0
	 * @see Logger
	 */
	public Logger(String name, String header) {
		this(name, header, System.out, System.err);
	}

	/**
	 * @param name String
	 * @param header String
	 * @param out PrintStream
	 * @param err PrintStream
	 * @param printDebug boolean
	 * @param printDate boolean
	 * @param printToConsole boolean
	 * @param printToFile boolean
	 * @param printLineDescriptor boolean
	 * @param printHeader boolean
	 * @param printName boolean
	 * @param logfile File
	 * 
	 * @since 2.1
	 * @see Logger
	 */
	public Logger(String name, String header, PrintStream out, PrintStream err, boolean printDebug, boolean printDate,
			boolean printToConsole, boolean printToFile, boolean printLineDescriptor, boolean printHeader,
			boolean printName, File logfile) {
		this.printDebug = printDebug;
		this.name = name;
		this.out = out;
		this.err = err;
		this.printDate = printDate;
		this.header = header;
		this.printToConsole = printToConsole;
		this.printToFile = printToFile;
		this.printLineDescriptor = printLineDescriptor;
		this.logfile = logfile;
		this.printName = printName;

		if (!printLineDescriptor) {
			this.printDate = false;
		}

		if (printToFile) {
			if (logfile == null) {
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
			} else {
				System.out.println(logfile.getPath());
				try {
					new File(logfile.getParent()).mkdirs();
					logfile.createNewFile();
					pw = new PrintWriter(logfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (printHeader) {
				pw.println(this.header);
			}
		}

		if (printToConsole) {
			if (printHeader) {
				out.println(this.header);
			}
		}
	}
	
	/**
	 * Print <tt>text</tt> with given {@link LogLevel} <tt>level</tt>
	 * @param text String, the text to print
	 * @param level LogLevel, the level to use
	 * @since 2.2
	 * @see LogLevel
	 */
	public void log(String text, LogLevel level) {
		switch (level) {
		case INFO:
			info(text);
			break;
		case DEBUG:
			debug(text);
			break;
		case ERR:
			err(text);
			break;
		default:
			return;
		}
	}
	
	/**
	 * Print <tt>text</tt> with given {@link LogLevel} <tt>level</tt> and format with <tt>args</tt>
	 * @param text String, the text to print
	 * @param level LogLevel, the level to use
	 * @param args Object..., arguments to parse possible placeholders
	 * @since 2.2
	 * @see LogLevel
	 */
	public void log(String text, LogLevel level, Object... args) {
		if (args == null) {
			log(text, level);
			return;
		}
		switch (level) {
		case INFO:
			info(text, args);
			break;
		case DEBUG:
			debug(text, args);
			break;
		case ERR:
			err(text, args);
			break;
		default:
			return;
		}
	}
	
	/**
	 * Print <tt>info</tt> with <tt>LogLevel.INFO</tt>
	 * 
	 * @param info String, the text to print
	 * @since 1.0
	 * @see Logger#out
	 * @see LogLevel
	 */
	public void info(String info) {
		if (printToConsole) {
			out.printf(baseLineDescriptor, getPrintedName(), getPrintedDate(), getPrintedLevel(LogLevel.INFO), info);
			out.flush();
		}
		if (printToFile) {
			pw.printf(baseLineDescriptor, getPrintedName(), getPrintedDate(), getPrintedLevel(LogLevel.INFO), info);
			pw.flush();
		}
	}
	
	/**
	 * Print <tt>info</tt> with <tt>LogLevel.INFO</tt> and parse possible placeholders with <tt>args</tt>
	 * 
	 * @param info String, the text to print
	 * @param args Object..., arguments to parse possible placeholders
	 * @since 2.2
	 * @see Logger#out
	 * @see LogLevel
	 */
	public void info(String info, Object... args) {
		if (printToConsole) {
			out.printf(formatLineDescriptor + info + "%n", formatArgs(LogLevel.INFO, args));
			out.flush();
		}
		if (printToFile) {
			pw.printf(formatLineDescriptor + info + "%n", formatArgs(LogLevel.INFO, args));
			pw.flush();
		}
	}
	

	/**
	 * Print <tt>error</tt> with <tt>LogLevel.ERR</tt>
	 * 
	 * @param error String, the text to print
	 * @since 1.0
	 * @see Logger#err
	 * @see LogLevel
	 */
	public void err(String error) {
		if (printToConsole) {
			err.printf(baseLineDescriptor, getPrintedName(), getPrintedDate(), getPrintedLevel(LogLevel.ERR), error);
			err.flush();
		}
		if (printToFile) {
			pw.printf(baseLineDescriptor, getPrintedName(), getPrintedDate(), getPrintedLevel(LogLevel.ERR), error);
			pw.flush();
		}
	}
	
	/**
	 * Print <tt>error</tt> with <tt>LogLevel.ERR</tt> and parse possible placeholders with <tt>args</tt>
	 * 
	 * @param error String, the text to print
	 * @param args Object..., arguments to parse possible placeholders
	 * @since 2.2
	 * @see Logger#err
	 * @see LogLevel
	 */
	public void err(String error, Object... args) {
		if (args == null) {
			err(error);
			return;
		}
		if (printToConsole) {
			err.printf(formatLineDescriptor + error + "%n", formatArgs(LogLevel.ERR, args));
			err.flush();
		}
		if (printToFile) {
			pw.printf(formatLineDescriptor + error + "%n", formatArgs(LogLevel.ERR, args));
			pw.flush();
		}
	}

	/**
	 * Print <tt>debug</tt> with <tt>LogLevel.DEBUG</tt>
	 * 
	 * @param debug String, the text to print
	 * @since 1.0
	 * @see Logger#out
	 * @see LogLevel
	 */
	public void debug(String debug) {
		if (printDebug) {
			if (printToConsole) {
				out.printf(baseLineDescriptor, getPrintedName(), getPrintedDate(), getPrintedLevel(LogLevel.DEBUG), debug);
				out.flush();
			}
			if (printToFile) {
				pw.printf(baseLineDescriptor, getPrintedName(), getPrintedDate(), getPrintedLevel(LogLevel.DEBUG), debug);
				pw.flush();
			}
		}
	}
	
	/**
	 * Print <tt>debug</tt> with <tt>LogLevel.DEBUG</tt> and parse possible placeholders with <tt>args</tt>
	 * 
	 * @param debug String, the text to print
	 * @param args Object..., arguments to parse possible placeholders
	 * @since 2.2
	 * @see Logger#out
	 * @see LogLevel
	 */
	public void debug(String debug, Object... args) {
		if (args == null) {
			debug(debug);
			return;
		}
		if (printDebug) {
			if (printToConsole) {
				out.printf(formatLineDescriptor + debug + "%n", formatArgs(LogLevel.DEBUG, args));
				out.flush();
			}
			if (printToFile) {
				pw.printf(formatLineDescriptor + debug + "%n", formatArgs(LogLevel.DEBUG, args));
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
			if (printDate)
				return "- " + getFDate() + " ";
			else
				return "";
		} else
			return "";
	}

	private String getPrintedLevel(LogLevel level) {
		if (printLineDescriptor)
			switch (level) {
			case INFO:
				return ": ";
			case DEBUG:
				return "[Debug]: ";
			case ERR:
				return "[Error]: ";
			default:
				return ": ";
			}
		else
			return ": ";
	}

	private String getPrintedName() {
		if (printLineDescriptor && printName)
			return "[" + name + "] ";
		return "";
	}
	
	private Object[] formatArgs(LogLevel level, Object[] args) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(getPrintedName());
		list.add(getPrintedDate());
		list.add(getPrintedLevel(level));
		for (Object arg : args) {
			list.add(arg);
		}
		return list.toArray(new Object[list.size()]);
	}

	/**
	 * Show or hide debug messages.
	 * 
	 * @param printDebug
	 * @see Logger#printDebug
	 */
	public void setDebug(boolean printDebug) {
		this.printDebug = printDebug;
	}
	
	/**
	 * 
	 * @return the file where this logger object writes or null if printToFile has been set to false
	 * @since 2.2
	 */
	public File getLogFile() {
		return printToFile ? logfile : null;
	}

	/**
	 * Close this Logger instance. Will throw some exceptions if you
	 * use the closed object.
	 * 
	 * @since 1.0
	 * @see Logger
	 */
	public void close() {
		pw.close();
		pw = null;
		out.close();
		out = null;
	}
}
