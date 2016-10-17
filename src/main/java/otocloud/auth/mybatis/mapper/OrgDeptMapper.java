package otocloud.auth.mybatis.mapper;

import java.util.List;
import org.apache.ibatis.session.RowBounds;
import otocloud.auth.mybatis.entity.OrgDept;
import otocloud.auth.mybatis.entity.OrgDeptExample;

public interface OrgDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrgDept record);

    int insertSelective(OrgDept record);

    List<OrgDept> selectByExampleWithRowbounds(OrgDeptExample example, RowBounds rowBounds);

    List<OrgDept> selectByExample(OrgDeptExample example);

    OrgDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrgDept record);

    int updateByPrimaryKey(OrgDept record);

    List<OrgDept> selectByAcct(int orgAcctId);
}