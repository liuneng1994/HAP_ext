package hap.extend.core.dataPermission;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yyz on 2017/2/14.
 *
 * @author yazheng.yang@hand-china.com
 */
public class Test {
    public static void main(String[] args) {
//        String s = "com.hand.hap.function.mapper.ResourceMapper.select".replaceAll("\\.", ":");
//        System.out.println(s);
//        String mapperMethod_count = "com.hand.hap.function.mapper.ResourceMapper.select_COUNT";
//        System.out.println(mapperMethod_count.substring(0,mapperMethod_count.length()-6));

//        String sql = "SELECT REQUEST_ID, PROGRAM_ID, OBJECT_VERSION_NUMBER, CREATED_BY, CREATION_DATE, LAST_UPDATED_BY, LAST_UPDATE_DATE, LAST_UPDATE_LOGIN, ATTRIBUTE_CATEGORY, ATTRIBUTE1, ATTRIBUTE2, ATTRIBUTE3, ATTRIBUTE4, ATTRIBUTE5, ATTRIBUTE6, ATTRIBUTE7, ATTRIBUTE8, ATTRIBUTE9, ATTRIBUTE10, ATTRIBUTE11, ATTRIBUTE12, ATTRIBUTE13, ATTRIBUTE14, ATTRIBUTE15, rule_id, rule_sql, rule_name, is_include_type FROM hapextend_rules WHERE ((1 = 1) AND (10002 != 10001)) ORDER BY rule_id ASC LIMIT ? OFFSET ?";
//        Pattern p = Pattern.compile("LIMIT \\? OFFSET \\?");
//        Matcher m = p.matcher(sql);
//        String strNoBlank = m.replaceAll(" ");
//        System.out.println(strNoBlank);

        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set(null);
        System.out.println(threadLocal.get());
    }
}
