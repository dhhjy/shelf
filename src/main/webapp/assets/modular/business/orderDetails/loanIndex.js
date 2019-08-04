layui.use(['layer', 'form', 'table', 'ztree', 'laydate', 'admin', 'ax'], function () {
    var layer = layui.layer;
    var table = layui.table;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;
    var admin = layui.admin;

    /**
     * 订单管理——待放款订单
     */
    var loanTable = {
        tableId: "toLoanListTable",    //表格id
        condition: {
            name: "",
            deptId: "",
            timeLimit: ""
        }
    };

    /**
     * 初始化表格的列
     */
    loanTable.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'userId', title: '用户id'},
            {field: 'name', title: '姓名'},
            {field: 'productCodeName', title: '产品名称', minWidth: 150},
            {field: 'modeOfRepaymentName', title: '还款方式', minWidth: 150},
            {
                field: 'amount', title: '借款金额', minWidth: 150, templet: function (d) {
                    return "<span class='tag normal'>" + d.amount + " 元</span>"
                }
            },
            {
                field: 'debtDuration', title: '借款时长', templet: function (d) {
                    return d.debtDuration + " 天";
                }
            },
            {
                field: 'accrual', title: '总利息', templet: function (d) {
                    return "<span class='tag danger'>" + d.accrual + " 元</span>"
                }
            },
            {
                field: 'serviceCharge', title: '服务费', templet: function (d) {
                    return "<span class='tag danger'>" + d.serviceCharge + " 元</span>"
                }
            },
            {
                field: 'actualAmount', title: '实际放款', templet: function (d) {
                    return "<span class='tag normal'>" + d.actualAmount + " 元</span>"
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
    loanTable.onClickDept = function (e, treeId, treeNode) {
        loanTable.condition.deptId = treeNode.id;
        loanTable.search();
    };

    /**
     * 点击查询按钮
     */
    loanTable.search = function () {
        var queryData = {};
        queryData['deptId'] = loanTable.condition.deptId;
        queryData['name'] = $("#name").val();
        queryData['timeLimit'] = $("#timeLimit").val();
        table.reload(loanTable.tableId, {where: queryData});
    };

    /**
     * 导出excel按钮
     */
    loanTable.exportExcel = function () {
        var checkRows = table.checkStatus(loanTable.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 打开放款审核
     */
    loanTable.onLoanCheck = function (data) {
        admin.putTempData('formOk', false);
        var index = layer.open({
            type: 2,
            title: '人工审核',
            content: Feng.ctxPath + "/orderDetails/loanCheckIndex/" + data.userId,
            closeBtn: 0,
            shadeClose: true,
            resize: true,
            btn: ['关闭'],
            end: function () {
                admin.getTempData('formOk') && loanTable.search();
            }
        });
        layer.full(index);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + loanTable.tableId,
        url: Feng.ctxPath + '/orderDetails/toLoanList',
        page: true,
        limits: [20, 50, 100],  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
        limit: 20, //每页默认显示的数量
        height: "full-98",
        cellMinWidth: 100,
        cols: loanTable.initColumn()
    });

    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });

    //初始化左侧部门树
    var ztree = new $ZTree("deptTree", "/dept/tree");
    ztree.bindOnClick(loanTable.onClickDept);
    ztree.init();

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        loanTable.search();
    });

    // 导出excel
    $('#btnExp').click(function () {
        loanTable.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + loanTable.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'loanCheck') {
            loanTable.onLoanCheck(data);
        }
    });
});
