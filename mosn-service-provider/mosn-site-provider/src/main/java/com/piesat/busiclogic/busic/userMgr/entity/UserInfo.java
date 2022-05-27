package com.piesat.busiclogic.busic.userMgr.entity;

import com.piesat.busiclogic.busic.roleMgr.entity.RoleMgr;
import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class UserInfo {

    @MapperColumn("ID")
    private String id;

    @MapperColumn("USERNAME")
    private String userName;

    @MapperColumn("USER_TYPE")
    private String user_type;

    @MapperColumn("PASSWORD")
    private String passWord;

    @MapperColumn("SALT")
    private String salt;

    @MapperColumn("AUTH_STATUS")
    private String auth_status;

    @MapperColumn("IS_LOCK")
    private String is_lock;

    @MapperColumn("NICKNAME")
    private String nickName;

    @MapperColumn("IS_ONLINE")
    private String is_online;

    @MapperColumn("CREATE_DATE")
    private String create_date;

    @MapperColumn("CREATE_BY_USERID")
    private String create_by_userid;

    @MapperColumn("CREATE_BY_USERNAME")
    private String create_by_username;

    @MapperColumn("UPDATE_DATE")
    private String update_date;

    @MapperColumn("UPDATE_BY_USERID")
    private String update_by_userid;

    @MapperColumn("UPDATE_BY_USERNAME")
    private String update_by_username;

    @MapperColumn("VERSION")
    private String version;

    @MapperColumn("IN_NUMBER_ID")
    private String in_number_id;

    @MapperColumn("ENABLE")
    private String enable;

    @MapperColumn("ORG_ID")
    private String org_id;

    @MapperColumn("AUTH_TYPE")
    private String auth_type;

    @MapperColumn("EXTENDED1")
    private String extended1;

    @MapperColumn("ORGAN_NAME")
    private String organ_name;

    @MapperColumn("NAME")
    private String name;

    @MapperColumn("POLITICAL_FEATURE")
    private String political_feature;

    @MapperColumn("IDCARD")
    private String idcard;

    @MapperColumn("SEX")
    private String sex;

    @MapperColumn("PHONE")
    private String phone;

    @MapperColumn("EMAIL")
    private String email;

    @MapperColumn("BIRTHDAY")
    private String birthday;

    @MapperColumn("NATION")
    private String nation;

    @MapperColumn("DESCRIPTION")
    private String description;

    @MapperColumn("ROLEMGRLIST")
//    private List<User2Role> roleMgrList = new ArrayList<>();
    private String  roleMgrList ;
    @MapperColumn("ROLEMGRBACK")
    private List<User2Role> roleMgrBack = new ArrayList<>();

}
