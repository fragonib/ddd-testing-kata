package clean.the.forest.area.infrastructure

import clean.the.forest.area.model.Area
import clean.the.forest.area.model.Country
import clean.the.forest.area.model.GeoPos
import clean.the.forest.shared.testing.TestClassification
import org.junit.jupiter.api.Tag
import spock.lang.Specification


@Tag(TestClassification.INTEGRATION)
class InMemoryAreaRepositoryTest extends Specification {

    def 'all known areas contains "Calderona, Mariola, Penyagolosa"'() {

        given:
        AreaRepository sut = new InMemoryAreaRepository()

        when:
        def knownAreas = sut.allKnown().collectList().block()


        then:
        knownAreas.size() == 3
        knownAreas.name == ["Calderona", "Mariola", "Penyagolosa"]
        knownAreas.position.lat == [39.67d, 38.72d, 40.23d]
        knownAreas.position.lon == [-0.43d, -0.53d, -0.29d]
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
        areaName      | lat   | lon   | countryCode
        "Calderona"   | 39.67 | -0.43 | "ES"
        "Mariola"     | 38.72 | -0.53 | "ES"
        "Penyagolosa" | 40.23 | -0.29 | "ES"
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
        def newArea = new Area("newArea", 40, 0, "ES")
        sut.addArea(newArea).block()

        then:
        def knownAreas = sut.allKnown().toIterable()
        knownAreas.any { it == newArea }

    }

    def 'adding an yet known area should complain'() {

        given:
        AreaRepository sut = new InMemoryAreaRepository()

        when:
        def areaName = new Area("Calderona", 40, 0, "ES")
        sut.addArea(areaName).block()

        then:
        ConflictWithExistingArea ex = thrown()
        ex.message == "There is yet known area called [Calderona]"

    }

}
