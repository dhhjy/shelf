package com.quick.shelf.modular.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.modular.business.entity.BPicture;
import com.quick.shelf.modular.business.mapper.BPictureMapper;
import org.springframework.stereotype.Service;

/**
 * 用户关联的照片表业务层
 * 主要操作 'b_picture' 表
 *
 * @author zcn
 * @date 2019/07/12
 */
@Service
public class BPictureService extends ServiceImpl<BPictureMapper, BPicture> {

    /**
     *  通过用户表主键查询关联用户所有照片URL地址
     *
     * @param userId
     * @return BPicture
     */
    public BPicture selectBPictureByUserId(Integer userId){
        return this.baseMapper.selectBPictureByUserId(userId);
    }
}
