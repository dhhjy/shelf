package com.quick.shelf.core.common.controller;

import com.quick.shelf.core.shiro.ShiroKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;

/**
 * 全局的控制器
 *
 * @author zcn
 * @date 2016年11月13日 下午11:04:45
 */
@Controller
@RequestMapping("/global")
public class GlobalController {

    /**
     * 跳转到404页面
     *
     * @author fengshuonan
     */
    @RequestMapping(path = "/error")
    public String errorPage() {
        return "/404.html";
    }

    /**
     * 跳转到session超时页面
     *
     * @author fengshuonan
     */
    @RequestMapping(path = "/sessionError")
    public String errorPageInfo() {
        List<Long> roles = Objects.requireNonNull(ShiroKit.getUser()).getRoleList();
        // 转发到相应的登录页面
        if (roles.get(0) == 999)
            return "/h5/login.html";
        else
            return "/login.html";
    }
}
