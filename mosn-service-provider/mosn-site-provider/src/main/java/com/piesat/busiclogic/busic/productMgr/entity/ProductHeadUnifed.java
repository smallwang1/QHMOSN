package com.piesat.busiclogic.busic.productMgr.entity;

import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

@Data
public class ProductHeadUnifed {

    @MapperColumn("ID")
    private String id;

    @MapperColumn("PRODUCT_ID")
    private String product_id ;

    @MapperColumn("SELECT_NAME")
    private String select_name = "";

    @MapperColumn("SELECT_VALUE")
    private String select_value = "";

    @MapperColumn("IS_SELECT")
    private String is_select = "";

    @MapperColumn("SELECT_ID")
    private String select_id = "";

    @MapperColumn("SORT")
    private String sort = "";

    @MapperColumn("KEY")
    private String key = "";

    @MapperColumn("VALUE")
    private String value = "";

    @MapperColumn("EXPAND1")
    private String expand1 = "";

    @MapperColumn("MULTIPLE_SELECT")
    private String multiple_select = "";

    @MapperColumn("LISTOPIONS")
    private String listOpions = "";
}
