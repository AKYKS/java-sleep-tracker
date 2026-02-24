package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.Duration;

public class SleepingSession {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SleepQuality sleepQuality;

    public SleepingSession(LocalDateTime startTime, LocalDateTime endTime, SleepQuality sleepQuality) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.sleepQuality = sleepQuality;
    }

    public SleepingSession(LocalDateTime startTime, LocalDateTime endTime, String sleepQuality) {
        this.startTime = startTime;
        this.endTime = endTime;
        sleepQuality = sleepQuality.trim().toUpperCase();
        switch (sleepQuality) {
            case "NORMAL":
                this.sleepQuality = SleepQuality.NORMAL;
                break;
            case "GOOD":
                this.sleepQuality = SleepQuality.GOOD;
                break;
            case "BAD":
                this.sleepQuality = SleepQuality.BAD;
                break;
            default:
                throw new IllegalArgumentException("Неправильный ввод данных");
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public SleepQuality getQuality() {
        return sleepQuality;
    }

    // Продолжительность в минутах
    public long getDurationInMinutes() {
        return Duration.between(startTime, endTime).toMinutes();
    }

    @Override
    public String toString() {
        return "fa";
    }
}
