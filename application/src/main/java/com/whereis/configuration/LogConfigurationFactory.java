/*
package com.whereis.configuration;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.Component;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.properties.PropertiesConfiguration;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.net.URI;

@Plugin(name = "LogConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class LogConfigurationFactory extends ConfigurationFactory {

    static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);
        builder.setStatusLevel(Level.ALL);
        builder.add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL).
                addAttribute("level", Level.DEBUG));

        AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout", "CONSOLE").
                addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        appenderBuilder.add(builder.newLayout("PatternLayout").
                addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"));
        appenderBuilder.add(builder.newFilter("MarkerFilter", Filter.Result.DENY,
                Filter.Result.NEUTRAL).addAttribute("marker", "FLOW"));

        builder.add(appenderBuilder);
        builder.add(builder.newLogger("org.apache.logging.log4j", Level.DEBUG).
                add(builder.newAppenderRef("Stdout")).
                addAttribute("additivity", false));
        builder.add(builder.newRootLogger(Level.ERROR).add(builder.newAppenderRef("Stdout")));
        return builder.build();
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] {"*"};
    }

    public class Config extends PropertiesConfiguration {

        public Config(final ConfigurationSource configSource) {
            super(LogManager.getContext(false), configSource, );
        }

        @Override
        protected void doConfigure() {
            super.doConfigure();
            final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            final Layout layout = PatternLayout.createLayout(PatternLayout.SIMPLE_CONVERSION_PATTERN, config, null,
                    null,null, null);
            final Appender appender = FileAppender.createAppender("target/test.log", "false", "false", "File", "true",
                    "false", "false", "4000", layout, null, "false", null, config);
            appender.start();
            addAppender(appender);
            LoggerConfig loggerConfig = LoggerConfig.createLogger("false", "info", "org.apache.logging.log4j",
                    "true", refs, null, config, null );
            loggerConfig.addAppender(appender, null, null);
            addLogger("org.apache.logging.log4j", loggerConfig);
        }

    }
}
*/
