package springIoc.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 自定义spring ioc，使用xml方式实现
 */
public class ClassPathXmlApplicationContext {
    /**
     * 大体步骤就是
     * ①解析xml
     * ②使用getBean方法传入的参数bean-id在配置文件中查找一致的id
     * ③找到对应的class值，然后通过反射创建对象
     */

    private String xmlPath; //xml配置文件的路径

    public ClassPathXmlApplicationContext(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    /**
     * getBean方法返回一个Object，具体使用时候强转即可
     *
     * @param beanId
     * @return
     */
    public Object getBean(String beanId) {
        if (null == beanId || beanId.trim().equals("")) {
            throw new RuntimeException("bean-id is null or empty");
        }
        //①所有所有结点信息
        List<Element> elements = readXMLInfo();
        if (null == elements || elements.isEmpty()) { //配置文件中没有配置信息
            throw new RuntimeException(xmlPath + " is empty");
        }
        String classPath = getClassPath(elements,beanId);
        if(null == classPath) {
            throw new RuntimeException("找不到对应的beanId或class值未配置");
        } else if(classPath.trim().equals("")) {
            throw new RuntimeException("请检查对应bean的class属性值");
        }
        return getInstance(classPath);
    }

    /**
     * 查找指定beanId对应的class类路径
     * @param elements
     * @param beanId
     * @return
     */
    private String getClassPath(List<Element> elements, String beanId) {
        //通过传入的beanId进行查找
        for (Element element : elements) {
            String xmlBeanId = element.attributeValue("id"); //找到配置文件中的id属性然后进行对比
            if (null == xmlBeanId || xmlBeanId.trim().equals("")) {
                continue;
            }
            if (xmlBeanId.equals(beanId)) {
                //找到对应的class属性值
                return element.attributeValue("class");
            }
        }
        return null;
    }

    /**
     * 通过传递的class类路径反射创建对象
     *
     * @param classPath
     * @return
     */
    private Object getInstance(String classPath) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            System.out.println("找不到对应的类,请检查对应的class配置值");
            e.printStackTrace();
        }
        //获得所有构造器
        Constructor constructor = null;
        try {
            constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            System.out.println("没有无参构造");
            e.printStackTrace();
        }
        constructor.setAccessible(true);
        Object obj = null;
        try {
            obj = constructor.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 读取xml信息
     *
     * @return
     */
    private List<Element> readXMLInfo() {
        //①解析读取xml文件信息
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(getXMLInputStream());
        } catch (DocumentException e) {
            System.out.println("请检查配置文件路径");
            e.printStackTrace();
        }
        //②读根节点
        Element root = document.getRootElement();
        //③获取根节点下面的子节点
        List<Element> elements = root.elements();
        return elements;
    }

    /**
     * 通过xml路径获取配置文件输入流
     *
     * @return
     */
    private InputStream getXMLInputStream() {
        return this.getClass().getClassLoader().getResourceAsStream(xmlPath); //返回读取的xml的InputStream
    }
}
