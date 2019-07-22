package cn.stylefeng.guns.system;

import cn.stylefeng.guns.base.BaseJunit;
import com.quick.shelf.modular.business.service.BSysUserService;
import com.quick.shelf.modular.system.mapper.UserMapper;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * 用户测试
 *
 * @author fengshuonan
 * @date 2017-04-27 17:05
 */
public class UserTest extends BaseJunit {

    @Resource
    UserMapper userMapper;

    @Resource
    private BSysUserService bSysUserService;

    @Test
    public void userTest() throws Exception {
        bSysUserService.phoneIsExist("15260793127");
    }

}
