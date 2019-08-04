layui.use(['layer', 'form', 'table', 'ztree', 'laydate', 'admin', 'ax'], function () {
    var layer = layui.layer;
    var table = layui.table;
    var $ZTree = layui.ztree;
    var $ax = layui.ax;
    var laydate = layui.laydate;

    /**
     * 订单管理——待还款列表
     */
    var rePaymentList = {
        tableId: "rePaymentListTable",    //表格id
        condition: {
            name: "",
            deptId: "",
            timeLimit: ""
        }
    };

    /**
     * 初始化表格的列
     */
    rePaymentList.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'userId', hide: true, title: '用户id'},
            {field: 'orderNumber', title: '订单号', width: 200},
            {field: 'name', title: '姓名'},
            {field: 'productCodeName', title: '产品名称', minWidth: 150},
            {field: 'modeOfRepaymentName', title: '还款方式', minWidth: 150},
            {
                field: 'amount', title: '借款金额(元)', minWidth: 150, templet: function (d) {
                    return "<span class='tag normal'>" + d.amount + " 元</span>";
                }
            },
            {
                field: 'overdueNumber', title: '逾期数', templet: function (d) {
                    return "<span class='tag danger'>" + d.overdueNumber + " 期</span>";
                }
            },
            {
                field: 'debtDuration', title: '借款时长', templet: function (d) {
                    return d.debtDuration + " 天";
                }
            },
            {field: 'statusName', title: '当前状态'},
            {field: 'deptName', title: '所属部门', minWidth: 150},
            {field: 'createTime', title: '申请时间', minWidth: 200},
            {minWidth: 200, align: 'center', title: '操作', toolbar: '#tableBar', fixed: 'right'}
        ]];
    };

    /**
     * 选择部门时
     */
    rePaymentList.onClickDept = function (e, treeId, treeNode) {
        rePaymentList.condition.deptId = treeNode.id;
        rePaymentList.search();
    };

    /**
     * 点击查询按钮
     */
    rePaymentList.search = function () {
        var queryData = {};
        queryData['deptId'] = rePaymentList.condition.deptId;
        queryData['name'] = $("#name").val();
        queryData['timeLimit'] = $("#timeLimit").val();
        table.reload(rePaymentList.tableId, {where: queryData});
    };

    /**
     * 导出excel按钮
     */
    rePaymentList.exportExcel = function () {
        var checkRows = table.checkStatus(rePaymentList.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 打开分期账单窗口
     */
    rePaymentList.openStagingListDlg = function (data) {
        window.location = Feng.ctxPath + "/stagingList/index/" + data.userId + "/" + data.orderNumber + "/" + "rePaymentIndex";
    };

    /**
     * 提前还款功能
     */
    rePaymentList.onPrePaymentFun = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/stagingList/adjRepayment", function (data) {
                Feng.info(data.message);
                layer.closeAll();
                tableResult.reload();
            }, function (data) {
                Feng.error("还款失败！" + data.responseJSON.message)
            });
            ajax.set({"orderNumber": data.orderNumber});
            ajax.start();
        };

        layui.admin.open({
            type: 1,
            title: "提前还款",
            btn: ['确定', '取消'],
            content: '<div style="padding: 50px; line-height: 22px; background-color: #f2f2f2; color: #000000;">确定当前用户提前还款吗？</div>',
            yes: function () {
                operation();
            }
        });
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + rePaymentList.tableId,
        url: Feng.ctxPath + '/orderDetails/rePaymentList',
        page: true,
        limits: [20, 50, 100],  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
        limit: 20, //每页默认显示的数量
        height: "full-98",
        cellMinWidth: 100,
        cols: rePaymentList.initColumn()
    });

    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });

    //初始化左侧部门树
    var ztree = new $ZTree("deptTree", "/dept/tree");
    ztree.bindOnClick(rePaymentList.onClickDept);
    ztree.init();

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        rePaymentList.search();
    });

    // 导出excel
    $('#btnExp').click(function () {
        rePaymentList.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + rePaymentList.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        // 分期账单
        if (layEvent === 'stagingList') {
            rePaymentList.openStagingListDlg(data);
        } else {
            // 提前还款
            rePaymentList.onPrePaymentFun(data);
        }
    });
});
