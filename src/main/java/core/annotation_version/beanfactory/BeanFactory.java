package core.annotation_version.beanfactory;

import core.annotation_version.annotation.Service;
import core.annotation_version.utils.ClassUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 手写Spring专题 注解版本注入bean
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BeanFactory {

    private String packageName;        //扫包范围
    private ConcurrentHashMap<String, Object> initBean = null;

    public BeanFactory(String packageName) {
        super();
        this.packageName = packageName;
    }

    /**
     * 使用beanID查找对象(SringIOC注解实现主类 )
     * @param beanId
     * @return
     * @throws Exception
     */
    public Object getBean(String beanId) throws Exception {
        List<Class> listClassAnnotation = findClassByExisService();         // 1.使用反射机制获取该包下所有的类已经存在bean的注解类
        if(listClassAnnotation == null || listClassAnnotation.isEmpty()) {
            throw new Exception("没有需要初始化的bean");
        }
        initBean = initBean(listClassAnnotation);                    // 2.使用Java反射机制初始化对象
        if (initBean == null || initBean.isEmpty()) {
            throw new Exception("初始化bean为空!");
        }
        Object object = initBean.get(beanId);                        // 3.使用beanID查找查找对应bean对象
        attriAssign(object);                                         // 4.使用反射读取类的属性,赋值信息
        return object;
    }

    /**
     *   使用反射读取类的属性,赋值信息
     * @param object
     */
    private void attriAssign(Object object)
            throws IllegalArgumentException, IllegalAccessException {
        // TODO Auto-generated method stub
        Class<? extends Object> classInfo = object.getClass();        // 1.获取类的属性是否存在 获取bean注解
        Field[] declaredFields = classInfo.getDeclaredFields();
        for(Field field : declaredFields) {
            String name = field.getName();               // 属性名称
            Object bean = initBean.get(name);            // 2.使用属性名称查找bean容器赋值
            if(bean !=null) {
                field.setAccessible(true);               // 私有访问允许访问
                field.set(object, bean);                 // 给属性赋值
                continue;
            }
        }
    }

    /**
     *    初始化bean对象,ConcurrentHashMap保存
     * @param listClassAnnotation
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private ConcurrentHashMap<String, Object> initBean(List<Class> listClassAnnotation)
            throws InstantiationException, IllegalAccessException {
        // TODO Auto-generated method stub
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap<String, Object>();
        for(Class classInfo : listClassAnnotation) {
            Object newInstance = classInfo.newInstance();        // 初始化对象
            String beanId = toLowerCaseFirstOne(classInfo.getSimpleName());     // 获取父类名称
            concurrentHashMap.put(beanId, newInstance);
        }
        return concurrentHashMap;
    }

    /**
     *    首字母转小写
     * @return
     */
    private String toLowerCaseFirstOne(String s) {
        // TODO Auto-generated method stub
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    /**
     *    使用反射机制获取该包下所有的类已经存在bean的注解类
     * @return
     * @throws Exception
     */
    private List<Class> findClassByExisService() throws Exception {
        // TODO Auto-generated method stub
        if(null == packageName || packageName.trim().equals("")) {          // 1.使用反射机制获取该包下所有的类
            throw new Exception("扫包地址不能为空!");
        }
        List<Class<?>> classByPackageName = ClassUtils.getClasses(packageName);     // 2.使用反射技术获取当前包下所有的类
        List<Class> exisClassesAnnotation = new ArrayList<Class>();                // 3.存放类上有bean注入注解

        // 4.判断该类上属否存在注解
        for(Class classInfo : classByPackageName) {
            Service extService = (Service) classInfo.getDeclaredAnnotation(Service.class);
            if(extService !=null) {
                exisClassesAnnotation.add(classInfo);
                continue;
            }
        }
        return exisClassesAnnotation;
    }
}
