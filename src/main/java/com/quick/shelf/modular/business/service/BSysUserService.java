package com.quick.shelf.modular.business.service;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.modular.business.entity.BIdentityInfo;
import com.quick.shelf.modular.business.entity.BLiMuData;
import com.quick.shelf.modular.business.entity.BLocation;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.mapper.BSysUserMapper;
import com.quick.shelf.modular.constant.BusinessConst;
import com.quick.shelf.modular.creditPort.liMu.LiMuConstantEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户信息主表的业务层
 * 主要操作 'b_sys_user' 表
 *
 * @author zcn
 * @date 2019/07/11
 */
@Service
public class BSysUserService extends ServiceImpl<BSysUserMapper, BSysUser> {

    @Resource
    private BLocationService bLocationService;

    @Resource
    private BLiMuService bLiMuService;

    @Resource
    private BIdentityInfoService bIdentityInfoService;

    /**
     * 根据条件查询用户列表
     *
     * @author zcn
     * @Date 2019/7/10 15:35
     */
    public Page<Map<String, Object>> selectBSysUsers(DataScope dataScope, String name, String beginTime, String endTime, Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectBSysUsers(page, dataScope, name, beginTime, endTime, deptId);
    }

    /**
     * 根据用户主键ID查询用户信息
     *
     * @author zcn
     * @Date 2019/7/10 15:35
     */
    public BSysUser selectBSysUserByUserId(Integer userId) {
        return this.baseMapper.selectById(userId);
    }

    /**
     *
     */
    public void insert(BSysUser bSysUser) {
        this.baseMapper.insert(bSysUser);
    }

    /**
     * 逻辑删除用户
     *
     * @param userId
     */
    public void delete(Integer userId) {
        this.baseMapper.deleteBSysUserByUserId(userId);
    }

    /**
     * 根据手机号查询用户
     *
     * @param phoneNumber
     * @return
     */
    public BSysUser selectBSysUserByPhone(String phoneNumber) {
        return this.baseMapper.getByPhoneNumber(phoneNumber);
    }

    /**
     * 修改用户登陆密码
     */
    public void updateUserPassword(BSysUser bSysUser) {
        this.baseMapper.updateUserPassword(bSysUser);
    }

    /**
     * 查询电话是否存在
     *
     * @param phone
     * @return boolean 不存在返回 true 存在返回 false
     */
    public boolean phoneIsExist(String phone) {
        return this.baseMapper.getByPhoneNumber(phone) == null;
    }

    public Integer getBSysUserCount(Long deptId) {
        return this.baseMapper.getBSysUserCount(deptId);
    }

    public Integer getBSysUserToDayCount(Long deptId) {
        return this.baseMapper.getBSysUserToDayCount(deptId);
    }

    /**
     * 信息合并
     */
    public List<BLocation> infoMerging(Integer userId) {
        // GPS地址
        List<BLocation> locations = this.bLocationService.selectBLocationByUserId(userId);

        // 淘宝收货地址
        List<String> tbAddress = getTBAddress(userId);

        if (null == tbAddress)
            tbAddress = new ArrayList<>();

        // 身份证
        String idCardAddress = getIdCardAddress(userId);

        for (BLocation locat : locations) {
            for (String str : tbAddress) {
                locat.setDescription(str);
                tbAddress.remove(str);

                if (null == idCardAddress)
                    break;

                Map<String, String> map = addressResolution(str);
                String province = map.get("province"), city = map.get("city"), county = map.get("county");

                if (null != province && idCardAddress.contains(province)) {
                    if (null != city && idCardAddress.contains(city)) {
                        locat.setDeviceInfo("模糊匹配");
                        if (null != county && idCardAddress.contains(county))
                            locat.setDeviceInfo("精准匹配");
                    } else {
                        locat.setDeviceInfo("1");
                    }
                } else {
                    locat.setDeviceInfo("1");
                }
                break;
            }
        }
        return locations;
    }

    /**
     * 根据用户主键获取用户淘宝
     * 原始数据报告中的收货地址
     *
     * @param userId
     * @return List<Location>
     */
    private List<String> getTBAddress(Integer userId) {
        List<String> tbAddress = new ArrayList<>();
        BLiMuData liMuData = this.bLiMuService.selectBLiMuDataByUserId(userId,
                BusinessConst.ORIGINAL_DATA.toString(), LiMuConstantEnum.API_NAME_TB.getApiName());

        if (null == liMuData)
            return null;

        // 获取JSON格式数据
        JSONObject jsonData = JSONObject.parseObject(liMuData.getDataValue()).getJSONObject("data");
        JSONArray jsonArr = jsonData.getJSONArray("recAddInfos");
        for (Object json : jsonArr) {
            JSONObject data = JSONObject.parseObject(json.toString());
            tbAddress.add(data.getString("receiveAddress").replace(" ", ""));
        }
        return tbAddress;
    }

    /**
     * 根据用户主键获取身份证信息
     */
    public String getIdCardAddress(Integer userId) {
        BIdentityInfo bIdentityInfo = this.bIdentityInfoService.selectBIdentityInfoByUserId(userId);
        return null == bIdentityInfo ? null : bIdentityInfo.getAddress();
    }

    /**
     * 解析地址
     *
     * @param address
     * @return
     */
    private Map<String, String> addressResolution(String address) {
        Map<String, String> map = new HashMap<>();
        address = address.replace("中国", "");
        String regex = "((?<province>[^省]+省|.+自治区)|上海|北京|天津|重庆)(?<city>[^市]+市|.+自治州)(?<county>[^县]+县|.+区|.+镇|.+局)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        Matcher m = Pattern.compile(regex).matcher(address);
        String province = null, city = null, county = null;
        while (m.find()) {
            province = m.group("province");
            city = m.group("city");
            county = m.group("county");
        }
        map.put("province", province);
        map.put("city", city);
        map.put("county", county);
        return map;
    }
}
