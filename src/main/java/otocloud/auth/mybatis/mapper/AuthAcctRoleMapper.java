package otocloud.auth.mybatis.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import otocloud.auth.mybatis.entity.AuthAcctRole;
import otocloud.auth.mybatis.entity.AuthAcctRoleExample;

public interface AuthAcctRoleMapper {
    int countByExample(AuthAcctRoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AuthAcctRole record);

    int insertSelective(AuthAcctRole record);

    List<AuthAcctRole> selectByExampleWithRowbounds(AuthAcctRoleExample example, RowBounds rowBounds);

    List<AuthAcctRole> selectByExample(AuthAcctRoleExample example);

    AuthAcctRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AuthAcctRole record, @Param("example") AuthAcctRoleExample example);

    int updateByExample(@Param("record") AuthAcctRole record, @Param("example") AuthAcctRoleExample example);

    int updateByPrimaryKeySelective(AuthAcctRole record);

    int updateByPrimaryKey(AuthAcctRole record);
}