package com.quick.shelf.modular.creditPort.quickMoney.bill99.entity;

import lombok.Data;

@Data
public class TransInfo {
    private String postUrl;             //提交地址
    private boolean FLAG;               //用来判断选用的解析函数
    private String recordeText_1;       //用来记录XML内容格式字段，用来记录XML第一个标志内容格式字段
    private String recordeText_2;       //当标记的内容多时，用来记录XML第二个标志内容格式字段
    private String txnType;             //交易类型
}