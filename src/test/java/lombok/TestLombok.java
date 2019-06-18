package lombok;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Getter
@Setter
@ToString(of = {"id","name"})
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class TestLombok {

    /**
     * 一、@Getter/@Setter
     *  作用于类上，生成所有变量的get和set方法；
     *  作用于成员变量上，生成该成员变量的get和set方法
     *  可以设置访问权限、是否懒加载等
     * 二、@ToString
     *  作用于类，默认覆盖默认的toString方法，可以通过of属性限定某些现实某些字段，通过exclude排除某些字段
     * 三、@EqualsAndHashCode
     *  作用于类，覆盖默认的equals和hashCode
     * 四、@NonNull
     *  主要作用于成员变量和参数中，标识不能为空，否则抛出空指针异常
     * 五、@NoArgsConstructor / @RequiredArgsConstructor / @AllArgsConstructor
     *  作用于类上，用于生成构造函数，有staticName、access等属性。staticName属性一旦设定，将采用静态的方式生成实例
     *  // @NoArgsConstructor生成无参构造器
     *  // @RequiredArgsConstructor注解生成包含NoNull注解的成员变量的构造器
     * 六、@Data
     *  作用于类上，是这些注解的集合：@Getter、@Setter、@EqualsAndHashCode、@ToString、 @RequiredArgsConstructor
     * 七、@Builder
     *  作用于类上，将类变为建造者模式
     * 八、@Log
     *  作用于类上，生成日志变量。针对不同的日志实现有不同的注解
     * 九、@Cleanup
     *  自动关闭资源，针对实现了java.io.Closeable接口的对象有效，比如典型的IO流对象
     * 十、@SneakyThrows
     *  可以对受检异常进行捕捉并抛出
     */

//    @Getter(value = AccessLevel.PUBLIC,lazy = false)
    @Setter(value = AccessLevel.PUBLIC)
    @NonNull
    private Integer id;
    private boolean del;
    private String name;
    private final Integer versionId = 1;

    @SneakyThrows
    public static void main(String[] args) {
        File file = new File("F:\\test.txt");
        @Cleanup InputStream inputStream = new FileInputStream(file); //@SneakyThrows
        byte[] bytes = new byte[10240];
        int len;
        while ((len = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes,0,len));
        }
    }
}
