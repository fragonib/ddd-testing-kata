package clean.the.forest.shared.testing.functional

import clean.the.forest.shared.testing.integration.Collaborator.OPEN_WEATHER
import clean.the.forest.shared.testing.integration.CollaboratorLifecycle
import clean.the.forest.shared.testing.integration.RestCollaboratorDefinition
import clean.the.forest.shared.testing.integration.CollaboratorsConfig
import io.cucumber.plugin.EventListener
import io.cucumber.plugin.Plugin
import io.cucumber.plugin.event.EventHandler
import io.cucumber.plugin.event.EventPublisher
import io.cucumber.plugin.event.TestRunFinished
import io.cucumber.plugin.event.TestRunStarted


open class WireMockLifeCycle : Plugin, EventListener {

    companion object {
        val collaboratorLifecycle = CollaboratorLifecycle(
            CollaboratorsConfig(
                mapOf(
                    OPEN_WEATHER.literal to RestCollaboratorDefinition(dynamicPort = true, recording = false)
                )
            )
        )
    }

    override fun setEventPublisher(publisher: EventPublisher) {
        publisher.registerHandlerFor(TestRunStarted::class.java, setup)
        publisher.registerHandlerFor(TestRunFinished::class.java, teardown)
    }

    private val setup = EventHandler { _: TestRunStarted ->
        collaboratorLifecycle.setupCollaborators()
    }

    private val teardown = EventHandler { _: TestRunFinished ->
        collaboratorLifecycle.teardownCollaborators()
    }

}
