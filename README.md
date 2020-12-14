# QDev backend

CleanTheForest is an activist group that helps to clean up the environment. To avoid unnecessary and time-consuming work they first examine the area using a drone before going to the field.

We need a new service to help CleanTheForest to know the weather status of the location before sending the drone with the programmed route. If the weather is bad the drone won't check out the area.

<h2> To-do </h2>

Nowadays the application exposes an API with a dummy endpoint with fake data of the weather status.

1. We need to know the weather data of the next locations:

    - Name: Ipiñaburu Area, Lat:43.07, Lon:-2.75, Country code: ES
    - Name: Ibarra Area, Lat: 43.05, Lon: -2.57, Country code: ES
    - Name: Zegama Area, Lat: 42.97, Lon: -2.29, Country code: ES


You can obtain the weather data from [Open Weather Map API](https://openweathermap.org/api).

To simplify we have proposed these locations, but feel free if you prefer using other locations.

2. We need two endpoints:
   
- One to provide the current weather data of three areas.

    Example (you can create your own payload):

    ```
    GET: http://localhost:8080/weather

    {
      "location": "Iñipaburu",
      "coordinates_lat": "43.07",
      "coordinates_lon": "-2.75",
      "weather": "Clouds",
      "checkout": "false",
      "report_date": "2019-10-23T10:00:00",
    },
    {
      "location": "Ibarra",
      "coordinates_lat": "43.05",
      "coordinates_lon": "-2.57",
      "weather": "Clear",
      "checkout": "true",
      "report_date": "2019-10-23T10:00:00",
    },
    {
      "location": "Zegama",
      "coordinates_lat": "42.97",
      "coordinates_lon": "-2.29",
      "weather": "Drizzle",
      "checkout": "false",
      "report_date": "2019-10-23T10:00:00",
    },

    ```

- Another one to tell us if the location weather is ok to send there the drone.

    ```
    GET: http://localhost:8080/weather?location=zegama

    {
      "location": "Zegama",
      "coordinates_lat": "42.97",
      "coordinates_lon": "-2.29",
      "weather": "Drizzle",
      "checkout": "false",
      "report_date": "2019-10-23T10:00:00",
    } 

    ```

    NOTE:

    We consider good weather when the `main` field from openweathermap is equals `Clear`
    You can see an example here:

    https://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22


3. It is not necessary, but we appreciate if you create an endpoint to add new areas.

<h2> Open Weather API docs </h2>

- [Open Weather API guide](https://openweathermap.org/guide)
- [Open Weather API how to start](https://openweathermap.org/appid#get)
- [Open Weather current weather](https://openweathermap.org/current)
- [Open Weather conditions](https://openweathermap.org/weather-conditions)

<h2> What we are looking for: </h2>

- Feel free to use the language that you want.
- Pay attention about how your code is organized.
- How you are reflecting the domain in the code.
- We love clean code.
- Remember to document: how we can run the app, how we can launch the tests and more information if you think that is relevant.
- You need to apply the testing techniques that you know. (unit, integration, contract, functional etc.)
- We are looking forward to seeing your code and discussing your solution with you.
