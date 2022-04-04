package clean.the.forest.area.application

import clean.the.forest.area.infrastructure.AreaRepository
import clean.the.forest.area.infrastructure.WeatherProvider
import clean.the.forest.area.model.Area
import clean.the.forest.area.model.GeoPos
import clean.the.forest.shared.testing.TestClassification
import org.junit.jupiter.api.Tag
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Unroll


@Tag(TestClassification.UNIT)
class ReportWeatherOfAllKnownAreasUseCaseTest extends Specification {

    @Unroll
    def 'weather of area "#areaName" should be "#expectedWeather"'() {

        given: "External dependencies stubbed"
        AreaRepository areaRepository = Stub()
        areaRepository.allKnown() >> Flux.just(
                new Area("Calderona", 39.67, -0.43, "ES"),
                new Area("Mariola", 38.72, -0.53, "ES"),
                new Area("Penyagolosa", 40.23, -0.29, "ES")
        )

        WeatherProvider weatherProvider = Stub()
        weatherProvider.byGeoPosition(new GeoPos(39.67, -0.43)) >> Mono.just("Clouds")
        weatherProvider.byGeoPosition(new GeoPos(38.72, -0.53)) >> Mono.just("Clear")
        weatherProvider.byGeoPosition(new GeoPos(40.23, -0.29)) >> Mono.just("Drizzle")

        when: "request weather of all known areas"
        def sut = new ReportWeatherOfAllKnownAreasUseCase(areaRepository, weatherProvider)
        def checkoutReports = sut.report().toIterable()

        then: "report should has expected area and weather condition"
        def particularAreaReport = checkoutReports.find { it.area.name == areaName }
        particularAreaReport.area == new Area(areaName, expectedLat, expectedLon, expectedCountry)
        particularAreaReport.weatherCondition == expectedWeather

        where:
        areaName      || expectedLat | expectedLon | expectedCountry | expectedWeather
        "Calderona"   || 39.67       | -0.43       | "ES"            | "Clouds"
        "Mariola"     || 38.72       | -0.53       | "ES"            | "Clear"
        "Penyagolosa" || 40.23       | -0.29       | "ES"            | "Drizzle"

    }

}
