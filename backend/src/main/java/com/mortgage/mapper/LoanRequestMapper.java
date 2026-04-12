package com.mortgage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mortgage.model.entity.LoanRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoanRequestMapper extends BaseMapper<LoanRequest> {
}
