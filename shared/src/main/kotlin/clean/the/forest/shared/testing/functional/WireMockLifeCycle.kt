package clean.the.forest.shared.testing.functional

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer
import io.cucumber.plugin.EventListener
import io.cucumber.plugin.Plugin
import io.cucumber.plugin.event.EventHandler
import io.cucumber.plugin.event.EventPublisher
import io.cucumber.plugin.event.TestRunFinished
import io.cucumber.plugin.event.TestRunStarted


open class WireMockLifeCycle : Plugin, EventListener {

    companion object {
        val wireMockServer: WireMockServer =
            WireMockServer(WireMockConfiguration.wireMockConfig()
                .dynamicPort()
                .extensions(ResponseTemplateTransformer(false)))
    }

    override fun setEventPublisher(publisher: EventPublisher) {
        publisher.registerHandlerFor(TestRunStarted::class.java, setup)
        publisher.registerHandlerFor(TestRunFinished::class.java, teardown)
    }

    private val setup = EventHandler { event: TestRunStarted ->
        wireMockServer.start()
    }

    private val teardown = EventHandler { event: TestRunFinished ->
        wireMockServer.stop()
    }

}
