$(function(){
    //导航
    $(".nav ul a").click(function(){
        $(".nav>ul a").removeClass("current");
        $(this).addClass("current");
    });
    //管理中心导航
    $(".Mnav ul a").click(function(){
        $(".Mnav ul a").removeClass("current");
        $(this).addClass("current");
    });

    //输入框
    $(".placeholder").click(function(){
        $(this).siblings(".Input").focus();
    });
    $(".Input").keydown(function(){
        $(this).siblings(".placeholder").hide();
    });
    $(".Input").blur(function(){
        if($(this).val() == ''){
            $(this).siblings(".placeholder").show();
        }
    });

    //获取验证码
    $(".GET").click(function(){
        var m = 60;
        $(this).addClass("btn_disabled").attr("disabled","disabled");
        var time = setInterval(function(){

            $(".GET").val(m+"s后重新获取");

            m--;

            if(m<0){
                clearInterval(time);
                $(".GET").removeClass("btn_disabled").removeAttr("disabled").val("获取短信验证码");
            }

        },1000);
    });

    //报告查询侧边菜单
    $(".MleftNav a").click(function(){
        $(".MleftNav a").removeClass("current");
        $(this).addClass("current");
    });
    //报告查询地区切换
    //公积金
    $(".loginSearch select,.gjjLogin select").change(function(){
        var name = $(this).val();

        if(name=='北京'){
            $(this).parent().parent().find(".gjj").html("证件号:");
        }else if(name=='上海'){
            $(this).parent().parent().find(".gjj").html("用户名:");
        }else{
            $(this).parent().parent().find(".gjj").html("账号:");
        }
    });
    //社保
    $(".sbLogin select").change(function(){
        var name = $(this).val();

        if(name=='北京' || name=='上海'){
            $(this).parent().parent().find(".sb").html("身份证号:");
        }else{
            $(this).parent().parent().find(".sb").html("用户名:");
        }
    });

    //翻页
    $(".paging li:not(:first,:last) a").click(function(){
        $(".paging a").removeClass("current");
        $(this).addClass("current");
    });


    //api资源库-数据接口-电商数据接口tab切换
    $(".APItab span").click(function(){
        $(this).addClass("current").siblings().removeClass("current");
        $(".APIport>div").eq($(this).index()).show().siblings().hide();
    });

    /*20170328资源库*/
    /*资源库左侧菜单*/
    $('.library_nav li').on('click',function(){
        $(this).addClass('current').siblings().removeClass('current');
    });

    /*资源库tab切换定位*/
    if($('.library_tab_box').length>0){
        var library_tabTop = $('.library_tab_box').offset().top;
        var winTop = 0;
        var eleTop = [];
        var eleTopFun = function (){
            $('.scenario h4').map(function(index){
                eleTop[index] = $(this).offset().top-80;
            });
        };
        eleTopFun();
        /*window scroll function*/
        function winScr(){
            $(this).scrollTop()>library_tabTop?$('.library_tab').addClass('position'):$('.library_tab').removeClass('position');
            winTop = $(this).scrollTop();
            if(winTop>=eleTop[0] && winTop<eleTop[1]){
                $('.library_tab span').eq(0).addClass('current').siblings().removeClass('current');
            }else if(winTop>=eleTop[1] && winTop<eleTop[2]){
                $('.library_tab span').eq(1).addClass('current').siblings().removeClass('current');
            }else if(winTop>=eleTop[2] && winTop<eleTop[3]){
                $('.library_tab span').eq(2).addClass('current').siblings().removeClass('current');
            }else if(winTop>=eleTop[3]){
                $('.library_tab span').eq(3).addClass('current').siblings().removeClass('current');
            };
        }
        /*滚动页面*/
        $(window).on('scroll',winScr);
        /*点击定位*/
        $('.library_tab span').on('click',function(){
            $(window).off('scroll');
            $(this).addClass('current').siblings().removeClass('current');
            $('html,body').stop().animate({'scrollTop':eleTop[$(this).index()]},300,function(){
                $(window).on('scroll',winScr);
            });
        });
    }
});
//报告查询运营商右侧快速定位
var arr = [];
function arrtop(){
    if($(".MtableMain").length>0){
        for (var i=0; i<6 ; i++) {
            arr[i]=$(".MtableMain").eq(i).offset().top;
        }
    }else{
        return;
    }
};

