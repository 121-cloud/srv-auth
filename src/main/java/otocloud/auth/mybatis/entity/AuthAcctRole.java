package otocloud.auth.mybatis.entity;

import java.util.Date;

public class AuthAcctRole {
    private Integer id;

    private Integer orgAcctId;

    private Integer authRoleId;

    private String authTypeCode;

    private Integer refId;

    private Integer entryId;

    private Date entryDatetime;

    private Integer updateId;

    private Date updateDatetime;

    private Integer deleteId;

    private Date deleteDatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrgAcctId() {
        return orgAcctId;
    }

    public void setOrgAcctId(Integer orgAcctId) {
        this.orgAcctId = orgAcctId;
    }

    public Integer getAuthRoleId() {
        return authRoleId;
    }

    public void setAuthRoleId(Integer authRoleId) {
        this.authRoleId = authRoleId;
    }

    public String getAuthTypeCode() {
        return authTypeCode;
    }

    public void setAuthTypeCode(String authTypeCode) {
        this.authTypeCode = authTypeCode == null ? null : authTypeCode.trim();
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public Integer getEntryId() {
        return entryId;
    }

    public void setEntryId(Integer entryId) {
        this.entryId = entryId;
    }

    public Date getEntryDatetime() {
        return entryDatetime;
    }

    public void setEntryDatetime(Date entryDatetime) {
        this.entryDatetime = entryDatetime;
    }

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public Integer getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(Integer deleteId) {
        this.deleteId = deleteId;
    }

    public Date getDeleteDatetime() {
        return deleteDatetime;
    }

    public void setDeleteDatetime(Date deleteDatetime) {
        this.deleteDatetime = deleteDatetime;
    }
}