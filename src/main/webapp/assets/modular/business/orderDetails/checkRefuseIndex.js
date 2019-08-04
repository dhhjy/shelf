layui.use(['layer', 'form', 'table', 'ztree', 'laydate', 'admin', 'ax'], function () {
    var table = layui.table;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 订单管理——拒绝订单列表
     */
    var checkRefuse = {
        tableId: "checkRefuseListTable",    //表格id
        condition: {
            name: "",
            deptId: "",
            timeLimit: ""
        }
    };

    /**
     * 初始化表格的列
     */
    checkRefuse.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'userId', title: '用户id'},
            {field: 'name', title: '姓名'},
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
            {field: 'statusName', title: '当前状态'},
            {field: 'deptName', title: '所属部门', minWidth: 150},
            {
                field: 'statusName', title: '拒绝原因', minWidth: 450, templet: function (d) {
                    if (d.drawMessage !== '')
                        return d.drawMessage;
                    else
                        return d.checkMessage;
                }
            },
            {field: 'createTime', title: '申请时间', minWidth: 200}
        ]];
    };

    /**
     * 选择部门时
     */
    checkRefuse.onClickDept = function (e, treeId, treeNode) {
        checkRefuse.condition.deptId = treeNode.id;
        checkRefuse.search();
    };

    /**
     * 点击查询按钮
     */
    checkRefuse.search = function () {
        var queryData = {};
        queryData['deptId'] = checkRefuse.condition.deptId;
        queryData['name'] = $("#name").val();
        queryData['timeLimit'] = $("#timeLimit").val();
        table.reload(checkRefuse.tableId, {where: queryData});
    };

    /**
     * 导出excel按钮
     */
    checkRefuse.exportExcel = function () {
        var checkRows = table.checkStatus(checkRefuse.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + checkRefuse.tableId,
        url: Feng.ctxPath + '/orderDetails/checkRefuseList',
        page: true,
        limits: [20, 50, 100],  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
        limit: 20, //每页默认显示的数量
        height: "full-98",
        cellMinWidth: 100,
        cols: checkRefuse.initColumn()
    });

    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });

    //初始化左侧部门树
    var ztree = new $ZTree("deptTree", "/dept/tree");
    ztree.bindOnClick(checkRefuse.onClickDept);
    ztree.init();

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        checkRefuse.search();
    });

    // 导出excel
    $('#btnExp').click(function () {
        checkRefuse.exportExcel();
    });
});
