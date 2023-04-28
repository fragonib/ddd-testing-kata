package clean.the.forest.area.infrastructure

import clean.the.forest.area.application.AddAreaUseCase
import clean.the.forest.area.application.ReportCheckabilityOfParticularAreaUseCase
import clean.the.forest.area.application.ReportWeatherOfAllKnownAreasUseCase
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AreaConfig {

    @Bean
    fun inMemoryAreaRepository(): InMemoryAreaRepository {
        return InMemoryAreaRepository()
    }

    @Bean
    fun openWeatherProvider(
        @Value("\${open-weather-map.base-url}") baseUrl: String,
        @Value("\${open-weather-map.api-key}") apiKey: String
    ): WeatherProvider {
        return OpenWeatherProvider(baseUrl, apiKey)
    }

    @Bean
    fun reportOfAllKnownAreasUseCase(
        areaRepository: AreaRepository,
        weatherProvider: WeatherProvider
    ): ReportWeatherOfAllKnownAreasUseCase {
        return ReportWeatherOfAllKnownAreasUseCase(areaRepository, weatherProvider)
    }

    @Bean
    fun reportOfParticularAreaUseCase(
        areaRepository: AreaRepository,
        weatherProvider: WeatherProvider
    ): ReportCheckabilityOfParticularAreaUseCase {
        return ReportCheckabilityOfParticularAreaUseCase(areaRepository, weatherProvider)
    }

    @Bean
    fun addAreaUseCase(areaRepository: AreaRepository): AddAreaUseCase {
        return AddAreaUseCase(areaRepository)
    }

}
