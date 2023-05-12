package clean.the.forest.shared.testing.integration

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.common.SingleRootFileSource
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.github.tomakehurst.wiremock.recording.RecordSpec
import com.github.tomakehurst.wiremock.recording.RecordingStatus.Recording

class CollaboratorLifecycle(collaboratorsConfig: CollaboratorsConfig) {

    private var collaboratorDefinitions: Map<String, RestCollaboratorDefinition> = collaboratorsConfig.collaborators
    private var collaboratorMocks: Map<String, WireMockServer> = collaboratorDefinitions
        .mapValues { (name, definition) -> buildMockRestServer(name, definition) }

    fun collaboratorMock(name: String) = collaboratorMocks[name]!!

    fun setupCollaborators() {
        collaboratorMocks.forEach { (name, mockServer) ->
            mockServer.start()
            val definition = collaboratorDefinitions[name]!!
            if (definition.recording) {
                mockServer.startRecording(recordingConfig(definition.recordingUrl!!))
            }
        }
    }

    fun teardownCollaborators() {
        collaboratorMocks.forEach { (_, mockServer) ->
            if (mockServer.recordingStatus.status == Recording)
                mockServer.stopRecording()
            mockServer.stop()
        }
    }

    private fun buildMockRestServer(name: String, definition: RestCollaboratorDefinition): WireMockServer {
        return WireMockServer(
            WireMockConfiguration.options()
                .fileSource(SingleRootFileSource("src/test/resources/collaborators/$name"))
                .notifier(ConsoleNotifier(true))
                .apply {
                    if (definition.dynamicPort) dynamicPort()
                    else port(definition.port)
                }
                .extensions(ResponseTemplateTransformer(false))
        )
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

}
