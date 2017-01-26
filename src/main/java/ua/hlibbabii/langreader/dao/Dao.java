package ua.hlibbabii.langreader.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ua.hlibbabii.langreader.text.TextView;

import javax.annotation.PostConstruct;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hlib on 18.04.16.
 */
@Repository
public class Dao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert textViewSimpleJdbcInsert;
    private SimpleJdbcInsert unknownWordOcurrenceJdbcInsert;

    @PostConstruct
    public void init() {
        textViewSimpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("TEXT_VIEW")
                                                                     .usingColumns("textId", "userId", "dateTime")
                                                                     .usingGeneratedKeyColumns("textViewId");

        unknownWordOcurrenceJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("UNKNOWN_WORD_OCURRENCE")
                                                                           .usingColumns("userId", "textViewId",
                                                                                   "normalizedForm");
    }

    public void addWordOccurence(int userId, String textViewId, String normalizedForm) {
        unknownWordOcurrenceJdbcInsert.execute(new HashMap<String, Object>() {{
            put("userId", userId);
            put("textViewId", textViewId);
            put("normalizedForm", normalizedForm);
        }});
    }

    public int addTextView(String textId, int userId, Date timestamp) {
        return textViewSimpleJdbcInsert.executeAndReturnKey(new HashMap<String, Object>() {{
            put("textId", textId);
            put("userId", userId);
            put("dateTime ", timestamp);
        }}).intValue();
    }

    public Map<String, List<Integer>> getAllUnknownWordsWithTextViews(int userId) {
        Map<String, List<Integer>> result = new HashMap<>();
        String sql = "SELECT w.normalizedForm, t.textViewId from UNKNOWN_WORD_OCURRENCE as w INNER JOIN TEXT_VIEW as " +
                "" + "t on w.textViewId = t.textViewId inner join USER as u on u.userId = t.userId where u.userId = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, new Object[]{userId}, new int[]{Types.INTEGER});
        for (Map<String, Object> map : maps) {
            String normalizedForm = (String) map.get("normalizedForm");
            List<Integer> textViewIds = result.get(normalizedForm);
            if (textViewIds == null) {
                textViewIds = new ArrayList<>();
            }
            textViewIds.add((Integer) map.get("textViewId"));
            result.put(normalizedForm, textViewIds);
        }
        return result;
    }

    public Map<Integer, TextView> getAllTextViewsByUser(int userId) {
        String sql = "SELECT * from TEXT_VIEW as t where t.userId = ? order by t.datetime";

        List<TextView> textViews = jdbcTemplate.query(sql, new Object[]{userId}, new int[]{Types.INTEGER}, (rs,
                                                                                                            rowNum) -> {
            TextView textView = new TextView();
            textView.setTextViewId(rs.getInt("textViewId"));
            textView.setTextId(rs.getString("textId"));
            textView.setUserId(rs.getInt("userId"));
            textView.setDateTime(rs.getTimestamp("dateTime"));
            textView.setTextNumber(rowNum);
            return textView;
        });
        Map<Integer, TextView> result = textViews.stream()
                                                 .collect(Collectors.toMap(tw -> tw.getTextViewId(), tw -> tw));
        return result;
    }
}
