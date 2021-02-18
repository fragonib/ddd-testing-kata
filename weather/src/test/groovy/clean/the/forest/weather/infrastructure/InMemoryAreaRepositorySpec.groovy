package clean.the.forest.weather.infrastructure

import clean.the.forest.weather.model.Country
import clean.the.forest.weather.model.GeoPos
import clean.the.forest.weather.shared.testing.TestClassification
import org.junit.jupiter.api.Tag
import spock.lang.Specification


@Tag(TestClassification.INTEGRATION)
class InMemoryAreaRepositorySpec extends Specification {

    AreaRepository sut = new InMemoryAreaRepository()

    def 'area "#areaName" is known'() {

        when:
        def foundArea = sut.findByName(areaName).block()

        then:
        foundArea.country == new Country(countryCode)
        foundArea.position == new GeoPos(lat, lon)

        where:
        areaName    | lat   | lon   | countryCode
        "ipiñaburu" | 43.07 | -2.75 | "ES"
        "ibarra"    | 43.05 | -2.57 | "ES"
        "zegama"    | 42.97 | -2.29 | "ES"
    }

    def 'area "#areaName" is NOT known'() {

        when:
        sut.findByName(areaName).block()

        then:
        IllegalArgumentException ex = thrown()
        ex.message == expectedMessage

        where:
        areaName  || expectedMessage
        "unknown" || "There is no area called [unknown]"
    }

}
