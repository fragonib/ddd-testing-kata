package clean.the.forest.weather.infraestructure

import clean.the.forest.weather.model.Country
import clean.the.forest.weather.model.GeoPos
import spock.lang.Specification


class AreaRepositorySpec extends Specification {

    AreaRepository sut = new AreaRepository()

    def 'area "#name" is known'() {

        when:
        def foundArea = sut.findByName(name)

        then:
        foundArea.position == new GeoPos(lat, lon)
        foundArea.country == new Country(countryCode)

        where:
        name        | lat   | lon   | countryCode
        "ipiñaburu" | 43.07 | -2.75 | "ES"
        "ibarra"    | 43.05 | -2.57 | "ES"
        "zegama"    | 42.97 | -2.29 | "ES"
    }

    def 'area "#unknownName" is NOT known'() {

        when:
        sut.findByName(unknownName)

        then:
        IllegalArgumentException ex = thrown()
        ex.message == expectedMessage

        where:
        unknownName || expectedMessage
        "unknown"   || "There is no area called [unknown]"
    }

}
