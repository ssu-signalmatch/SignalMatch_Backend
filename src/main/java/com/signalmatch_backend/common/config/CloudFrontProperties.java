package com.signalmatch_backend.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cloudfront")
public class CloudFrontProperties {
    private String domain;

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
}
