<<<<<<<< HEAD:shared/src/main/kotlin/clean/the/forest/shared/testing/TestClassification.kt
package clean.the.forest.shared.testing
========
package clean.the.forest.weather.shared.testing
>>>>>>>> refs/rewritten/Merge:shared/src/main/kotlin/clean/the/forest/weather/shared/testing/TestClassification.kt

interface TestClassification {
    companion object {
        const val UNIT = "unit"
        const val INTEGRATION = "integration"
        const val CONTRACT = "contract"
        const val FUNCTIONAL = "functional"
    }
}