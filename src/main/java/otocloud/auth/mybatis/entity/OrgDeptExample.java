package otocloud.auth.mybatis.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrgDeptExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OrgDeptExample() {
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

        public Criteria andDeptNameIsNull() {
            addCriterion("dept_name is null");
            return (Criteria) this;
        }

        public Criteria andDeptNameIsNotNull() {
            addCriterion("dept_name is not null");
            return (Criteria) this;
        }

        public Criteria andDeptNameEqualTo(String value) {
            addCriterion("dept_name =", value, "deptName");
            return (Criteria) this;
        }

        public Criteria andDeptNameNotEqualTo(String value) {
            addCriterion("dept_name <>", value, "deptName");
            return (Criteria) this;
        }

        public Criteria andDeptNameGreaterThan(String value) {
            addCriterion("dept_name >", value, "deptName");
            return (Criteria) this;
        }

        public Criteria andDeptNameGreaterThanOrEqualTo(String value) {
            addCriterion("dept_name >=", value, "deptName");
            return (Criteria) this;
        }

        public Criteria andDeptNameLessThan(String value) {
            addCriterion("dept_name <", value, "deptName");
            return (Criteria) this;
        }

        public Criteria andDeptNameLessThanOrEqualTo(String value) {
            addCriterion("dept_name <=", value, "deptName");
            return (Criteria) this;
        }

        public Criteria andDeptNameLike(String value) {
            addCriterion("dept_name like", value, "deptName");
            return (Criteria) this;
        }

        public Criteria andDeptNameNotLike(String value) {
            addCriterion("dept_name not like", value, "deptName");
            return (Criteria) this;
        }

        public Criteria andDeptNameIn(List<String> values) {
            addCriterion("dept_name in", values, "deptName");
            return (Criteria) this;
        }

        public Criteria andDeptNameNotIn(List<String> values) {
            addCriterion("dept_name not in", values, "deptName");
            return (Criteria) this;
        }

        public Criteria andDeptNameBetween(String value1, String value2) {
            addCriterion("dept_name between", value1, value2, "deptName");
            return (Criteria) this;
        }

        public Criteria andDeptNameNotBetween(String value1, String value2) {
            addCriterion("dept_name not between", value1, value2, "deptName");
            return (Criteria) this;
        }

        public Criteria andDeptManagerIsNull() {
            addCriterion("dept_manager is null");
            return (Criteria) this;
        }

        public Criteria andDeptManagerIsNotNull() {
            addCriterion("dept_manager is not null");
            return (Criteria) this;
        }

        public Criteria andDeptManagerEqualTo(String value) {
            addCriterion("dept_manager =", value, "deptManager");
            return (Criteria) this;
        }

        public Criteria andDeptManagerNotEqualTo(String value) {
            addCriterion("dept_manager <>", value, "deptManager");
            return (Criteria) this;
        }

        public Criteria andDeptManagerGreaterThan(String value) {
            addCriterion("dept_manager >", value, "deptManager");
            return (Criteria) this;
        }

        public Criteria andDeptManagerGreaterThanOrEqualTo(String value) {
            addCriterion("dept_manager >=", value, "deptManager");
            return (Criteria) this;
        }

        public Criteria andDeptManagerLessThan(String value) {
            addCriterion("dept_manager <", value, "deptManager");
            return (Criteria) this;
        }

        public Criteria andDeptManagerLessThanOrEqualTo(String value) {
            addCriterion("dept_manager <=", value, "deptManager");
            return (Criteria) this;
        }

        public Criteria andDeptManagerLike(String value) {
            addCriterion("dept_manager like", value, "deptManager");
            return (Criteria) this;
        }

        public Criteria andDeptManagerNotLike(String value) {
            addCriterion("dept_manager not like", value, "deptManager");
            return (Criteria) this;
        }

        public Criteria andDeptManagerIn(List<String> values) {
            addCriterion("dept_manager in", values, "deptManager");
            return (Criteria) this;
        }

        public Criteria andDeptManagerNotIn(List<String> values) {
            addCriterion("dept_manager not in", values, "deptManager");
            return (Criteria) this;
        }

        public Criteria andDeptManagerBetween(String value1, String value2) {
            addCriterion("dept_manager between", value1, value2, "deptManager");
            return (Criteria) this;
        }

        public Criteria andDeptManagerNotBetween(String value1, String value2) {
            addCriterion("dept_manager not between", value1, value2, "deptManager");
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