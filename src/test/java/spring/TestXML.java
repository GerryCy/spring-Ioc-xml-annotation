package spring;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class TestXML {

    public static void main(String[] args) throws DocumentException {
        TestXML testXML = new TestXML();
        testXML.get();
    }

    //获取配置文件信息
    public void get() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        //读取xml文件信息
        Document document = saxReader.read(getXMLInputStream("test.xml"));
        //读根节点
        Element root = document.getRootElement();
        getNodes(root);
    }

    //获取节点的属性值
    public void getNodes(Element root) {
        System.out.println("根节点：" + root.getName());
        List<Attribute> list = root.attributes();
        for (Attribute attribute: list) {
            if(attribute.getValue() != null)
            System.out.println(attribute.getName() + ":" + attribute.getValue());
        }
        //使用迭代器
        Iterator iterable =  root.elementIterator();
        while(iterable.hasNext()) {
            //取出元素
            Element element = (Element) iterable.next();
            System.out.println(element.getName() + ":" + element.attributeValue("id"));
            getNodes(element);
        }
    }

    public InputStream getXMLInputStream(String xmlPath) {
        return this.getClass().getClassLoader().getResourceAsStream(xmlPath); //返回读取的xml的InputStream
    }
}
