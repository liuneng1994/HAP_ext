package hap.extend.core.operation.scanner;

import hap.extend.core.operation.component.HtmlComponent;
import hap.extend.core.operation.scanner.converter.FreemarkerTemplateConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * HtmlScanner Tester.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/applicationContext-HapExtend.xml"})
public class HtmlScannerTest {
    @Autowired
    private FreemarkerTemplateScanner scanner;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testScan() throws Exception {
        List<HtmlComponent> components = scanner.scan(new File(HtmlScannerTest.class.getClassLoader().getResource("test.html").toURI()));
        System.out.println(components);
        Assert.assertTrue(components.size() == 2);
    }

} 
