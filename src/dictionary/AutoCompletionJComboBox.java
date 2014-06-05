package dictionary;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Interface to search an underlying inventory of items and return a collection of found items.
 *
 * @param <E> The type of items to be found.
 * @param <V> The type of items to be searched
 */
interface Searchable<E, V> {
    /**
     * Searches an underlying inventory of items consisting of type E
     *
     * @param value A searchable value of type V
     * @return A Collection of items of type E.
     */
    public LinkedList<E> search(V value, int wordsCount);
}

/**
 * Implementation of the Searchable interface that searches a List of String objects.
 * <p/>
 * This implementation searches only the beginning of the words, and is not be optimized
 * <p/>
 * for very large Lists.
 */
class StringSearchable implements Searchable<String, String> {
    private DAO dao;

    /**
     * Constructs a new object based upon the parameter terms.
     */
    public StringSearchable(DAO dao) {
        this.dao = dao;
    }

    @Override
    public LinkedList<String> search(String value, int wordsCount) {
        return dao.getEngWordsStartsWith(value, wordsCount);
    }
}

/**
 * JComboBox with an autocomplete drop down menu. This class is hard-coded for String objects, but can be
 * <p/>
 * altered into a generic form to allow for any searchable item.
 */
public class AutoCompletionJComboBox extends JComboBox {
    private static final int WORDS_TO_AUTOCOMPLETE = 400;
    private final Searchable<String, String> searchable;

    /**
     * Constructs a new object based upon the parameter searchable
     *
     * @param s
     */
    public AutoCompletionJComboBox(Searchable<String, String> s, JTextArea translationTextArea, DAO dao) {
        super();
        this.searchable = s;
        setEditable(true);
        Component c = getEditor().getEditorComponent();

        if (c instanceof JTextField) {
            final JTextField tc = (JTextField) c;
            tc.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent arg0) {
                }

                @Override
                public void insertUpdate(DocumentEvent arg0) {
                    update();
                }

                @Override
                public void removeUpdate(DocumentEvent arg0) {
                    update();
                }

                public void update() {
                    tc.setSelectedTextColor(Color.BLACK);
                    //perform separately, as listener conflicts between the editing component
                    //and JComboBox will result in an IllegalStateException due to editing
                    //the component when it is locked.
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {

                            LinkedList<String> founds = new LinkedList<String>(searchable.search(tc.getText(), WORDS_TO_AUTOCOMPLETE));
                            Collections.sort(founds);//sort alphabetically
                            setEditable(false);
                            removeAllItems();

                            //if founds contains the search text, then only add once
                            if (!founds.contains(tc.getText())) {
                                addItem(tc.getText());
                            }
                            for (String s : founds) {
                                addItem(s);
                            }
                            setEditable(true);
                            requestFocusInWindow();

                            setPopupVisible(true);
                        }
                    });
                }
            });


            //When the text component changes, focus is gained
            //and the menu disappears. To account for this, whenever the focus
            //is gained by the JTextComponent and it has searchable values, we show the popup.
            tc.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent arg0) {
                    if (tc.getText().length() > 0) {
                        setPopupVisible(true);
                    }
                }

                @Override
                public void focusLost(FocusEvent arg0) {
                }
            });

            tc.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (dao.isHoldingTranslation(tc.getText().trim())) {
                            //перевод есть - переводим
                            translationTextArea.setText(dao.getTranslation(tc.getText().trim()));
                        } else {
                            tc.selectAll();
                            tc.setSelectedTextColor(Color.RED);
                        }

                    }
                }
            });
        } else {
            throw new IllegalStateException("Editing component is not a JTextComponent!");
        }
    }
}
