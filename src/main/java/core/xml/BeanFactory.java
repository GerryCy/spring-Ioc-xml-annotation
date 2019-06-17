package core.xml;

/**
 * 获取bean的接口，只一个方法
 */
public interface BeanFactory {

    Object getBean(String beanId);
}
