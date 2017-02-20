package Hap_extend.core.operation.scanner;

/**
 * Created by liuneng on 2017/2/20.
 */
public class HtmlScanner {
    private static HtmlScanner ourInstance = new HtmlScanner();

    public static HtmlScanner getInstance() {
        return ourInstance;
    }

    private HtmlScanner() {
    }
}
