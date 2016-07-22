package ua.hlibbabii.langreader.words;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by hlib on 03.04.16.
 */
public class WordStatistics {
    private String normalizedForm;
    private Optional<KnowledgeRate> initialKnowledgeRate = Optional.empty();
    private List<WordOccurrence> wordOccurrences = new ArrayList<>();

    public String getNormalizedForm() {
        return normalizedForm;
    }

    public void setNormalizedForm(String normalizedForm) {
        this.normalizedForm = normalizedForm;
    }

    public void addWordOccurrence(ZonedDateTime dateTime, boolean known, int textNumber, int wordOccurencesInText) {
        wordOccurrences.add(new WordOccurrence(dateTime, known, textNumber, wordOccurencesInText));
    }

    @Override
    public String toString() {
        return "WordStatistics{" +
                "normalizedForm='" + normalizedForm + '\'' +
                ", initialKnowledgeRate=" + initialKnowledgeRate +
                ", " + wordOccurrences +
                '}';
    }

    public KnowledgeRate getKnowledgeRate() {
        return KnowledgeRate.AVERAGE;
    }

    private WordOccurrence getLastWordOccurrence() {
        return wordOccurrences.get(wordOccurrences.size() - 1);
    }

    public double getPercentOfTextsWordAppears() {
        WordOccurrence lastWordOccurrence = getLastWordOccurrence();
        return (double) wordOccurrences.size() / lastWordOccurrence.textNumber;
    }

    public int getNearestNextDesiredTextNumberWithThisWord() {
        // get next number based on several last occurences
        return 100;
    }

    private static class WordOccurrence {
        private ZonedDateTime dateTime;
        private boolean known;
        private int textNumber;
        private int wordOccurrencesInText;

        public WordOccurrence(ZonedDateTime dateTime, boolean known, int textNumber, int wordOccurrencesInText) {
            this.dateTime = dateTime;
            this.known = known;
            this.textNumber = textNumber;
            this.wordOccurrencesInText = wordOccurrencesInText;
        }

        @Override
        public String toString() {
            String hoursAgo = String.valueOf(ChronoUnit.MINUTES.between(dateTime, ZonedDateTime.now()));
            if (!known) {
                hoursAgo = "!!! " + hoursAgo + " !!!";
            }
            hoursAgo += " [" + wordOccurrencesInText + "]";
            return hoursAgo;
        }
    }
}
