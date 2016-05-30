package ua.hlibbabii.langreader.textsource;

import ua.hlibbabii.langreader.text.NormalizedText;

import java.util.List;
import java.util.Set;

/**
 * Created by hlib on 27.05.16.
 */
public interface TextDataSource {
    String save(NormalizedText normalizedParagraph);
    NormalizedText getById(String id);
    Set<String> getAllTextIds();
    Set<String> getAllTextSnippets();
    long getNumberOfAvailableTexts();
    int removeAllTexts();
    List<String> search(String searchPhrase);
}
