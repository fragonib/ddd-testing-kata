package clean.the.forest.area.infrastructure

import clean.the.forest.shared.testing.integration.CollaboratorLifecycle
import clean.the.forest.shared.testing.integration.CollaboratorsConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [TestConfig])
class BaseCollaboratorSpec extends Specification {

    CollaboratorLifecycle collaboratorLifecycle

    def setup() {
        collaboratorLifecycle = new CollaboratorLifecycle(collaboratorsConfig)
        collaboratorLifecycle.setupCollaborators()
    }

    def cleanup() {
        collaboratorLifecycle?.teardownCollaborators()
    }

    @Autowired
    private CollaboratorsConfig collaboratorsConfig

    @Configuration
    static class TestConfig {
        @Bean
        CollaboratorsConfig collaboratorsConfig(Environment env) {
            Binder.get(env).bindOrCreate('collaborators-config', CollaboratorsConfig)
        }
    }

}
