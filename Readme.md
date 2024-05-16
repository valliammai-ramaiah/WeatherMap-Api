WeatherMapApi connects to openWeatherMap api to get the description of weather based on city and country.
Once a request to the Api has been made it stores the information in H2 database with city, country and description information and queries the data from database.
Video of the working code has been uploaded as PR description

The getWeather controller takes in 2 path variables as city name and country name. Api key is passed in as header as x-api-key to the API. 

request can be made using postman like below
http://localhost:8080/weather/Perth/Australia
header - x-api-key = your_key

Steps to run: 
1. run ./gradlew clean build
2. run the WeatherApplication using jar file/ or your ide
3. your application will be launched on port 8080
4. make a request to the api using http://localhost:8080/weather/Perth/Australia and passing x-api-key to header
5. you can login to your h2 database using http://localhost:8080/h2-console/ credentials are in application.properties file

