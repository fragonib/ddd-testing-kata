package clean.the.forest.shared.testing

import io.cucumber.junit.platform.engine.Cucumber

/**
 * Gradle does not support JUnit platform discovery selectors yet. This class helps Gradle to discover all tests.
 *
 * In order to run scenarios, use 'gradle test'. Executing this class in your IDE won't work.
 *
 * @see [Gradle 4773](https://github.com/gradle/gradle/issues/4773)
 */
@Cucumber
class BuildToolSupport
