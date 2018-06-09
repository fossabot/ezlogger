/*******************************************************************************
 * Copyright (C) 2018 LimelioN/LimeiloN <memorial.limelion@gmail.com>
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
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A highly customizable logging utility.
 * See {@link Builder} for customization options and basic instantiation.
 *
 * @author LimelioN/LimeiloN &lt;memorial.limelion@gmail.com&gt;
 * @see Builder
 * @version 3.0
 */
public class Logger {

    /**
	 * All possible log levels
     *
	 * @author LimelioN/LimeiloN
	 * @since 2.2
	 * @see Logger#log(String, LogLevel)
	 * @see Logger#info(String, Object...)
	 * @see Logger#debug(String, Object...)
	 * @see Logger#err(String, Object...)
	 */
	public enum LogLevel {
		INFO, DEBUG, ERR;

		public String toString() {
		    return this.name();
        }

        public String format() {
		    switch (this) {
                case INFO:
                    return ": ";
                case DEBUG:
                    return "[Debug]: ";
                case ERR:
                    return "[Error]: ";
                default:
                    return ": ";
            }
        }
	}

	private String name;

	private final String lineHeading = "%s%s%s";

	private File logFile;
	private PrintWriter pw;

    private boolean doPrintLineHeading = true;
	private boolean doPrintTime = true;
	private boolean doPrintName = true;

	private boolean doPrintDebug = true;
    private boolean doPrintErr = true;
    private boolean doPrintInfo = true;

	private boolean doPrintToConsole = true;
	private boolean doPrintToFile = true;


	private SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

    /**
     * Initialize a new Logger using a Builder object.
     * Similar as calling {@link Builder#build()}.
     *
     * @param b the builder
     * @throws IOException
     */
	public Logger(Builder b) throws IOException {
	    this(b.name(),
                b.header(),
                b.doPrintLineHeading(),
                b.doPrintTime(),
                b.doPrintName(),
                b.doPrintDebug(),
                b.doPrintErr(),
                b.doPrintInfo(),
                b.doPrintToConsole(),
                b.doPrintToFile(),
                b.baseLogFileName());
    }

    /**
     * Create a new Logger by specifying all the parameters (very long, you may prefer to use the builder)
     *
     * @param name the text printed in brackets in the line heading
     * @param header the text printed at the start of the logger
     * @param doPrintLineHeading define if this logger should print the line heading
     * @param doPrintTime define if this logger should print out the current time in the line heading
     * @param doPrintName define if this logger should print out the name in the line heading
     * @param doPrintDebug define if this logger should print out debug messages
     * @param doPrintErr define if this logger should print out error messages
     * @param doPrintInfo define if this logger should print out info messages
     * @param doPrintToConsole define if this logger should print to console
     * @param doPrintToFile define if this logger should print to file
     * @param baseLogFileName name of the log file (may contains an extension - default to '.log')
     * @throws IOException
     * @since 1.0
     */
    public Logger(String name, String header, boolean doPrintLineHeading, boolean doPrintTime, boolean doPrintName, boolean doPrintDebug, boolean doPrintErr, boolean doPrintInfo, boolean doPrintToConsole, boolean doPrintToFile, String baseLogFileName) throws IOException {
	    this.name = name;
	    this.doPrintLineHeading = doPrintLineHeading;
	    this.doPrintTime = doPrintTime;
	    this.doPrintName = doPrintName;
	    this.doPrintDebug = doPrintDebug;
	    this.doPrintErr = doPrintErr;
	    this.doPrintInfo = doPrintInfo;
	    this.doPrintToConsole = doPrintToConsole;
	    this.doPrintToFile = doPrintToFile;

	    if (doPrintToFile) {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
            String base = baseLogFileName.contains(".") ? baseLogFileName.substring(0, baseLogFileName.lastIndexOf('.')) : baseLogFileName;
            String extension = baseLogFileName.contains(".") ? baseLogFileName.substring(baseLogFileName.lastIndexOf('.')) : ".log";
            this.logFile = new File(base + "_" + sdf.format(d) + extension);

            if (!logFile.getParentFile().exists())
                logFile.getParentFile().mkdirs();

            if (logFile.exists())
                logFile.delete();

            if (!logFile.createNewFile())
                throw new IOException("Cannot create log file !");

            pw = new PrintWriter(logFile);

            pw.printf(header != "" ? header + "%n" : "");
        }

        if (doPrintToConsole)
            System.out.printf(header != "" ? header + "%n" : "");
    }

