package ua.hlibbabii.langreader.text

import org.apache.commons.io.IOUtils;

/**
 * Created by hlib on 04.04.16.
 */
class NLPLemmatizerTest extends GroovyTestCase {

    private String text1 = IOUtils.toString(new FileInputStream("/home/hlib/dev/js/langreader/langreader-java-rest/src/main/resources/corpus/Article247_4.txt"));
    private String text2 = "\'Hello\'!";

    void testProcess() {
        def nlpLemmatizer = new NLPLemmatizer()
        nlpLemmatizer.init()
        def normalizedText = nlpLemmatizer.process(text2)
        println normalizedText.toJson();
    }
}
