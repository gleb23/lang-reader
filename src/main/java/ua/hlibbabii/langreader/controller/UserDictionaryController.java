package ua.hlibbabii.langreader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.hlibbabii.langreader.service.TextAnalyzer;
import ua.hlibbabii.langreader.words.UserDictionary;

/**
 * Created by hlib on 26.01.17.
 */
@Controller
public class UserDictionaryController {

    @Autowired
    private TextAnalyzer textAnalyzer;

    @RequestMapping(value = "/dictionary", method = RequestMethod.GET)
    public String dictionaryPage() {
        return "user_dictionary.html";
    }

    @RequestMapping(value = "/get-dictionary", method = RequestMethod.GET)
    public
    @ResponseBody
    UserDictionary getDictionary(@RequestParam int userId) {
        return textAnalyzer.getUserDictionary(userId);
    }
}
