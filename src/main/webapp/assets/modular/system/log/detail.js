layui.use(['layer', 'form', 'admin', 'ax', 'code'], function () {
    var admin = layui.admin;
    var $ = layui.jquery;

    // 让当前iframe弹层高度适应
    admin.iframeAuto();

    layui.code({
        title: '操作详情',
        encode: true,
        skin: 'notepad',
        about: false
    }); //引用code方法

    //返回按钮
    $("#backupPage").click(function () {
        window.location.href = Feng.ctxPath + "/log";
    });
});