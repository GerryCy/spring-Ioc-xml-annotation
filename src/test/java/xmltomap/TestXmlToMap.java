package xmltomap;

import core.xml_version.mapdata.BeanDefinition;
import core.xml_version.config.BeanInfo;

import java.util.Map;

public class TestXmlToMap {

    public static void main(String[] args) {
        Map<String, BeanInfo> map = new BeanDefinition().getXmlInfo("test.xml");

        for(Map.Entry<String, BeanInfo> entry : map.entrySet()) {
            System.out.println("key : " + entry.getKey() + ", value : " + entry.getValue());
        }
    }
}
