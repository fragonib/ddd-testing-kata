package clean.the.forest.area.infrastructure

import clean.the.forest.area.model.Area
import clean.the.forest.area.model.Country
import clean.the.forest.area.model.GeoPos
import clean.the.forest.shared.testing.TestClassification
import org.junit.jupiter.api.Tag
import spock.lang.Specification

@Tag(TestClassification.INTEGRATION)
class InMemoryAreaRepositoryTest extends Specification {

    def 'all known areas contains "Ipiñaburu, Ibarra, Zegama"'() {

        given:
        AreaRepository sut = new InMemoryAreaRepository()

        when:
        def knownAreas = sut.allKnown().collectList().block()

        then:
        knownAreas.size() == 3
        knownAreas.name == ["Ipiñaburu", "Ibarra", "Zegama"]
        knownAreas.position.lat == [43.07d, 43.05d, 42.97d]
        knownAreas.position.lon == [-2.75d, -2.57d, -2.29d]
        knownAreas.country.code.every { it == "ES" }

    }

    def 'finding area "#areaName" is known'() {

        given:
        AreaRepository sut = new InMemoryAreaRepository()

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

    def 'finding area "#areaName" is NOT known'() {

        given:
        AreaRepository sut = new InMemoryAreaRepository()

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

    def 'adding an area should aggregate it to known areas'() {

        given:
        AreaRepository sut = new InMemoryAreaRepository()

        when:
        def newArea = new Area("newArea", 40, -2, "ES")
        sut.addArea(newArea).block()

        then:
        def knownAreas = sut.allKnown().toIterable()
        knownAreas.any { it == newArea }

    }

    def 'adding an yet known area should complain'() {

        given:
        AreaRepository sut = new InMemoryAreaRepository()

        when:
        def areaName = new Area("Ipiñaburu", 40, -2, "ES")
        sut.addArea(areaName).block()

        then:
        IllegalStateException ex = thrown()
        ex.message == "There is yet known area called [Ipiñaburu]"

    }

}
