package com.godokan.rutiner.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {
    private static DateHelper dateHelper = null;
    private static LocalDate selectedDate;
    private DateTimeFormatter formatter;

    public DateHelper() {
        selectedDate = null;
        formatter = null;
    }

    public static DateHelper getInstance() {
        if (dateHelper == null) dateHelper = new DateHelper();
        return dateHelper;
    }

    public void setNowDate() {
        selectedDate = LocalDate.now();
    }

    public void setSelectedDate(LocalDate localDate) {
        selectedDate = localDate;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public LocalDate getNowDate() {
        return LocalDate.now();
    }

    public String parseDateString(LocalDate localDate) {
        formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        return localDate.format(formatter);
    }

    public LocalDate parseStringDate(String date) {
        formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        return LocalDate.parse(date, formatter);
    }
}
