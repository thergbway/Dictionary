package dictionary;

import javax.swing.*;
import java.awt.*;

public class AutoCompletionJComboBoxAsJPanel extends JPanel {
    public AutoCompletionJComboBoxAsJPanel(DAO dao, JTextArea translationTextArea) {
        super(new FlowLayout());

        add(new AutoCompletionJComboBox(new StringSearchable(dao), translationTextArea, dao));
    }
}