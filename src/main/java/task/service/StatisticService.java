package task.service;

import lombok.extern.slf4j.Slf4j;
import task.statistic.StatisticCollector;

import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Thread.sleep;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static task.statistic.StatisticCollector.getStatisticCollector;

@Slf4j
public class StatisticService implements Runnable {
    private final StatisticCollector statisticCollector = getStatisticCollector();

    @Override
    public void run() {
        while (true) {
            try {
                FileWriter fileWriter = new FileWriter("statistic.txt");
                fileWriter.write(now().format(ofPattern("dd-MM-yyyy HH:mm:ss")) + "\n");
                fileWriter.write("\n");
                statisticCollector.getStatistic().forEach(string -> {
                    try {
                        fileWriter.write(string + "\n");
                    } catch (IOException e) {
                        log.error("can't write task.statistic in file", e);
                    }
                });
                fileWriter.close();
            } catch (IOException e) {
                log.error("can't write task.statistic in file", e);
            }

            try {
                sleep(5_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}