function backtop(){
    var webW = $("html,body").width();
    var webH = $("html,body").height();
    if(webW<1400){
        $(".rightGps").css("margin-left","582px");
    }
    if(webH>800){
        $(".rightGps").css("top","20%");
    }


    //
    $(".rightGps .backTop1").click(function(){
        $(".rightGps li a").removeClass("current");
        $("html,body").stop().animate({"scrollTop":0},400);
    });

    $(".rightGps li a").click(function(){
        $(".rightGps li a").removeClass("current");
        $(this).addClass("current");
    });


    arrtop();

    var rightGpsTOP;

    $(window).scroll(function(){
        rightGpsTOP = $(".rightGps").offset().top;
        if( arr[0] <= rightGpsTOP &&  rightGpsTOP< arr[1]){
            num(0);
        }else if(arr[1] <= rightGpsTOP &&  rightGpsTOP< arr[2]){
            num(1);
        }else if(arr[2] <= rightGpsTOP &&  rightGpsTOP< arr[3]){
            num(2);
        }else if(arr[3] <= rightGpsTOP &&  rightGpsTOP< arr[4]){
            num(3);
        }else if(arr[4] <= rightGpsTOP &&  rightGpsTOP< arr[5]){
            num(4);
        }else {
            num(5);
        }

        function num(m){
            $(".rightGps li a").removeClass("current");
            $(".rightGps li a").eq(m).addClass("current");
        }


    });

}
//公积金，社保，电商返回顶部
function other(){
    var webW = $("html,body").width();
    if(webW<1400){
        $(".rightGps").css("margin-left","582px");
    }
    $(".rightGps .backTop").click(function(){
        $("html,body").stop().animate({"scrollTop":0},400);
    });



}

/** 后台错误码转换 勿修改 **/
function convertErrMsgByData(data) {
    var ret = "";
    if(data){
        if("1007" == data.code || "1019" == data.code || "1010" == data.code || "1101" == data.code || "1102" == data.code ||
            "1014" == data.code || "1015" == data.code ||
            "1103" == data.code || "1104" == data.code || "1105" == data.code || "1106" == data.code || "1107" == data.code ||
            "1108" == data.code || "1109" == data.code || "1111" == data.code || "1117" == data.code || "1118" == data.code ||
            "1110" == data.code || "2030" == data.code ||
            "1119" == data.code || "2003" == data.code || "2008" == data.code || "2009" == data.code || "2010" == data.code ||
            "2011" == data.code || "2014" == data.code || "2015" == data.code || "2016" == data.code || "2017" == data.code ||
            "2020" == data.code || "2021" == data.code || "2022" == data.code || "2025" == data.code || "2026" == data.code ||
            "2032" == data.code || "2034" == data.code || "2035" == data.code || "2036" == data.code || "2037" == data.code ||
            "2038" == data.code || "3001" == data.code || "3002" == data.code || "2050" == data.code || "2045" == data.code ||
            "2046" == data.code || "2043" == data.code || "1009" == data.code || "1016" == data.code || "1017" == data.code ||
            "1120" == data.code || "1121" == data.code || "1122" == data.code || "1123" == data.code || "1124" == data.code ||
            "1140" == data.code || "2027" == data.code || "2028" == data.code || "2029" == data.code ||
            0 == data.code.indexOf('30') || 0 == data.code.indexOf('40') || 0 == data.code.indexOf('50') ||
            0 == data.code.indexOf('60') || 0 == data.code.indexOf('70') || 0 == data.code.indexOf('80') ||
            0 == data.code.indexOf('90')
        ) {
            ret = data.msg;
        }else if("2052" == data.code){
            ret = "该手机号查询已达上限，请明日再试";
        }else if("1115" == data.code){
            ret = "查询数据为空";
        }else{
            ret = "操作失败,请稍后重试";
        }
    }else {
        ret = "操作失败,请稍后重试";
    }
    return ret;
}

//判断当前返回的是否为登录页面
function validateLogin(data){
    var ret =false;
    if(typeof(data)=='string' && data.indexOf('登录')>=0 && data.indexOf('form-login')>=0){
        ret=true;
    }
    return ret;
}