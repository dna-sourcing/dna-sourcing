package com.dna.sourcing.mapper;

import com.dna.sourcing.dao.Event;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface EventMapper extends Mapper<Event> {

}