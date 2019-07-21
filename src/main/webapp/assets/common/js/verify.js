//邮箱
var emailReg = /^([a-z0-9A-Z_]+[-|\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\.)+[a-zA-Z]{2,}$/;
//手机号
var mobileReg = /^0?1(3|4|5|7|8)\d{9}$/;
//电话号码
var telReg = /(^[0-9]{3,4}-[0-9]{7,8}-[0-9]{3,4}$)|(^[0-9]{3,4}-[0-9]{7,8}$)|(^[0-9]{7,8}-[0-9]{3,4}$)|(^[0-9]{7,15}$)/;
var domainReg = /http:\/\/.+/;
var zipcodeReg = /^[0-9]{6}$/;
//数字
var numReg = /^[0-9]+$/;
//身份证
var isIDCardReg = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/;
//用户名
var userNameReg = /^[_a-zA-Z0-9\u4E00-\u9FFF]{2,20}/;
//中文
var zh = /[\u4E00-\u9FFF]/g;
var httpUrl = /[a-zA-z]+:\/\/[^s]*/;
var doubleReg = /((^0\.)|(^[1-9]+[0-9]*\.\d)$)|(^[1-9]+[0-9]*$)/;

function checkEmail(val) {
    return emailReg.test(val);
}

function checkDouble(double) {
    return doubleReg.test(double);
}

function checkHttpUrl(url) {
    return httpUrl.test(url);
}

function checkIsIDCard(isIDCard) {
    return isIDCardReg.test(isIDCard);
}

function checkMobile(mobile) {
    return mobileReg.test(mobile);
}

function checkTel(tel) {
    return telReg.test(tel);
}

function checkDomain(domain) {
    return domainReg.test(domain);
}

function checkZipcode(zipcode) {
    return zipcodeReg.test(zipcode);
}

function checkNum(num) {
    return numReg.test(num);
}

function checkZh(val) {
    return zh.test(val);
}