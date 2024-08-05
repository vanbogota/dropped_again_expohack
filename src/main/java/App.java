import View.partnerRegForm;
import config.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        partnerRegForm partnerRegForm = context.getBean("regForm", partnerRegForm.class);
        partnerRegForm.setContext(context); // Установите контекст перед вызовом mainWindow()
    }

}
