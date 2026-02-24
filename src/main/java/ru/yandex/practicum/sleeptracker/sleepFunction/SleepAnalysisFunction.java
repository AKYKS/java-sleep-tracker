package ru.yandex.practicum.sleeptracker.sleepFunction;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

public interface SleepAnalysisFunction extends Function<List<SleepingSession>, SleepAnalysisResult> {
}
