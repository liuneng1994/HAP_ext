package hap.extend.core.operation.dto;

import com.hand.hap.system.dto.BaseDTO;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by yyz on 2017/3/16.
 *
 * @author yazheng.yang@hand-china.com
 */
public class DataCarrier extends BaseDTO {
    private String name;
    private Map<String,Object> data = new HashedMap();
    private Object lock = new Object();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setProp(String key, Object value){
        synchronized (lock){
            Object o = data.get(key);
            if(null != o){
                data.remove(key);
            }
            data.put(key,value);
        }
    }
}
