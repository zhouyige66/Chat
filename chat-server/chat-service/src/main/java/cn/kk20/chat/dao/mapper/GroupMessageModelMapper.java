package cn.kk20.chat.dao.mapper;

import cn.kk20.chat.dao.model.GroupMessageModel;
import cn.kk20.chat.dao.model.GroupMessageModelQuery;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GroupMessageModelMapper {
    long countByCondition(GroupMessageModelQuery example);

    int deleteByCondition(GroupMessageModelQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(GroupMessageModel record);

    int insertSelective(GroupMessageModel record);

    List<GroupMessageModel> selectByConditionWithBLOBs(GroupMessageModelQuery example);

    List<GroupMessageModel> selectByCondition(GroupMessageModelQuery example);

    GroupMessageModel selectByPrimaryKey(Long id);

    int updateByConditionSelective(@Param("record") GroupMessageModel record, @Param("example") GroupMessageModelQuery example);

    int updateByExampleWithBLOBs(@Param("record") GroupMessageModel record, @Param("example") GroupMessageModelQuery example);

    int updateByCondition(@Param("record") GroupMessageModel record, @Param("example") GroupMessageModelQuery example);

    int updateByPrimaryKeySelective(GroupMessageModel record);

    int updateByConditionWithBLOBs(GroupMessageModel record);

    int updateByPrimaryKey(GroupMessageModel record);
}