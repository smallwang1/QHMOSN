package com.piesat.site.identifier.service.config;

import com.oid.active.util.LoadUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OidActiveStartConfig {

    @PostConstruct
    public void init(){
        new LoadUtil().load();
    }
}
