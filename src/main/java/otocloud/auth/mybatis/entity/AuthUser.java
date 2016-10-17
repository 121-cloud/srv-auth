package otocloud.auth.mybatis.entity;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class AuthUser {
    @Expose
    private Integer id;

    @Expose
    private Integer orgAcctId;

    @Expose
    private Integer orgDeptId;

    @Expose
    private String name;

    @Expose
    private String cellNo;

    @Expose
    private String email;

    private String password;

    @Expose
    private String erpUserCode;

    @Expose
    private String connectedWithErp;

    private Date lastPwdChangedDatetime;

    private Date lastLoginDatetime;

    private Date lastFailedDatetime;

    private Integer loginFailedTimes;

    private String status;

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

    public Integer getOrgDeptId() {
        return orgDeptId;
    }

    public void setOrgDeptId(Integer orgDeptId) {
        this.orgDeptId = orgDeptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCellNo() {
        return cellNo;
    }

    public void setCellNo(String cellNo) {
        this.cellNo = cellNo == null ? null : cellNo.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getErpUserCode() {
        return erpUserCode;
    }

    public void setErpUserCode(String erpUserCode) {
        this.erpUserCode = erpUserCode == null ? null : erpUserCode.trim();
    }

    public String getConnectedWithErp() {
        return connectedWithErp;
    }

    public void setConnectedWithErp(String connectedWithErp) {
        this.connectedWithErp = connectedWithErp == null ? null : connectedWithErp.trim();
    }

    public Date getLastPwdChangedDatetime() {
        return lastPwdChangedDatetime;
    }

    public void setLastPwdChangedDatetime(Date lastPwdChangedDatetime) {
        this.lastPwdChangedDatetime = lastPwdChangedDatetime;
    }

    public Date getLastLoginDatetime() {
        return lastLoginDatetime;
    }

    public void setLastLoginDatetime(Date lastLoginDatetime) {
        this.lastLoginDatetime = lastLoginDatetime;
    }

    public Date getLastFailedDatetime() {
        return lastFailedDatetime;
    }

    public void setLastFailedDatetime(Date lastFailedDatetime) {
        this.lastFailedDatetime = lastFailedDatetime;
    }

    public Integer getLoginFailedTimes() {
        return loginFailedTimes;
    }

    public void setLoginFailedTimes(Integer loginFailedTimes) {
        this.loginFailedTimes = loginFailedTimes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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