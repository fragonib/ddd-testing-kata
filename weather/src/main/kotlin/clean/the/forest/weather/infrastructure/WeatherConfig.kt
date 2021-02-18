package clean.the.forest.weather.infrastructure

import clean.the.forest.weather.application.WeatherOfParticularAreaUseCase
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class WeatherConfig {

    @Bean
    fun inMemoryAreaRepository(): InMemoryAreaRepository {
        return InMemoryAreaRepository()
    }

    @Bean
    fun openWeatherProvider(@Value("\${OPENWEATHER_APIKEY}") apiKey: String): WeatherProvider {
        return OpenWeatherProvider(apiKey)
    }

    @Bean
    fun weatherOfParticularAreaUseCase(
        areaRepository: AreaRepository, weatherProvider: WeatherProvider
    ): WeatherOfParticularAreaUseCase {
        return WeatherOfParticularAreaUseCase(areaRepository, weatherProvider)
    }

}