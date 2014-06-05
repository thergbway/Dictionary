package dictionary;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    private static final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config-dictionary.xml");
    private static final DAO dao = (DAO) applicationContext.getBean("DAOImpl");

    public static void main(String[] args) {
        new MainForm(dao);
    }
}
