package task.statistic;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Stream;

import static java.lang.Integer.compare;

@Slf4j
public class StatisticCollector {
    private static StatisticCollector statisticCollector;

    private final Map<String, Integer> map = new TreeMap<>((s1, s2) -> {
        Scanner scanner1 = new Scanner(s1.replaceAll("[a-z:]", ""));
        Scanner scanner2 = new Scanner(s2.replaceAll("[a-z:]", ""));

        int compareResult = compare(scanner1.nextInt(), scanner2.nextInt());
        if (compareResult == 0) {
            compareResult = compare(scanner1.nextInt(), scanner2.nextInt());
            if (compareResult == 0) {
                return compare(scanner1.nextInt(), scanner2.nextInt());
            }
            return compareResult;
        }
        return compareResult;
    });

    public synchronized static StatisticCollector getStatisticCollector() {
        if (statisticCollector == null) {
            statisticCollector = new StatisticCollector();
        }
        return statisticCollector;
    }

    public synchronized void add(String key) {
        Integer value = map.get(key);
        if (value != null) {
            value++;
        } else {
            value = 1;
        }
        map.put(key, value);
    }

    public Stream<String> getStatistic() {
        return map.entrySet().stream().map(entry -> entry.getKey() + " | " + entry.getValue());
    }
}