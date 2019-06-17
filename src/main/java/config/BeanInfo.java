package config;

import java.util.ArrayList;
import java.util.List;

/**
 * xml中的Bean信息
 */
public class BeanInfo {

    private String id;
    private String classPath;
    private String scope = "singleton"; //指定的scope
    List<PropertyInfo> propertList = new ArrayList<PropertyInfo>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<PropertyInfo> getPropertList() {
        return propertList;
    }

    public void setPropertList(List<PropertyInfo> propertList) {
        this.propertList = propertList;
    }

    @Override
    public String toString() {
        return "BeanInfo{" +
                "id='" + id + '\'' +
                ", classPath='" + classPath + '\'' +
                ", scope='" + scope + '\'' +
                ", propertList=" + propertList +
                '}';
    }
}
