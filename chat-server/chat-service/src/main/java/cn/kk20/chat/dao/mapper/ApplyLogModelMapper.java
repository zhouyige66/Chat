package cn.kk20.chat.dao.mapper;

import cn.kk20.chat.dao.model.ApplyLogModel;
import cn.kk20.chat.dao.model.ApplyLogModelQuery;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ApplyLogModelMapper {
    long countByCondition(ApplyLogModelQuery example);

    int deleteByCondition(ApplyLogModelQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(ApplyLogModel record);

    int insertSelective(ApplyLogModel record);

    List<ApplyLogModel> selectByCondition(ApplyLogModelQuery example);

    ApplyLogModel selectByPrimaryKey(Long id);

    int updateByConditionSelective(@Param("record") ApplyLogModel record, @Param("example") ApplyLogModelQuery example);

    int updateByCondition(@Param("record") ApplyLogModel record, @Param("example") ApplyLogModelQuery example);

    int updateByPrimaryKeySelective(ApplyLogModel record);

    int updateByPrimaryKey(ApplyLogModel record);
}