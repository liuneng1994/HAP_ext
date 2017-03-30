package hap.extend.core.dataPermission.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * read config properties for data permission.
 * Created by yyz on 17/3/29.
 */
public final class ConfigUtil {
    private static Properties properties;
    public static boolean enableWrapSqlWithSelectAll = false;
    public static String dbType;

    static {
        properties = new Properties();
        try {
            properties.load(ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties"));
            String flag = properties.getProperty("hap_extend.data_permission.enable_wrap_sql_with_select_all.flag","false");
            dbType = properties.getProperty("db.type","");
            if("true".equalsIgnoreCase(flag)){
                enableWrapSqlWithSelectAll = true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
