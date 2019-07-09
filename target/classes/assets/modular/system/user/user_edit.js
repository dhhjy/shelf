/**
 * 用户详情对话框
 */
var UserInfoDlg = {
    data: {
        deptId: "",
        deptName: ""
    }
};

layui.use(['layer', 'form', 'admin', 'laydate', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var laydate = layui.laydate;
    var layer = layui.layer;

    // 让当前iframe弹层高度适应
    admin.iframeAuto();

    //获取用户信息
    var ajax = new $ax(Feng.ctxPath + "/mgr/getUserInfo?userId=" + Feng.getUrlParam("userId"));
    var result = ajax.start();
    form.val('userForm', result.data);
    //由于select 不能通过 form.val 设置值，需要自己手动赋值
    ajax = new $ax(Feng.ctxPath + "/dept/tree");
    var data = ajax.start();

    var option = "";
    $.each(data, function (i, o) {
        if(o.id === 0)
            return;
        if(result.data.deptId === o.id)
        {
            option += "<option value='" + o.id + "' selected>" + o.name + "</option>";
            $("#deptId").val(o.id);
        }
        else{
            option += "<option value='" + o.id + "'>" + o.name + "</option>";
        }
    });
    $("#deptName").append(option);
    // 单独渲染select，否则不生效
    form.render("select");

    form.on('select(deptName)', function (data) {
        $("#deptId").val(data.value);
    });

    // 添加表单验证方法
    form.verify({
        psw: [/^[\S]{6,12}$/, '密码必须6到12位，且不能出现空格'],
        repsw: function (value) {
            if (value !== $('#userForm input[name=password]').val()) {
                return '两次密码输入不一致';
            }
        }
    });

    // 渲染时间选择框
    laydate.render({
        elem: '#birthday'
    });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/mgr/edit", function () {
            Feng.success("修改成功！");
            window.location.href = Feng.ctxPath + "/mgr";
        }, function (data) {
            Feng.error("修改成功！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();

        return false;
    });

    //返回按钮
    $("#backupPage").click(function () {
        window.location.href = Feng.ctxPath + "/dictType";
    });
});