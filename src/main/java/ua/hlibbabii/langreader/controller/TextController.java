package ua.hlibbabii.langreader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.hlibbabii.langreader.textsource.TextDataSource;
import ua.hlibbabii.langreader.textsource.TextProcessor;

import java.util.List;

/**
 * Created by hlib on 31.05.16.
 */
@Controller
public class TextController {

    @Autowired
    private TextProcessor textProcessor;

    @Autowired
    private TextDataSource textDataSource;

    @RequestMapping(value = "/texts", method = RequestMethod.GET)
    public String uploadTextPage() {
        return "_texts.html";
    }

    @RequestMapping(value = "/upload-text", method = RequestMethod.POST)
    public
    @ResponseBody
    void uploadText(@RequestParam String text) {
        textProcessor.processText(text);
    }

    @RequestMapping(value = "/remove-all-texts", method = RequestMethod.POST)
    public
    @ResponseBody
    int removeAllTexts() {
        return textDataSource.removeAllTexts();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public
    @ResponseBody
    List<String> searh(@RequestParam String searchPhrase) {
        return textDataSource.search(searchPhrase);
    }
}
