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
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A highly customizable logging utility. See {@link Builder} for customization options and basic instantiation.
 *
 * @author LimelioN/LimeiloN &lt;memorial.limelion@gmail.com&gt;
 * @version 4.0.0
 * @see Logger#builder()
 * @see Builder
 */
public class Logger {

    /**
     * Basic Hours:Minutes:Seconds time format.
     */
    private static SimpleDateFormat fullTime = new SimpleDateFormat("HH:mm:ss");
    private static Pattern dateFmtPattern = Pattern.compile("\\$\\{datefmt\\((.+)\\)}");

    private String lineFmt;
    private String name;

    private File logFile;
    private PrintWriter pw;

    private boolean doPrintLineFmt = true;
    private boolean doPrintDebug = true;
    private boolean doPrintErr = true;
    private boolean doPrintInfo = true;
    private boolean doPrintToConsole = true;
    private boolean doPrintToFile = true;

    /**
     * Initialize a new Logger using a Builder object. Similar as calling {@link Builder#build()}.
     *
     * @param b the builder
     *
     * @throws IOException if the log file cannot be manipulated
     */
    public Logger(Builder b) throws IOException {
        this(b.name(),
             b.header(),
             b.doPrintLineFmt(),
             b.doPrintDebug(),
             b.doPrintErr(),
             b.doPrintInfo(),
             b.doPrintToConsole(),
             b.doPrintToFile(),
             b.fileNameFmt(),
             b.lineFmt());
    }

    /**
     * Create a new Logger by specifying all the parameters (very long, you may prefer to use the builder)
     *
     * @param name             the text printed in brackets in the line heading
     * @param header           the text printed at the start of the logger
     * @param doPrintLineFmt   define if this logger should print the line heading
     * @param doPrintDebug     define if this logger should print out debug messages
     * @param doPrintErr       define if this logger should print out error messages
     * @param doPrintInfo      define if this logger should print out info messages
     * @param doPrintToConsole define if this logger should print to console
     * @param doPrintToFile    define if this logger should print to file
     * @param fileNameFmt      name of the log file
     * @param lineFmt          text to print before each entry
     *
     * @throws IOException if the log file cannot be manipulated
     */
    public Logger(String name, String header, boolean doPrintLineFmt, boolean doPrintDebug, boolean doPrintErr, boolean doPrintInfo, boolean doPrintToConsole, boolean doPrintToFile, String fileNameFmt, String lineFmt) throws IOException {
        this.name = name;
        this.doPrintLineFmt = doPrintLineFmt;
        this.doPrintDebug = doPrintDebug;
        this.doPrintErr = doPrintErr;
        this.doPrintInfo = doPrintInfo;
        this.doPrintToConsole = doPrintToConsole;
        this.doPrintToFile = doPrintToFile;
        this.lineFmt = lineFmt;

        if (doPrintToFile) {

            this.logFile = new File(format(fileNameFmt, new Date(), null));

            if (! logFile.getParentFile().exists())
                logFile.getParentFile().mkdirs();

            if (logFile.exists())
                logFile.delete();

            if (! logFile.createNewFile())
                throw new IOException("Cannot create log file !");

            pw = new PrintWriter(logFile);

            pw.printf(! header.equals("") ? header + "%n" : "");
        }

        if (doPrintToConsole)
            System.out.printf(header != "" ? header + "%n" : "");
    }

    /**
     * Get a Builder instance to create and customize a new Logger easily.
     *
     * @return a new Builder instance
     *
     * @since 3.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Print given message with given {@link LogLevel}.
     *
     * @param text  String, the text to print
     * @param level LogLevel, the level to use
     *
     * @see LogLevel
     * @since 2.2
     */
    public void log(String text, LogLevel level) {
        log(text, level, new Object[] { });
    }

    /**
     * Print given message with given {@link LogLevel} and format with given args.
     *
     * @param text  String, the text to print
     * @param level LogLevel, the level to use
     * @param args  Object..., arguments to parse possible placeholders
     *
     * @see LogLevel
     * @since 2.2
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
     *
     * @see LogLevel
     * @since 1.0
     */
    public void info(String info) {
        info(info, new Object[] { });
    }

    /**
     * Print a textual representation of given object with {@code LogLevel.INFO}.
     *
     * @param info Object, the object to print
     *
     * @see LogLevel
     * @since 2.3
     */
    public void info(Object info) {
        info(String.valueOf(info));
    }

    /**
     * Print given info message with {@code LogLevel.INFO} and parse placeholders with given args.
     *
     * @param info String, the text to print
     * @param args Object..., arguments to parse possible placeholders
     *
     * @see LogLevel
     * @since 2.2
     */
    public void info(String info, Object... args) {
        if (doPrintInfo) {
            String toPrint = format((doPrintLineFmt ? lineFmt : null) + info + "%n", new Date(), LogLevel.INFO, args);
            if (doPrintToConsole) {
                System.out.print(toPrint);
                System.out.flush();
            }
            if (doPrintToFile) {
                pw.print(toPrint);
                pw.flush();
            }
        }
    }

