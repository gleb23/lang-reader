package ua.hlibbabii.langreader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.hlibbabii.langreader.text.TextInfo;
import ua.hlibbabii.langreader.textsource.TextDataSource;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hlib on 28.05.16.
 */
@Service
public class TextRating {

    @Autowired
    private TextDataSource textDataSource;

    private List<TextInfo> textInfoList;

    @PostConstruct
    public void init() {
        textInfoList = textDataSource.getAllTextIds().stream()
                .map(id -> new TextInfo(id))
                .collect(Collectors.toList());
        System.out.println(textInfoList);
    }

    public TextInfo getFirst(int userId) {
        return textInfoList.get(c++ % textInfoList.size());
    }
    
    private static int c = 0;
}