    /**
     * Get a Builder instance to create and customize a new Logger easily.
     * @return a new Builder instance
     * @since 3.0
     */
    public static Builder getBuilder() {
	    return new Builder();
    }
	
	/**
	 * Print given message with given {@link LogLevel}.
	 * @param text String, the text to print
	 * @param level LogLevel, the level to use
	 * @since 2.2
	 * @see LogLevel
	 */
	public void log(String text, LogLevel level) {
		log(text, level, new Object[] {});
	}
	
	/**
	 * Print given message with given {@link LogLevel} and format with given args.
	 * @param text String, the text to print
	 * @param level LogLevel, the level to use
	 * @param args Object..., arguments to parse possible placeholders
	 * @since 2.2
	 * @see LogLevel
	 */
	public void log(String text, LogLevel level, Object... args) {
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
	 * Print given info message with {@code LogLevel.INFO}.
	 * 
	 * @param info String, the text to print
	 * @since 1.0
	 * @see LogLevel
	 */
	public void info(String info) {
		info(info, new Object[] {});
	}
	
	/**
	 * Print a textual representation of given object with {@code LogLevel.INFO}.
	 * 
	 * @param info Object, the object to print
	 * @since 2.3
	 * @see LogLevel
	 */
	public void info(Object info) {
		info(String.valueOf(info));
	}
	
	/**
	 * Print given info message with {@code LogLevel.INFO} and parse placeholders with given args.
	 * 
	 * @param info String, the text to print
	 * @param args Object..., arguments to parse possible placeholders
	 * @since 2.2
	 * @see LogLevel
	 */
	public void info(String info, Object... args) {
	    if (doPrintInfo) {
            if (doPrintToConsole) {
                System.out.printf(lineHeading + info + "%n", formatArgs(LogLevel.INFO, args));
                System.out.flush();
            }
            if (doPrintToFile) {
                pw.printf(lineHeading + info + "%n", formatArgs(LogLevel.INFO, args));
                pw.flush();
            }
        }
	}

    /**
     * Shortcut of {@link Logger#info(String)}.
     *
     * @param info the info message to print
     * @since 3.0
     */
	public void i(String info) {
	    info(info);
    }

    /**
     * Shortcut of {@link Logger#info(Object)}.
     *
     * @param info
     * @since 3.0
     */
    public void i(Object info) {
	    info(info);
    }

    /**
     * Shortcut of {@link Logger#info(String, Object...)}.
     *
     * @param info
     * @param args
     * @since 3.0
     */
    public void i(String info, Object... args) {
	    info(info, args);
    }

	/**
	 * Print given error message with {@code LogLevel.ERR}.
	 * 
	 * @param error String, the text to print
	 * @since 1.0
	 * @see LogLevel
	 */
	public void err(String error) {
		err(error, new Object[] {});
	}
	
	/**
	 * Print a textual representation of given object with {@code LogLevel.ERR}.
	 * 
	 * @param error Object, the object to print
	 * @since 2.3
	 * @see LogLevel
	 */
	public void err(Object error) {
		err(String.valueOf(error));
	}
	
	/**
	 * Print given error message with {@code LogLevel.ERR} and parse possible placeholders with given args.
	 * 
	 * @param error String, the text to print
	 * @param args Object..., arguments to parse possible placeholders
	 * @since 2.2
	 * @see LogLevel
	 */
	public void err(String error, Object... args) {
	    if (doPrintErr) {
            if (doPrintToConsole) {
                System.err.printf(lineHeading + error + "%n", formatArgs(LogLevel.ERR, args));
                System.err.flush();
            }
            if (doPrintToFile) {
                pw.printf(lineHeading + error + "%n", formatArgs(LogLevel.ERR, args));
                pw.flush();
            }
        }
	}

    /**
     * Shortcut of {@link Logger#err(String)}.
     *
     * @param err the error message to print
     * @since 3.0
     */
	public void e(String err) {
	    err(err);
    }

    /**
     * Shortcut of {@link Logger#err(Object)}.
     *
     * @param err
     * @since 3.0
     */
    public void e(Object err) {
	    err(err);
    }

    /**
     * Shortcut of {@link Logger#err(String, Object...)}.
     *
     * @param err
     * @param args
     * @since 3.0
     */
    public void e(String err, Object... args) {
	    err(err, args);
    }

	/**
	 * Print <tt>debug</tt> with <tt>LogLevel.DEBUG</tt>
	 * 
	 * @param debug String, the text to print
	 * @since 1.0
	 * @see LogLevel
	 */
	public void debug(String debug) {
		debug(debug, new Object[] {});
	}
	
	/**
	 * Print a textual representation of <tt>debug</tt> with <tt>LogLevel.DEBUG</tt>
	 * 
	 * @param debug Object, the object to print
	 * @since 2.3
	 * @see LogLevel
	 */
	public void debug(Object debug) {
		debug(String.valueOf(debug));
	}
	
	/**
	 * Print <tt>debug</tt> with <tt>LogLevel.DEBUG</tt> and parse possible placeholders with <tt>args</tt>
	 * 
	 * @param debug String, the text to print
	 * @param args Object..., arguments to parse possible placeholders
	 * @since 2.2
	 * @see LogLevel
	 */
	public void debug(String debug, Object... args) {
		if (doPrintDebug) {
			if (doPrintToConsole) {
				System.out.printf(lineHeading + debug + "%n", formatArgs(LogLevel.DEBUG, args));
				System.out.flush();
			}
			if (doPrintToFile) {
				pw.printf(lineHeading + debug + "%n", formatArgs(LogLevel.DEBUG, args));
				pw.flush();
			}
		}
	}

    /**
     * Shortcut of {@link Logger#debug(String)}.
     *
     * @param debug
     * @since 3.0
     */
	public void d(String debug) {
	    debug(debug);
    }

    /**
     * Shortcut of {@link Logger#debug(Object)}.
     *
     * @param debug
     * @since 3.0
     */
    public void d(Object debug) {
	    debug(debug);
    }

    /**
     * Shortcut of {@link Logger#debug(String, Object...)}.
     *
     * @param debug
     * @param args
     * @since 3.0
     */
    public void d(String debug, Object... args) {
	    debug(debug, args);
    }

	private String getFDate() {
		Date d = new Date();
		return time.format(d);
	}

	private String getPrintedDate() {
		if (doPrintLineHeading) {
			if (doPrintTime)
				return "- " + getFDate() + " ";
			else
				return "";
		} else
			return "";
	}

	private String getPrintedLevel(LogLevel level) {
		if (doPrintLineHeading)
			return level.format();
		else
			return LogLevel.INFO.format();
	}

	private String getPrintedName() {
		if (doPrintLineHeading && doPrintName)
			return "[" + name + "] ";
		return "";
	}
	
	private Object[] formatArgs(LogLevel level, Object[] args) {
		ArrayList<Object> list = new ArrayList<>();
		list.add(getPrintedName());
		list.add(getPrintedDate());
		list.add(getPrintedLevel(level));
		if (args != null)
		for (Object arg : args) {
			list.add(arg);
		}
		return list.toArray(new Object[list.size()]);
	}

	/**
	 * Show or hide debug messages.
	 * 
	 * @param doPrintDebug define if this logger prints out debug messages
	 * @since 1.0
	 */
	public void doPrintDebug(boolean doPrintDebug) {
		this.doPrintDebug = doPrintDebug;
	}
	
	/**
	 * 
	 * @return the file where this logger object writes or null if doPrintToFile has been set to false
	 * @since 2.2
	 */
	public File getLogFile() {
		return doPrintToFile ? logFile : null;
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
	}
}
