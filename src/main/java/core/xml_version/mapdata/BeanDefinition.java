package core.xml_version.mapdata;

import core.xml_version.config.BeanInfo;
import core.xml_version.config.PropertyInfo;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将xml中的信息存到Map数据结构中，我们使用dom4j来解析xml文件
 */
public class BeanDefinition {

    //用来存放解析xml文件后的map数据结构
    private static Map<String , BeanInfo> xmlInfo = new HashMap<String, BeanInfo>();

    /**
     * 读取配置文件，
     * key：配置的bean的id
     * value：配置的class类对应的对象(我们在BeanInfo中配置的scope是singleton)
     * @return
     */
    public Map<String ,BeanInfo> getXmlInfo(String xmlPath) {
        //创建解析器
        SAXReader saxReader = new SAXReader();
        //获取配置文件对应的输入流
        InputStream inputStream = getXMLInputStream(xmlPath);
        //得到对应的Document对象
        Document document = null;
        try {
            document = saxReader.read(inputStream);
        } catch (DocumentException e) {
            System.out.println("请检查配置文件路径");
            e.printStackTrace();
        }
        //获取根节点
        Element root = document.getRootElement();
        //获取所有bean的子节点
        List<Element> beanElementList = root.elements();
        //遍历子节点集合，将每个bean的id，class，properties封装到BeanInfo中
        for (Element bean : beanElementList) {
            BeanInfo beanInfo = new BeanInfo();
            String id = bean.attributeValue("id");
            String classPath = bean.attributeValue("class");
            String scope = bean.attributeValue("scope");
            beanInfo.setId(id);
            beanInfo.setClassPath(classPath);
            //接下来是得到scope属性，判断是否单例
            if(scope != null) {
                beanInfo.setScope(scope);
            }
            // 第二层循环是获得bean下面的property子节点的信息，将这些信息封装到一个个Property对象中，
            // 然后将之添加到刚刚创建的beanInfo对象中
            List<Element> propertyElementList = bean.elements();
            for (Element property :propertyElementList ) {
                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName(property.attributeValue("name"));
                propertyInfo.setValue(property.attributeValue("value"));
                propertyInfo.setRef(property.attributeValue("ref"));
                //将该bean下的Property添加到对应的List集合中
                beanInfo.getPropertList().add(propertyInfo);
            }
            xmlInfo.put(id,beanInfo);
        }
        return xmlInfo;
    }

    /**
     * 通过xml路径获取配置文件输入流
     *
     * @return
     */
    private InputStream getXMLInputStream(String xmlPath) {
        return this.getClass().getClassLoader().getResourceAsStream(xmlPath); //返回读取的xml的InputStream
    }
}
