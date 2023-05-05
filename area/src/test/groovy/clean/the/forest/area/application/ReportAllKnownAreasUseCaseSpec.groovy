package clean.the.forest.area.application

import clean.the.forest.shared.testing.TestClassification
import clean.the.forest.area.infrastructure.AreaRepository
import clean.the.forest.area.infrastructure.InMemoryAreaRepository
import clean.the.forest.area.infrastructure.WeatherProvider
import clean.the.forest.area.model.Area
import clean.the.forest.area.model.GeoPos
import org.junit.jupiter.api.Tag
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Unroll


@Tag(TestClassification.UNIT)
class ReportAllKnownAreasUseCaseSpec extends Specification {

    @Unroll
    def 'checkable status of area named "#areaName" should be "#checkableStatus"'() {

        given:
        AreaRepository areaRepository = new InMemoryAreaRepository()

        WeatherProvider weatherProvider = Stub()
        weatherProvider.reportWeatherByGeoPos(new GeoPos(43.07, -2.75)) >> Mono.just("Clouds")
        weatherProvider.reportWeatherByGeoPos(new GeoPos(43.05, -2.57)) >> Mono.just("Clear")
        weatherProvider.reportWeatherByGeoPos(new GeoPos(42.97, -2.29)) >> Mono.just("Drizzle")

        when:
        def sut = new ReportAllKnownAreasUseCase(areaRepository, weatherProvider)
        def checkoutReports = sut.report().toIterable()

        then:
        def particularAreaReport = checkoutReports.find { it.area.name == areaName }
        particularAreaReport.area == new Area(areaName, lat, lon, country)
        particularAreaReport.checkable == checkableStatus

        where:
        areaName    || lat   | lon   | country | weatherCondition | checkableStatus
        "Ipiñaburu" || 43.07 | -2.75 | "ES"    | "Clouds"         | false
        "Ibarra"    || 43.05 | -2.57 | "ES"    | "Clear"          | true
        "Zegama"    || 42.97 | -2.29 | "ES"    | "Drizzle"        | false
    }

}
