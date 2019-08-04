layui.use(['layer', 'table', 'ax', 'admin', 'laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var layer = layui.layer;
    var laydate = layui.laydate;

    /**
     * 字典类型表管理
     */
    var stagingList = {
        tableId: "stagingListTable"
    };

    /**
     * 初始化表格的列
     */
    stagingList.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: '主键'},
            {field: 'pOrderNumber', title: '订单号'},
            {
                field: 'countRefund', title: '总共应还', templet: function (d) {
                    return "<span class='tag normal' style='background-color: #2486FF'>" + d.countRefund + " 元</span>"
                }
            },
            {
                field: 'everyDayRefund', sort: true, title: '本期应还', templet: function (d) {
                    return "<span class='tag normal'>" + d.everyDayRefund + " 元</span>"
                }
            },
            {
                field: 'surplusRefund', sort: true, title: '剩余应还', templet: function (d) {
                    return "<span class='tag danger'>" + d.surplusRefund + " 元</span>";
                }
            },
            {
                field: 'status', sort: true, title: '还款状态', templet: function (d) {
                    if (d.status === 0)
                        return "<span class='tag danger' style='background-color:gray'>待还款</span>";
                    if (d.status === 1)
                        return "<span class='tag normal'>正常</span>";
                    if (d.status === 2)
                        return "<span class='tag danger'>逾期</span>";
                }
            },
            {
                field: 'predictRepaymentTime', sort: true, title: '还款日期', templet: function (d) {
                    return d.predictRepaymentTime.split(" ")[0];
                }
            },
            {field: 'repaymentTime', sort: true, title: '实际还款日期'},
            {field: 'payee', sort: true, title: '收款人'},
            {align: 'center', toolbar: '#tableBar', title: '操作'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    stagingList.search = function () {
        var queryData = {};
        queryData['timeLimit'] = $("#timeLimit").val();
        queryData['status'] = $("#status").val();
        table.reload(stagingList.tableId, {where: queryData});
    };

    /**
     * 导出excel按钮
     */
    stagingList.exportExcel = function () {
        var checkRows = table.checkStatus(stagingList.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 还款
     * @param data
     */
    stagingList.onRepayment = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/stagingList/repayment", function (data) {
                Feng.info(data.message);
                layer.closeAll();
                tableResult.reload();
            }, function (data) {
                Feng.error("还款失败！" + data.responseJSON.message)
            });
            ajax.set({"id": data.id});
            ajax.start();
        };

        if (data.status !== 0) {
            Feng.error("已还款或已逾期的账单不可操作");
            return false;
        }

        if (data.predictRepaymentTime.split(" ")[0] !== getDateString()) {
            layui.admin.open({
                type: 1,
                title: "还款提醒",
                btn: ['确定', '取消'],
                content: '<div style="padding: 50px; line-height: 22px; background-color: #f2f2f2; color: #000000;">账单并非今日账单，确定要提前还款吗？</div>',
                yes: function () {
                    operation();
                }
            });
        } else {
            operation();
        }
    };

    /**
     * 逾期
     * @param data
     */
    stagingList.onOverdue = function (data) {
        if (data.status === 1) {
            Feng.error("已还款订单不允许逾期");
            return false;
        }
        if (data.status === 2) {
            Feng.error("逾期订单不允许重复逾期");
            return false;
        }

        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/stagingList/overdue", function (data) {
                Feng.info(data.message);
                tableResult.reload();
            }, function (data) {
                Feng.error("还款失败！" + data.responseJSON.message)
            });
            ajax.set({"id": data.id});
            ajax.start();
        };

        layui.admin.open({
            type: 1,
            title: "逾期提醒",
            btn: ['确定', '取消'],
            content: '<div style="padding: 50px; line-height: 22px; background-color: #f2f2f2; color: #000000;">确定手动逾期该用户账单吗？</div>',
            yes: function () {
                operation();
            }
        });
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + stagingList.tableId,
        url: Feng.ctxPath + '/stagingList/list/' + $("#userId").val(),
        where: {"orderNumber": $("#orderNumber").val()},
        page: true,
        height: "full-98",
        limits: [20, 50, 100],  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
        limit: 20, //每页默认显示的数量
        cellMinWidth: 100,
        cols: stagingList.initColumn()
    });

    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        stagingList.search();
    });

    // 导出excel
    $('#btnExp').click(function () {
        stagingList.exportExcel();
    });

    // 关闭页面
    $('#btnBack').click(function () {
        window.location.href = Feng.ctxPath + "/orderDetails/" + $("#target").val();
    });


    // 工具条点击事件
    table.on('tool(' + stagingList.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'repayment') {
            stagingList.onRepayment(data);
        } else if (layEvent === 'overdue') {
            stagingList.onOverdue(data);
        }
    });
});

function getDateString() {
    var date = new Date();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    return date.getFullYear() + '-' + month + '-' + strDate;
}
