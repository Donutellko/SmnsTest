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
•	**DONE!** Basic authorization for both client and sensor  
•	**DONE!** Extend the application by adding a feature to filter by city. The city should be retrieved automatically from the 
    given coordinates  
•	**DONE!** Implement basic UI (use your favorite JS framework)  
You can describe, how would you do optional tasks in words in case it seems difficult or long for you  


# Build

Use `gradlew bootRun` to start the application.  

Application will be accessible on port `8080` by default: `http://localhost:8080/`

The project is developed in IntelliJ IDEA, and does not seem to require any additional setup, 
so running the project in IDEA might be the best option.


# Implementation

Used technologies are: 
  1. Spring Framework (Boot, MVC, Data JPA)
  2. Hibernate
  3. JUnit
  4. AngularJS
  5. Less

## Endpoints

### Login
```http
POST /login
```

Required parameters:
* username 
* password 

#### Roles

There are three levels of access:
* SENSOR: can only access `/api/add`;
* USER: can access `/api/**`;
* ADMIN: can access `/test/**`.

There is account for each role for testing and demonstration purposes.
Logins are: admin, user and sensor, and password is `smns`. 

### Authentication example

We may use this request to login and store a cookie to a file: 
```shell script
curl -i -X POST -d username=user -d password=smns -c ~/cookies.txt \
 http://localhost:8080/login
```

And then use the saved cookie with further request like this:
```shell script
curl -i --header "Accept:application/json" -X GET -b ~/cookies.txt \
 http://localhost:8080/api/latest
```

### Add new input to database
```http
POST /api/add
```
Required parameters: 
* lat latitude
* lon: longitude
* value: temperature

Optional parameters:
* time: date and time

Expected response:
* `200 OK` and an object of an added row as a payload, if added successfully.
* `400 BAD REQUEST` and human-readable violated constraint names and values which are not correct, as a payload.
    Example: 
    ```json
    {
      "success": false,
      "message": "Constraint violation",
      "vilated": ["Invalid latitude: 1337", "Invalid temperature: -9000"]
    }
    ``` 
Example: `Invalid longitude: 1337.0; Invalid latitude: 1337.0`
* `401 FORBIDDEN` if user is not authenticated

### Get last inputs
```http
GET /api/latest
```

Optional parameters:
* count: required count of inputs. Default value is 10.
* filter: a substring of location names to be filtered. 

Expected response: 
* `200 OK` and a JSON array of not more than `count` last entries (less, if not enough in database), containing:
  * `id` their ID in database;
  * `temperature` temperature;
  * `lat` latitude;
  * `lon` longitude;
  * `datetime` timestamp that was specified in `/add` request or a moment of adding it to database.
  * `location` city and country, retrieved by coordinates; or "Empty space", if is not in a country (e.g. oceans). 
  Example: 
  ``` JSON
  [
    {
        "id": 3,
        "temperature": 10.0,
        "lat": 10.0,
        "lon": 40.0,
        "datetime": "2019-11-21T15:04:02.888009"
        "location": "Saint-Petersburg, Russia"
    }, ...
  ]
  ```
* `401 UNAUTHORIZED` if user is not authenticated
* `403 FORBIDDEN` if user is not authorized

## Endpoints for testing purposes

These endpoints are not specified in task description, but were added for convenience during manual testing and for 
demonstration purposes.
There are also sensitive commands, such as `clear`, and this API is not secured from access, as it is not meant to be 
in production. It is also why I didn't cover this API by any tests.  

### Populate database

```http
GET /test/populate
```

Optional parameters:
* `count`: amount of entries to be added. Default value is 20.

Response:
* JSON containing list of all generated inputs.

The generated entries contain some semi-random values as temperature, longitude and latitude, and datetime is a timestamp of when 
this entry was created. 
UPD: coordinates are in surroundings of Saint-Petersburg, so there is no too much requests to geocode API.

### Clear database

```http
GET /test/clear
```

Removes all entries from database.
Response: 
```json
{
  "success": true,
  "message": "Cleared."
}
```

## Basic UI

**There are currently several issues:**      
* No datetime input for add operation, however API supports it.  
* Design is ugly.  

```http
GET /
```

Some sort of cabinet for a user. It contains several elements:  
* Menu:
  Contains just a single Logout button. Nice and simple.
* Admin zone:  
  Available for role ADMIN only. Contains two buttons: to populate DB with example data (entered into a field nearby), and 
  to clear the DB.
* User zone:
  Available for role USER. Contains table, filled by 10 latest entries. 
  Controls are: Reload button, Filter and Count fields.
* Sensor zone:
  Fields for temperature, latitude and longitude. A button to send it to server. 

## Geocode

Retrieving of location is implemented using Google API.  
Locations are retrieved during Add operation, and result is stored in database.  
After retrieving Bounds information it is also stored in database, so we get information 
for each city just once. If some locations intersect (e.g. SPb and its Oblast), I choose the 
smallest location by its area, as most specific.

Google API key is set in `src/main/resources/application-geocode.yaml` like this:

```yaml
geocode:
  apikey: # Google API key
```

UPD: only after implementing the whole thing I realised that I could have been doing it
in reverse way: by getting Bounds for some given city, and comparing all values in database
to them.  
But as soon as I get information from Geocode API just once for each city, I consider it to
be good enough in terms of performance and cost (price per request).

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

## Database population
For database population there is a `/test/populate?count=X` request, which generates random values.  
There is also `src/test/populate.sh` shell script file, which contains curl requests.  

## Testing 

### Unit-tests
I couldn't find any functionality which is not covered completely by integration tests.   
I do understand advantages of unit-tests, and I know, how to write them, but in this example I don't see, where they 
could be even a little bit useful. 

