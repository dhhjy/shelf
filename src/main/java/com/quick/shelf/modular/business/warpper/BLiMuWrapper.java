package com.quick.shelf.modular.business.warpper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.common.constant.factory.ConstantFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.util.DecimalUtil;

import java.util.List;
import java.util.Map;

/**
 * @ClassName BLiMuWrapper
 * @Description TODO
 * @Author ken
 * @Date 2019/7/15 21:24
 * @Version 1.0
 */
public class BLiMuWrapper extends BaseControllerWrapper {
    public BLiMuWrapper(Map<String, Object> single) {
        super(single);
    }

    public BLiMuWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public BLiMuWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public BLiMuWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    /**
     * 脱敏手机号
     *
     * @param phoneNumber
     * @return
     */
    private static String getPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() > 10) {
            return phoneNumber.substring(0, 3) + "*****" + phoneNumber.substring(8);
        } else {
            return phoneNumber;
        }
    }

    /**
     * 脱敏身份证
     *
     * @param idCard
     * @return
     */
    private static String getIdCard(String idCard) {
        if (idCard.length() > 14) {
            return idCard.substring(0, 4) + "********" + idCard.substring(14);
        } else if (("null").equals(idCard)) {
            return "";
        } else {
            return idCard;
        }
    }

    /**
     * 立木数据列表脱敏
     *
     * @param map
     */
    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String a = "1";
        map.put("deptName", ConstantFactory.me().getDeptName(DecimalUtil.getLong(map.get("deptId"))));
        if (!ShiroKit.isAdmin() && !ShiroKit.isDeptAdmin()) {
            map.put("phoneNumber", getPhoneNumber(String.valueOf(map.get("phoneNumber"))));
            map.put("idCard", getIdCard(String.valueOf(map.get("idCard"))));
        }
    }

    /**
     * 替换HTML页面中的引入 js、css 的URL
     *
     * @param html
     */
    public static String replaceHtmlUrl(String html) {
        return html
                .replace("<link rel=\"stylesheet\" type=\"text/css\" href=\"/static/web/css/common.css?_v=\"/>",
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"/assets/limu/css/common.css?_v=\"/>")
                .replace("<link rel=\"stylesheet\" type=\"text/css\" href=\"/static/web/css/manage.css?_v=\"/>",
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"/assets/limu/css/manage.css?_v=\"/>")
                .replace("<script src=\"/static/web/js/jquery-1.11.1.min.js?_v=\" type=\"text/javascript\" charset=\"utf-8\"></script>",
                "<script src=\"/assets/limu/js/jquery-1.11.1.min.js?_v=\" type=\"text/javascript\" charset=\"utf-8\"></script>")
                .replace("<script src=\"/static/web/js/echarts-4.2.1.common.min.js?_v=\" type=\"text/javascript\" charset=\"utf-8\"></script>",
                "<script src=\"/assets/limu/js/echarts-4.2.1.common.min.js?_v=\" type=\"text/javascript\" charset=\"utf-8\"></script>")
                .replace("<script src=\"/static/web/js/common.js?_v=\" type=\"text/javascript\" charset=\"utf-8\"></script>",
                "<script src=\"/assets/limu/js/common.js?_v=\" type=\"text/javascript\" charset=\"utf-8\"></script>")
                .replace("<script src=\"/static/web/js/echarts.common.min.js?_v=1.5.3\" type=\"text/javascript\" charset=\"utf-8\"></script>",
                        "<script src=\"/assets/limu/js/echarts-4.2.1.common.min.js?_v=1.5.3\" type=\"text/javascript\" charset=\"utf-8\"></script>")
                .replace("/static/web/images/mingzhong.png","/assets/limu/images/mingzhong.png")
                .replace("立木", "").replace("征信", "");
    }
}
