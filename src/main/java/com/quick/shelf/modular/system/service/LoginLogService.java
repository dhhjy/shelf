package com.quick.shelf.modular.system.service;

import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.modular.system.entity.LoginLog;
import com.quick.shelf.modular.system.mapper.LoginLogMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 登录记录 服务实现类
 * </p>
 *
 * @author quick
 * @since 2018-12-07
 */
@Service
public class LoginLogService extends ServiceImpl<LoginLogMapper, LoginLog> {

    /**
     * 获取登录日志列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    public List<Map<String, Object>> getLoginLogs(Page page, String beginTime, String endTime, String logName) {
        Long userId = null;
        if (!ShiroKit.isAdmin())
            userId = Objects.requireNonNull(ShiroKit.getUser()).getDeptId();
        return this.baseMapper.getLoginLogs(page, userId, beginTime, endTime, logName);
    }
}
