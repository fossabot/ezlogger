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

import java.io.IOException;

/**
 * Utility class to easily create and customize a new {@link Logger} object. Access with {@link Logger#builder()}
 *
 * @author LimelioN/LimeiloN &lt;memorial.limelion@gmail.com&gt;
 * @see Logger
 * @since 3.0
 */
public class Builder {

    private boolean doPrintLineFmt = true;

    private boolean doPrintDebug = true;
    private boolean doPrintInfo = true;
    private boolean doPrintErr = true;

    private boolean doPrintToConsole = true;
    private boolean doPrintToFile = true;

    private String fileNameFmt = "${name}_${datefmt(yyyyMMdd-HHmmss)}.log";

    private String name = "";
    private String header = "";

    private String lineFmt = "[${name}] - ${fulltime} ${loglevel}: ";

    /**
     * Accessible with {@link Logger#builder()}
     *
     * @see Logger#builder()
     */
    Builder() {

    }

    /**
     * Finally create a new Logger instance with all the customizations.
     *
     * @return a new Logger instance
     *
     * @see Logger
     */
    public Logger build() {
        try {
            return new Logger(this);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String fileNameFmt() {
        return fileNameFmt;
    }

    public Builder fileNameFmt(String baseLogFileName) {
        this.fileNameFmt = baseLogFileName;
        return this;
    }

    public boolean doPrintLineFmt() {
        return doPrintLineFmt;
    }

    public Builder doPrintLineFmt(boolean doPrintLineFmt) {
        this.doPrintLineFmt = doPrintLineFmt;
        return this;
    }

    public boolean doPrintDebug() {
        return doPrintDebug;
    }

    public Builder doPrintDebug(boolean doPrintDebug) {
        this.doPrintDebug = doPrintDebug;
        return this;
    }

    public boolean doPrintInfo() {
        return doPrintInfo;
    }

    public Builder doPrintInfo(boolean doPrintInfo) {
        this.doPrintInfo = doPrintInfo;
        return this;
    }

    public boolean doPrintErr() {
        return doPrintErr;
    }

    public Builder doPrintErr(boolean doPrintErr) {
        this.doPrintErr = doPrintErr;
        return this;
    }

    public boolean doPrintToConsole() {
        return doPrintToConsole;
    }

    public Builder doPrintToConsole(boolean doPrintToConsole) {
        this.doPrintToConsole = doPrintToConsole;
        return this;
    }

    public boolean doPrintToFile() {
        return doPrintToFile;
    }

    public Builder doPrintToFile(boolean doPrintToFile) {
        this.doPrintToFile = doPrintToFile;
        return this;
    }

    public String name() {
        return name;
    }

    public Builder name(String name) {
        this.name = name;
        return this;
    }

    public String header() {
        return header;
    }

    public Builder header(String header) {
        this.header = header;
        return this;
    }

    public Builder lineFmt(String lineFmt) {
        this.lineFmt = lineFmt;
        return this;
    }

    public String lineFmt() {
        return lineFmt;
    }
}
