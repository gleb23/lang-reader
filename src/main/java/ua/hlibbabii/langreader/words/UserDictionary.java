package ua.hlibbabii.langreader.words;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by hlib on 03.04.16.
 */
public class UserDictionary {
    private int userId;

    // keep it sorted
    private Map<String, WordStatistics> wordToStatisticsMap = new HashMap<>();

    public void add(WordStatistics wordStatistics) {
        wordToStatisticsMap.put(wordStatistics.getNormalizedForm(), wordStatistics);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserDictionary{" +
                "userId=" + userId +
                "\n" +
                wordToStatisticsMap.values().stream().map(w -> w.toString() + "\n").collect(Collectors.toList()) + '}';
    }

    public String toStringSortedByFrequency() {
        List<WordStatistics> wordStatisticsList = new ArrayList<>(wordToStatisticsMap.values());
        Collections.sort(wordStatisticsList, new WordStatisticsFrequencyComparator());
        return "UserDictionary{" +
                "userId=" + userId +
                "\n" + wordStatisticsList.stream().map(w -> w.toString() + "\n").collect(Collectors.toList()) + '}';
    }
}
