package clean.the.forest.area.infrastructure

import clean.the.forest.area.application.WeatherGateway
import clean.the.forest.area.domain.GeoPos
import clean.the.forest.area.domain.WeatherCondition
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient


class OpenWeatherGateway(
    private val baseUrl: String,
    private val apiKey: String
) : WeatherGateway {

    /**
     * Weather by geographic coordinates using OpenWeatherMap
     *
     * API call: GET api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
     *
     * Parameters
     * lat, lon required 	Geographical coordinates (latitude, longitude)
     * appid 	required 	Your unique API key (you can always find it on your account page under the "API key" tab)
     * mode 	optional 	Response format. Possible values are xml and html. If you don't use the mode parameter format is JSON by default. Learn more
     * units 	optional 	Units of measurement. standard, metric and imperial units are available. If you do not use the units parameter, standard units will be applied by default. Learn more
     * lang 	optional 	You can use this parameter to get the output in your language. Learn more
     *
     * @return Weather condition
     */
    override fun byGeoPosition(geoPos: GeoPos): Mono<WeatherCondition> {

        return buildWebClient(baseUrl)
            .get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/weather")
                    .queryParam("appid", apiKey)
                    .queryParam("lat", geoPos.lon)
                    .queryParam("lon", geoPos.lat)
                    .build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(typeReference<Map<String, Any>>())
            .map(::extractWeatherCondition)
            .switchIfEmpty(Mono.error { Throwable("There is no weather condition for location [$geoPos]") })

    }

    private fun extractWeatherCondition(responseBody: JsonObject): WeatherCondition {
        @Suppress("UNCHECKED_CAST")
        val reports = responseBody["weather"] as List<JsonObject>
        return reports.first()["main"] as String
    }

    private fun buildWebClient(baseUrl: String): WebClient {
        val httpClient: HttpClient = HttpClient.create()
            .wiretap(true)
        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .baseUrl(baseUrl)
            .build()
    }

}

typealias JsonObject = Map<String, Any>

private inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}
