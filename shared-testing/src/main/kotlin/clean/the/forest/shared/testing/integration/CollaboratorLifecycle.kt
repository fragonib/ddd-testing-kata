package clean.the.forest.shared.testing.integration

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.common.SingleRootFileSource
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.github.tomakehurst.wiremock.recording.RecordSpec
import com.github.tomakehurst.wiremock.recording.RecordingStatus.Recording

class CollaboratorLifecycle(private val collaboratorsConfig: RestCollaborators) {

    private var collaboratorMocks: List<WireMockServer> = listOf()

    fun setupCollaborators() {
        collaboratorMocks = collaboratorsConfig.collaborators.map { (name, definition) ->
            val wireMockServer = buildMockRestServer(name, definition)
            wireMockServer.start()
            if (definition.recording) {
                wireMockServer.startRecording(recordingConfig(definition.recordingUrl))
            }
            wireMockServer
        }
    }

    fun teardownCollaborators() {
        collaboratorMocks.forEach { mockServer ->
            if (mockServer.recordingStatus.status == Recording)
                mockServer.stopRecording()
            mockServer.stop()
        }
    }

    private fun recordingConfig(targetURL: String): RecordSpec {
        return WireMock.recordSpec()
            .forTarget(targetURL)
            .onlyRequestsMatching(RequestPatternBuilder.allRequests())
            .makeStubsPersistent(true)
            .ignoreRepeatRequests()
            .matchRequestBodyWithEqualToJson(true, true)
            .build()
    }

    private fun buildMockRestServer(name: String, collaboratorDefinition: RestCollaborator): WireMockServer {
        return WireMockServer(WireMockConfiguration.options()
            .port(collaboratorDefinition.port)
            .fileSource(SingleRootFileSource("src/test/resources/collaborators/$name"))
            .notifier(ConsoleNotifier(true)))
    }

}
