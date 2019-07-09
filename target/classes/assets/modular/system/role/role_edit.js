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
    var layer = layui.layer;

    // 让当前iframe弹层高度适应
    admin.iframeAuto();

    //初始化角色的详情数据
    var ajax = new $ax(Feng.ctxPath + "/role/view/" + Feng.getUrlParam("roleId"));
    var result = ajax.start();
    form.val('roleForm',result.data);

    //由于select 不能通过 form.val 设置值，需要自己手动赋值
    ajax = new $ax(Feng.ctxPath + "/role/roleTreeList");
    var data = ajax.start();

    var option = "";
    $.each(data, function (i, o) {
        if(o.id === 0)
            return;

        if(result.data.pid === o.id)
        {
            option += "<option value='" + o.id + "' selected>" + o.name + "</option>";
            $("#pid").val(o.id);
        }else if(result.data.pid === 0){
            option += "<option value='" + result.data.pid + "' selected>顶级</option>";
            $("#pid").val(result.data.pid);
        }
        else{
            option += "<option value='" + o.id + "'>" + o.name + "</option>";
        }
    });
    $("#pName").append(option);
    // 单独渲染select，否则不生效
    form.render("select");

    form.on('select(pName)', function (data) {
        $("#pid").val(data.value);
    });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/role/edit", function (data) {
            Feng.success("修改成功!");
            window.location.href = Feng.ctxPath + "/role/";
        }, function (data) {
            Feng.error("修改失败!" + data.responseJSON.message + "!");
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