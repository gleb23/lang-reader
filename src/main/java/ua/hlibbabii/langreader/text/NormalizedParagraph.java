package ua.hlibbabii.langreader.text;

import com.google.gson.Gson;

import java.util.*;

/**
 * Created by hlib on 03.04.16.
 */
public class NormalizedParagraph {
    private List<String> text = new ArrayList<>();
    private Map<Integer, String> normalized = new HashMap<>();

    public NormalizedParagraph() {
    }

    public void addLearnableWord(String originalText, String lemma) {
        int index = text.size();
        text.add(originalText);
        normalized.put(index, lemma);
    }

    public void addUnlearnableText(String unlearnableText) {
        text.add(unlearnableText);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.size(); i++) {
            stringBuilder.append(text.get(i));
            String normalizedWord = normalized.get(i);
            if (normalizedWord != null) {
                stringBuilder.append("[" + normalizedWord + "]");
            }
        }
        return stringBuilder.toString();
    }

    public String toJson() {
        return new Gson().toJson(this, this.getClass());
    }

    public List<String> getText() {
        return text;
    }

    public Map<Integer, String> getNormalized() {
        return normalized;
    }

    public Collection<String> getAllNormilizedWords() {
        return normalized.values();
    }
}
