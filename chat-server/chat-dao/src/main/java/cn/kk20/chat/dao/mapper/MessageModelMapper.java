package cn.kk20.chat.dao.mapper;

import cn.kk20.chat.dao.model.MessageModel;
import cn.kk20.chat.dao.model.MessageModelQuery;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MessageModelMapper {
    long countByCondition(MessageModelQuery example);

    int deleteByCondition(MessageModelQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(MessageModel record);

    int insertSelective(MessageModel record);

    List<MessageModel> selectByConditionWithBLOBs(MessageModelQuery example);

    List<MessageModel> selectByCondition(MessageModelQuery example);

    MessageModel selectByPrimaryKey(Long id);

    int updateByConditionSelective(@Param("record") MessageModel record, @Param("example") MessageModelQuery example);

    int updateByExampleWithBLOBs(@Param("record") MessageModel record, @Param("example") MessageModelQuery example);

    int updateByCondition(@Param("record") MessageModel record, @Param("example") MessageModelQuery example);

    int updateByPrimaryKeySelective(MessageModel record);

    int updateByConditionWithBLOBs(MessageModel record);

    int updateByPrimaryKey(MessageModel record);
}