
package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.sleepFunction.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class SleepTrackerAppTest {

    static List<SleepingSession> sleepingSessions = new ArrayList<>();

    @BeforeAll
    public static void init() {
        try (FileReader fr = new FileReader("src/main/resources/sleep_log.txt");
             BufferedReader br = new BufferedReader(fr)) {

            String line;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // пропускаем пустые строки

                String[] parts = line.split(";");
                if (parts.length < 3) {
                    System.err.println("Некорректный формат строки: " + line);
                    continue;
                }

                LocalDateTime start = LocalDateTime.parse(parts[0], dtf);
                LocalDateTime end = LocalDateTime.parse(parts[1], dtf);
                String quality = parts[2];

                sleepingSessions.add(new SleepingSession(start, end, quality));
            }
        } catch (Exception e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testSleepingSessions() {
        Assertions.assertEquals(13, sleepingSessions.size());
    }

    @Test
    public void testAvgSessionDurationFunctionOnTestFile() {
        AvgSessionDurationFunction avgSessionDurationFunction = new AvgSessionDurationFunction();
        String result = (avgSessionDurationFunction.apply(sleepingSessions)).toString();
        int testValue = 345;
        Assertions.assertEquals("Средняя продолжительность сессии (мин): " + testValue, result.toString());
    }

    @Test
    public void testAvgSessionDurationFunction() {
        List<SleepingSession> sleepingSessionsTest = new ArrayList<>();
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 10, 22, 30),
                LocalDateTime.of(2026, 1, 11, 6, 30), SleepQuality.GOOD));
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 11, 23, 30),
                LocalDateTime.of(2026, 1, 12, 7, 30), SleepQuality.GOOD));
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 13, 0, 30),
                LocalDateTime.of(2026, 1, 13, 8, 30), SleepQuality.GOOD));

        AvgSessionDurationFunction avgSessionDurationFunction = new AvgSessionDurationFunction();
        String result = (avgSessionDurationFunction.apply(sleepingSessionsTest)).toString();
        int testValue = 480;
        Assertions.assertEquals("Средняя продолжительность сессии (мин): " + testValue, result.toString());
    }

    @Test
    public void testBadQualitySessionsCountFunctionOnTestFile() {
        BadQualitySessionsCountFunction bqsCountFunction = new BadQualitySessionsCountFunction();
        String result = (bqsCountFunction.apply(sleepingSessions)).toString();
        Assertions.assertEquals("Количество сессий с плохим качеством сна: 2", result.toString());
    }

    @Test
    public void testBadQualitySessionsCountFunction() {
        List<SleepingSession> sleepingSessionsTest = new ArrayList<>();
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 10, 22, 30),
                LocalDateTime.of(2026, 1, 11, 6, 30), SleepQuality.BAD));
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 11, 23, 30),
                LocalDateTime.of(2026, 1, 12, 7, 30), SleepQuality.NORMAL));
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 13, 0, 30),
                LocalDateTime.of(2026, 1, 13, 8, 30), SleepQuality.GOOD));

        BadQualitySessionsCountFunction bqsCountFunction = new BadQualitySessionsCountFunction();
        String result = (bqsCountFunction.apply(sleepingSessionsTest)).toString();
        Assertions.assertEquals("Количество сессий с плохим качеством сна: 1", result.toString());
    }

    @Test
    public void testChronotypeAnalysisFunctionOnTestFile() {
        ChronotypeAnalysisFunction chronotypeAnalysisFunction = new ChronotypeAnalysisFunction();
        String result = (chronotypeAnalysisFunction.apply(sleepingSessions)).toString();
        Assertions.assertEquals("Хронотип пользователя: Голубь", result.toString());
    }

    @Test
    public void testChronotypeAnalysisFunction() {
        List<SleepingSession> sleepingSessionsTest = new ArrayList<>();
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 10, 22, 30),
                LocalDateTime.of(2026, 1, 11, 6, 30), SleepQuality.BAD));
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 11, 23, 30),
                LocalDateTime.of(2026, 1, 12, 10, 30), SleepQuality.NORMAL));
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 12, 23, 30),
                LocalDateTime.of(2026, 1, 13, 10, 30), SleepQuality.GOOD));

        ChronotypeAnalysisFunction chronotypeAnalysisFunction = new ChronotypeAnalysisFunction();
        String result = (chronotypeAnalysisFunction.apply(sleepingSessionsTest)).toString();
        Assertions.assertEquals("Хронотип пользователя: Сова", result.toString());

        List<SleepingSession> sleepingSessionsTest2 = new ArrayList<>();
        sleepingSessionsTest2.add(new SleepingSession(LocalDateTime.of(2026, 1, 10, 22, 30),
                LocalDateTime.of(2026, 1, 11, 6, 30), SleepQuality.BAD));
        sleepingSessionsTest2.add(new SleepingSession(LocalDateTime.of(2026, 1, 11, 21, 30),
                LocalDateTime.of(2026, 1, 12, 6, 30), SleepQuality.NORMAL));
        sleepingSessionsTest2.add(new SleepingSession(LocalDateTime.of(2026, 1, 12, 21, 30),
                LocalDateTime.of(2026, 1, 13, 6, 30), SleepQuality.GOOD));

        result = (chronotypeAnalysisFunction.apply(sleepingSessionsTest2)).toString();
        Assertions.assertEquals("Хронотип пользователя: Жаворонок", result.toString());
    }

    @Test
    public void testMaxSessionDurationFunctionOnTestFile() {
        MaxSessionDurationFunction maxSessionDurationFunction = new MaxSessionDurationFunction();
        String result = (maxSessionDurationFunction.apply(sleepingSessions)).toString();
        Assertions.assertEquals("Максимальная продолжительность сессии (мин): 500", result.toString());
    }

    @Test
    public void testMaxSessionDurationFunction() {
        List<SleepingSession> sleepingSessionsTest = new ArrayList<>();
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 10, 22, 30),
                LocalDateTime.of(2026, 1, 11, 6, 30), SleepQuality.BAD));
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 11, 23, 30),
                LocalDateTime.of(2026, 1, 12, 10, 30), SleepQuality.NORMAL));
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 12, 23, 30),
                LocalDateTime.of(2026, 1, 13, 11, 30), SleepQuality.GOOD));

        MaxSessionDurationFunction maxSessionDurationFunction = new MaxSessionDurationFunction();
        String result = (maxSessionDurationFunction.apply(sleepingSessionsTest)).toString();
        Assertions.assertEquals("Максимальная продолжительность сессии (мин): 720", result.toString());
    }

    @Test
    public void testMinSessionDurationFunctionOnTestFile() {
        MinSessionDurationFunction minSessionDurationFunction = new MinSessionDurationFunction();
        String result = (minSessionDurationFunction.apply(sleepingSessions)).toString();
        Assertions.assertEquals("Минимальная продолжительность сессии (мин): 45", result.toString());
    }

    @Test
    public void testMinSessionDurationFunction() {
        List<SleepingSession> sleepingSessionsTest = new ArrayList<>();
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 10, 22, 30),
                LocalDateTime.of(2026, 1, 11, 6, 30), SleepQuality.BAD));
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 11, 23, 30),
                LocalDateTime.of(2026, 1, 12, 10, 30), SleepQuality.NORMAL));
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 12, 23, 30),
                LocalDateTime.of(2026, 1, 13, 11, 30), SleepQuality.GOOD));

        MinSessionDurationFunction minSessionDurationFunction = new MinSessionDurationFunction();
        String result = (minSessionDurationFunction.apply(sleepingSessionsTest)).toString();
        Assertions.assertEquals("Минимальная продолжительность сессии (мин): 480", result.toString());
    }

    @Test
    public void testSleeplessNightsCountFunctionOnTestFile() {
        SleeplessNightsCountFunction sleeplessNightsCountFunction = new SleeplessNightsCountFunction();
        String result = (sleeplessNightsCountFunction.apply(sleepingSessions)).toString();
        Assertions.assertEquals("Количество бессонных ночей: 20", result.toString());
    }

    @Test
    public void testSleeplessNightsCountFunction() {
        List<SleepingSession> sleepingSessionsTest = new ArrayList<>();
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 10, 22, 30),
                LocalDateTime.of(2026, 1, 11, 6, 30), SleepQuality.BAD));
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 11, 23, 30),
                LocalDateTime.of(2026, 1, 12, 10, 30), SleepQuality.NORMAL));
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 13, 23, 30),
                LocalDateTime.of(2026, 1, 14, 11, 30), SleepQuality.GOOD));

        SleeplessNightsCountFunction sleeplessNightsCountFunction = new SleeplessNightsCountFunction();
        String result = (sleeplessNightsCountFunction.apply(sleepingSessionsTest)).toString();
        Assertions.assertEquals("Количество бессонных ночей: 1", result.toString());
    }

    @Test
    public void testTotalSessionsCountFunctionOnTestFile() {
        TotalSessionsCountFunction totalSessionsCountFunction = new TotalSessionsCountFunction();
        String result = (totalSessionsCountFunction.apply(sleepingSessions)).toString();
        Assertions.assertEquals("Общее количество сессий сна: 13", result.toString());
    }

    @Test
    public void testTotalSessionsCountFunction() {
        List<SleepingSession> sleepingSessionsTest = new ArrayList<>();
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 10, 22, 30),
                LocalDateTime.of(2026, 1, 11, 6, 30), SleepQuality.BAD));
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 11, 23, 30),
                LocalDateTime.of(2026, 1, 12, 10, 30), SleepQuality.NORMAL));
        sleepingSessionsTest.add(new SleepingSession(LocalDateTime.of(2026, 1, 13, 23, 30),
                LocalDateTime.of(2026, 1, 14, 11, 30), SleepQuality.GOOD));

        TotalSessionsCountFunction totalSessionsCountFunction = new TotalSessionsCountFunction();
        String result = (totalSessionsCountFunction.apply(sleepingSessionsTest)).toString();
        Assertions.assertEquals("Общее количество сессий сна: 3", result.toString());
    }
}