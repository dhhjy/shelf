layui.use(['layer', 'form', 'table', 'ztree', 'laydate', 'admin', 'ax', 'element'], function () {
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ZTree = layui.ztree;
    var $ax = layui.ax;
    var laydate = layui.laydate;
    var admin = layui.admin;
    var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块

    //监听导航点击
    element.on('nav(demo)', function(elem){
        //console.log(elem)
        layer.msg(elem.text());
    });

    /**
     * 征信管理——立木征信
     */
    var liMuTable = {
        tableId: "liMuTable",    //表格id
        condition: {
            name: "",
            deptId: "",
            timeLimit: ""
        }
    };

    /**
     * 初始化表格的列
     */
    liMuTable.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', title: '用户id'},
            {field: 'name', title: '姓名'},
            {field: 'phoneNumber', title: '电话', minWidth: 150},
            {field: 'idCard', title: '身份证号', minWidth: 200},
            {field: 'deptName', title: '部门', minWidth: 200},
            {field: 'createTime', sort: true, title: '认证时间', minWidth: 200},
            {minWidth: 200, align:'center', title: '操作', toolbar: '#tableBar', fixed: 'right'}
        ]];
    };

    /**
     * 选择部门时
     */
    liMuTable.onClickDept = function (e, treeId, treeNode) {
        liMuTable.condition.deptId = treeNode.id;
        liMuTable.search();
    };

    /**
     * 点击查询按钮
     */
    liMuTable.search = function () {
        var queryData = {};
        queryData['deptId'] = liMuTable.condition.deptId;
        queryData['name'] = $("#name").val();
        queryData['timeLimit'] = $("#timeLimit").val();
        table.reload(liMuTable.tableId, {where: queryData});
    };

    /**
     * 导出excel按钮
     */
    liMuTable.exportExcel = function () {
        var checkRows = table.checkStatus(liMuTable.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 打开立木运营商报告
     */
    liMuTable.onMobileReport = function (data) {
        var index = layer.open({
            type: 2,
            title: '运营商报告',
            content: Feng.ctxPath + "/liMu/getMobileReport/" + data.id,
            closeBtn: 0,
            shadeClose: true,
            resize: true,
            btn: ['关闭']
        });
        layer.full(index);
    };

    /**
     * 打开淘宝报告
     */
    liMuTable.onTaoBaoReport = function (data) {
        var index = layer.open({
            type: 2,
            title: '淘宝报告',
            content: Feng.ctxPath + "/liMu/getTaoBaoReport/" + data.id,
            closeBtn: 0,
            shadeClose: true,
            resize: true,
            btn: ['关闭']
        });
        layer.full(index);
    };

    /**
     * 打开立方升级报告
     */
    liMuTable.onLiFangUpgradeCheck = function (data) {
        var index = layer.open({
            type: 2,
            title: '升级报告',
            content: Feng.ctxPath + "/liMu/getLiFangUpgradeCheck/" + data.id,
            closeBtn: 0,
            shadeClose: true,
            resize: true,
            btn: ['关闭']
        });
        layer.full(index);
    };

    /**
     * 打开设备指纹报告
     */
    liMuTable.onFingerprint = function (data) {
        var index = layer.open({
            type: 2,
            title: '设备报告',
            content: Feng.ctxPath + "/liMu/getFingerprint/" + data.id,
            closeBtn: 0,
            shadeClose: true,
            resize: true,
            btn: ['关闭']
        });
        layer.full(index);
    };

    /**
     * 打开机审报告
     */
    liMuTable.onMachineCheck = function (data) {
        var index = layer.open({
            type: 2,
            title: '机审报告',
            content: Feng.ctxPath + "/liMu/getMachineCheck/" + data.id,
            closeBtn: 0,
            shadeClose: true,
            resize: true,
            btn: ['关闭']
        });
        layer.full(index);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + liMuTable.tableId,
        url: Feng.ctxPath + '/liMu/list',
        page: true,
        limits: [20, 50, 100],  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
        limit: 20, //每页默认显示的数量
        height: "full-98",
        cellMinWidth: 100,
        cols: liMuTable.initColumn()
    });

    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });

    //初始化左侧部门树
    var ztree = new $ZTree("deptTree", "/dept/tree");
    ztree.bindOnClick(liMuTable.onClickDept);
    ztree.init();

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        liMuTable.search();
    });

    // 导出excel
    $('#btnExp').click(function () {
        liMuTable.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + liMuTable.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'mobileReport') {
            liMuTable.onMobileReport(data);
        } else if (layEvent === 'taobaoReport') {
            liMuTable.onTaoBaoReport(data);
        }  else if (layEvent === 'lifangupgradecheck') {
            liMuTable.onLiFangUpgradeCheck(data);
        }  else if (layEvent === 'fingerprint') {
            liMuTable.onFingerprint(data);
        }  else if (layEvent === 'machinecheck') {
            liMuTable.onMachineCheck(data);
        }
    });
});
