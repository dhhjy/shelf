package com.quick.shelf.core.log.factory;

import cn.stylefeng.roses.core.util.SpringContextHolder;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.quick.shelf.core.common.constant.state.LogSucceed;
import com.quick.shelf.core.common.constant.state.LogType;
import com.quick.shelf.core.log.LogManager;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.service.BPortCountService;
import com.quick.shelf.modular.business.service.BSysUserService;
import com.quick.shelf.modular.system.entity.LoginLog;
import com.quick.shelf.modular.system.entity.OperationLog;
import com.quick.shelf.modular.system.mapper.LoginLogMapper;
import com.quick.shelf.modular.system.mapper.OperationLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 日志操作任务创建工厂
 *
 * @author zcn
 * @date 2019/07/15
 */
public class LogTaskFactory {

    private static Logger logger = LoggerFactory.getLogger(LogManager.class);
    private static LoginLogMapper loginLogMapper = SpringContextHolder.getBean(LoginLogMapper.class);
    private static OperationLogMapper operationLogMapper = SpringContextHolder.getBean(OperationLogMapper.class);
    private static BPortCountService bPortCountService = SpringContextHolder.getBean(BPortCountService.class);
    private static BSysUserService bSysUserService = SpringContextHolder.getBean(BSysUserService.class);

    public static TimerTask loginLog(final Long userId, final String ip) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    LoginLog loginLog = LogFactory.createLoginLog(LogType.LOGIN, userId, null, ip);
                    loginLogMapper.insert(loginLog);
                } catch (Exception e) {
                    logger.error("创建登录日志异常!", e);
                }
            }
        };
    }

    public static TimerTask loginLog(final String username, final String msg, final String ip) {
        return new TimerTask() {
            @Override
            public void run() {
                LoginLog loginLog = LogFactory.createLoginLog(
                        LogType.LOGIN_FAIL, null, "账号:" + username + "," + msg, ip);
                try {
                    loginLogMapper.insert(loginLog);
                } catch (Exception e) {
                    logger.error("创建登录失败异常!", e);
                }
            }
        };
    }

    public static TimerTask exitLog(final Long userId, final String ip) {
        return new TimerTask() {
            @Override
            public void run() {
                LoginLog loginLog = LogFactory.createLoginLog(LogType.EXIT, userId, null, ip);
                try {
                    loginLogMapper.insert(loginLog);
                } catch (Exception e) {
                    logger.error("创建退出日志异常!", e);
                }
            }
        };
    }

    /**
     * 业务日志
     * @param userId
     * @param bussinessName
     * @param clazzName
     * @param methodName
     * @param msg
     * @return
     */
    public static TimerTask bussinessLog(final Long userId, final String bussinessName, final String clazzName, final String methodName, final String msg) {
        return new TimerTask() {
            @Override
            public void run() {
                OperationLog operationLog = LogFactory.createOperationLog(
                        LogType.BUSSINESS, userId, bussinessName, clazzName, methodName, msg, LogSucceed.SUCCESS);
                try {
                    operationLogMapper.insert(operationLog);
                } catch (Exception e) {
                    logger.error("创建业务日志异常!", e);
                }
            }
        };
    }

    /**
     * 异常日志
     * @param userId
     * @param exception
     * @return
     */
    public static TimerTask exceptionLog(final Long userId, final Exception exception) {
        return new TimerTask() {
            @Override
            public void run() {
                String msg = ToolUtil.getExceptionMsg(exception);
                OperationLog operationLog = LogFactory.createOperationLog(
                        LogType.EXCEPTION, userId, "", null, null, msg, LogSucceed.FAIL);
                try {
                    operationLogMapper.insert(operationLog);
                } catch (Exception e) {
                    logger.error("创建异常日志异常!", e);
                }
            }
        };
    }

    /**
     * 接口日志
     *
     * @param userId
     * @param exception
     * @return
     */
    public static TimerTask portLog(final long userId, final String type, final String typeName, final String msg) {
        BSysUser bSysUser = bSysUserService.selectBSysUserByUserId((int) userId);
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    bPortCountService.insertBPortCount(bSysUser.getDeptId(), type, typeName, msg);
                } catch (Exception e) {
                    logger.error("创建接口统计异常!", e);
                }
            }
        };
    }

    /**
     * 接口回调日志
     *
     * @param userId
     * @param exception
     * @return
     */
    public static TimerTask callBackLog(final Integer userId, final String bussinessName, final String clazzName, final String methodName, final String msg) {
        return new TimerTask() {
            @Override
            public void run() {
                OperationLog operationLog = LogFactory.createOperationLog(
                        LogType.CALLBACK, Long.valueOf(userId), bussinessName, clazzName, methodName, msg, LogSucceed.SUCCESS);
                try {
                    operationLogMapper.insert(operationLog);
                } catch (Exception e) {
                    logger.error("创建回调日志异常!", e);
                }
            }
        };
    }
}
