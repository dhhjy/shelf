layui.use(['layer', 'table', 'ax', 'treetable', 'admin'], function () {
    var layer = layui.layer;
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var treetable = layui.treetable;
    var admin = layui.admin;

    /**
     * 基础字典管理
     */
    var Dict = {
        tableId: "dictTable"
    };

    /**
     * 初始化表格的列
     */
    Dict.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'dictId', hide: true, title: '字典id'},
            {field: 'name', sort: true, title: '字典名称'},
            {field: 'code', sort: true, title: '字典编码'},
            {field: 'description', sort: true, title: '字典的描述'},
            {
                field: 'status', sort: true, title: '状态', templet: function (d) {
                    if (d.status === 'ENABLE') {
                        return "启用";
                    } else {
                        return "禁用";
                    }
                }
            },
            {field: 'createTime', sort: true, title: '创建时间'},
            {field: 'createUser', sort: true, title: '创建人'},
            {align: 'center', toolbar: '#tableBar', title: '操作'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Dict.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        Dict.initTable(Dict.tableId, queryData);
    };

    /**
     * 弹出添加对话框
     */
    Dict.openAddDlg = function () {
        admin.putTempData('formOk', false);
        var index = layer.open({
            type: 2,
            title: '',
            content: Feng.ctxPath + '/dict/add?dictTypeId=' + $("#dictTypeId").val(),
            closeBtn: 0,
            btn: ['确定', '关闭'],
            yes: function (index) {
                //取子页面的btn
                var btn = layer.getChildFrame('#dictSubmitBtn', index);
                btn.click();
            },
            end: function () {
                admin.getTempData('formOk') && Dict.search();
            }
        });
        layer.full(index);
    };

    /**
     * 点击编辑
     *
     * @param data 点击按钮时候的行数据
     */
    Dict.openEditDlg = function (data) {
        admin.putTempData('formOk', false);
        var index = layer.open({
            type: 2,
            title: '',
            content: Feng.ctxPath + '/dict/edit?dictId=' + data.dictId,
            closeBtn: 0,
            btn: ['确定', '关闭'],
            yes: function (index) {
                //取子页面的btn
                var btn = layer.getChildFrame('#dictSubmitBtn', index);
                btn.click();
            },
            end: function () {
                admin.getTempData('formOk') && Dict.search();
            }
        });
        layer.full(index);
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Dict.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/dict/delete", function () {
                layer.closeAll();
                Feng.info("删除成功!");
                Dict.search();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("dictId", data.dictId);
            ajax.start();
        };
        layui.admin.open({
            type: 1,
            title: "删除字典",
            btn: ['确定', '取消'],
            content: '<div style="padding: 50px; line-height: 22px; background-color: #f2f2f2; color: #000000;">是否确定删除字典 ' + data.name + ' ?</div>',
            yes: function () {
                operation();
            }
        });
    };

    /**
     * 渲染表格
     */
    Dict.initTable = function (dictId, data) {
        return treetable.render({
            elem: '#' + dictId,
            url: Feng.ctxPath + '/dict/list?dictTypeId=' + $("#dictTypeId").val(),
            where: data,
            height: "full-98",
            cellMinWidth: 100,
            cols: Dict.initColumn(),
            page: false,
            treeColIndex: 2,
            treeSpid: "0",
            treeIdName: 'dictId',
            treePidName: 'parentId',
            treeDefaultClose: false,
            treeLinkage: true
        });
    };

    Dict.initTable(Dict.tableId);

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Dict.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Dict.openAddDlg();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Dict.exportExcel();
    });

    // 关闭页面
    $('#btnBack').click(function () {
        window.location.href = Feng.ctxPath + "/dictType";
    });

    // 工具条点击事件
    table.on('tool(' + Dict.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Dict.openEditDlg(data);
        } else if (layEvent === 'delete') {
            Dict.onDeleteItem(data);
        }
    });
});
