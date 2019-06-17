package ioc;

import bean.Person;
import service.PersonService;
import springIoc.xml.ClassPathXmlApplicationContext;

public class TestIoc {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("test.xml");
        PersonService personService = (PersonService) classPathXmlApplicationContext.getBean("personService");
        Person person = personService.getPersonInfo();
        System.out.println(person);
    }
}
