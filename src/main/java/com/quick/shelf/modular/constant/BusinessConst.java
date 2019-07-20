package com.quick.shelf.modular.constant;

public interface BusinessConst {
    String OK = "1";

    String NO = "0";

    // 原始数据值
    Integer ORIGINAL_DATA = 0;

    // 页面数据值
    Integer PAGE_DATA = 1;

    // 网站人数缓存键
    String CONSOLE_PERSOON = "CONSOLE_PERSON";

    // 认证接口缓存键
    String CONSOLE_PORT = "CONSOLE_PORT";

    // 短信接口缓存键
    String COMSOLE_SMS = "COMSOLE_SMS";

    // 网站总人数缓存Key
    String CLIENT_USER_CONT = "CLIENT_USER_CONT_";

    // 网站当天人数缓存Key
    String CLIENT_TO_DAY_USER_CONT = "CLIENT_TO_DAY_USER_CONT_";

    // 接口今日、总共调用次数
    String PORT_CALL_NUMBER = "PORT_CALL_NUMBER_";

    // 接口今日、总共调用价格
    String PORT_CALL_PRICE = "PORT_CALL_PRICE_";

    // 短信接口次数
    String SMS_PORT = "SMS_PORT_";

    // 短信接口价格
    String SMS_PORT_PRICE = "SMS_PORT_PRICE_";
}
