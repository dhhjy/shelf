/**
 * 角色详情对话框
 */
var RoleInfoDlg = {
    data: {
        pid: "",
        pName: ""
    }
};

layui.use(['layer', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;

    // 让当前iframe弹层高度适应
    admin.iframeAuto();

    var ajax = new $ax(Feng.ctxPath + "/role/roleTreeList", function (result) {
        var option = "";
        $.each(result, function (i, o) {
            if(o.id === 0)
                return;
            option += "<option value='" + o.id + "'>" + o.name + "</option>";
        });
        $("#pName").append(option);
        // 单独渲染select，否则不生效
        form.render("select");
    });
    ajax.start();

    form.on('select(pName)', function (data) {
        $("#pid").val(data.value);
    });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/role/add", function () {
            Feng.success("添加成功！");
            window.location.href = Feng.ctxPath + "/role/";
        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();

        return false;
    });

    //返回按钮
    $("#backupPage").click(function () {
        window.location.href = Feng.ctxPath + "/role/";
    });
});