layui.use(['layer', 'table', 'ax', 'admin'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var layer = layui.layer;

    /**
     * 字典类型表管理
     */
    var DictType = {
        tableId: "dictTypeTable"
    };

    /**
     * 初始化表格的列
     */
    DictType.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'dictTypeId', hide: true, title: '字典类型id'},
            {
                field: 'name', sort: true, title: '类型名称', templet: function (d) {
                    var url = Feng.ctxPath + '/dict?dictTypeId=' + d.dictTypeId;
                    return '<a style="color: #01AAED;" href="' + url + '">' + d.name + '</a>';
                }
            },
            {
                field: 'code', sort: true, title: '类型编码', templet: function (d) {
                    var url = Feng.ctxPath + '/dict?dictTypeId=' + d.dictTypeId;
                    return '<a style="color: #01AAED;" href="' + url + '">' + d.code + '</a>';
                }
            },
            {
                field: 'systemFlag', sort: true, title: '是否是系统字典', templet: function (d) {
                    if (d.systemFlag === 'Y') {
                        return "是";
                    } else {
                        return "否";
                    }
                }
            },
            {field: 'description', sort: true, title: '字典描述'},
            {
                field: 'status', sort: true, title: '状态', templet: function (d) {
                    if (d.status === 'ENABLE') {
                        return "启用";
                    } else {
                        return "禁用";
                    }
                }
            },
            {field: 'createTime', sort: true, title: '添加时间'},
            {field: 'createUser', sort: true, title: '创建人'},
            {align: 'center', toolbar: '#tableBar', title: '操作'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    DictType.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        queryData['systemFlag'] = $("#systemFlag").val();
        queryData['status'] = $("#status").val();
        table.reload(DictType.tableId, {where: queryData});
    };

    /**
     * 弹出添加对话框
     */
    DictType.openAddDlg = function () {
        admin.putTempData('formOk', false);
        var index = layer.open({
            type: 2,
            title: '',
            content: Feng.ctxPath + '/dictType/add',
            closeBtn: 0,
            btn: ['确定', '关闭'],
            yes: function (index) {
                //取子页面的btn
                var btn = layer.getChildFrame('#dictTypeSubmitBtn', index);
                btn.click();
            },
            end: function () {
                admin.getTempData('formOk') && table.reload(DictType.tableId);
            }
        });
        layer.full(index);
    };

    /**
     * 点击编辑
     *
     * @param data 点击按钮时候的行数据
     */
    DictType.openEditDlg = function (data) {
        admin.putTempData('formOk', false);
        var index = layer.open({
            type: 2,
            title: '',
            content: Feng.ctxPath + '/dictType/edit?dictTypeId=' + data.dictTypeId,
            closeBtn: 0,
            btn: ['确定', '关闭'],
            yes: function (index) {
                //取子页面的btn
                var btn = layer.getChildFrame('#dictTypeSubmitBtn', index);
                btn.click();
            },
            end: function () {
                admin.getTempData('formOk') && table.reload(DictType.tableId);
            }
        });
        layer.full(index);
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    DictType.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/dictType/delete", function (data) {
                layer.closeAll();
                Feng.info("删除成功!");
                table.reload(DictType.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("dictTypeId", data.dictTypeId);
            ajax.start();
        };
        layui.admin.open({
            type: 1,
            title: "删除字典",
            btn: ['确定', '取消'],
            content: '<div style="padding: 50px; line-height: 22px; background-color: #f2f2f2; color: #000000;">是否确定删除字典 ' + data.name + '?</div>',
            yes: function () {
                operation();
            }
        });
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + DictType.tableId,
        url: Feng.ctxPath + '/dictType/list',
        page: true,
        height: "full-98",
        cellMinWidth: 100,
        cols: DictType.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        DictType.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        DictType.openAddDlg();
    });

    // 工具条点击事件
    table.on('tool(' + DictType.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            DictType.openEditDlg(data);
        } else if (layEvent === 'delete') {
            DictType.onDeleteItem(data);
        }
    });
});
