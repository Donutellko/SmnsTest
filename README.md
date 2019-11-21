# Task

**Write a service for sensor data input.**
Let's assume we have a sensor that sends temperature and location data (coordinates). Server validates the data and 
stores it. The user is able to get the list of last 10 inputs.

## Required:  
•	Using Spring Framework (v>=4) implement two endpoints (POST and GET), validation controller, database layer (repository).  
•	Validation shall include a check of the coordinates format and temperature interval.  
•	Provide REST API for the client to get json with the list of last 10 inputs  
•	Cover functionality with unit- and integration tests  
•	Provide scripts to populate DB (please, use in-memory DB)  

## Optional, not compulsory (none, any or all):   
•	Basic authorization for both client and sensor  
•	Extend the application by adding a feature to filter by city. The city should be retrieved automatically from the 
    given coordinates  
•	Implement basic UI (use your favorite JS framework)  
You can describe, how would you do optional tasks in words in case it seems difficult or long for you  


# Build

Use `gradlew bootRun` to start the application.
Use `gradlew testClasses` to start JUnit tests.

The project is developed in IntelliJ IDEA, so running the project from it might be a preffered option.

# Implementation

Used technologies are: 
  1. Spring Framework (Boot, MVC, Data JPA)
  2. Hibernate
  3. JUnit

More to come:
  * Feign (with Geocode API)
  * AngularJS
  * 

## Endpoints

### Add new input to database
```http
POST /add
```
Required parameters: 
* lat latitude
* lon: longitude
* value: temperature

Optional parameters:
* time: date and time

Expected response:
* `200 OK` and an `ID` of an added row as a payload, if added successfully.
* `400 BAD REQUEST` and human-readable violated constraint names and values which are not correct, as a payload. 
Example: `Invalid longitude: 1337.0; Invalid latitude: 1337.0`

### Get last inputs
```http
GET /latest
```

Optional parameters:
* count: required count of inputs. Default value is 10.

Expected response: 
* `200 OK` and a JSON array of not more than `count` last entries (less, if not enough in database), containing:
  * `id` their ID in database;
  * `temperature` temperature;
  * `lat` latitude;
  * `lon` longitude;
  * `datetime` timestamp that was specified in `/add` request or a moment of adding it to database.
  Example: 
  ``` JSON
  [
    {
        "id": 3,
        "temperature": 10.0,
        "lat": 10.0,
        "lon": 40.0,
        "datetime": "2019-11-21T15:04:02.888009"
    }, ...
  ]
  ```

## Endpoints for testing purposes

These endpoints are not specified in task description, but were added for convenience during manual testing and for 
demonstration purposes.
There are also sensitive commands, such as `clear`, and this API is not secured from access, as it is not meant to be 
in production.

### Populate database

```http
GET /test/populate
```

Optional parameters:
* `count`: amount of entries to be added. Default value is 20.

The generated entries contain random values as temperature, longitude and latitude, and datetime is a timestamp of when 
this entry was created.

### Clear database

```http
GET /test/clear
```

Removes all entries from database.


## Comments

### "last"
As I understood the task, the GET request should return values based on in which order they have been added, not their 
timestamps. And timestamps are never mention in task description, so timestamp value in my Entity is obsolete in this 
case, but I feel like it might be a useful piece of information, if this was an actual system. 

### Last N measurments
Task description clearly says that system should return 10 last inputs, but I decided to also add `count` of inputs as 
an optional parameter. It didn't require any effort, but made testing a bit easier: so it is possible to fill database 
once with several inputs and simulate several cases:
1. Size of table is greater that requested `count`;
2. Size is equal to `count`;
3. Size is less than `count` (when we don't have enough rows);

Alternatively, I could have used `@BeforeEach` to clear and fill database before testing each of these cases instead, 
and it might be reasonable for actual project, but I right now I don't see or know a strong reason to do make it, and 
decided to keep it this, more simple and fast, way.

### Measurment scales
It is possible to add an additional request field to clarify, which scale (Celcius, Fahrenheit or Kelvin) is used, and 
also to convert every value to same scale, before saving it, for consistency. 

## Database population
For database population there is `src/test/populate.sh` file, which contains curl request with random values.

## Testing
I manually add some components to context for integration tests:

```java
@ContextConfiguration(
  classes = {
    TestJpaConfig.class, 
    ApiErrorHandler.class
  }, ...
)
```
That's because not all components automatically load into Context in testing phase, in particular, built-in and custom
 filters. I am not sure, if it doing so is OK, but I tried `@SpringBootTest` annotation and several other ways, but 
 couldn't force loading of the whole context, identical to how it happens during actual run.

### Unit-tests
I couldn't find any functionality which is not covered completely by integration tests.   
I do understand advantages of unit-tests, and I know, how to write them, but in this example I don't see, where they 
could be even a little bit useful. 

