package clean.the.forest.shared.testing

interface TestClassification {
    companion object {
        const val UNIT = "unit"
        const val INTEGRATION = "integration"
        const val CONTRACT = "contract"
        const val FUNCTIONAL = "functional"
    }
}