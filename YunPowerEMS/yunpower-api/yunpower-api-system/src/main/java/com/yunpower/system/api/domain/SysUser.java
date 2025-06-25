package com.yunpower.system.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.annotation.Excel.ColumnType;
import com.yunpower.common.core.annotation.Excel.Type;
import com.yunpower.common.core.annotation.Excels;
import com.yunpower.common.core.web.domain.BaseEntity;
import com.yunpower.common.core.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 *
 * @author wangjunfu
 * @date 2023-09-12
 */
@ApiModel("用户对象")
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号ID
     */
    @ApiModelProperty("编号ID")
    @Excel(name = "用户序号", type = Type.EXPORT, cellType = ColumnType.NUMERIC, prompt = "用户编号")
    private Long id;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    @Excel(name = "企业ID")
    private Long entId;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    @Excel(name = "部门编号", type = Type.IMPORT)
    private Long deptId;

    /**
     * 逻辑代码
     */
    @ApiModelProperty("逻辑代码")
    private String logicCode;

    /**
     * 登录帐号
     */
    @ApiModelProperty("登录帐号")
    @Excel(name = "登录帐号")
    private String logonName;

    /**
     * 登录密码
     */
    @ApiModelProperty("登录密码")
    @Excel(name = "登录密码")
    private String logonPwd;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    @Excel(name = "用户昵称")
    private String nickName;

    /**
     * 用户手机
     */
    @ApiModelProperty("用户手机")
    @Excel(name = "用户手机", cellType = ColumnType.TEXT)
    private String mobile;

    /**
     * 用户邮箱
     */
    @ApiModelProperty("用户邮箱")
    @Excel(name = "用户邮箱")
    private String email;

    /**
     * 用户岗位
     */
    @ApiModelProperty("用户岗位")
    @Excel(name = "用户岗位")
    private Integer postId;

    /**
     * 是否超管
     */
    @ApiModelProperty("是否超管")
    @Excel(name = "是否超管")
    private Integer isSupper;

    /**
     * 用户级别（1注册会员 2银卡会员 3金卡会员 4钻石会员 5Plus会员 10商户）
     */
    @ApiModelProperty("用户级别")
    @Excel(name = "用户级别", readConverterExp = "1=注册会员,2=银卡会员,3=金卡会员,4=钻石会员,5=Plus会员,10=商户")
    private Integer userLevel;

    /**
     * 用户性别（0女 1男 2未知）
     */
    @ApiModelProperty("用户性别（0女 1男 2未知）")
    @Excel(name = "用户性别", readConverterExp = "0=女,1=男,2=未知")
    private Integer sex;

    /**
     * 婚姻状态（0未婚 1已婚 2未知）
     */
    @ApiModelProperty("婚姻状态（0未婚 1已婚 2未知）")
    @Excel(name = "婚姻状态", readConverterExp = "0=未婚,1=已婚,2=未知")
    private Integer marry;

    /**
     * 出生日期
     */
    @ApiModelProperty("出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 用户年龄
     */
    @ApiModelProperty("用户年龄")
    @Excel(name = "用户年龄")
    private Integer age;

    /**
     * 真实姓名
     */
    @ApiModelProperty("真实姓名")
    @Excel(name = "真实姓名")
    private String trueName;

    /**
     * 证件类型
     */
    @ApiModelProperty("证件类型")
    @Excel(name = "证件类型")
    private Integer cardType;

    /**
     * 证件号码
     */
    @ApiModelProperty("证件号码")
    @Excel(name = "证件号码")
    private String cardNumber;

    /**
     * 身份证正
     */
    @ApiModelProperty("身份证正")
    @Excel(name = "身份证正")
    private String cardPicFront;

    /**
     * 身份证反
     */
    @ApiModelProperty("身份证反")
    @Excel(name = "身份证反")
    private String cardPicBack;

    /**
     * 是否认证
     */
    @ApiModelProperty("是否认证")
    private Integer isAuth;

    /**
     * 国家
     */
    @ApiModelProperty("国家")
    @Excel(name = "国家")
    private String country;

    /**
     * 省
     */
    @ApiModelProperty("省")
    private Integer province;

    /**
     * 市
     */
    @ApiModelProperty("市")
    private Integer city;

    /**
     * 县（区）
     */
    @ApiModelProperty("县（区）")
    private Integer county;

    /**
     * 镇（街道）
     */
    @ApiModelProperty("镇（街道）")
    private Integer town;

    /**
     * 村
     */
    @ApiModelProperty("村")
    private Integer village;

    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    @Excel(name = "详细地址")
    private String detailAddress;

    /**
     * 邮政编码
     */
    @ApiModelProperty("邮政编码")
    @Excel(name = "邮政编码")
    private String postCode;

    /**
     * 固定电话
     */
    @ApiModelProperty("固定电话")
    @Excel(name = "固定电话")
    private String phone;

    /**
     * 传真号码
     */
    @ApiModelProperty("传真号码")
    @Excel(name = "传真号码")
    private String fax;

    /**
     * 头像地址
     */
    @ApiModelProperty("头像地址")
    @Excel(name = "头像地址")
    private String headPic;

    /**
     * 个人简介
     */
    @ApiModelProperty("个人简介")
    @Excel(name = "个人简介")
    private String personDes;

    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "注册时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date regTime;

    /**
     * 注册IP
     */
    @ApiModelProperty("注册IP")
    @Excel(name = "注册IP")
    private String regIp;

    /**
     * 最后登录时间
     */
    @ApiModelProperty("最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /**
     * 最后登录IP
     */
    @ApiModelProperty("最后登录IP")
    @Excel(name = "最后登录IP")
    private String lastLoginIp;

    /**
     * 登录次数
     */
    @ApiModelProperty("登录次数")
    private Integer loginTimes = 0;
    /**
     * 密码最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("密码最后更新时间")
    private Date pwdUpdateDate;

    /**
     * 是否停用（0正常 1停用）
     */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /**
     * 是否删除（0正常1删除）
     */
    @ApiModelProperty("是否删除（0正常 1删除）")
    private Integer deleteFlag;

    /** 扩展 */

    /**
     * 部门对象
     */
    @Excels({
            @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
            @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    private SysDept dept;
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色组
     */
    private Long[] roleIds;

    /**
     * 角色对象
     */
    private List<SysRole> roles;

    /**
     * 用于列表页显示
     */
    private String roleNames;

    /**
     * 岗位组
     */
    private Long[] postIds;

    /**
     * 用于列表页显示
     */
    private String postNames;


    @Transient
    private String rawPassword;

    public SysUser() {

    }

    public SysUser(Long userId) {
        this.id = userId;
    }

    public boolean isAdmin() {
        return isAdmin(this.isSupper);
    }

    public static boolean isAdmin(Integer isSupper) {
        return isSupper != null && 1 == isSupper;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public Long getEntId() {
        return entId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setLogicCode(String logicCode) {
        this.logicCode = logicCode;
    }

    public String getLogicCode() {
        return logicCode;
    }

    public void setLogonName(String logonName) {
        this.logonName = logonName;
    }

    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    public String getLogonName() {
        return logonName;
    }

    public void setLogonPwd(String logonPwd) {
        this.logonPwd = logonPwd;
    }

    public String getLogonPwd() {
        return logonPwd;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Xss(message = "用户昵称不能包含脚本字符")
    @NotBlank(message = "用户昵称不能为空")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    public String getNickName() {
        return nickName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @NotBlank(message = "用户手机不能为空")
    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    public String getMobile() {
        return mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 100, message = "邮箱长度不能超过100个字符")
    public String getEmail() {
        return email;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setIsSupper(Integer isSupper) {
        this.isSupper = isSupper;
    }

    public Integer getIsSupper() {
        return isSupper;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getSex() {
        return sex;
    }

    public void setMarry(Integer marry) {
        this.marry = marry;
    }

    public Integer getMarry() {
        return marry;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardPicFront(String cardPicFront) {
        this.cardPicFront = cardPicFront;
    }

    public String getCardPicFront() {
        return cardPicFront;
    }

    public void setCardPicBack(String cardPicBack) {
        this.cardPicBack = cardPicBack;
    }

    public String getCardPicBack() {
        return cardPicBack;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getProvince() {
        return province;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getCity() {
        return city;
    }

    public void setCounty(Integer county) {
        this.county = county;
    }

    public Integer getCounty() {
        return county;
    }

    public void setTown(Integer town) {
        this.town = town;
    }

    public Integer getTown() {
        return town;
    }

    public void setVillage(Integer village) {
        this.village = village;
    }

    public Integer getVillage() {
        return village;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFax() {
        return fax;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setPersonDes(String personDes) {
        this.personDes = personDes;
    }

    public String getPersonDes() {
        return personDes;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegIp(String regIp) {
        this.regIp = regIp;
    }

    public String getRegIp() {
        return regIp;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLoginTimes(Integer loginTimes) {
        this.loginTimes = loginTimes;
    }

    public Integer getLoginTimes() {
        return loginTimes;
    }

    public Date getPwdUpdateDate() {
        return pwdUpdateDate;
    }

    public void setPwdUpdateDate(Date pwdUpdateDate) {
        this.pwdUpdateDate = pwdUpdateDate;
    }

    public void setStopFlag(Integer stopFlag) {
        this.stopFlag = stopFlag;
    }

    public Integer getStopFlag() {
        return stopFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }


    public SysDept getDept() {
        return dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds() {
        return postIds;
    }

    public void setPostIds(Long[] postIds) {
        this.postIds = postIds;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String getPostNames() {
        return postNames;
    }

    public void setPostNames(String postNames) {
        this.postNames = postNames;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("logicCode", getLogicCode())
                .append("logonName", getLogonName())
                .append("logonPwd", getLogonPwd())
                .append("nickName", getNickName())
                .append("mobile", getMobile())
                .append("email", getEmail())
                .append("postId", getPostId())
                .append("isSupper", getIsSupper())
                .append("userLevel", getUserLevel())
                .append("sex", getSex())
                .append("marry", getMarry())
                .append("birthday", getBirthday())
                .append("age", getAge())
                .append("trueName", getTrueName())
                .append("cardType", getCardType())
                .append("cardNumber", getCardNumber())
                .append("cardPicFront", getCardPicFront())
                .append("cardPicBack", getCardPicBack())
                .append("isAuth", getIsAuth())
                .append("country", getCountry())
                .append("province", getProvince())
                .append("city", getCity())
                .append("county", getCounty())
                .append("town", getTown())
                .append("village", getVillage())
                .append("detailAddress", getDetailAddress())
                .append("postCode", getPostCode())
                .append("phone", getPhone())
                .append("fax", getFax())
                .append("headPic", getHeadPic())
                .append("personDes", getPersonDes())
                .append("regTime", getRegTime())
                .append("regIp", getRegIp())
                .append("lastLoginTime", getLastLoginTime())
                .append("lastLoginIp", getLastLoginIp())
                .append("loginTimes", getLoginTimes())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("stopFlag", getStopFlag())
                .append("deleteFlag", getDeleteFlag())
                .toString();
    }
}
