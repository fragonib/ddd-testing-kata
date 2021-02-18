package clean.the.forest.weather.infrastructure

import clean.the.forest.shared.testing.TestClassification
import clean.the.forest.weather.model.Country
import clean.the.forest.weather.model.GeoPos
import org.junit.jupiter.api.Tag
import spock.lang.Specification


@Tag(TestClassification.INTEGRATION)
class InMemoryAreaRepositorySpec extends Specification {

    AreaRepository sut = new InMemoryAreaRepository()

    def 'area repository contains 3 areas "Ipiñaburu, Ibarra, Zegama"'() {

        when:
        def knownAreas = sut.findAll()
                .collectList()
                .block()

        then:
        knownAreas.size() == 3
        knownAreas.name == ["Ipiñaburu", "Ibarra", "Zegama"]
        knownAreas.position.lat == [43.07d, 43.05d, 42.97d]
        knownAreas.position.lon == [-2.75d, -2.57d, -2.29d]
        knownAreas.country.code.every {it == "ES"}

    }

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
        "ipiñabu" || "There is no area called [ipiñabu]"
    }

}
