package bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataSource {

    private String driverClass;
    private String jdbcUrl;
    private String user;
    private String password;

}
