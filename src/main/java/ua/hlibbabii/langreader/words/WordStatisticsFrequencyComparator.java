package ua.hlibbabii.langreader.words;

import java.util.Comparator;

/**
 * Created by hlib on 31.05.16.
 */
public class WordStatisticsFrequencyComparator
                            implements Comparator<WordStatistics> {
    @Override
    public int compare(WordStatistics o1, WordStatistics o2) {
        return Double.compare(o1.getPercentOfTextsWordAppears(),
                o2.getPercentOfTextsWordAppears());
    }
}
