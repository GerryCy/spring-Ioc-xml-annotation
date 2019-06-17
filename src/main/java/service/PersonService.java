package service;

import bean.Person;

public class PersonService {

    public Person getPersonInfo() {
        Person person = new Person();
        person.setId(1);
        person.setName("test");
        return person;
    }
}
