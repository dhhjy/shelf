layui.use(['layer', 'form', 'table', 'ztree', 'laydate', 'admin', 'ax'], function () {
    var layer = layui.layer;
    var table = layui.table;
    var $ZTree = layui.ztree;
    var $ax = layui.ax;
    var laydate = layui.laydate;
    var admin = layui.admin;

    /**
     * 订单管理——逾期列表
     */
    var overdueList = {
        tableId: "overdueListTable",    //表格id
        condition: {
            name: "",
            deptId: "",
            timeLimit: ""
        }
    };

    /**
     * 初始化表格的列
     */
    overdueList.initColumn = function () {
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
            {field: 'deptName', title: '所属部门', minWidth: 150},
            {field: 'createTime', title: '申请时间', minWidth: 200},
            {minWidth: 200, align: 'center', title: '操作', toolbar: '#tableBar', fixed: 'right'}
        ]];
    };

    /**
     * 选择部门时
     */
    overdueList.onClickDept = function (e, treeId, treeNode) {
        overdueList.condition.deptId = treeNode.id;
        overdueList.search();
    };

    /**
     * 点击查询按钮
     */
    overdueList.search = function () {
        var queryData = {};
        queryData['deptId'] = overdueList.condition.deptId;
        queryData['name'] = $("#name").val();
        queryData['timeLimit'] = $("#timeLimit").val();
        table.reload(overdueList.tableId, {where: queryData});
    };

    /**
     * 导出excel按钮
     */
    overdueList.exportExcel = function () {
        var checkRows = table.checkStatus(overdueList.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 打开分期账单窗口
     */
    overdueList.openOverdueDisposeDlg = function (data) {
        admin.putTempData('formOk', false);
        var index = layer.open({
            type: 2,
            title: '逾期处理',
            content: Feng.ctxPath + "/stagingList/overdueStagingIndex/" + data.userId,
            closeBtn: 0,
            shadeClose: true,
            resize: true,
            btn: ['关闭'],
            end: function () {
                admin.getTempData('formOk') && orderDetails.search();
            }
        });
        layer.full(index);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + overdueList.tableId,
        url: Feng.ctxPath + '/orderDetails/overdueList',
        page: true,
        limits: [20, 50, 100],  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
        limit: 20, //每页默认显示的数量
        height: "full-98",
        cellMinWidth: 100,
        cols: overdueList.initColumn()
    });

    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });

    //初始化左侧部门树
    var ztree = new $ZTree("deptTree", "/dept/tree");
    ztree.bindOnClick(overdueList.onClickDept);
    ztree.init();

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        overdueList.search();
    });

    // 导出excel
    $('#btnExp').click(function () {
        overdueList.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + overdueList.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        // 逾期处理
        if (layEvent === 'overdueDispose') {
            overdueList.openOverdueDisposeDlg(data);
        }
    });
});
