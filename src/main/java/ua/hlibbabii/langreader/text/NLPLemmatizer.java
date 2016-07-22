package ua.hlibbabii.langreader.text;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by hlib on 03.04.16.
 */
@Service
public class NLPLemmatizer implements Lemmatizer {

    private final static Logger logger = Logger.getLogger(NLPLemmatizer.class);

    private final static String STANFORD_NLP_BASE_PATH =
            "/home/hlib/dev/js/langreader/langreader-java-rest/src/main/thirdParty/";

    private static final int MAXIMUM_STRING_SIZE_TO_BE_A_CAPTION = 60;

    private final List<PartOfSpeech> nonLearnablePosList = Arrays.asList(PartOfSpeech.FOREIGN_WORD, PartOfSpeech
            .LIST_ITEM_MARKER, PartOfSpeech.NOUN_PROPER_PLURAL, PartOfSpeech.NOUN_PROPER_SINGULAR, PartOfSpeech.HASH,
            PartOfSpeech.DOLLAR_SIGN, PartOfSpeech.OPENING_QUOTE, PartOfSpeech.CLOSING_QUOTE, PartOfSpeech
                    .OPENING_PARANTHESIS, PartOfSpeech.CLOSING_PARANTHESIS, PartOfSpeech.COMMA, PartOfSpeech
                    .SENTENCE_TERMINATOR, PartOfSpeech.SENTENCE_SEPARATOR);

    private StanfordCoreNLP stanfordCoreNLP;

    public NLPLemmatizer() {
    }

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, parse");
        props.put("pos.model", STANFORD_NLP_BASE_PATH +
                "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
        props.put("parse.model", STANFORD_NLP_BASE_PATH + "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        this.stanfordCoreNLP = new StanfordCoreNLP(props);
    }

    private NormalizedParagraph processParagraph(String text) {
        Annotation document = new Annotation(text);
        this.stanfordCoreNLP.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        NormalizedParagraph normalizedParagraph = new NormalizedParagraph();
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String originalText = token.originalText();
                try {
                    PartOfSpeech pos = PartOfSpeech.get(token.tag());
                    String lemma = token.lemma();
                    if (nonLearnablePosList.contains(pos)) {
                        normalizedParagraph.addUnlearnableText(originalText);
                    } else {
                        normalizedParagraph.addLearnableWord(originalText, lemma);
                    }
                } catch (Exception ex) {
                    logger.error("Uknown POS (" + token.tag() + ") for \"" + originalText + "\"");
                    normalizedParagraph.addUnlearnableText(originalText);
                }
            }
        }

        return normalizedParagraph;
    }

    @Override
    public NormalizedText process(String text) {
        NormalizedText normalizedText = new NormalizedText();
        String[] paragraphs = text.split("\n");
        NormalizedParagraph normalizedFirstParagraph = processParagraph(paragraphs[0]);
        if (paragraphs[0].length() <= MAXIMUM_STRING_SIZE_TO_BE_A_CAPTION) {
            normalizedText.setCaption(normalizedFirstParagraph);
        } else {
            normalizedText.addParagraph(normalizedFirstParagraph);
        }
        for (int i = 1; i < paragraphs.length; i++) {
            NormalizedParagraph normalizedParagraph = processParagraph(paragraphs[i]);
            normalizedText.addParagraph(normalizedParagraph);
        }
        return normalizedText;
    }
}
