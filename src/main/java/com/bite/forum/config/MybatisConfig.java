package com.bite.forum.config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
// 配置类
@Configuration
// 指定Mybatis的扫描路径
@MapperScan("com.bite.forum.dao")
public class MybatisConfig {
}