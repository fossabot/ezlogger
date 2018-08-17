package com.limelion.logger;

/**
 * All possible log levels
 *
 * @see Logger#log(String, LogLevel)
 * @see Logger#info(String, Object...)
 * @see Logger#debug(String, Object...)
 * @see Logger#err(String, Object...)
 * @since 2.2
 */
public enum LogLevel {
    INFO, DEBUG, ERR;

    public String toString() {
        return this.name();
    }
}
