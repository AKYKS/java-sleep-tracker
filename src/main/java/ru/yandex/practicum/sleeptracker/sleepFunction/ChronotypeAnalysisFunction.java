package ru.yandex.practicum.sleeptracker.sleepFunction;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeAnalysisFunction implements SleepAnalysisFunction {
    public enum Chronotype {
        OWL, LARK, DOVE;

        @Override
        public String toString() {
            switch (this) {
                case OWL:
                    return "Сова";
                case LARK:
                    return "Жаворонок";
                case DOVE:
                    return "Голубь";
            }
            return "";
        }
    }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        List<SleepingSession> nightSessions = sessions.stream()
                .filter(this::isNightSession)
                .collect(Collectors.toList());

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя", Chronotype.DOVE);
        }


        Map<Chronotype, Long> typeCounts = nightSessions.stream()
                .map(this::classifyNight)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        Chronotype userType = determineUserChronotype(typeCounts);
        return new SleepAnalysisResult("Хронотип пользователя", userType);
    }

    private boolean isNightSession(SleepingSession session) {
        LocalTime startTime = session.getStartTime().toLocalTime();
        LocalTime endTime = session.getEndTime().toLocalTime();

        LocalTime evening = LocalTime.of(20, 0);
        LocalTime morning = LocalTime.of(11, 0);

        return startTime.isAfter(evening) || startTime.equals(evening) ||
                endTime.isBefore(morning) || endTime.equals(morning);
    }

    private Chronotype classifyNight(SleepingSession session) {
        LocalTime bedTime = session.getStartTime().toLocalTime();
        LocalTime wakeTime = session.getEndTime().toLocalTime();

        LocalTime owlBedThreshold = LocalTime.of(23, 0);
        LocalTime owlWakeThreshold = LocalTime.of(9, 0);

        LocalTime larkBedThreshold = LocalTime.of(22, 0);
        LocalTime larkWakeThreshold = LocalTime.of(7, 0);

        if (bedTime.isAfter(owlBedThreshold) && wakeTime.isAfter(owlWakeThreshold)) {
            return Chronotype.OWL;
        } else if (bedTime.isBefore(larkBedThreshold) && wakeTime.isBefore(larkWakeThreshold)) {
            return Chronotype.LARK;
        } else {
            return Chronotype.DOVE;
        }
    }

    private Chronotype determineUserChronotype(Map<Chronotype, Long> typeCounts) {
        long owlCount = typeCounts.getOrDefault(Chronotype.OWL, 0L);
        long larkCount = typeCounts.getOrDefault(Chronotype.LARK, 0L);
        long doveCount = typeCounts.getOrDefault(Chronotype.DOVE, 0L);

        if ((owlCount == larkCount) || (owlCount == doveCount) || (larkCount == doveCount)) {
            return Chronotype.DOVE;
        }

        if (owlCount > larkCount && owlCount > doveCount) {
            return Chronotype.OWL;
        } else if (larkCount > owlCount && larkCount > doveCount) {
            return Chronotype.LARK;
        } else {
            return Chronotype.DOVE;
        }
    }
}