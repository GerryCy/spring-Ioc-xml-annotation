package service;

import bean.Person;
import core.annotation_version.annotation.AutoWried;
import core.annotation_version.annotation.Service;

@Service
public class PersonService {

    @AutoWried
    private Person person;

    public Person getPersonInfo() {
        Person person = new Person();
        person.setId(1);
        person.setName("test");
        return person;
    }
}
