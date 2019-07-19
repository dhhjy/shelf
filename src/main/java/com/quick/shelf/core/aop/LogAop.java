package com.quick.shelf.core.aop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quick.shelf.core.common.annotion.BussinessLog;
import com.quick.shelf.core.common.annotion.CallBackLog;
import com.quick.shelf.core.common.annotion.PortLog;
import com.quick.shelf.core.log.LogManager;
import com.quick.shelf.core.log.factory.LogTaskFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.shiro.ShiroUser;
import com.quick.shelf.modular.creditPort.liMu.LiMuConstantMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 日志记录
 *
 * @author fengshuonan
 * @date 2016年12月6日 下午8:48:30
 */
@Aspect
@Component
public class LogAop {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 常规业务日志切点
     */
    @Pointcut(value = "@annotation(com.quick.shelf.core.common.annotion.BussinessLog)")
    public void cutService() {
    }

    /**
     * 系统外部接口统计切点
     */
    @Pointcut(value = "@annotation(com.quick.shelf.core.common.annotion.PortLog)")
    public void cutPort() {
    }

    /**
     * 系统外部接口回调通知切点
     */
    @Pointcut(value = "@annotation(com.quick.shelf.core.common.annotion.CallBackLog)")
    public void cutCallBack() {
    }


    @Around("cutService()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {

        //先执行业务
        Object result = point.proceed();

        try {
            handle(point);
        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }

        return result;
    }

    @Around("cutPort()")
    public Object recordPortLog(ProceedingJoinPoint point) throws Throwable {

        //先执行业务
        Object result = point.proceed();

        try {
            handlePort(point, result);
        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }

        return result;
    }

    @Around("cutCallBack()")
    public Object recordCallBackLog(ProceedingJoinPoint point) throws Throwable {

        //先执行业务
        Object result = point.proceed();

        try {
            handleCallBack(point);
        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }

        return result;
    }

    /**
     * 常规业务日志记录
     *
     * @param point
     * @throws Exception
     */
    private void handle(ProceedingJoinPoint point) throws Exception {

        //获取拦截的方法名
        Signature sig = point.getSignature();
        MethodSignature msig;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        String methodName = currentMethod.getName();

        //如果当前用户未登录，不做日志
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return;
        }

        //获取拦截方法的参数
        String className = point.getTarget().getClass().getName();
        Object[] params = point.getArgs();

        //获取操作名称
        BussinessLog annotation = currentMethod.getAnnotation(BussinessLog.class);
        String bussinessName = annotation.value();
//        String key = annotation.key();
//        Class dictClass = annotation.dict();

        StringBuilder sb = new StringBuilder();
        for (Object param : params) {
            sb.append(param);
            sb.append(" & ");
        }
        sb.substring(0, sb.length() - 1);

        LogManager.me().executeLog(LogTaskFactory.bussinessLog(user.getId(), bussinessName, className, methodName, sb.toString()));
    }

    /**
     * AOP 统计外部接口接口调用
     *
     * @param point
     * @param result
     * @throws Throwable
     */
    private void handlePort(ProceedingJoinPoint point, Object result) throws Throwable {
        //获取拦截的方法名
        Method currentMethod = getCurrentMethod(point);

        //获取操作名称
        PortLog annotation = currentMethod.getAnnotation(PortLog.class);
        String type = annotation.type();
        String typeName = annotation.typeName();
        JSONObject re = JSONObject.parseObject(result.toString());
        if(null != re.getString("code") &&
                !re.getString("code").equals(LiMuConstantMethod.SUCCESS_CODE))
            return;
        if (null != re.getString("success") &&
                !re.getBoolean("success"))
            return;

        LogManager.me().executeLog(LogTaskFactory.portLog(Long.valueOf(re.getString("userId")), type, typeName, result.toString()));
    }

    /**
     * APO 获取当前方法的封装
     * @param point
     * @return
     * @throws NoSuchMethodException
     */
    private Method getCurrentMethod(ProceedingJoinPoint point) throws NoSuchMethodException {
        Signature sig = point.getSignature();
        MethodSignature msig;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        return point.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes());
    }

    /**
     * AOP 记录外部接口回调通知状态
     *
     * @param point
     * @param result
     * @throws Throwable
     */
    private void handleCallBack(ProceedingJoinPoint point) throws Throwable {
        //获取拦截的方法名
        Method currentMethod = getCurrentMethod(point);
        String methodName = currentMethod.getName();
        //获取操作名称
        CallBackLog annotation = currentMethod.getAnnotation(CallBackLog.class);
        String value = annotation.value();
        //获取拦截方法的参数
        String className = point.getTarget().getClass().getName();
        Object[] params = point.getArgs();
        List<Object> param = new ArrayList<>(Arrays.asList(params));
        Integer userId = 0;
        if (param.size() > 1)
            userId = Integer.valueOf(param.get(1).toString());

        JSONArray resultArray = new JSONArray(param);

        LogManager.me().executeLog(LogTaskFactory.callBackLog(userId, value, className, methodName, resultArray.toString()));
    }
}