package com.quick.shelf.modular.business.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.shelf.modular.business.entity.BPicture;
import org.apache.ibatis.annotations.Param;

public interface BPictureMapper extends BaseMapper<BPicture> {

    /**
     * 通过用户主键id查询关联的各类照片信息
     * 银行卡啊照片 身份证照片 人脸识别照片
     *
     * @param userId
     * @return BPicture
     */
    BPicture selectBPictureByUserId(@Param("userId") Integer userId);
}
