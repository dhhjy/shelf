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
    var BSysUser = {
        tableId: "bSysUserTable",    //表格id
        condition: {
            name: "",
            deptId: "",
            timeLimit: ""
        }
    };

    /**
     * 初始化表格的列
     */
    BSysUser.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'userId', hide: true, sort: true, title: '用户id'},
            {field: 'userAccount', sort: true, title: '账号', minWidth: 150},
            {field: 'name', sort: true, title: '姓名'},
            {field: 'phoneNumber', sort: true, title: '手机号', minWidth: 150},
            {field: 'idCard', sort: true, title: '身份证号', minWidth: 150},
            {field: 'sex', sort: true, title: '性别'},
            {field: 'deptName', sort: true, title: '部门', minWidth: 150},
            {field: 'createTime', sort: true, title: '创建时间', minWidth: 150},
            {field: 'status', sort: true, templet: '#statusTpl', title: '状态'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 280}
        ]];
    };

    /**
     * 选择部门时
     */
    BSysUser.onClickDept = function (e, treeId, treeNode) {
        BSysUser.condition.deptId = treeNode.id;
        BSysUser.search();
    };

    /**
     * 点击查询按钮
     */
    BSysUser.search = function () {
        var queryData = {};
        queryData['deptId'] = BSysUser.condition.deptId;
        queryData['name'] = $("#name").val();
        queryData['timeLimit'] = $("#timeLimit").val();
        table.reload(BSysUser.tableId, {where: queryData});
    };

    /**
     * 跳转到添加用户的界面
     */
    BSysUser.openAddUser = function () {
        admin.putTempData('formOk', false);
        var index = layer.open({
            type: 2,
            title: '',
            content: Feng.ctxPath + '/mgr/user_add',
            btn: ['确定', '关闭'],
            yes: function (index) {
                //取子页面的btn
                var btn = layer.getChildFrame('#userSubmitBtn', index);
                btn.click();
            },
            end: function () {
                admin.getTempData('formOk') && table.reload(BSysUser.tableId);
            }
        });
        layer.full(index);
    };

    /**
     * 导出excel按钮
     */
    BSysUser.exportExcel = function () {
        var checkRows = table.checkStatus(BSysUser.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击编辑用户按钮时
     *
     * @param data 点击按钮时候的行数据
     */
    BSysUser.onEditUser = function (data) {
        admin.putTempData('formOk', false);
        var index = layer.open({
            type: 2,
            title: '',
            content: Feng.ctxPath + '/mgr/user_edit?userId=' + data.userId,
            btn: ['确定', '关闭'],
            yes: function (index) {
                //取子页面的btn
                var btn = layer.getChildFrame('#userSubmitBtn', index);
                btn.click();
            },
            end: function () {
                admin.getTempData('formOk') && table.reload(BSysUser.tableId);
            }
        });
        layer.full(index);
    };

    /**
     * 点击删除用户按钮
     *
     * @param data 点击按钮时候的行数据
     */
    BSysUser.onDeleteUser = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/mgr/delete", function () {
                layer.closeAll();
                table.reload(BSysUser.tableId);
                Feng.success("删除成功!");
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("userId", data.userId);
            ajax.start();
        };

        layui.admin.open({
            type: 1,
            title: "删除用户",
            btn: ['确定', '取消'],
            content: '<div style="padding: 50px; line-height: 22px; background-color: #f2f2f2; color: #000000;">是否确定删除用户 ' + data.account + ' ?</div>',
            yes: function () {
                operation();
            }
        });
    };

    /**
     * 分配角色
     *
     * @param data 点击按钮时候的行数据
     */
    BSysUser.roleAssign = function (data) {
        admin.putTempData('success', false);
        layui.admin.popupRight({
            type: 2,
            title: '角色分配',
            content: Feng.ctxPath + '/mgr/role_assign?userId=' + data.userId,
            btn: ['分配'],
            yes: function (index) {
                //取子页面的btn
                var btn = layer.getChildFrame('#saveButton', index);
                btn.click();
            },
            end: function () {
                admin.getTempData('success') && table.reload(BSysUser.tableId);
            }
        });
    };

    /**
     * 重置密码
     *
     * @param data 点击按钮时候的行数据
     */
    BSysUser.resetPassword = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/mgr/reset", function (data) {
                layer.closeAll();
                Feng.success("重置密码成功!");
            }, function (data) {
                Feng.error("重置密码失败!");
            });
            ajax.set("userId", data.userId);
            ajax.start();
        };

        layui.admin.open({
            type: 1,
            title: "重置密码",
            btn: ['确定', '取消'],
            content: '<div style="padding: 50px; line-height: 22px; background-color: #f2f2f2; color: #000000;">是否重置密码为111111?</div>',
            yes: function () {
                operation();
            }
        });
    };

    /**
     * 修改用户状态
     *
     * @param userId 用户id
     * @param checked 是否选中（true,false），选中就是解锁用户，未选中就是锁定用户
     */
    BSysUser.changeUserStatus = function (userId, checked) {
        if (checked) {
            var ajax = new $ax(Feng.ctxPath + "/mgr/unfreeze", function (data) {
                Feng.success("解除冻结成功!");
            }, function (data) {
                Feng.error("解除冻结失败!");
                table.reload(BSysUser.tableId);
            });
            ajax.set("userId", userId);
            ajax.start();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/mgr/freeze", function (data) {
                Feng.success("冻结成功!");
            }, function (data) {
                Feng.error("冻结失败!" + data.responseJSON.message + "!");
                table.reload(BSysUser.tableId);
            });
            ajax.set("userId", userId);
            ajax.start();
        }
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + BSysUser.tableId,
        url: Feng.ctxPath + '/business/bSysUser/list',
        page: true,
        limits: [20, 50, 100],  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
        limit: 20, //每页默认显示的数量
        height: "full-98",
        cellMinWidth: 100,
        cols: BSysUser.initColumn()
    });

    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });

    //初始化左侧部门树
    var ztree = new $ZTree("deptTree", "/dept/tree");
    ztree.bindOnClick(BSysUser.onClickDept);
    ztree.init();

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        BSysUser.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        BSysUser.openAddUser();
    });

    // 导出excel
    $('#btnExp').click(function () {
        BSysUser.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + BSysUser.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            BSysUser.onEditUser(data);
        } else if (layEvent === 'delete') {
            BSysUser.onDeleteUser(data);
        } else if (layEvent === 'roleAssign') {
            BSysUser.roleAssign(data);
        } else if (layEvent === 'reset') {
            BSysUser.resetPassword(data);
        }
    });

    // 修改user状态
    form.on('switch(status)', function (obj) {

        var userId = obj.elem.value;
        var checked = obj.elem.checked ? true : false;

        BSysUser.changeUserStatus(userId, checked);
    });

});
