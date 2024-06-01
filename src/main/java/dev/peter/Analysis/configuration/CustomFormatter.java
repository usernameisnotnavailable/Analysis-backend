package dev.peter.Analysis.configuration;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class CustomFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        String className = record.getSourceClassName();
        className = className.substring(className.lastIndexOf(".") + 1);

        return String.format("%1$tF %1$tT [%2$s] - (%3$s) - %4$s %n",
                new Date(record.getMillis()),
                record.getLevel(),
                className,
                record.getMessage());

    }
}