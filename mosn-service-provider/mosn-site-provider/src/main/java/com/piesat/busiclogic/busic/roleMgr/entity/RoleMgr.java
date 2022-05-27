package com.piesat.busiclogic.busic.roleMgr.entity;


import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoleMgr {

    @MapperColumn("ID")
    private String id;

    @MapperColumn("ROLE_NAME")
    private String role_name;

    @MapperColumn("ROLE_CODE")
    private String role_code;

    @MapperColumn("REMARK")
    private String remark;

    @MapperColumn("CREATE_DATE")
    private String create_date;

    @MapperColumn("UPDATE_DATE")
    private String update_date;

    @MapperColumn("CREATE_BY")
    private String create_by;

    @MapperColumn("UPDATE_BY")
    private String update_by;

    @MapperColumn("TYPE")
    private String type;

    @MapperColumn("STATUS")
    private String status;

    @MapperColumn("MENUROLELIST")
    private String menuRoleList ;

    @MapperColumn("MENUROLEBACK")
    private List<MenuRole> menuRoleBack = new ArrayList<>() ;

    private String productRole;

    private List<ProductRole> productRoleBack = new ArrayList<>();

}
