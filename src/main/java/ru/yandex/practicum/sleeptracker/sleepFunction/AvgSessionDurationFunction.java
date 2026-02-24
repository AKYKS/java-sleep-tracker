package ru.yandex.practicum.sleeptracker.sleepFunction;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class AvgSessionDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        double avgDuration = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .average()
                .orElse(0.0);
        return new SleepAnalysisResult("Средняя продолжительность сессии (мин)", String.format("%.2f", avgDuration));
    }
}
