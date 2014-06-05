package dictionary;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JFrame {

    public AboutDialog(int translationsCount, int xPosition, int yPosition) {
        FlowLayout flowLayout = new FlowLayout();
        setLayout(flowLayout);
        JTextArea textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setText("    Программа представляет собой англо-русский словарь\n" +
                "с возможностю добавлять новые слова. Интеллектуальная\nсистема подсказок " +
                "делает поиск более простым. В словаре\nсодержиться " +
                translationsCount + " переводов слов\n" +
                "   Выполнено 15 апреля 2014г. Автор Селиванов Андрей.");
        setBounds(xPosition, yPosition, getWidth(), getHeight());
        textArea.setEditable(false);
        setResizable(false);
        setTitle("О словаре");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        add(textArea);
        pack();
        setVisible(true);
    }
}
