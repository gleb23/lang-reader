package ua.hlibbabii.langreader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.hlibbabii.langreader.dao.Dao;
import ua.hlibbabii.langreader.dao.TextReader;
import ua.hlibbabii.langreader.service.TextAnalyzer;
import ua.hlibbabii.langreader.text.NormalizedText;
import ua.hlibbabii.langreader.text.TextView;
import ua.hlibbabii.langreader.textsource.TextDataSource;

import java.util.Date;

/**
 * Created by hlib on 03.04.16.
 */
@RestController
public class AddNewWordController {

    private static int nextTextViewId = 1000;

    private int getNextTextViewId() {
        return nextTextViewId++;
    }

    private int userId = 1;

    @Autowired
    private Dao dao;

    @Autowired
    private TextReader textReader;

    @Autowired
    private TextAnalyzer textAnalyzer;

    @Autowired
    private TextDataSource textDataSource;

    @RequestMapping(value = "/{textViewId}/newword", method = RequestMethod.POST)
    public void addNewWord(@PathVariable String textViewId, @RequestParam String normalized) {
        dao.addWordOccurence(userId, textViewId, normalized);
    }

    @RequestMapping(value = "/next", method = RequestMethod.GET)
    public TextView getNextText() {
        System.out.println(textAnalyzer.getUserDictionary(userId).toStringSortedByFrequency());
        try {
            String textId = textAnalyzer.getNextTextForUser(userId);
            if (textId == null) {
                throw new IllegalStateException("No text found");
            }
            NormalizedText normalizedParagraph = textDataSource.getById(textId);
            Date timestamp = new Date();
            int nextTextViewId = dao.addTextView(textId, userId, timestamp);
            return new TextView(nextTextViewId, userId, timestamp, null, normalizedParagraph, 0);
        } catch (Exception e) {
            throw new RuntimeException(e); //TODO pass error to the client
        }
    }
}
