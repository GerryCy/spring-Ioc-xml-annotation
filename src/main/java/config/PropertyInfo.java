package config;

/**
 * xml中配置的bean的属性信息
 */
public class PropertyInfo {

    private String name; //属性名
    private String value; //属性值
    private String ref; //引用类型属性

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
