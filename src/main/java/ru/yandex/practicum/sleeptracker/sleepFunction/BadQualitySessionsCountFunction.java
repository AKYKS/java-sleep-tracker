package ru.yandex.practicum.sleeptracker.sleepFunction;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class BadQualitySessionsCountFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long badCount = sessions.stream()
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult("Количество сессий с плохим качеством сна", badCount);
    }
}
