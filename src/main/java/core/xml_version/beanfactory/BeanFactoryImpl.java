package core.xml_version.beanfactory;

import core.xml_version.mapdata.BeanDefinition;
import core.xml_version.config.BeanInfo;
import core.xml_version.config.PropertyInfo;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取bean的实现类
 */
public class BeanFactoryImpl implements BeanFactory {

    //获取配置文件中的已经转换为map结构的信息
    private Map<String, BeanInfo> xmlMapInfo;
    //作为IOC容器，存放着已经创建好的对象
    private Map<String, Object> instanceMap = new HashMap<String, Object>();

    //构造方法中将bean中的所有类都初始化一个对象，在测试程序中调用的时候直接从IOC容器也就是instanceMap中取
    public BeanFactoryImpl(String xmlPath) {
        xmlMapInfo = new BeanDefinition().getXmlInfo(xmlPath);
        for (Map.Entry<String,BeanInfo> entry : xmlMapInfo.entrySet()) {
            String beanId = entry.getKey(); //beanId对应xmlMapInfo中的key
            BeanInfo beanInfo = entry.getValue(); //beanInfo对应xmlMapInfo中的value
            // 下面是避免重复创建，因为我们本身在配置文件中没有做id冲突的检测，所以这里简单
            // 处理配置beanId相同的情况，这也是保证配置的scope为singleton
            Object beanExsit = instanceMap.get(beanId); //查看我们的IOC容器中是否有
            //如果没有，而且要求是单例模式的，就创建对象然后添加到容器中
            if(null == beanExsit && beanInfo.getScope().equals("singleton")) {
                instanceMap.put(beanId, createBeanInstacne(beanInfo));
            }
        }
    }

    /**
     * 传递过来的是BeanInfo，其中有BeanId,要创建对象的classPath
     * 我们就可以通过反射创建对象
     * @param beanInfo
     * @return
     */
    private Object createBeanInstacne(BeanInfo beanInfo) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(beanInfo.getClassPath());
        } catch (ClassNotFoundException e) {
            System.out.println("请检查配置的bean-id为"+ beanInfo.getId() +"的class是否存在");
            e.printStackTrace();
        }
        //创建对象实例
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException e) {
            System.out.println("请检查是否有无参构造器");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("请检查你的构造器权限");
            e.printStackTrace();
        }
        //如果该bean有property属性，我们需要将其set到实例中
        if(beanInfo.getPropertList() != null) {
            for (PropertyInfo property : beanInfo.getPropertList()) {
                String propertyName = property.getName();
                String propertyValue = property.getValue();
                String propertyRef = property.getRef();
                //value!=null,ref=null
                if(null != propertyValue) {
                    Map<String,String[]> map = new HashMap<String,String[]>();
                    map.put(propertyName,new String[] {propertyValue});
                    try {
                        BeanUtils.populate(obj,map);
                    } catch (IllegalAccessException e) {
                        System.out.println("请检查属性为" + propertyName + "的权限");
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                if(null != propertyRef) {
                    //首先查看当前IOC容器中是否存在引用的bean对象，如果没有需要创建bean对象
                    Object beanExsit = instanceMap.get(propertyRef);
                    if(null == beanExsit) {
                        //当前还不存在，就需要创建
                        beanExsit = createBeanInstacne(xmlMapInfo.get(propertyRef));
                        //创建好之后，需要放在IOC容器中(singleton时候才放入)
                        if(xmlMapInfo.get(propertyRef).getScope().equals("singleton")) {
                            instanceMap.put(propertyRef,beanExsit);
                        }
                    }
                    //通过BeanUtils为obj设置引用类型的属性
                    try {
                        BeanUtils.setProperty(obj,propertyName,beanExsit);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj;
    }

    /**
     * 从IOC容器中直接获取bean对象
     * @param beanId
     * @return
     */
    public Object getBean(String beanId) {
        Object beanObj = instanceMap.get(beanId);
        if(null == beanObj) {
            beanObj = createBeanInstacne(xmlMapInfo.get(beanId));
        }
        return beanObj;
    }
}
