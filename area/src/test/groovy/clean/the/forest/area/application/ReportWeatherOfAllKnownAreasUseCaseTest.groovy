package clean.the.forest.area.application


import clean.the.forest.area.domain.Area
import clean.the.forest.area.domain.GeoPos
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll


class ReportWeatherOfAllKnownAreasUseCaseTest extends Specification {

    AreaRepository areaRepository = Mock()
    WeatherProvider weatherProvider = Mock()

    @Subject
    def sut = new ReportWeatherOfAllKnownAreasUseCase(areaRepository, weatherProvider)

    @Unroll
    def 'weather of area "#areaName" should be "#expectedWeather"'() {

        given: "External dependencies stubbed"
        areaRepository.allKnown() >> Flux.just(
                new Area("Calderona", 39.67, -0.43, "ES"),
                new Area("Mariola", 38.72, -0.53, "ES"),
                new Area("Penyagolosa", 40.23, -0.29, "ES")
        )

        weatherProvider.byGeoPosition(new GeoPos(39.67, -0.43)) >> Mono.just("Clouds")
        weatherProvider.byGeoPosition(new GeoPos(38.72, -0.53)) >> Mono.just("Clear")
        weatherProvider.byGeoPosition(new GeoPos(40.23, -0.29)) >> Mono.just("Drizzle")

        when: "request weather of all known areas"
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
