package clean.the.forest.area.infrastructure

import clean.the.forest.shared.testing.integration.RestCollaborators
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class RestCollaboratorConfig {

    @Bean
    RestCollaborators collaboratorsConfig(Environment env) {
        Binder.get(env).bindOrCreate("collaborators-config", RestCollaborators.class)
    }

}
