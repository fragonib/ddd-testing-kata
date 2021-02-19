package clean.the.forest.shared.testing

/**
 * An instance of this class will be injected in each scenario, thus allowing all scenarios to be run in parallel.
 */
class ScenarioState {

    private val state = mutableMapOf<String, Any>()

    operator fun set(key: String, value: Any) {
        state[key] = value
    }

    operator fun get(key: String): Any? {
        return state[key]
    }

}