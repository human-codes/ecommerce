package uz.pdp.salemartpro.config;/*
package uz.pdp.salemartpro.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;

import java.util.List;
*/
/*

@OpenAPIDefinition(
        info = @Info(
                title = "SaleMartPro API",
                version = "1.0",
                description = "API documentation for the SaleMartPro e-commerce application",
                contact = @Contact(
                        name = "Support Team",
                        email = "support@salemartpro.com",
                        url = "https://salemartpro.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        security = {
                @SecurityRequirement(
                        name = "b"
                )
        }
)

@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "bearerAuth",
        description = "JWT auth description",
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
*//*


public class OpenApiConfig {
    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ELECTRONIC COMMERCE")
                        .description("G46 GROUP")
                        .version("Nechinchi versiyaligi esimda yo'q")
                        .contact(new Contact()
                                .name("Behruzbek Mahmudjonov")
                                .email("behruzbekmahmudjonov08@gmail.com")
                                .url("pdp.uz"))
                        .license(new License()
                                .name("Benom")
                                .url("http://springdoc.org"))
                        .termsOfService("http://swagger.io/terms/"))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Production")
                ))

                .addSecurityItem(new SecurityRequirement().addList("tokenAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("tokenAuth",
                                new SecurityScheme()
                                        .name("token")
                                        .type(SecurityScheme.Type.APIKEY)  // APIKEY bo‘lishi kerak
                                        .in(SecurityScheme.In.HEADER)      // Header ichida bo‘ladi
                                        .bearerFormat("JWT")));
    }

}
*/

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ELECTRONIC COMMERCE")
                        .description("G46 GROUP")
                        .version("Nechinchi versiyaligi esimda yo'q")
                        .contact(new Contact()
                                .name("Behruzbek Mahmudjonov")
                                .email("behruzbekmahmudjonov08@gmail.com")
                                .url("pdp.uz"))
                        .license(new License()
                                .name("Benom")
                                .url("http://springdoc.org"))
                        .termsOfService("http://swagger.io/terms/"))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Production")
                ))

                .addSecurityItem(new SecurityRequirement().addList("tokenAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("tokenAuth",
                                new SecurityScheme()
                                        .name("token")
                                        .type(SecurityScheme.Type.APIKEY)  // APIKEY bo‘lishi kerak
                                        .in(SecurityScheme.In.HEADER)      // Header ichida bo‘ladi
                                        .bearerFormat("JWT")));
    }
}
