package ua.hlibbabii.langreader.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hlib on 31.05.16.
 */
public class NormalizedText {
    private NormalizedParagraph caption;
    private List<NormalizedParagraph> paragraphs = new ArrayList<>();

    public NormalizedParagraph getCaption() {
        return caption;
    }

    public void setCaption(NormalizedParagraph caption) {
        this.caption = caption;
    }

    public List<NormalizedParagraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<NormalizedParagraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public void addParagraph(NormalizedParagraph normalizedParagraph) {
        paragraphs.add(normalizedParagraph);
    }

    public Collection<String> getAllNormilizedWords() {
        Collection<String> wordsFromAllParagraph = paragraphs.stream()
                .map(p -> p.getAllNormilizedWords())
                .flatMap(p -> p.stream())
                .collect(Collectors.toList());
        if (caption != null) {
            wordsFromAllParagraph.addAll(caption.getAllNormilizedWords());
        }
        return wordsFromAllParagraph;
    }
}
