package com.piesat.busiclogic.busic.notice.entity;

import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class NoticeEntity {

    @MapperColumn("ID")
    private String id;

    @MapperColumn("TITLE")
    private String title;

    @MapperColumn("TYPE")
    private String type;

    @MapperColumn("LINKURL")
    private String linkurl;

    @MapperColumn("CONTENT")
    private String content;

    @MapperColumn("ISPUBLISH")
    private String ispublish;

    @MapperColumn("ISFIRSTSHOW")
    private String isfirstshow;

    @MapperColumn("INVALID")
    private String invalid;

    @MapperColumn("PUBLISHDATE")
    private String publishdate;

    @MapperColumn("PUBLISHER")
    private String publisher;

    @MapperColumn("CREATED")
    private String created;

    @MapperColumn("ISTOP")
    private String istop;

    @MapperColumn("attachEntity")
    private String  attachEntity ;

    @MapperColumn("attachEntityList")
    private List<Map<String,Object>> attachEntityList = new ArrayList<>();
}
