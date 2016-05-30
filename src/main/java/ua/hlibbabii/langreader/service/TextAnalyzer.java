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

    public String getNextTextForUser(int userId) {
        TextInfo text = textRating.getFirst(userId);
        return (text != null ? text.getId() : null);
    }

    public Map<String, MultiSet<Integer>> getAllWordsWithTextViews(Collection<TextView> allTextViews) {
        Map<String, MultiSet<Integer>> allWordsWithTextViews = new HashMap<>();
        for (TextView textView : allTextViews) {
            int textViewId = textView.getTextViewId();
            Collection<String> allNormilizedWordsCount = textDataSource.getById(textView.getTextId()).getAllNormilizedWords();
            for (String word : allNormilizedWordsCount) {
                MultiSet<Integer> textViewIdMultiSet = allWordsWithTextViews.get(word);
                if (textViewIdMultiSet == null) {
                    textViewIdMultiSet = new HashMultiSet<>();
                }
                textViewIdMultiSet.add(textViewId);
                allWordsWithTextViews.put(word, textViewIdMultiSet);
            }
        }
        return allWordsWithTextViews;
    }

    public UserDictionary getUserDictionary(int userId) {
        UserDictionary userDictionary = new UserDictionary();
        Map<String, List<Integer>> allUnknownWordsWithTextViews = dao.getAllUnknownWordsWithTextViews(userId);
        Map<Integer, TextView> allTextViews = dao.getAllTextViewsForUser(userId);
        Map<String, MultiSet<Integer>> allWordsWithTextViews = getAllWordsWithTextViews(allTextViews.values());
        for (Map.Entry<String, MultiSet<Integer>> wordEntry : allWordsWithTextViews.entrySet()) {
            String word = wordEntry.getKey();
            List<Integer> textViewIdsWithUnknownWord;
            if (allUnknownWordsWithTextViews.containsKey(word)) {
                textViewIdsWithUnknownWord = allUnknownWordsWithTextViews.get(word);
            } else {
                textViewIdsWithUnknownWord = new ArrayList<>();
            }
            MultiSet<Integer> textViewsWithWord = allWordsWithTextViews.get(word);
            WordStatistics wordStatistics = new WordStatistics();
            wordStatistics.setNormalizedForm(word);
            for (Integer textViewId : textViewsWithWord.uniqueSet()) {
                TextView textView = allTextViews.get(textViewId);
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
