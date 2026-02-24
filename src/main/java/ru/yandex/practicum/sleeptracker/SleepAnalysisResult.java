package ru.yandex.practicum.sleeptracker;

public class SleepAnalysisResult {
    private String description;
    private Object result;

    public SleepAnalysisResult(String description, Object result) {
        this.description = description;
        this.result = result;
    }

    @Override
    public String toString() {
        return description + ": " + result.toString();
    }
}
