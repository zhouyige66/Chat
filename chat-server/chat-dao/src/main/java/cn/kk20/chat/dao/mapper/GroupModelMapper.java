package cn.kk20.chat.dao.mapper;

import cn.kk20.chat.dao.model.GroupModel;
import cn.kk20.chat.dao.model.GroupModelQuery;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GroupModelMapper {
    long countByCondition(GroupModelQuery example);

    int deleteByCondition(GroupModelQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(GroupModel record);

    int insertSelective(GroupModel record);

    List<GroupModel> selectByConditionWithBLOBs(GroupModelQuery example);

    List<GroupModel> selectByCondition(GroupModelQuery example);

    GroupModel selectByPrimaryKey(Long id);

    int updateByConditionSelective(@Param("record") GroupModel record, @Param("example") GroupModelQuery example);

    int updateByExampleWithBLOBs(@Param("record") GroupModel record, @Param("example") GroupModelQuery example);

    int updateByCondition(@Param("record") GroupModel record, @Param("example") GroupModelQuery example);

    int updateByPrimaryKeySelective(GroupModel record);

    int updateByConditionWithBLOBs(GroupModel record);

    int updateByPrimaryKey(GroupModel record);
}