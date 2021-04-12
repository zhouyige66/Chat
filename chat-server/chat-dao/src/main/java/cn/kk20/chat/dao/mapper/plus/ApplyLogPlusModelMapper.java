package cn.kk20.chat.dao.mapper.plus;

import cn.kk20.chat.dao.mapper.ApplyLogModelMapper;
import cn.kk20.chat.dao.model.plus.ApplyLogPlusModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplyLogPlusModelMapper extends ApplyLogModelMapper {

    ApplyLogPlusModel selectByPrimaryKey2(Long id);

}