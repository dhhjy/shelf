package com.quick.shelf.modular.creditPort.xinYan;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 新颜征信回调通知参数
 */
@Data
@Getter
@Setter
public class XinYanResult {
    private String msg;         //本次任务通知结果
    private String taskId;      //任务唯一性标识(系统中使用了userId当作任务ID)
    private String token;       //本次任务查询凭证
    private String apiUser;     //商户号
    private String apiEnc;      //H5回调专用验签md5 32(apiUser + apiKey + token)
    private String apiName;     //数据源英文名
    private String success;     //任务状态 true 成功 false 失败 暂时不返回失败
    private String type;        //report，报告通知标识，只有设置报告通知地址且通知类型为报告时才返回该字段
}
