package com.oc.elastic;

import lombok.ToString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void split() {
        String[] s = "name^1.5".split("\\^");
        System.out.println("name: " + s[0] + " \nboost: " + Float.valueOf(s[1]));
    }

}
