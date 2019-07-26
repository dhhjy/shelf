layui.use(['layer', 'table', 'ax', 'admin'], function () {
    var $ = layui.$;
    var table = layui.table;

    /**
     * 字典类型表管理
     */
    var Mobile = {
        tableId: "mobileTable"
    };

    /**
     * 初始化表格的列
     */
    Mobile.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', sort: false, title: '主键'},
            {field: 'isHitRiskList', sort: false, title: '运营商'},
            {field: 'callNum', sort: true, title: '号码'},
            {field: 'create_time', sort: true, title: '查询时间',templet:function(d){
                    var util = layui.util;
                    var format="yyyy-MM-dd";
                    return util.toDateString(d.create_time, format);
                }}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Mobile.search = function () {
        var queryData = {};
        queryData['tMobile'] = $("#tMobile").val();
        table.reload(Mobile.tableId, {where: queryData});
    };

    /**
     * 导出excel按钮
     */
    Mobile.exportExcel = function () {
        var checkRows = table.checkStatus(Mobile.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Mobile.tableId,
        url: Feng.ctxPath + '/mobile/list',
        page: true,
        limits: [200, 500, 1000, 2000, 5000, 10000],  //每页条数的选择项，默认：[10,20,30,40,50,60,70,80,90]。
        limit: 200, //每页默认显示的数量
        height: "full-98",
        cellMinWidth: 100,
        cols: Mobile.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Mobile.search();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Mobile.exportExcel();
    });
});
