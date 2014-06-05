package dictionary;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DAOImpl implements DAO {
    JdbcTemplate jdbcTemplate;
    HashSet<String> engWordsHolding;

    synchronized public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        engWordsHolding = new HashSet<>(jdbcTemplate.query("select eng from dictionary", (resultSet, i) -> new String(resultSet.getString("eng"))));
    }

    @Override
    synchronized public LinkedList<String> getEngWordsStartsWith(String str, int count) {
        LinkedList<String> results = new LinkedList<>();
        Iterator<String> it = engWordsHolding.iterator();

        while (it.hasNext()) {
            String s = it.next();
            if (s.startsWith(str)) {
                results.add(s);
                if (results.size() >= count)
                    break;
            }

        }
        return results;
    }

    @Override
    synchronized public boolean isHoldingTranslation(String engWord) {
        return engWordsHolding.contains(engWord);
    }

    @Override
    synchronized public String getTranslation(String engWord) {
        List<String> strings;
        strings = jdbcTemplate.query("select rus from dictionary where eng = \"" + engWord + "\"", (resultSet, i) -> new String(resultSet.getString("rus")));
        //strings = jdbcTemplate.query("select rus from dictionary where eng = \'" + engWord + "\'", (resultSet, i) -> new String(resultSet.getString("rus")));
        if (strings.size() == 0)
            return null;
        else
            return strings.get(0);
    }

    @Override
    synchronized public void addTranslation(String engWord, String rusWord) {
        if (isHoldingTranslation(engWord))
            return;

        jdbcTemplate.update("insert into dictionary (eng, rus) " +
                "values (\'" + engWord + "\',\'" + rusWord + "\')");

        engWordsHolding.add(engWord);
    }

    @Override
    synchronized public int getTotalTranslationsCount() {
        return engWordsHolding.size();
    }
}
