package com.dna.sourcing.mapper.contract;

import com.dna.sourcing.dao.contract.Contract;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ContractMapper extends Mapper<Contract> {

    void insertBatch(@Param("contractList") List<Contract> contractList);

}