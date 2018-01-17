/*******************************************************************************
 * Copyright (C) 2017 LimelioN
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

public class Logger {

	private PrintWriter pw;
	private PrintStream out = System.out;
	private PrintStream err = System.err;
	private boolean printDate = true;
	private String name = "";
	private String header = "";
	private boolean debug = false;
	private boolean toConsole = true;
	private boolean toFile = true;
	private boolean allowColors;

	private Date d;
	private SimpleDateFormat sdf;

	private String baseLogName;
	private String dateMarker;
	private String finalLogName;

	public Logger(String name, String header, PrintStream out, PrintStream err, boolean debug, boolean printDate,
			boolean toConsole, boolean toFile) {

		this.debug = debug;
		this.name = name;
		this.out = out;
		this.err = err;
		this.printDate = printDate;
		this.header = header;
		this.toConsole = toConsole;
		this.toFile = toFile;

		if (toFile) {
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
				this.pw = new PrintWriter(log);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			pw.println(this.header);
		}

		if (toConsole)
			out.println(this.header);
	}

	public Logger(String name, String header, PrintStream out, PrintStream err) {
		this(name, header, out, err, false, true, true, true);
	}

	public void info(String info) {

		if (printDate) {
			if (toFile)
				pw.printf("[%s] - %s : %s%n", name, getFDate(), info);
			if (toConsole)
				out.printf("[%s] - %s : %s%n", name, getFDate(), info);
		} else {
			if (toFile)
				pw.printf("[%s] : %s%n", name, info);
			if (toConsole)
				out.printf("[%s] : %s%n", name, info);
		}

		if (toFile)
			pw.flush();
		if (toConsole)
			out.flush();
	}

	public void err(String error) {

		if (printDate) {
			if (toFile)
				pw.printf("[%s] %s [Error]: %s%n", name, getFDate(), error);
			if (toConsole)
				err.printf("[%s] %s [Error]: %s%n", name, getFDate(), error);
		} else {
			if (toFile)
				pw.printf("[%s] %s [Error]: %s%n", name, error);
			if (toConsole)
				err.printf("[%s] %s [Error]: %s%n", name, error);
		}

		if (toFile)
			pw.flush();
		if (toConsole)
			err.flush();
	}

	public void debug(String debug) {

		if (this.debug) {
			if (printDate) {
				if (toFile)
					pw.printf("[%s] %s [Debug]: %s%n", name, getFDate(), debug);
				if (toConsole)
					out.printf("[%s] %s [Debug]: %s%n", name, getFDate(), debug);
			} else {
				if (toFile)
					pw.printf("[%s] %s [Debug]: %s%n", name, debug);
				if (toConsole)
					out.printf("[%s] %s [Debug]: %s%n", name, debug);
			}

			if (toFile)
				pw.flush();
			if (toConsole)
				out.flush();

		}
	}

	private String getFDate() {
		d = new Date();
		sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(d);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void close() {
		pw.close();
		pw = null;
	}
}
