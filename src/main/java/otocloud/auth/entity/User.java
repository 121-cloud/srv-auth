package otocloud.auth.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.Instant;

/**
 * Created by better/zhangye on 15/9/28.
 */
public class User extends JsonableAdapter {
    @Id
    @Column(name = "id")
    private int ID;

    @JsonKey(name = "openId")
    private String openID;

    @Column(name = "org_acct_id")
    @JsonKey(name = "org_acct_id")
    private int orgAcctId; //企业账户ID

    @Column(name = "org_dept_id")
    @JsonKey(name = "org_dept_id")
    private int orgDeptId; //部门ID

    @Column(name = "name")
    @JsonKey(name = "name")
    private String userName;

    @Column
    @JsonKey
    private String password;

    @Column(name = "cell_no")
    @JsonKey(name = "cell_no")
    private String cellNo;

    @Column
    @JsonKey
    private String email;

    @JsonKey(name = "entry_id")
    @Column(name = "entry_id")
    private int entryId;

    @Column(name = "entry_datetime")
    private Instant entryDatetime;

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public Instant getEntryDatetime() {
        return entryDatetime;
    }

    public void setEntryDatetime(Instant entryDatetime) {
        this.entryDatetime = entryDatetime;
    }

    public int getOrgAcctId() {
        return orgAcctId;
    }

    public void setOrgAcctId(int orgAcctId) {
        this.orgAcctId = orgAcctId;
    }

    public int getOrgDeptId() {
        return orgDeptId;
    }

    public void setOrgDeptId(int orgDeptId) {
        this.orgDeptId = orgDeptId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCellNo() {
        return cellNo;
    }

    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