    /**
     * Shortcut of {@link Logger#info(String)}.
     *
     * @param info the info message to print
     *
     * @since 3.0
     */
    public void i(String info) {
        info(info);
    }

    /**
     * Shortcut of {@link Logger#info(Object)}.
     *
     * @param info
     *
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
     *
     * @since 3.0
     */
    public void i(String info, Object... args) {
        info(info, args);
    }

    /**
     * Print given error message with {@code LogLevel.ERR}.
     *
     * @param error String, the text to print
     *
     * @see LogLevel
     * @since 1.0
     */
    public void err(String error) {
        err(error, new Object[] { });
    }

    /**
     * Print a textual representation of given object with {@code LogLevel.ERR}.
     *
     * @param error Object, the object to print
     *
     * @see LogLevel
     * @since 2.3
     */
    public void err(Object error) {
        err(String.valueOf(error));
    }

    /**
     * Print given error message with {@code LogLevel.ERR} and parse possible placeholders with given args.
     *
     * @param error String, the text to print
     * @param args  Object..., arguments to parse possible placeholders
     *
     * @see LogLevel
     * @since 2.2
     */
    public void err(String error, Object... args) {
        if (doPrintErr) {
            String toPrint = format((doPrintLineFmt ? lineFmt : null) + error + "%n", new Date(), LogLevel.ERR, args);
            if (doPrintToConsole) {
                System.err.print(toPrint);
                System.err.flush();
            }
            if (doPrintToFile) {
                pw.print(toPrint);
                pw.flush();
            }
        }
    }

    /**
     * Shortcut of {@link Logger#err(String)}.
     *
     * @param err the error message to print
     *
     * @since 3.0
     */
    public void e(String err) {
        err(err);
    }

    /**
     * Shortcut of {@link Logger#err(Object)}.
     *
     * @param err
     *
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
     *
     * @since 3.0
     */
    public void e(String err, Object... args) {
        err(err, args);
    }

    /**
     * Print <tt>debug</tt> with <tt>LogLevel.DEBUG</tt>
     *
     * @param debug String, the text to print
     *
     * @see LogLevel
     * @since 1.0
     */
    public void debug(String debug) {
        debug(debug, new Object[] { });
    }

    /**
     * Print a textual representation of <tt>debug</tt> with <tt>LogLevel.DEBUG</tt>
     *
     * @param debug Object, the object to print
     *
     * @see LogLevel
     * @since 2.3
     */
    public void debug(Object debug) {
        debug(String.valueOf(debug));
    }

    /**
     * Print <tt>debug</tt> with <tt>LogLevel.DEBUG</tt> and parse possible placeholders with <tt>args</tt>
     *
     * @param debug String, the text to print
     * @param args  Object..., arguments to parse possible placeholders
     *
     * @see LogLevel
     * @since 2.2
     */
    public void debug(String debug, Object... args) {
        if (doPrintDebug) {
            String toPrint = format((doPrintLineFmt ? lineFmt : null) + debug + "%n", new Date(), LogLevel.DEBUG, args);
            if (doPrintToConsole) {
                System.out.print(toPrint);
                System.out.flush();
            }
            if (doPrintToFile) {
                pw.print(toPrint);
                pw.flush();
            }
        }
    }

    /**
     * Shortcut of {@link Logger#debug(String)}.
     *
     * @param debug
     *
     * @since 3.0
     */
    public void d(String debug) {
        debug(debug);
    }

    /**
     * Shortcut of {@link Logger#debug(Object)}.
     *
     * @param debug
     *
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
     *
     * @since 3.0
     */
    public void d(String debug, Object... args) {
        debug(debug, args);
    }

    /**
     * @return the file where this logger object writes or null if doPrintToFile has been set to false
     *
     * @since 2.2
     */
    public File getLogFile() {
        return doPrintToFile ? logFile : null;
    }

    /**
     * Close this Logger instance. Will throw some exceptions if you use the closed object.
     *
     * @see Logger
     * @since 1.0
     */
    public void close() {
        pw.close();
    }

    /**
     * Format the line.
     *
     * @param fmt
     * @param d
     * @param l
     * @param args
     *
     * @return
     *
     * @since 4.0.0
     */
    private String format(String fmt, Date d, LogLevel l, Object... args) {

        if (fmt.contains("${name}"))
            fmt = fmt.replaceAll("\\$\\{name\\}", name);
        if (fmt.contains("${fulltime}"))
            fmt = fmt.replaceAll("\\$\\{fulltime\\}", fullTime.format(d));
        if (fmt.contains("${loglevel}"))
            fmt = fmt.replaceAll("\\$\\{loglevel\\}", l.toString());
        Matcher m = dateFmtPattern.matcher(fmt);
        if (m.find()) {
            fmt = m.replaceAll(new SimpleDateFormat(m.group(1)).format(d));
        }
        return String.format(fmt, args);
    }
}
