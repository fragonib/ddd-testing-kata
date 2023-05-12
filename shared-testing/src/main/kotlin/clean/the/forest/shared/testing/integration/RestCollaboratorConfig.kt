package clean.the.forest.shared.testing.integration

data class CollaboratorsConfig (
    val collaborators: Map<String, RestCollaboratorDefinition>
)

data class RestCollaboratorDefinition(
    val port: Int = 0,
    val dynamicPort: Boolean = true,
    val recording: Boolean = false,
    val recordingUrl: String? = null,
)
