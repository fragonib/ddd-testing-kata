package clean.the.forest.shared.testing.functional

import io.cucumber.plugin.EventListener
import io.cucumber.plugin.Plugin
import io.cucumber.plugin.event.*
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import java.time.Duration


open class AppContainerLifeCycle : Plugin, EventListener {

    companion object {

        private const val APP_TEST_PORT = 8080
        private val container: KGenericContainer =
            KGenericContainer(DockerImageName.parse("clean-the-forest/app:latest"))
                .withEnv(System.getenv())
                .withExposedPorts(APP_TEST_PORT)
                .waitingFor(Wait
                    .forHttp("/")
                    .forStatusCode(404)
                    .withStartupTimeout(Duration.ofMinutes(1)))
    }

    override fun setEventPublisher(publisher: EventPublisher) {
        publisher.registerHandlerFor(TestRunStarted::class.java, setup)
        publisher.registerHandlerFor(TestRunFinished::class.java, teardown)
    }

    private val setup = EventHandler { _: TestRunStarted ->
        println("********* ${ProcessHandle.current().pid()}")
        container.start()
    }

    private val teardown = EventHandler { _: TestRunFinished ->
        container.stop()
    }

}

/**
 * [GenericContainer] to avoid generics errors on Kotlin
 */
class KGenericContainer(imageName: DockerImageName) : GenericContainer<KGenericContainer>(imageName)
