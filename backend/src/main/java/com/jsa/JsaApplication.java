package com.jsa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 健身打卡系统（JSA）启动类。
 *
 * @MapperScan 扫描 DAO 层的 MyBatis Mapper 接口（见 docs/02 分层设计）。
 */
@SpringBootApplication
@MapperScan("com.jsa.dao")
public class JsaApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsaApplication.class, args);
    }
}
