package spring_ioc_xml;

import bean.DataSource;
import bean.SqlSessionFactoryBean;
import core.xml_version.beanfactory.BeanFactory;
import core.xml_version.beanfactory.BeanFactoryImpl;
import service.PersonService;

public class TestIocXml {

    public static void main(String[] args) {

        BeanFactory beanFactory = new BeanFactoryImpl("test.xml");
        //这里先作为间隔，验证上面调用Bean工厂实现类的时候，IOC容器中配置为singleton的bean都已添加到容器中，
        //下面调用getBean是直接荣容器中取，而不是再创建
        System.out.println("下面的bean实例直接从IOC容器中取出");
        PersonService personService = (PersonService)beanFactory.getBean("personService");
        System.out.println(personService.getPersonInfo());

        DataSource dataSource = (DataSource) beanFactory.getBean("dataSource");
        System.out.println(dataSource);

        SqlSessionFactoryBean sqlSessionFactoryBean = (SqlSessionFactoryBean) beanFactory.getBean("sqlSessionFactory");
        System.out.println(sqlSessionFactoryBean);
    }
}
