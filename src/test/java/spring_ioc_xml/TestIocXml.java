package spring_ioc_xml;

import core.xml.BeanFactory;
import core.xml.BeanFactoryImpl;
import service.PersonService;

public class TestIocXml {

    public static void main(String[] args) {

        BeanFactory beanFactory = new BeanFactoryImpl("test.xml");
        PersonService personService = (PersonService)beanFactory.getBean("personService");
        System.out.println(personService.getPersonInfo());
    }
}
