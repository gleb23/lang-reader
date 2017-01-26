package ua.hlibbabii.langreader.service

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.test.context.ContextConfiguration
import ua.hlibbabii.langreader.dao.Dao
import ua.hlibbabii.langreader.text.TextView
import ua.hlibbabii.langreader.textsource.TextDataSource

/**
 * Created by hlib on 25.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = ["classpath:spring/spring.xml"])
class TextAnalyzerTest extends GroovyTestCase {

    @Mock
    Dao dao;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TextDataSource textDataSource;

    @InjectMocks
    TextAnalyzer textAnalyzer = new TextAnalyzer();

    @Test
    void calculateUserDictionaryTest() throws Exception {
        Mockito.when(dao.getAllUnknownWordsWithTextViews(1)).thenReturn([dog: [1, 2, 3], cat: [1, 2]])

        def textView1 = new TextView(1, 1, new Date(), "text1", null, 0);
        def textView2 = new TextView(2, 1, new Date(), "text2", null, 0);
        def textView3 = new TextView(3, 1, new Date(), "text3", null, 0);
        Mockito.when(dao.getAllTextViewsByUser(1)).thenReturn([1: textView1, 2: textView2, 3: textView3])

        Mockito.when(textDataSource.getById("text1").allNormilizedWords).thenReturn(["dog", "cat", "text1"])
        Mockito.when(textDataSource.getById("text2").allNormilizedWords).thenReturn(["dog", "cat", "text2"])
        Mockito.when(textDataSource.getById("text3").allNormilizedWords).thenReturn(["dog", "text3"])

        print textAnalyzer.getUserDictionary(1)

        //TODO add assertion
    }

}