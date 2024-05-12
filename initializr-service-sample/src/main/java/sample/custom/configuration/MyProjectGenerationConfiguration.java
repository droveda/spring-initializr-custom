package sample.custom.configuration;

import io.spring.initializr.generator.condition.ConditionalOnLanguage;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import org.springframework.context.annotation.Bean;
import sample.custom.contributor.MyContributor;

@ProjectGenerationConfiguration
public class MyProjectGenerationConfiguration {

    @Bean
    @ConditionalOnLanguage("java")
    public MyContributor myContributor() {
        return new MyContributor();
    }

}
