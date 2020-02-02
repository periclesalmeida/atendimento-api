package br.com.periclesalmeida.atendimento.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class DateUtil {

    private static LocalDateTime localDateTime;

    public static void mockLocalDateTime(LocalDateTime localDateTime) {
        localDateTime = localDateTime;
    }

    public static LocalDate getLocalDateNow() {
        return getLocalDateTimeNow().toLocalDate();
    }

    public static LocalDateTime getLocalDateTimeNow() {
        return Optional.ofNullable(localDateTime).isPresent() ? localDateTime: LocalDateTime.now();
    }
}
