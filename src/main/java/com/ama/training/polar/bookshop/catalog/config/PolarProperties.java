package com.ama.training.polar.bookshop.catalog.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "polar")
@Getter @Setter
public class PolarProperties {


    /**
     * The greetings presented to the application users
     */
    private String greeting;
}
