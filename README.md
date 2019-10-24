# QDev backend

CleanTheForest is an activist group that helps to clean up the environment. To avoid unnecessary and time-consuming work they first examine the area using a drone before going to the field.

GoodTime2Go is the application that helps CleanTheForest to know the weather status of the location before sending the drone with the programmed route. If the weather is bad the drone does not check out the area.

<h2> To-do </h2>

Nowadays the application exposes an API with a dummy endpoint with fake data of the weather status.

1. We need to know the weather data of the next locations:

    - Name: Gorbea Area, Lat:43.04454, Lon:-2.776776
    - Name: Anboto Area, Lat: 43.089897, Lon: -2.595321
    - Name: Aizkorri Area, Lat: 42.951927, Lon: -2.347414

    You can obtain the weather data in [Open Weather Map API](https://openweathermap.org/api).

2. The current entity model is not complete, so you need to update it with this data:

    - Location.
    - Weather.
    - Check out permission (Boolean, based on the logic of what good weather is for us).
    - The report date (The date on which we obtained the data).

    NOTE:

    We consider good weather when the `main` field from openweathermap is equals `Clear`
    You can see an example here:

    https://samples.openweathermap.org/data/2.5/weather?q=London&appid=b1b15e88fa797225412429c1c50c122a1


3. We need two endpoints:

    - One to provide the current weather data of three areas.

    Example (you can create your own payload):

    ```
    GET: http://localhost:8080/weather

    {
      "location": "Gorbea",
      "coordinates_lat": "43.04454",
      "coordinates_lon": "-2.776776",
      "weather": "Clouds",
      "checkout": "true",
      "report_date": "2019-10-23T10:00:00",
    },
    {
      "location": "Anboto",
      "coordinates_lat": "43.089897",
      "coordinates_lon": "-2.595321",
      "weather": "Clear",
      "checkout": "true",
      "report_date": "2019-10-23T10:00:00",
    },
    {
      "location": "Aizkorri",
      "coordinates_lat": "42.951927",
      "coordinates_lon": "-2.347414",
      "weather": "Drizzle",
      "checkout": "false",
      "report_date": "2019-10-23T10:00:00",
    },

    ```

    - Another one to tell us if the location weather is ok to send there the drone.

    ```
    GET: http://localhost:8080/weather?location=gorbea

    {
      "location": "Gorbea",
      "coordinates_lat": "43.04454",
      "coordinates_lon": "-2.776776",
      "weather": "Clouds",
      "checkout": "true",
      "report_date": "2019-10-23T10:00:00",
    } 

    ```

    NOTE:

    We consider good weather when the `main` field from openweathermap is equals `Clear`
    You can see an example here:

    https://samples.openweathermap.org/data/2.5/weather?q=London&appid=b1b15e88fa797225412429c1c50c122a1

4. It is not necessary, but we appreciate if you create an endpoint to add new areas.


<h2> Open Weather API docs </h2>

- [Open Weather API guide](https://openweathermap.org/guide)
- [Open Weather API how to start](https://openweathermap.org/appid#get)
- [Open Weather current weather](https://openweathermap.org/current)
- [Open Weather conditions](https://openweathermap.org/weather-conditions)

<h2> What we are looking for: </h2>

- The base app is developed in Java with Spring Boot, but if you feel more comfortable with other language feel free to use it.
- Pay attention about how your code is organized.
- How you are reflecting the domain in the code.
- We love clean code.
- You need to apply the testing techniques that you know. (unit, integration, contract, functional etc.)
- We are looking forward to seeing your code and discuss with you your solution.