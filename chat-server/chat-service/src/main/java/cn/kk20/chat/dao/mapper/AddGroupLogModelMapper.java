package cn.kk20.chat.dao.mapper;

import cn.kk20.chat.dao.model.AddGroupLogModel;
import cn.kk20.chat.dao.model.AddGroupLogModelQuery;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AddGroupLogModelMapper {
    long countByCondition(AddGroupLogModelQuery example);

    int deleteByCondition(AddGroupLogModelQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(AddGroupLogModel record);

    int insertSelective(AddGroupLogModel record);

    List<AddGroupLogModel> selectByCondition(AddGroupLogModelQuery example);

    AddGroupLogModel selectByPrimaryKey(Long id);

    int updateByConditionSelective(@Param("record") AddGroupLogModel record, @Param("example") AddGroupLogModelQuery example);

    int updateByCondition(@Param("record") AddGroupLogModel record, @Param("example") AddGroupLogModelQuery example);

    int updateByPrimaryKeySelective(AddGroupLogModel record);

    int updateByPrimaryKey(AddGroupLogModel record);
}