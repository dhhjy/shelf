/**
 * 用户黑名单管理
 */
var BBlackList = {
    tableId: "blackListTable",    //表格id
    condition: {
        name: "",
        deptId: "",
        timeLimit: ""
    },
    data: {
        deptId: "",
        deptName: ""
    }
};

layui.use(['layer', 'form', 'table', 'ztree', 'laydate', 'admin', 'ax'], function () {
    var table = layui.table;
    var $ZTree = layui.ztree;
    var $ax = layui.ax;
    var laydate = layui.laydate;

    /**
     * 初始化表格的列
     */
    BBlackList.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'userId', hide: true, sort: true, title: '用户id'},
            {field: 'userAccount', sort: true, title: '拉黑账号', minWidth: 150},
            {field: 'name', sort: true, title: '拉黑姓名', minWidth: 150},
            {field: 'deptName', sort: true, title: '部门', minWidth: 150},
            {field: 'operate', sort: true, title: '操作人', minWidth: 150},
            {field: 'description', sort: true, title: ' 拉黑原因', minWidth: 300},
            {field: 'blackTime', sort: true, title: '拉黑时间', minWidth: 200},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200, fixed: 'right'}
        ]];
    };

    /**
     * 选择部门时
     */
    BBlackList.onClickDept = function (e, treeId, treeNode) {
        BBlackList.condition.deptId = treeNode.id;
        BBlackList.search();
    };

    /**
     * 点击查询按钮
     */
    BBlackList.search = function () {
        var queryData = {};
        queryData['deptId'] = BBlackList.condition.deptId;
        queryData['name'] = $("#name").val();
        queryData['timeLimit'] = $("#timeLimit").val();
        table.reload(BBlackList.tableId, {where: queryData});
    };

    /**
     * 导出excel按钮
     */
    BBlackList.exportExcel = function () {
        var checkRows = table.checkStatus(BBlackList.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 解除限制（解除拉黑）
     *
     * @param data 点击按钮时候的行数据
     */
    BBlackList.onUnBlackList = function (data) {
        var ajax = new $ax(Feng.ctxPath + "/blackList/unBlackList/" + data.userId, function (result) {
            if (result.success) {
                Feng.info(result.message);
                table.reload(BBlackList.tableId);
            }
        }, function (data) {
            Feng.error("解除限制失败!" + data.responseJSON.message + "!");
        });
        ajax.type = "PUT";
        ajax.start();
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + BBlackList.tableId,
        url: Feng.ctxPath + '/blackList/list',
        page: true,
        limits: [20, 50, 100],  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
        limit: 20, //每页默认显示的数量
        height: "full-98",
        cellMinWidth: 100,
        cols: BBlackList.initColumn()
    });

    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });

    //初始化左侧部门树
    var ztree = new $ZTree("deptTree", "/dept/tree");
    ztree.bindOnClick(BBlackList.onClickDept);
    ztree.init();

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        BBlackList.search();
    });

    // 导出excel
    $('#btnExp').click(function () {
        BBlackList.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + BBlackList.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'details') {
            // 解除限制
            BBlackList.onUnBlackList(data);
        }
    });
});
