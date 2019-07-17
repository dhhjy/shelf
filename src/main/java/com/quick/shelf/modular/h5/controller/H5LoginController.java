package com.quick.shelf.modular.h5.controller;

import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.common.annotion.Permission;
import com.quick.shelf.core.log.LogIp;
import com.quick.shelf.core.log.LogManager;
import com.quick.shelf.core.log.factory.LogTaskFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.shiro.ShiroUser;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName H5LoginController
 * @Description TODO
 * @Author ken
 * @Date 2019/7/16 13:54
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/h5")
public class H5LoginController extends BaseController {
    /**
     * log
     */
    public static final Logger logger = LoggerFactory.getLogger(H5LoginController.class);

    private static final String H5_PATH = "/H5/";

    @RequestMapping(value = {"/","login"}, method = RequestMethod.GET)
    public String login() {
        return H5_PATH + "login.html";
    }

    @Permission
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return H5_PATH + "/index.html";
    }

    @RequestMapping(value = "/goLogin", method = RequestMethod.POST)
    public String loginVali(HttpServletRequest request) {

        String phoneNumber = super.getPara("phoneNumber").trim();
        String password = super.getPara("password").trim();

        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(phoneNumber, password.toCharArray());

        //执行shiro登录操作
        currentUser.login(token);

        //登录成功，记录
        ShiroUser shiroUser = ShiroKit.getUserNotNull();
        ShiroKit.getSession().setAttribute("sessionFlag", true);
        LogManager.me().executeLog(LogTaskFactory.loginLog(shiroUser.getId(), LogIp.getIpAddr(request)));

        return REDIRECT + "/h5/index";
    }
}
