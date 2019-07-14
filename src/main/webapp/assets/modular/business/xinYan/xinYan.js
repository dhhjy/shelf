layui.use(['layer', 'form', 'table', 'ztree', 'laydate', 'admin', 'ax'], function () {
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ZTree = layui.ztree;
    var $ax = layui.ax;
    var laydate = layui.laydate;
    var admin = layui.admin;

    /**
     * 系统管理--用户管理
     */
    var xinYanTable = {
        tableId: "xinYanTable",    //表格id
        condition: {
            name: "",
            deptId: "",
            timeLimit: ""
        }
    };

    /**
     * 初始化表格的列
     */
    xinYanTable.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', title: '用户id'},
            {field: 'name', title: '姓名'},
            {field: 'phoneNumber', title: '电话', minWidth: 150},
            {field: 'idCard', title: '身份证号', minWidth: 200},
            {field: 'deptName', title: '部门', minWidth: 200},
            {field: 'createTime', sort: true, title: '认证时间', minWidth: 200},
            {minWidth: 260, align:'center', title: '操作', toolbar: '#tableBar'}
        ]];
    };

    /**
     * 选择部门时
     */
    xinYanTable.onClickDept = function (e, treeId, treeNode) {
        xinYanTable.condition.deptId = treeNode.id;
        xinYanTable.search();
    };

    /**
     * 点击查询按钮
     */
    xinYanTable.search = function () {
        var queryData = {};
        queryData['deptId'] = xinYanTable.condition.deptId;
        queryData['name'] = $("#name").val();
        queryData['timeLimit'] = $("#timeLimit").val();
        table.reload(xinYanTable.tableId, {where: queryData});
    };

    /**
     * 导出excel按钮
     */
    xinYanTable.exportExcel = function () {
        var checkRows = table.checkStatus(xinYanTable.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 打开全景雷达
     */
    xinYanTable.onRadar = function (data) {
        var index = layer.open({
            type: 2,
            title: '全景雷达',
            content: Feng.ctxPath + "/xinYan/getReDerData/" + data.id,
            closeBtn: 0,
            shadeClose: true,
            resize: true,
            btn: ['关闭']
        });
        layer.full(index);
    };

    /**
     * 打开芝麻分
     */
    xinYanTable.onTaoBaoWeb = function (data) {
        var index = layer.open({
            type: 2,
            title: '芝麻分报告',
            content: Feng.ctxPath + "/xinYan/getTaoBaoWebReport/" + data.id,
            closeBtn: 0,
            shadeClose: true,
            resize: true,
            btn: ['关闭']
        });
        layer.full(index);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + xinYanTable.tableId,
        url: Feng.ctxPath + '/xinYan/list',
        page: true,
        limits: [20, 50, 100],  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
        limit: 20, //每页默认显示的数量
        height: "full-98",
        cellMinWidth: 100,
        cols: xinYanTable.initColumn()
    });

    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });

    //初始化左侧部门树
    var ztree = new $ZTree("deptTree", "/dept/tree");
    ztree.bindOnClick(xinYanTable.onClickDept);
    ztree.init();

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        xinYanTable.search();
    });

    // 导出excel
    $('#btnExp').click(function () {
        xinYanTable.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + xinYanTable.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'radar') {
            xinYanTable.onRadar(data);
        } else if (layEvent === 'taobaoweb') {
            xinYanTable.onTaoBaoWeb(data);
        }
    });
});
