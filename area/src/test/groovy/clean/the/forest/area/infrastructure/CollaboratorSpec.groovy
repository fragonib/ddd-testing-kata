package clean.the.forest.area.infrastructure

import clean.the.forest.shared.testing.integration.CollaboratorLifecycle
import clean.the.forest.shared.testing.integration.CollaboratorsConfig
import spock.lang.Specification

class CollaboratorSpec extends Specification {

    CollaboratorLifecycle collaboratorLifecycle

    def setupO(CollaboratorsConfig collaboratorsConfig) {
        collaboratorLifecycle = new CollaboratorLifecycle(collaboratorsConfig)
        collaboratorLifecycle.setupCollaborators()
    }

    def cleanup() {
        collaboratorLifecycle.teardownCollaborators()
    }

}
