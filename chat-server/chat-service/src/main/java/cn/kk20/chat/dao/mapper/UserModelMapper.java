package cn.kk20.chat.dao.mapper;

import cn.kk20.chat.dao.model.UserModel;
import cn.kk20.chat.dao.model.UserModelQuery;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserModelMapper {
    long countByCondition(UserModelQuery example);

    int deleteByCondition(UserModelQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(UserModel record);

    int insertSelective(UserModel record);

    List<UserModel> selectByConditionWithBLOBs(UserModelQuery example);

    List<UserModel> selectByCondition(UserModelQuery example);

    UserModel selectByPrimaryKey(Long id);

    int updateByConditionSelective(@Param("record") UserModel record, @Param("example") UserModelQuery example);

    int updateByExampleWithBLOBs(@Param("record") UserModel record, @Param("example") UserModelQuery example);

    int updateByCondition(@Param("record") UserModel record, @Param("example") UserModelQuery example);

    int updateByPrimaryKeySelective(UserModel record);

    int updateByConditionWithBLOBs(UserModel record);

    int updateByPrimaryKey(UserModel record);
}