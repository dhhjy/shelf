layui.use(['layer', 'form', 'table', 'admin', 'ax'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;

    /**
     * 系统管理--角色管理
     */
    var Role = {
        tableId: "roleTable",    //表格id
        condition: {
            roleName: ""
        }
    };

    /**
     * 初始化表格的列
     */
    Role.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'roleId', hide: true, sort: true, title: '角色id'},
            {field: 'name', sort: true, title: '名称'},
            {field: 'pName', sort: true, title: '上级角色'},
            {field: 'description', sort: true, title: '别名'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Role.search = function () {
        var queryData = {};
        queryData['roleName'] = $("#roleName").val();
        table.reload(Role.tableId, {where: queryData});
    };

    /**
     * 弹出添加角色
     */
    Role.openAddRole = function () {
        admin.putTempData('formOk', false);
        var index = layer.open({
            type: 2,
            title: '',
            content: Feng.ctxPath + '/role/role_add',
            closeBtn: 0,
            btn: ['确定', '关闭'],
            yes: function (index) {
                //取子页面的btn
                var btn = layer.getChildFrame('#saveButton', index);
                btn.click();
            },
            end: function () {
                admin.getTempData('formOk') && table.reload(Role.tableId);
            }
        });
        layer.full(index);
    };

    /**
     * 导出excel按钮
     */
    Role.exportExcel = function () {
        var checkRows = table.checkStatus(Role.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击编辑角色
     *
     * @param data 点击按钮时候的行数据
     */
    Role.onEditRole = function (data) {
        admin.putTempData('formOk', false);
        var index = layer.open({
            type: 2,
            title: '',
            content: Feng.ctxPath + '/role/role_edit?roleId=' + data.roleId,
            closeBtn: 0,
            btn: ['确定', '关闭'],
            yes: function (index) {
                //取子页面的btn
                var btn = layer.getChildFrame('#saveButton', index);
                btn.click();
            },
            end: function () {
                admin.getTempData('formOk') && table.reload(Role.tableId);
            }
        });
        layer.full(index);
    };

    /**
     * 点击删除角色
     *
     * @param data 点击按钮时候的行数据
     */
    Role.onDeleteRole = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/role/remove", function () {
                layer.closeAll();
                Feng.success("删除成功!");
                table.reload(Role.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("roleId", data.roleId);
            ajax.start();
        };
        layui.admin.open({
            type: 1,
            title: "删除角色",
            btn: ['确定', '取消'],
            content: '<div style="padding: 50px; line-height: 22px; background-color: #f2f2f2; color: #000000;">是否确定删除角色 ' + data.name + ' ?</div>',
            yes: function () {
                operation();
            }
        });
    };

    /**
     * 分配菜单
     *
     * @param data 点击按钮时候的行数据
     */
    Role.roleAssign = function (data) {
        admin.putTempData('success', false);
        layui.admin.popupRight({
            type: 2,
            title: '菜单分配',
            content: Feng.ctxPath + '/role/role_assign/' + data.roleId,
            btn: ['分配'],
            yes: function (index) {
                //取子页面的btn
                var btn = layer.getChildFrame('#saveButton', index);
                btn.click();
            },
            end: function () {
                admin.getTempData('success') && table.reload(Role.tableId);
            }
        });
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Role.tableId,
        url: Feng.ctxPath + '/role/list',
        page: true,
        limits: [20, 50, 100],  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
        limit: 20, //每页默认显示的数量
        height: "full-98",
        cellMinWidth: 100,
        cols: Role.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Role.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Role.openAddRole();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Role.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Role.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Role.onEditRole(data);
        } else if (layEvent === 'delete') {
            Role.onDeleteRole(data);
        } else if (layEvent === 'roleAssign') {
            Role.roleAssign(data);
        }
    });
});
