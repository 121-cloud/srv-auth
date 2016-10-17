package otocloud.auth.mybatis.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthUserExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AuthUserExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andOrgAcctIdIsNull() {
            addCriterion("org_acct_id is null");
            return (Criteria) this;
        }

        public Criteria andOrgAcctIdIsNotNull() {
            addCriterion("org_acct_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrgAcctIdEqualTo(Integer value) {
            addCriterion("org_acct_id =", value, "orgAcctId");
            return (Criteria) this;
        }

        public Criteria andOrgAcctIdNotEqualTo(Integer value) {
            addCriterion("org_acct_id <>", value, "orgAcctId");
            return (Criteria) this;
        }

        public Criteria andOrgAcctIdGreaterThan(Integer value) {
            addCriterion("org_acct_id >", value, "orgAcctId");
            return (Criteria) this;
        }

        public Criteria andOrgAcctIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("org_acct_id >=", value, "orgAcctId");
            return (Criteria) this;
        }

        public Criteria andOrgAcctIdLessThan(Integer value) {
            addCriterion("org_acct_id <", value, "orgAcctId");
            return (Criteria) this;
        }

        public Criteria andOrgAcctIdLessThanOrEqualTo(Integer value) {
            addCriterion("org_acct_id <=", value, "orgAcctId");
            return (Criteria) this;
        }

        public Criteria andOrgAcctIdIn(List<Integer> values) {
            addCriterion("org_acct_id in", values, "orgAcctId");
            return (Criteria) this;
        }

        public Criteria andOrgAcctIdNotIn(List<Integer> values) {
            addCriterion("org_acct_id not in", values, "orgAcctId");
            return (Criteria) this;
        }

        public Criteria andOrgAcctIdBetween(Integer value1, Integer value2) {
            addCriterion("org_acct_id between", value1, value2, "orgAcctId");
            return (Criteria) this;
        }

        public Criteria andOrgAcctIdNotBetween(Integer value1, Integer value2) {
            addCriterion("org_acct_id not between", value1, value2, "orgAcctId");
            return (Criteria) this;
        }

        public Criteria andOrgDeptIdIsNull() {
            addCriterion("org_dept_id is null");
            return (Criteria) this;
        }

        public Criteria andOrgDeptIdIsNotNull() {
            addCriterion("org_dept_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrgDeptIdEqualTo(Integer value) {
            addCriterion("org_dept_id =", value, "orgDeptId");
            return (Criteria) this;
        }

        public Criteria andOrgDeptIdNotEqualTo(Integer value) {
            addCriterion("org_dept_id <>", value, "orgDeptId");
            return (Criteria) this;
        }

        public Criteria andOrgDeptIdGreaterThan(Integer value) {
            addCriterion("org_dept_id >", value, "orgDeptId");
            return (Criteria) this;
        }

        public Criteria andOrgDeptIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("org_dept_id >=", value, "orgDeptId");
            return (Criteria) this;
        }

        public Criteria andOrgDeptIdLessThan(Integer value) {
            addCriterion("org_dept_id <", value, "orgDeptId");
            return (Criteria) this;
        }

        public Criteria andOrgDeptIdLessThanOrEqualTo(Integer value) {
            addCriterion("org_dept_id <=", value, "orgDeptId");
            return (Criteria) this;
        }

        public Criteria andOrgDeptIdIn(List<Integer> values) {
            addCriterion("org_dept_id in", values, "orgDeptId");
            return (Criteria) this;
        }

        public Criteria andOrgDeptIdNotIn(List<Integer> values) {
            addCriterion("org_dept_id not in", values, "orgDeptId");
            return (Criteria) this;
        }

        public Criteria andOrgDeptIdBetween(Integer value1, Integer value2) {
            addCriterion("org_dept_id between", value1, value2, "orgDeptId");
            return (Criteria) this;
        }

        public Criteria andOrgDeptIdNotBetween(Integer value1, Integer value2) {
            addCriterion("org_dept_id not between", value1, value2, "orgDeptId");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andCellNoIsNull() {
            addCriterion("cell_no is null");
            return (Criteria) this;
        }

        public Criteria andCellNoIsNotNull() {
            addCriterion("cell_no is not null");
            return (Criteria) this;
        }

        public Criteria andCellNoEqualTo(String value) {
            addCriterion("cell_no =", value, "cellNo");
            return (Criteria) this;
        }

        public Criteria andCellNoNotEqualTo(String value) {
            addCriterion("cell_no <>", value, "cellNo");
            return (Criteria) this;
        }

        public Criteria andCellNoGreaterThan(String value) {
            addCriterion("cell_no >", value, "cellNo");
            return (Criteria) this;
        }

        public Criteria andCellNoGreaterThanOrEqualTo(String value) {
            addCriterion("cell_no >=", value, "cellNo");
            return (Criteria) this;
        }

        public Criteria andCellNoLessThan(String value) {
            addCriterion("cell_no <", value, "cellNo");
            return (Criteria) this;
        }

        public Criteria andCellNoLessThanOrEqualTo(String value) {
            addCriterion("cell_no <=", value, "cellNo");
            return (Criteria) this;
        }

        public Criteria andCellNoLike(String value) {
            addCriterion("cell_no like", value, "cellNo");
            return (Criteria) this;
        }

        public Criteria andCellNoNotLike(String value) {
            addCriterion("cell_no not like", value, "cellNo");
            return (Criteria) this;
        }

        public Criteria andCellNoIn(List<String> values) {
            addCriterion("cell_no in", values, "cellNo");
            return (Criteria) this;
        }

        public Criteria andCellNoNotIn(List<String> values) {
            addCriterion("cell_no not in", values, "cellNo");
            return (Criteria) this;
        }

        public Criteria andCellNoBetween(String value1, String value2) {
            addCriterion("cell_no between", value1, value2, "cellNo");
            return (Criteria) this;
        }

        public Criteria andCellNoNotBetween(String value1, String value2) {
            addCriterion("cell_no not between", value1, value2, "cellNo");
            return (Criteria) this;
        }

        public Criteria andEmailIsNull() {
            addCriterion("email is null");
            return (Criteria) this;
        }

        public Criteria andEmailIsNotNull() {
            addCriterion("email is not null");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("email =", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotEqualTo(String value) {
            addCriterion("email <>", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThan(String value) {
            addCriterion("email >", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThanOrEqualTo(String value) {
            addCriterion("email >=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThan(String value) {
            addCriterion("email <", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThanOrEqualTo(String value) {
            addCriterion("email <=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("email like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotLike(String value) {
            addCriterion("email not like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailIn(List<String> values) {
            addCriterion("email in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotIn(List<String> values) {
            addCriterion("email not in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailBetween(String value1, String value2) {
            addCriterion("email between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotBetween(String value1, String value2) {
            addCriterion("email not between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNull() {
            addCriterion("password is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("password is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("password =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("password <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("password >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("password >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("password <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("password <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("password like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("password not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("password in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("password not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("password between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("password not between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andErpUserCodeIsNull() {
            addCriterion("erp_user_code is null");
            return (Criteria) this;
        }

        public Criteria andErpUserCodeIsNotNull() {
            addCriterion("erp_user_code is not null");
            return (Criteria) this;
        }

        public Criteria andErpUserCodeEqualTo(String value) {
            addCriterion("erp_user_code =", value, "erpUserCode");
            return (Criteria) this;
        }

        public Criteria andErpUserCodeNotEqualTo(String value) {
            addCriterion("erp_user_code <>", value, "erpUserCode");
            return (Criteria) this;
        }

        public Criteria andErpUserCodeGreaterThan(String value) {
            addCriterion("erp_user_code >", value, "erpUserCode");
            return (Criteria) this;
        }

        public Criteria andErpUserCodeGreaterThanOrEqualTo(String value) {
            addCriterion("erp_user_code >=", value, "erpUserCode");
            return (Criteria) this;
        }

        public Criteria andErpUserCodeLessThan(String value) {
            addCriterion("erp_user_code <", value, "erpUserCode");
            return (Criteria) this;
        }

        public Criteria andErpUserCodeLessThanOrEqualTo(String value) {
            addCriterion("erp_user_code <=", value, "erpUserCode");
            return (Criteria) this;
        }

        public Criteria andErpUserCodeLike(String value) {
            addCriterion("erp_user_code like", value, "erpUserCode");
            return (Criteria) this;
        }

        public Criteria andErpUserCodeNotLike(String value) {
            addCriterion("erp_user_code not like", value, "erpUserCode");
            return (Criteria) this;
        }

        public Criteria andErpUserCodeIn(List<String> values) {
            addCriterion("erp_user_code in", values, "erpUserCode");
            return (Criteria) this;
        }

        public Criteria andErpUserCodeNotIn(List<String> values) {
            addCriterion("erp_user_code not in", values, "erpUserCode");
            return (Criteria) this;
        }

        public Criteria andErpUserCodeBetween(String value1, String value2) {
            addCriterion("erp_user_code between", value1, value2, "erpUserCode");
            return (Criteria) this;
        }

        public Criteria andErpUserCodeNotBetween(String value1, String value2) {
            addCriterion("erp_user_code not between", value1, value2, "erpUserCode");
            return (Criteria) this;
        }

        public Criteria andConnectedWithErpIsNull() {
            addCriterion("connected_with_erp is null");
            return (Criteria) this;
        }

        public Criteria andConnectedWithErpIsNotNull() {
            addCriterion("connected_with_erp is not null");
            return (Criteria) this;
        }

        public Criteria andConnectedWithErpEqualTo(String value) {
            addCriterion("connected_with_erp =", value, "connectedWithErp");
            return (Criteria) this;
        }

        public Criteria andConnectedWithErpNotEqualTo(String value) {
            addCriterion("connected_with_erp <>", value, "connectedWithErp");
            return (Criteria) this;
        }

        public Criteria andConnectedWithErpGreaterThan(String value) {
            addCriterion("connected_with_erp >", value, "connectedWithErp");
            return (Criteria) this;
        }

        public Criteria andConnectedWithErpGreaterThanOrEqualTo(String value) {
            addCriterion("connected_with_erp >=", value, "connectedWithErp");
            return (Criteria) this;
        }

        public Criteria andConnectedWithErpLessThan(String value) {
            addCriterion("connected_with_erp <", value, "connectedWithErp");
            return (Criteria) this;
        }

        public Criteria andConnectedWithErpLessThanOrEqualTo(String value) {
            addCriterion("connected_with_erp <=", value, "connectedWithErp");
            return (Criteria) this;
        }

        public Criteria andConnectedWithErpLike(String value) {
            addCriterion("connected_with_erp like", value, "connectedWithErp");
            return (Criteria) this;
        }

        public Criteria andConnectedWithErpNotLike(String value) {
            addCriterion("connected_with_erp not like", value, "connectedWithErp");
            return (Criteria) this;
        }

        public Criteria andConnectedWithErpIn(List<String> values) {
            addCriterion("connected_with_erp in", values, "connectedWithErp");
            return (Criteria) this;
        }

        public Criteria andConnectedWithErpNotIn(List<String> values) {
            addCriterion("connected_with_erp not in", values, "connectedWithErp");
            return (Criteria) this;
        }

        public Criteria andConnectedWithErpBetween(String value1, String value2) {
            addCriterion("connected_with_erp between", value1, value2, "connectedWithErp");
            return (Criteria) this;
        }

        public Criteria andConnectedWithErpNotBetween(String value1, String value2) {
            addCriterion("connected_with_erp not between", value1, value2, "connectedWithErp");
            return (Criteria) this;
        }

        public Criteria andLastPwdChangedDatetimeIsNull() {
            addCriterion("last_pwd_changed_datetime is null");
            return (Criteria) this;
        }

        public Criteria andLastPwdChangedDatetimeIsNotNull() {
            addCriterion("last_pwd_changed_datetime is not null");
            return (Criteria) this;
        }

        public Criteria andLastPwdChangedDatetimeEqualTo(Date value) {
            addCriterion("last_pwd_changed_datetime =", value, "lastPwdChangedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastPwdChangedDatetimeNotEqualTo(Date value) {
            addCriterion("last_pwd_changed_datetime <>", value, "lastPwdChangedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastPwdChangedDatetimeGreaterThan(Date value) {
            addCriterion("last_pwd_changed_datetime >", value, "lastPwdChangedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastPwdChangedDatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("last_pwd_changed_datetime >=", value, "lastPwdChangedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastPwdChangedDatetimeLessThan(Date value) {
            addCriterion("last_pwd_changed_datetime <", value, "lastPwdChangedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastPwdChangedDatetimeLessThanOrEqualTo(Date value) {
            addCriterion("last_pwd_changed_datetime <=", value, "lastPwdChangedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastPwdChangedDatetimeIn(List<Date> values) {
            addCriterion("last_pwd_changed_datetime in", values, "lastPwdChangedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastPwdChangedDatetimeNotIn(List<Date> values) {
            addCriterion("last_pwd_changed_datetime not in", values, "lastPwdChangedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastPwdChangedDatetimeBetween(Date value1, Date value2) {
            addCriterion("last_pwd_changed_datetime between", value1, value2, "lastPwdChangedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastPwdChangedDatetimeNotBetween(Date value1, Date value2) {
            addCriterion("last_pwd_changed_datetime not between", value1, value2, "lastPwdChangedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastLoginDatetimeIsNull() {
            addCriterion("last_login_datetime is null");
            return (Criteria) this;
        }

        public Criteria andLastLoginDatetimeIsNotNull() {
            addCriterion("last_login_datetime is not null");
            return (Criteria) this;
        }

        public Criteria andLastLoginDatetimeEqualTo(Date value) {
            addCriterion("last_login_datetime =", value, "lastLoginDatetime");
            return (Criteria) this;
        }

        public Criteria andLastLoginDatetimeNotEqualTo(Date value) {
            addCriterion("last_login_datetime <>", value, "lastLoginDatetime");
            return (Criteria) this;
        }

        public Criteria andLastLoginDatetimeGreaterThan(Date value) {
            addCriterion("last_login_datetime >", value, "lastLoginDatetime");
            return (Criteria) this;
        }

        public Criteria andLastLoginDatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("last_login_datetime >=", value, "lastLoginDatetime");
            return (Criteria) this;
        }

        public Criteria andLastLoginDatetimeLessThan(Date value) {
            addCriterion("last_login_datetime <", value, "lastLoginDatetime");
            return (Criteria) this;
        }

        public Criteria andLastLoginDatetimeLessThanOrEqualTo(Date value) {
            addCriterion("last_login_datetime <=", value, "lastLoginDatetime");
            return (Criteria) this;
        }

        public Criteria andLastLoginDatetimeIn(List<Date> values) {
            addCriterion("last_login_datetime in", values, "lastLoginDatetime");
            return (Criteria) this;
        }

        public Criteria andLastLoginDatetimeNotIn(List<Date> values) {
            addCriterion("last_login_datetime not in", values, "lastLoginDatetime");
            return (Criteria) this;
        }

        public Criteria andLastLoginDatetimeBetween(Date value1, Date value2) {
            addCriterion("last_login_datetime between", value1, value2, "lastLoginDatetime");
            return (Criteria) this;
        }

        public Criteria andLastLoginDatetimeNotBetween(Date value1, Date value2) {
            addCriterion("last_login_datetime not between", value1, value2, "lastLoginDatetime");
            return (Criteria) this;
        }

        public Criteria andLastFailedDatetimeIsNull() {
            addCriterion("last_failed_datetime is null");
            return (Criteria) this;
        }

        public Criteria andLastFailedDatetimeIsNotNull() {
            addCriterion("last_failed_datetime is not null");
            return (Criteria) this;
        }

        public Criteria andLastFailedDatetimeEqualTo(Date value) {
            addCriterion("last_failed_datetime =", value, "lastFailedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastFailedDatetimeNotEqualTo(Date value) {
            addCriterion("last_failed_datetime <>", value, "lastFailedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastFailedDatetimeGreaterThan(Date value) {
            addCriterion("last_failed_datetime >", value, "lastFailedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastFailedDatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("last_failed_datetime >=", value, "lastFailedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastFailedDatetimeLessThan(Date value) {
            addCriterion("last_failed_datetime <", value, "lastFailedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastFailedDatetimeLessThanOrEqualTo(Date value) {
            addCriterion("last_failed_datetime <=", value, "lastFailedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastFailedDatetimeIn(List<Date> values) {
            addCriterion("last_failed_datetime in", values, "lastFailedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastFailedDatetimeNotIn(List<Date> values) {
            addCriterion("last_failed_datetime not in", values, "lastFailedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastFailedDatetimeBetween(Date value1, Date value2) {
            addCriterion("last_failed_datetime between", value1, value2, "lastFailedDatetime");
            return (Criteria) this;
        }

        public Criteria andLastFailedDatetimeNotBetween(Date value1, Date value2) {
            addCriterion("last_failed_datetime not between", value1, value2, "lastFailedDatetime");
            return (Criteria) this;
        }

        public Criteria andLoginFailedTimesIsNull() {
            addCriterion("login_failed_times is null");
            return (Criteria) this;
        }

        public Criteria andLoginFailedTimesIsNotNull() {
            addCriterion("login_failed_times is not null");
            return (Criteria) this;
        }

        public Criteria andLoginFailedTimesEqualTo(Integer value) {
            addCriterion("login_failed_times =", value, "loginFailedTimes");
            return (Criteria) this;
        }

        public Criteria andLoginFailedTimesNotEqualTo(Integer value) {
            addCriterion("login_failed_times <>", value, "loginFailedTimes");
            return (Criteria) this;
        }

        public Criteria andLoginFailedTimesGreaterThan(Integer value) {
            addCriterion("login_failed_times >", value, "loginFailedTimes");
            return (Criteria) this;
        }

        public Criteria andLoginFailedTimesGreaterThanOrEqualTo(Integer value) {
            addCriterion("login_failed_times >=", value, "loginFailedTimes");
            return (Criteria) this;
        }

        public Criteria andLoginFailedTimesLessThan(Integer value) {
            addCriterion("login_failed_times <", value, "loginFailedTimes");
            return (Criteria) this;
        }

        public Criteria andLoginFailedTimesLessThanOrEqualTo(Integer value) {
            addCriterion("login_failed_times <=", value, "loginFailedTimes");
            return (Criteria) this;
        }

        public Criteria andLoginFailedTimesIn(List<Integer> values) {
            addCriterion("login_failed_times in", values, "loginFailedTimes");
            return (Criteria) this;
        }

        public Criteria andLoginFailedTimesNotIn(List<Integer> values) {
            addCriterion("login_failed_times not in", values, "loginFailedTimes");
            return (Criteria) this;
        }

        public Criteria andLoginFailedTimesBetween(Integer value1, Integer value2) {
            addCriterion("login_failed_times between", value1, value2, "loginFailedTimes");
            return (Criteria) this;
        }

        public Criteria andLoginFailedTimesNotBetween(Integer value1, Integer value2) {
            addCriterion("login_failed_times not between", value1, value2, "loginFailedTimes");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andEntryIdIsNull() {
            addCriterion("entry_id is null");
            return (Criteria) this;
        }

        public Criteria andEntryIdIsNotNull() {
            addCriterion("entry_id is not null");
            return (Criteria) this;
        }

        public Criteria andEntryIdEqualTo(Integer value) {
            addCriterion("entry_id =", value, "entryId");
            return (Criteria) this;
        }

        public Criteria andEntryIdNotEqualTo(Integer value) {
            addCriterion("entry_id <>", value, "entryId");
            return (Criteria) this;
        }

        public Criteria andEntryIdGreaterThan(Integer value) {
            addCriterion("entry_id >", value, "entryId");
            return (Criteria) this;
        }

        public Criteria andEntryIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("entry_id >=", value, "entryId");
            return (Criteria) this;
        }

        public Criteria andEntryIdLessThan(Integer value) {
            addCriterion("entry_id <", value, "entryId");
            return (Criteria) this;
        }

        public Criteria andEntryIdLessThanOrEqualTo(Integer value) {
            addCriterion("entry_id <=", value, "entryId");
            return (Criteria) this;
        }

        public Criteria andEntryIdIn(List<Integer> values) {
            addCriterion("entry_id in", values, "entryId");
            return (Criteria) this;
        }

        public Criteria andEntryIdNotIn(List<Integer> values) {
            addCriterion("entry_id not in", values, "entryId");
            return (Criteria) this;
        }

        public Criteria andEntryIdBetween(Integer value1, Integer value2) {
            addCriterion("entry_id between", value1, value2, "entryId");
            return (Criteria) this;
        }

        public Criteria andEntryIdNotBetween(Integer value1, Integer value2) {
            addCriterion("entry_id not between", value1, value2, "entryId");
            return (Criteria) this;
        }

        public Criteria andEntryDatetimeIsNull() {
            addCriterion("entry_datetime is null");
            return (Criteria) this;
        }

        public Criteria andEntryDatetimeIsNotNull() {
            addCriterion("entry_datetime is not null");
            return (Criteria) this;
        }

        public Criteria andEntryDatetimeEqualTo(Date value) {
            addCriterion("entry_datetime =", value, "entryDatetime");
            return (Criteria) this;
        }

        public Criteria andEntryDatetimeNotEqualTo(Date value) {
            addCriterion("entry_datetime <>", value, "entryDatetime");
            return (Criteria) this;
        }

        public Criteria andEntryDatetimeGreaterThan(Date value) {
            addCriterion("entry_datetime >", value, "entryDatetime");
            return (Criteria) this;
        }

        public Criteria andEntryDatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("entry_datetime >=", value, "entryDatetime");
            return (Criteria) this;
        }

        public Criteria andEntryDatetimeLessThan(Date value) {
            addCriterion("entry_datetime <", value, "entryDatetime");
            return (Criteria) this;
        }

        public Criteria andEntryDatetimeLessThanOrEqualTo(Date value) {
            addCriterion("entry_datetime <=", value, "entryDatetime");
            return (Criteria) this;
        }

        public Criteria andEntryDatetimeIn(List<Date> values) {
            addCriterion("entry_datetime in", values, "entryDatetime");
            return (Criteria) this;
        }

        public Criteria andEntryDatetimeNotIn(List<Date> values) {
            addCriterion("entry_datetime not in", values, "entryDatetime");
            return (Criteria) this;
        }

        public Criteria andEntryDatetimeBetween(Date value1, Date value2) {
            addCriterion("entry_datetime between", value1, value2, "entryDatetime");
            return (Criteria) this;
        }

        public Criteria andEntryDatetimeNotBetween(Date value1, Date value2) {
            addCriterion("entry_datetime not between", value1, value2, "entryDatetime");
            return (Criteria) this;
        }

        public Criteria andUpdateIdIsNull() {
            addCriterion("update_id is null");
            return (Criteria) this;
        }

        public Criteria andUpdateIdIsNotNull() {
            addCriterion("update_id is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateIdEqualTo(Integer value) {
            addCriterion("update_id =", value, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdNotEqualTo(Integer value) {
            addCriterion("update_id <>", value, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdGreaterThan(Integer value) {
            addCriterion("update_id >", value, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("update_id >=", value, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdLessThan(Integer value) {
            addCriterion("update_id <", value, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdLessThanOrEqualTo(Integer value) {
            addCriterion("update_id <=", value, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdIn(List<Integer> values) {
            addCriterion("update_id in", values, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdNotIn(List<Integer> values) {
            addCriterion("update_id not in", values, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdBetween(Integer value1, Integer value2) {
            addCriterion("update_id between", value1, value2, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateIdNotBetween(Integer value1, Integer value2) {
            addCriterion("update_id not between", value1, value2, "updateId");
            return (Criteria) this;
        }

        public Criteria andUpdateDatetimeIsNull() {
            addCriterion("update_datetime is null");
            return (Criteria) this;
        }

        public Criteria andUpdateDatetimeIsNotNull() {
            addCriterion("update_datetime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateDatetimeEqualTo(Date value) {
            addCriterion("update_datetime =", value, "updateDatetime");
            return (Criteria) this;
        }

        public Criteria andUpdateDatetimeNotEqualTo(Date value) {
            addCriterion("update_datetime <>", value, "updateDatetime");
            return (Criteria) this;
        }

        public Criteria andUpdateDatetimeGreaterThan(Date value) {
            addCriterion("update_datetime >", value, "updateDatetime");
            return (Criteria) this;
        }

        public Criteria andUpdateDatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_datetime >=", value, "updateDatetime");
            return (Criteria) this;
        }

        public Criteria andUpdateDatetimeLessThan(Date value) {
            addCriterion("update_datetime <", value, "updateDatetime");
            return (Criteria) this;
        }

        public Criteria andUpdateDatetimeLessThanOrEqualTo(Date value) {
            addCriterion("update_datetime <=", value, "updateDatetime");
            return (Criteria) this;
        }

        public Criteria andUpdateDatetimeIn(List<Date> values) {
            addCriterion("update_datetime in", values, "updateDatetime");
            return (Criteria) this;
        }

        public Criteria andUpdateDatetimeNotIn(List<Date> values) {
            addCriterion("update_datetime not in", values, "updateDatetime");
            return (Criteria) this;
        }

        public Criteria andUpdateDatetimeBetween(Date value1, Date value2) {
            addCriterion("update_datetime between", value1, value2, "updateDatetime");
            return (Criteria) this;
        }

        public Criteria andUpdateDatetimeNotBetween(Date value1, Date value2) {
            addCriterion("update_datetime not between", value1, value2, "updateDatetime");
            return (Criteria) this;
        }

        public Criteria andDeleteIdIsNull() {
            addCriterion("delete_id is null");
            return (Criteria) this;
        }

        public Criteria andDeleteIdIsNotNull() {
            addCriterion("delete_id is not null");
            return (Criteria) this;
        }

        public Criteria andDeleteIdEqualTo(Integer value) {
            addCriterion("delete_id =", value, "deleteId");
            return (Criteria) this;
        }

        public Criteria andDeleteIdNotEqualTo(Integer value) {
            addCriterion("delete_id <>", value, "deleteId");
            return (Criteria) this;
        }

        public Criteria andDeleteIdGreaterThan(Integer value) {
            addCriterion("delete_id >", value, "deleteId");
            return (Criteria) this;
        }

        public Criteria andDeleteIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("delete_id >=", value, "deleteId");
            return (Criteria) this;
        }

        public Criteria andDeleteIdLessThan(Integer value) {
            addCriterion("delete_id <", value, "deleteId");
            return (Criteria) this;
        }

        public Criteria andDeleteIdLessThanOrEqualTo(Integer value) {
            addCriterion("delete_id <=", value, "deleteId");
            return (Criteria) this;
        }

        public Criteria andDeleteIdIn(List<Integer> values) {
            addCriterion("delete_id in", values, "deleteId");
            return (Criteria) this;
        }

        public Criteria andDeleteIdNotIn(List<Integer> values) {
            addCriterion("delete_id not in", values, "deleteId");
            return (Criteria) this;
        }

        public Criteria andDeleteIdBetween(Integer value1, Integer value2) {
            addCriterion("delete_id between", value1, value2, "deleteId");
            return (Criteria) this;
        }

        public Criteria andDeleteIdNotBetween(Integer value1, Integer value2) {
            addCriterion("delete_id not between", value1, value2, "deleteId");
            return (Criteria) this;
        }

        public Criteria andDeleteDatetimeIsNull() {
            addCriterion("delete_datetime is null");
            return (Criteria) this;
        }

        public Criteria andDeleteDatetimeIsNotNull() {
            addCriterion("delete_datetime is not null");
            return (Criteria) this;
        }

        public Criteria andDeleteDatetimeEqualTo(Date value) {
            addCriterion("delete_datetime =", value, "deleteDatetime");
            return (Criteria) this;
        }

        public Criteria andDeleteDatetimeNotEqualTo(Date value) {
            addCriterion("delete_datetime <>", value, "deleteDatetime");
            return (Criteria) this;
        }

        public Criteria andDeleteDatetimeGreaterThan(Date value) {
            addCriterion("delete_datetime >", value, "deleteDatetime");
            return (Criteria) this;
        }

        public Criteria andDeleteDatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("delete_datetime >=", value, "deleteDatetime");
            return (Criteria) this;
        }

        public Criteria andDeleteDatetimeLessThan(Date value) {
            addCriterion("delete_datetime <", value, "deleteDatetime");
            return (Criteria) this;
        }

        public Criteria andDeleteDatetimeLessThanOrEqualTo(Date value) {
            addCriterion("delete_datetime <=", value, "deleteDatetime");
            return (Criteria) this;
        }

        public Criteria andDeleteDatetimeIn(List<Date> values) {
            addCriterion("delete_datetime in", values, "deleteDatetime");
            return (Criteria) this;
        }

        public Criteria andDeleteDatetimeNotIn(List<Date> values) {
            addCriterion("delete_datetime not in", values, "deleteDatetime");
            return (Criteria) this;
        }

        public Criteria andDeleteDatetimeBetween(Date value1, Date value2) {
            addCriterion("delete_datetime between", value1, value2, "deleteDatetime");
            return (Criteria) this;
        }

        public Criteria andDeleteDatetimeNotBetween(Date value1, Date value2) {
            addCriterion("delete_datetime not between", value1, value2, "deleteDatetime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}