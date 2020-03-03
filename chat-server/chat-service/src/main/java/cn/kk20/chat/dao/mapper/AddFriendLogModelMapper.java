package cn.kk20.chat.dao.mapper;

import cn.kk20.chat.dao.model.AddFriendLogModel;
import cn.kk20.chat.dao.model.AddFriendLogModelQuery;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AddFriendLogModelMapper {
    long countByCondition(AddFriendLogModelQuery example);

    int deleteByCondition(AddFriendLogModelQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(AddFriendLogModel record);

    int insertSelective(AddFriendLogModel record);

    List<AddFriendLogModel> selectByCondition(AddFriendLogModelQuery example);

    AddFriendLogModel selectByPrimaryKey(Long id);

    int updateByConditionSelective(@Param("record") AddFriendLogModel record, @Param("example") AddFriendLogModelQuery example);

    int updateByCondition(@Param("record") AddFriendLogModel record, @Param("example") AddFriendLogModelQuery example);

    int updateByPrimaryKeySelective(AddFriendLogModel record);

    int updateByPrimaryKey(AddFriendLogModel record);
}