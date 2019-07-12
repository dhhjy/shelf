package com.quick.shelf.modular.creditPort.xinYan;

import lombok.Data;

/**
 * 新颜征信查询数据时，返回的对象模型
 */
@Data
public class XinYanDataResult {
    private String msg;
    private String code;
    private boolean success;
    private Object detail;
}
