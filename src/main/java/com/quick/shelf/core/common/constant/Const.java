/**
 * Copyright 2018-2020 quick & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quick.shelf.core.common.constant;

import cn.hutool.core.collection.CollectionUtil;

import java.util.List;

/**
 * 系统常量
 *
 * @author fengshuonan
 * @date 2017年2月12日 下午9:42:53
 */
public interface Const {

    /**
     * 默认管理系统的名称
     */
    String DEFAULT_SYSTEM_NAME = "鑫炜金融后台管理系统";

    /**
     * 默认欢迎界面的提示
     */
    String DEFAULT_WELCOME_TIP = "欢迎使用鑫炜金融后台管理系统!";

    /**
     * 默认用户系统的名称
     */
    String SYSTEM_CLIENT_NAME = "秒秒贷";

    /**
     * 默认欢迎界面的提示
     */
    String DEFAULT_CLIENT_TIP = "欢迎!";

    /**
     * 系统默认的管理员密码
     */
    String DEFAULT_PWD = "111111";

    /**
     * 管理员角色的名字
     */
    String ADMIN_NAME = "administrator";

    /**
     * 公司管理员角色的名字
     */
    String COMPANY_ADMIN_NAME = "companyAdministrator";

    /**
     * 管理员id
     */
    Long ADMIN_ID = 1L;

    /**
     * 超级管理员角色id
     */
    Long ADMIN_ROLE_ID = 1L;

    /**
     * 接口文档的菜单名
     */
    String API_MENU_NAME = "接口文档";

    /**
     * 客户端用户默认权限为999
     */
    String CLIENT_USER_ROLE = "999";

    /**
     * 不需要权限验证的资源表达式
     */
    List<String> NONE_PERMISSION_RES = CollectionUtil.newLinkedList("/assets/**", "/gunsApi/**",
            "/login", "/global/sessionError", "/kaptcha", "/error", "/global/error", "/xinYan/callbackJson/*", "/xinYan/callbackReport/*",
            "/h5", "/h5/", "/h5/login", "/h5/registerIndex", "/h5/findPasswordIndex", "/h5/register", "/h5/findPassword",
            "/liMu/sign", "/liMu/callBack/**", "/system/sendSms/*");
}
