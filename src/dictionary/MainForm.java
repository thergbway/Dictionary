package dictionary;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainForm extends JFrame {
    private JPanel rootPanel;
    private JTextArea translationArea;
    private AutoCompletionJComboBoxAsJPanel autoCompletionJComboBoxAsJPanel;
    private JButton aboutButton;
    private JButton addNewWordButton;
    private JTextField newWordTextField;
    private JTextField newTranslationTextField;
    private JLabel addingNewWordLabel;
    private DAO dao;
    private AboutDialog aboutDialog;

    public MainForm(final DAO dao) {
        this.dao = dao;
        setTitle("Англо-русский словарь. 46000 слов. Версия 1.0");

        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setBounds(200, 200, 800, 600);
        setVisible(true);
        pack();
        setResizable(false);

        addNewWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (newWordTextField.getText() == null || newWordTextField.getText().equals("")) {
                    addingNewWordLabel.setText("Введите слово");
                    return;
                }
                if (newTranslationTextField.getText() == null || newTranslationTextField.getText().equals("")) {
                    addingNewWordLabel.setText("Введите перевод");
                    return;
                }
                if (dao.isHoldingTranslation(newWordTextField.getText().trim())) {
                    addingNewWordLabel.setText("Такое слово уже есть в словаре");
                    return;
                }
                dao.addTranslation(newWordTextField.getText(), newTranslationTextField.getText());
                addingNewWordLabel.setText("Слово успешно добавлено в словарь");
            }
        });
        newWordTextField.addKeyListener(new
                                                KeyAdapter() {
                                                    @Override
                                                    public void keyPressed(KeyEvent e) {
                                                        if (e.getKeyCode() == KeyEvent.VK_ENTER)
                                                            newTranslationTextField.requestFocusInWindow();
                                                    }
                                                });
        newTranslationTextField.addKeyListener(new
                                                       KeyAdapter() {
                                                           @Override
                                                           public void keyPressed(KeyEvent e) {
                                                               if (e.getKeyCode() == KeyEvent.VK_ENTER)
                                                                   addNewWordButton.requestFocusInWindow();
                                                           }
                                                       });
        addNewWordButton.addKeyListener(new
                                                KeyAdapter() {
                                                    @Override
                                                    public void keyPressed(KeyEvent e) {
                                                        if (e.getKeyCode() == KeyEvent.VK_ENTER)
                                                            addNewWordButton.doClick();
                                                    }
                                                });
        aboutButton.addActionListener(new
                                              ActionListener() {
                                                  @Override
                                                  public void actionPerformed(ActionEvent e) {
                                                      if (aboutDialog != null)
                                                          aboutDialog.dispose();
                                                      aboutDialog = new AboutDialog(dao.getTotalTranslationsCount(), getWidth() / 2, getHeight() / 2);
                                                  }
                                              });
    }

    private void createUIComponents() {
        translationArea = new JTextArea();
        autoCompletionJComboBoxAsJPanel = new AutoCompletionJComboBoxAsJPanel(dao, translationArea);
        pack();
    }
}
