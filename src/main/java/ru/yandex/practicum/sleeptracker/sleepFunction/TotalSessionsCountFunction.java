package ru.yandex.practicum.sleeptracker.sleepFunction;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class TotalSessionsCountFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long count = sessions.stream().count();
        return new SleepAnalysisResult("Общее количество сессий сна", count);
    }
}
