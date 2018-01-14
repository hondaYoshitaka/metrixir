package example.metrixir.configuration;

import enkan.Env;
import enkan.collection.OptionMap;
import enkan.component.ApplicationComponent;
import enkan.component.doma2.DomaProvider;
import enkan.component.flyway.FlywayMigration;
import enkan.component.hikaricp.HikariCPComponent;
import enkan.component.jackson.JacksonBeansConverter;
import enkan.component.thymeleaf.ThymeleafTemplateEngine;
import enkan.component.undertow.UndertowComponent;
import enkan.system.EnkanSystem;

import static enkan.component.ComponentRelationship.component;
import static enkan.util.BeanBuilder.builder;

public class SystemConfiguration implements enkan.config.EnkanSystemFactory {

    @Override
    public EnkanSystem create() {
        return EnkanSystem.of(
                "doma", new DomaProvider(),
                "jackson", new JacksonBeansConverter(),
                "flyway", new FlywayMigration(),
                "template", new ThymeleafTemplateEngine(),
                "datasource", new HikariCPComponent(OptionMap.of(
                        "uri", Env.getString("datasource.url", "jdbc:h2:mem:test"),
                        "username", Env.getString("datasource.username", ""),
                        "password", Env.getString("datasource.password", ""))),
                "app", new ApplicationComponent(ApplicationConfiguration.class.getName()),
                "http", builder(new UndertowComponent())
                        .set(UndertowComponent::setPort, Env.getInt("PORT", 3001))
                        .build()
        ).relationships(
                component("http").using("app"),
                component("app").using("datasource", "template", "doma", "jackson"),
                component("doma").using("datasource", "flyway"),
                component("flyway").using("datasource")
        );

    }
}