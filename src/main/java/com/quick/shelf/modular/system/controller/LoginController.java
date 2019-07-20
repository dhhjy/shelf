package com.quick.shelf.modular.system.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import com.quick.shelf.core.common.node.MenuNode;
import com.quick.shelf.core.log.LogIp;
import com.quick.shelf.core.log.LogManager;
import com.quick.shelf.core.log.factory.LogTaskFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.shiro.ShiroUser;
import com.quick.shelf.modular.system.service.UserService;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 登录控制器
 *
 * @author zcn
 * @Date 2017年1月10日 下午8:25:24
 */
@Controller
public class LoginController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 跳转到主页
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:41 PM
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model,HttpServletRequest httpServletRequest) {
        //获取当前用户角色列表
        ShiroUser user = ShiroKit.getUserNotNull();
        List<Long> roleList = user.getRoleList();

        if (roleList == null || roleList.size() == 0) {
            ShiroKit.getSubject().logout();
            model.addAttribute("tips", "该用户没有角色，无法登陆");
            return "/login.html";
        }

        if(roleList.get(0) == 999)
            return REDIRECT + "/h5/index";

        List<MenuNode> menus = userService.getUserMenuNodes(roleList);
        model.addAttribute("menus", menus);

        return "/index.html";
    }

    /**
     * 跳转到登录页面
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:41 PM
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (ShiroKit.isAuthenticated() || ShiroKit.getUser() != null) {
            return REDIRECT + "/";
        } else {
            return "/login.html";
        }
    }

    /**
     * 点击登录执行的动作
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:42 PM
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginVali(HttpServletRequest request) {

        String username = super.getPara("username").trim();
        String password = super.getPara("password").trim();
        String remember = super.getPara("remember");
        String loginType = super.getPara("loginType");

        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());

        //如果开启了记住我功能
        if ("on".equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }

        //执行shiro登录操作
        currentUser.login(token);

        //登录成功，记录
        ShiroUser shiroUser = ShiroKit.getUserNotNull();
        LogManager.me().executeLog(LogTaskFactory.loginLog(shiroUser.getId(), LogIp.getIpAddr(request)));

        ShiroKit.getSession().setAttribute("sessionFlag", true);

        if(loginType.equals("PC"))
            return REDIRECT + "/";
        else
            return REDIRECT + "/h5/index";
    }

    /**
     * 退出登录
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:42 PM
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut(HttpServletRequest request) {
        List<Long> roles = ShiroKit.getUser().getRoleList();
        // 记录日志
        LogManager.me().executeLog(LogTaskFactory.exitLog(ShiroKit.getUserNotNull().getId(), LogIp.getIpAddr(request)));
        // 清除用户
        ShiroKit.getSubject().logout();
        // 删除cookie
        deleteAllCookie();
        // 转发到相应的登录页面
        if(roles.get(0) == 999)
            return REDIRECT + "/h5/";
        else
            return REDIRECT + "/login";
    }
}
