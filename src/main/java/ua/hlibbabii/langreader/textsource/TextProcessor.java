package ua.hlibbabii.langreader.textsource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.hlibbabii.langreader.text.Lemmatizer;
import ua.hlibbabii.langreader.text.NormalizedText;

/**
 * Created by hlib on 27.05.16.
 */
@Service
public class TextProcessor {

    @Autowired
    Lemmatizer lemmatizer;

    @Autowired
    TextDataSource textDataSource;

    public String processText(String text) {
        NormalizedText normalizedText = lemmatizer.process(text);
        String url = textDataSource.save(normalizedText);
        return url;
    }
}
