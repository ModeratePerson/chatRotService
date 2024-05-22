package com.moderatePerson.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * Parses a date string into a Date object using the provided date format.
     *
     * @param dateString the date string to parse
     * @param format the format of the date string
     * @return the parsed Date object
     * @throws ParseException if the date string cannot be parsed
     */
    public static Date parse(String dateString, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dateString);
    }
}

