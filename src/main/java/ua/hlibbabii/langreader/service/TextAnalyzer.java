package ua.hlibbabii.langreader.service;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.hlibbabii.langreader.dao.Dao;
import ua.hlibbabii.langreader.text.TextInfo;
import ua.hlibbabii.langreader.text.TextView;
import ua.hlibbabii.langreader.textsource.TextDataSource;
import ua.hlibbabii.langreader.words.UserDictionary;
import ua.hlibbabii.langreader.words.WordStatistics;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created by hlib on 27.05.16.
 */
@Service
public class TextAnalyzer {

    @Autowired
    private TextRating textRating;

    @Autowired
    private Dao dao;

    @Autowired
    private TextDataSource textDataSource;

    private Map<Integer, UserDictionary> userDictionaries = new HashMap<>();

    public String getNextTextForUser(int userId) {
        TextInfo text = textRating.getFirst(userId);
        return (text != null ? text.getId() : null);
    }

    /**
     * Returns the map where the keys are normalized words that can be found in @param{textViews}, values are multisets
     * of testViewIds that contain the word in the key.
     */
    public Map<String, MultiSet<Integer>> getWordsFromTextViews(Collection<TextView> textViews) {
        Map<String, MultiSet<Integer>> wordsToTextViewsMap = new HashMap<>();
        for (TextView textView : textViews) {
            int textViewId = textView.getTextViewId();
            Collection<String> allNormilizedWordsFromText = textDataSource.getById(textView.getTextId())
                    .getAllNormilizedWords();
            for (String normalizedWord : allNormilizedWordsFromText) {
                MultiSet<Integer> textViewIdMultiSet = wordsToTextViewsMap.get(normalizedWord);
                if (textViewIdMultiSet == null) {
                    textViewIdMultiSet = new HashMultiSet<>();
                }
                textViewIdMultiSet.add(textViewId);
                wordsToTextViewsMap.put(normalizedWord, textViewIdMultiSet);
            }
        }
        return wordsToTextViewsMap;
    }

    public synchronized UserDictionary getUserDictionary(int userId) {
        UserDictionary userDictionary = userDictionaries.get(userId);
        if (userDictionary == null) {
            userDictionary = calculateUserDictionary(userId);
            userDictionaries.put(userId, userDictionary);
        }
        return userDictionary;
    }

    public UserDictionary calculateUserDictionary(int userId) {
        Map<String, List<Integer>> unknownWordsToTextViewsMap = dao.getAllUnknownWordsWithTextViews(userId);
        Map<Integer, TextView> allUserTextViews = dao.getAllTextViewsByUser(userId);
        Map<String, MultiSet<Integer>> allWordsWithTextViewsMap = getWordsFromTextViews(allUserTextViews.values());

        UserDictionary userDictionary = new UserDictionary();
        for (Map.Entry<String, MultiSet<Integer>> wordEntry : allWordsWithTextViewsMap.entrySet()) {
            String word = wordEntry.getKey();
            MultiSet<Integer> textViewsWithWord = wordEntry.getValue();
            List<Integer> textViewIdsWithUnknownWord;
            boolean wordIsUnknown = unknownWordsToTextViewsMap.containsKey(word);
            if (wordIsUnknown) {
                textViewIdsWithUnknownWord = unknownWordsToTextViewsMap.get(word);
            } else {
                textViewIdsWithUnknownWord = Collections.emptyList();
            }
            WordStatistics wordStatistics = new WordStatistics();
            wordStatistics.setNormalizedForm(word);
            for (Integer textViewId : textViewsWithWord.uniqueSet()) {
                TextView textView = allUserTextViews.get(textViewId);
                wordStatistics.addWordOccurrence(
                        convertDateToZonedDateTime(textView.getDateTime()),
                        !textViewIdsWithUnknownWord.contains(textViewId),
                        textView.getTextNumber(),
                        textViewsWithWord.getCount(textViewId)
                );
            }
            userDictionary.add(wordStatistics);
        }
        userDictionary.setUserId(userId);
        return userDictionary;
    }

    private ZonedDateTime convertDateToZonedDateTime(Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
