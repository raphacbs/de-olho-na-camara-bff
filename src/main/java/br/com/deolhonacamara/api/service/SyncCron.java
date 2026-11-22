package br.com.deolhonacamara.api.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SyncCron {

    private static final Pattern TIME = Pattern.compile("^(\\d{1,2}):(\\d{2})$");

    private SyncCron() {
    }

    public static String fromTime(String time) {
        Matcher matcher = TIME.matcher(time == null ? "" : time.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Use o horário no formato HH:mm");
        }
        int hour = Integer.parseInt(matcher.group(1));
        int minute = Integer.parseInt(matcher.group(2));
        if (hour > 23 || minute > 59) {
            throw new IllegalArgumentException("Horário fora do intervalo");
        }
        return "0 " + minute + " " + hour + " * * *";
    }

    public static String toTime(String cron) {
        String[] parts = cron.trim().split("\\s+");
        int minute = Integer.parseInt(parts[1]);
        int hour = Integer.parseInt(parts[2]);
        return String.format("%02d:%02d", hour, minute);
    }
}
