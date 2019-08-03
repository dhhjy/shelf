layui.use(['layer', 'form', 'table', 'ztree', 'laydate', 'admin', 'ax'], function () {
    var table = layui.table;
    var $ZTree = layui.ztree;
    var laydate = layui.laydate;

    /**
     * 订单管理——已完结订单列表
     */
    var orderEndList = {
        tableId: "orderEndListTable",    //表格id
        condition: {
            name: "",
            deptId: "",
            timeLimit: ""
        }
    };

    /**
     * 初始化表格的列
     */
    orderEndList.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'userId', hide: true, title: '用户id'},
            {field: 'orderNumber', title: '订单号', width: 200},
            {field: 'name', title: '姓名'},
            {field: 'productCodeName', title: '产品名称', minWidth: 150},
            {field: 'modeOfRepaymentName', title: '还款方式', minWidth: 150},
            {
                field: 'amount', title: '借款金额(元)', minWidth: 150, templet: function (d) {
                    return "<span class='tag normal'>" + d.amount + "</span>"
                }
            },
            {field: 'debtDuration', title: '借款时长(天)'},
            {field: 'statusName', title: '当前状态'},
            {field: 'deptName', title: '所属部门'},
            {field: 'createTime', title: '申请时间', minWidth: 200},
            {minWidth: 200, align: 'center', title: '操作', toolbar: '#tableBar', fixed: 'right'}
        ]];
    };

    /**
     * 选择部门时
     */
    orderEndList.onClickDept = function (e, treeId, treeNode) {
        orderEndList.condition.deptId = treeNode.id;
        orderEndList.search();
    };

    /**
     * 点击查询按钮
     */
    orderEndList.search = function () {
        var queryData = {};
        queryData['deptId'] = orderEndList.condition.deptId;
        queryData['name'] = $("#name").val();
        queryData['timeLimit'] = $("#timeLimit").val();
        table.reload(orderEndList.tableId, {where: queryData});
    };

    /**
     * 导出excel按钮
     */
    orderEndList.exportExcel = function () {
        var checkRows = table.checkStatus(orderEndList.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 打开分期账单窗口
     */
    orderEndList.openStagingListDlg = function (data) {
        window.location = Feng.ctxPath + "/stagingList/index/" + data.userId + "/" + data.orderNumber + "/" + "orderEndIndex";
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + orderEndList.tableId,
        url: Feng.ctxPath + '/orderDetails/orderEndList',
        page: true,
        limits: [20, 50, 100],  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
        limit: 20, //每页默认显示的数量
        height: "full-98",
        cellMinWidth: 100,
        cols: orderEndList.initColumn()
    });

    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });

    //初始化左侧部门树
    var ztree = new $ZTree("deptTree", "/dept/tree");
    ztree.bindOnClick(orderEndList.onClickDept);
    ztree.init();

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        orderEndList.search();
    });

    // 导出excel
    $('#btnExp').click(function () {
        orderEndList.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + orderEndList.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        // 分期账单
        if (layEvent === 'stagingList') {
            orderEndList.openStagingListDlg(data);
        }
    });
});
