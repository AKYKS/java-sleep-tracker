package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.sleepFunction.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SleepTrackerApp {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static void main(String[] args) {
        try (FileReader fr = new FileReader("src/main/resources/sleep_log.txt")) {
            List<SleepingSession> sleepingSessions = readSleepingSessionsFromFile(fr);
            System.out.println((new TotalSessionsCountFunction().apply(sleepingSessions)));
            System.out.println((new MinSessionDurationFunction().apply(sleepingSessions)));
            System.out.println((new MaxSessionDurationFunction().apply(sleepingSessions)));
            System.out.println((new AvgSessionDurationFunction().apply(sleepingSessions)));
            System.out.println((new BadQualitySessionsCountFunction().apply(sleepingSessions)));
            System.out.println((new SleeplessNightsCountFunction().apply(sleepingSessions)));
            System.out.println((new ChronotypeAnalysisFunction().apply(sleepingSessions)));

        } catch (Exception e) {
            System.out.println("Произошла ошибка! - " + e.getMessage());
        }
    }

    private static List<SleepingSession> readSleepingSessionsFromFile(FileReader fileReader) {
        BufferedReader br = new BufferedReader(fileReader);
        return br.lines()
                .filter(line -> !line.trim().isEmpty())
                .map(SleepTrackerApp::parseLineToSleepingSession)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    private static SleepingSession parseLineToSleepingSession(String line) {
        try {
            String[] parts = line.split(";");
            if (parts.length < 3) {
                throw new Exception("Некорректный формат строки");
            }

            LocalDateTime start = LocalDateTime.parse(parts[0], DTF);
            LocalDateTime end = LocalDateTime.parse(parts[1], DTF);
            String quality = parts[2];

            return new SleepingSession(start, end, quality);
        } catch (Exception e) {
            return null;
        }
    }
}