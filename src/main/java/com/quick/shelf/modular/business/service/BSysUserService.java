package com.quick.shelf.modular.business.service;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.mapper.BSysUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

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
//    public List<BLocation> infoMerging(Integer userId) {
//        // GPS地址
//        List<BLocation> locations = this.bLocationService.selectBLocationByUserId(userId);
//
//        // 淘宝收货地址
//        List<String> tbAddress = getTBAddress(userId);
//
//        if(null == tbAddress)
//            tbAddress = new ArrayList<>();
//
//        // 身份证
//        String idCardAddress = getIdCardAddress(userId);
//
//        for (Location locat : locations) {
//            for (String str : tbAddress) {
//                locat.setDescription(str);
//                tbAddress.remove(str);
//
//                String locatStr = locat.getAddrStr();
//
//                if(null == idCardAddress )
//                    break;
//
//                Map<String,String> map = addressResolution(str);
//                String province = map.get("province"), city = map.get("city"), county = map.get("county");
//
//                if(null != province && idCardAddress.contains(province))
//                {
//                    if(null != city && idCardAddress.contains(city))
//                    {
//                        locat.setDeviceInfo("模糊匹配");
//                        if(null != county && idCardAddress.contains(county))
//                            locat.setDeviceInfo("精准匹配");
//                    }else{
//                        locat.setDeviceInfo("1");
//                    }
//                }else{
//                    locat.setDeviceInfo("1");
//                }
//                break;
//            }
//        }
//        return locations;
//    }
}
