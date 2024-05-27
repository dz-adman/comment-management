package com.adman.craft.comments_mgmt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "security")
public class SecurityConfigProperties {

    private List<String> whitelist = new ArrayList<>();

}
