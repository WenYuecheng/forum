//package com.bite.forum.controller;
//
//import com.bite.forum.exception.ApplicationException;
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.tags.Tags;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("/test")
//
//public class TestController {
//    @RequestMapping("/hello")
//    public String hello(){
//        return "Hello";
//    }
//    @GetMapping("/exception")
//    public String testException() throws Exception {
//        throw new Exception("这是⼀个Exception");
//    }
//    @GetMapping("/appException")
//    public String testApplicationException() {
//        throw new ApplicationException("Exception");
//    }
//}
package com.bite.forum.controller;

import com.bite.forum.common.AppResult;
import com.bite.forum.exception.ApplicationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// API 描述信息
@Tag(name = "TestController", description = "测试接口")
// 指定测试根路径映射地址前缀
@RequestMapping("/test")
// 返回JSON格式的Controller
@RestController
public class TestController {

    // API 方法描述
    @Operation(summary = "测试接口1，打印hello")
    // 地址映射
    @GetMapping("/hello")
    // 具体方法实现
    public String hello() {
        return "Hello, Spring Boot...";
    }

    // 注意：@Parameter 方法参数的注解，用来描述参数
    @Operation(summary = "测试接口3，返回 你好+传入参数")
    @GetMapping("/helloByName")
    public AppResult<String> helloByName(
            @Parameter(description = "名字", required = true) String name) {
        return AppResult.success("你好：" + name);
    }

    @Operation(summary = "测试接口2，跳转到index.html")
    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

    @Operation(summary = "测试接口4，返回一个异常结果")
    @GetMapping("/exception")
    public String testException() throws Exception {
        throw new Exception("这是一个Exception");
    }

    @Operation(summary = "测试接口5，返回一个自定义异常结果")
    @GetMapping("/appException")
    public String testApplicationException() {
        throw new ApplicationException("这是一个自定义的ApplicationException");
    }
}
