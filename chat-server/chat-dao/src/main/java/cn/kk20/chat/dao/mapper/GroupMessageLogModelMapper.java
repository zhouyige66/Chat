package cn.kk20.chat.dao.mapper;

import cn.kk20.chat.dao.model.GroupMessageLogModel;
import cn.kk20.chat.dao.model.GroupMessageLogModelQuery;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GroupMessageLogModelMapper {
    long countByCondition(GroupMessageLogModelQuery example);

    int deleteByCondition(GroupMessageLogModelQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(GroupMessageLogModel record);

    int insertSelective(GroupMessageLogModel record);

    List<GroupMessageLogModel> selectByCondition(GroupMessageLogModelQuery example);

    GroupMessageLogModel selectByPrimaryKey(Long id);

    int updateByConditionSelective(@Param("record") GroupMessageLogModel record, @Param("example") GroupMessageLogModelQuery example);

    int updateByCondition(@Param("record") GroupMessageLogModel record, @Param("example") GroupMessageLogModelQuery example);

    int updateByPrimaryKeySelective(GroupMessageLogModel record);

    int updateByPrimaryKey(GroupMessageLogModel record);
}