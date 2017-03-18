package hap.extend.core.operation.component;

/**
 * 页面控件信息对象
 */
public class HtmlComponent {
    private String id;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "HtmlComponent{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
