package ru.yandex.practicum.sleeptracker.sleepFunction;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleeplessNightsCountFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0);
        }

        // Определяем период: от первой сессии до последней
        LocalDate startDate = sessions.getFirst().getStartTime().toLocalDate();
        LocalDate endDate = sessions.getLast().getEndTime().toLocalDate();

        int totalNights = Period.between(startDate, endDate).getDays();

        Set<LocalDate> allNights = Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(totalNights)
                .collect(Collectors.toSet());

        // Находим ночи, когда был сон в интервале 0:00–6:00
        Set<LocalDate> nightsWithSleep = sessions.stream()
                .filter(this::sessionIntersectsNightTime)
                .map(this::getNightForSession)
                .collect(Collectors.toSet());

        long sleeplessNights = allNights.stream()
                .filter(night -> !nightsWithSleep.contains(night))
                .count();

        return new SleepAnalysisResult("Количество бессонных ночей", sleeplessNights);
    }

    private boolean sessionIntersectsNightTime(SleepingSession session) {
        LocalDateTime sessionStart = session.getStartTime();
        LocalDateTime sessionEnd = session.getEndTime();
        LocalTime nightStart = LocalTime.of(0, 0);
        LocalTime nightEnd = LocalTime.of(6, 0);

        // сессия пересекает полночь и захватывает ночное время
        if (sessionStart.toLocalDate().isBefore(sessionEnd.toLocalDate())) {
            // Проверяем ночное время текущего дня (0:00–6:00)
            LocalDateTime currentNightEnd = LocalDateTime.of(sessionStart.toLocalDate(), nightEnd);
            if (sessionEnd.isAfter(currentNightEnd) || sessionEnd.equals(currentNightEnd)) {
                return true;
            }
            // Проверяем ночное время следующего дня (0:00–6:00 следующего дня)
            LocalDateTime nextNightStart = LocalDateTime.of(sessionEnd.toLocalDate(), nightStart);
            return !sessionStart.isAfter(nextNightStart) && sessionEnd.isAfter(nextNightStart);
        }

        // сессия в пределах одного дня
        LocalTime sessionStartTime = sessionStart.toLocalTime();
        LocalTime sessionEndTime = sessionEnd.toLocalTime();

        return (sessionStartTime.isBefore(nightEnd) || sessionStartTime.equals(nightEnd)) &&
                (sessionEndTime.isAfter(nightStart) || sessionEndTime.equals(nightStart));
    }

    private LocalDate getNightForSession(SleepingSession session) {
        LocalDateTime start = session.getStartTime();
        LocalDateTime end = session.getEndTime();

        if (start.toLocalDate().equals(end.toLocalDate())) {
            return start.toLocalDate();
        } else {
            LocalDateTime nightEndToday = LocalDateTime.of(start.toLocalDate(), LocalTime.of(6, 0));

            if (end.isAfter(nightEndToday)) {
                return start.toLocalDate();
            } else {
                return start.toLocalDate();
            }
        }
    }
}

