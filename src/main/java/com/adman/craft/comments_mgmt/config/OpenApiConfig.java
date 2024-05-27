package com.adman.craft.comments_mgmt.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import java.util.List;


@Configuration
public class OpenApiConfig {

    @Value("${comments-mgmt.url.dev}")
    private String DEV_URL;

    @Value("${comments-mgmt.url.staging}")
    private String STAGING_URL;

    @Value("${comments-mgmt.url.prod}")
    private String PROD_URL;

    @Bean
    public OpenAPI myOpenAPI() {

        Server devServer = devServer();
        Server stagingServer = stagingServer();
        Server prodServer = prodServerServer();

        Contact contact = contact();

        Info info = info(contact);

        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components().addSecuritySchemes("BearerAuth", securityScheme()));
                //.servers(List.of(devServer, stagingServer, prodServer));
    }

    private static Info info(Contact contact) {
        Info info = new Info()
                .title("Comments Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage comments.")
                .termsOfService("https://accounts.intuit.com/terms-of-service")
                .license(null);
        return info;
    }

    private static Contact contact() {
        Contact contact = new Contact();
        contact.setEmail("queries@intuit.com");
        contact.setName("Arun Kumar");
        contact.setUrl("https://www.intuit.com");
        return contact;
    }

    private Server devServer() {
        Server devServer = new Server();
        devServer.setUrl(DEV_URL);
        devServer.setDescription("Dev Env Server URL");
        return devServer;
    }

    private Server stagingServer() {
        Server stagingServer = new Server();
        stagingServer.setUrl(STAGING_URL);
        stagingServer.setDescription("Staging Env Server URL");
        return stagingServer;
    }

    private Server prodServerServer() {
        Server prodServer = new Server();
        prodServer.setUrl(PROD_URL);
        prodServer.setDescription("Dev Env Server URL");
        return prodServer;
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name("BearerAuth")
                .description("JWT Auth")
                .scheme("bearer")
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER);
    }
}
