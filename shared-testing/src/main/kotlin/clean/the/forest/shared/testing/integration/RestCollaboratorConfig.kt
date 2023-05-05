package clean.the.forest.shared.testing.integration

data class RestCollaborators (
    val collaborators: Map<String, RestCollaborator>
)

data class RestCollaborator(
    val port: Int,
    val recording: Boolean,
    val recordingUrl: String,
)
