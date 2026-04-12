package com.mortgage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mortgage.model.entity.RepaymentDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RepaymentDetailMapper extends BaseMapper<RepaymentDetail> {
    void batchInsert(java.util.List<RepaymentDetail> list);
}
