package cn.kk20.chat.dao.mapper;

import cn.kk20.chat.dao.model.LoginLogModel;
import cn.kk20.chat.dao.model.LoginLogModelQuery;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginLogModelMapper {
    long countByCondition(LoginLogModelQuery example);

    int deleteByCondition(LoginLogModelQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(LoginLogModel record);

    int insertSelective(LoginLogModel record);

    List<LoginLogModel> selectByCondition(LoginLogModelQuery example);

    LoginLogModel selectByPrimaryKey(Long id);

    int updateByConditionSelective(@Param("record") LoginLogModel record, @Param("example") LoginLogModelQuery example);

    int updateByCondition(@Param("record") LoginLogModel record, @Param("example") LoginLogModelQuery example);

    int updateByPrimaryKeySelective(LoginLogModel record);

    int updateByPrimaryKey(LoginLogModel record);
}