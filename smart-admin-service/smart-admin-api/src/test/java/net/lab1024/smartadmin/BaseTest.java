package net.lab1024.smartadmin;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试基类
 *
 * @author lizongliang
 * @date 2017/09/29 10:54
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartAdminApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {

    @Before
    public void before() {
        System.out.println("测试开始------------------");
    }

    @After
    public void after() {
        System.out.println("测试结束------------------");
    }
}
