package annotation;

import core.annotation_version.beanfactory.BeanFactory;
import lombok.SneakyThrows;
import service.PersonService;

public class TestIOC {

    @SneakyThrows
    public static void main(String[] args) {
        BeanFactory beanFactory = new BeanFactory("service");
        PersonService personService = (PersonService) beanFactory.getBean("personService");
        System.out.println(personService.getPersonInfo());
    }
}
