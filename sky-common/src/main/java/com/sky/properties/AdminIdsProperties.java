package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "sky.admin")
@Data
public class AdminIdsProperties {
    private List<Long> ids;

    public boolean isAdmin(Long id){
        return ids.contains(id);
    }
}
