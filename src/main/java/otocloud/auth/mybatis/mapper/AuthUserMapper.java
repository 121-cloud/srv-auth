package otocloud.auth.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import otocloud.auth.mybatis.entity.AuthUser;
import otocloud.auth.mybatis.entity.AuthUserExample;

import java.util.List;

public interface AuthUserMapper {
    int countByExample(AuthUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AuthUser record);

    int insertSelective(AuthUser record);

    List<AuthUser> selectByExampleWithRowbounds(AuthUserExample example, RowBounds rowBounds);

    List<AuthUser> selectByExample(AuthUserExample example);

    AuthUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AuthUser record, @Param("example") AuthUserExample example);

    int updateByExample(@Param("record") AuthUser record, @Param("example") AuthUserExample example);

    int updateByPrimaryKeySelective(AuthUser record);

    int updateByPrimaryKey(AuthUser record);

    boolean isRegisteredCellNo(String cellNo);

    List<AuthUser> selectByAcctAndDept(@Param("orgAcctId") int orgAcctId, @Param("orgDeptId") int orgDeptId);

    int insertBatch(List<AuthUser> userList);
}