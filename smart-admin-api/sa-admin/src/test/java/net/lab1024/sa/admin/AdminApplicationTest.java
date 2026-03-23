package net.lab1024.sa.admin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AdminApplicationTest {

    @BeforeEach
    public void before() {
        System.out.println("----------------------- 测试开始 -----------------------");

    }

    @AfterEach
    public void after() {
        System.out.println("----------------------- 测试结束 -----------------------");
    }

}

