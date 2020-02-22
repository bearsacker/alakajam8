package com.guillot.engine.configs;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;


public abstract class Config {

    protected Configuration configuration;

    public Config(String file) {
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(new Parameters().properties().setFileName(file));

        try {
            configuration = builder.getConfiguration();
        } catch (ConfigurationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
