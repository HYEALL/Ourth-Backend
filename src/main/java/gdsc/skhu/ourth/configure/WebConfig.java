package gdsc.skhu.ourth.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig
{
    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedHeaders(HttpMethod.GET.name(),
                                HttpMethod.PATCH.name(),
                                HttpMethod.POST.name(),
                                HttpMethod.DELETE.name(),
                                HttpMethod.OPTIONS.name())
                        .allowCredentials(true)
                        .maxAge(3600)
                        .allowedMethods("*");
            }
        };
    }
}