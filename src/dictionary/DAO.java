package dictionary;

import java.util.LinkedList;

public interface DAO {
    LinkedList<String> getEngWordsStartsWith(String str, int count);

    boolean isHoldingTranslation(String engWord);

    String getTranslation(String engWord);

    void addTranslation(String engWord, String rusWord);

    int getTotalTranslationsCount();
}
