# Task

**Write a service for sensor data input.**
Let's assume we have a sensor that sends temperature and location data (coordinates). Server validates the data and stores it. The user is able to get the list of last 10 inputs.

## Required:  
•	Using Spring Framework (v>=4) implement two endpoints (POST and GET), validation controller, database layer (repository).  
•	Validation shall include a check of the coordinates format and temperature interval.  
•	Provide REST API for the client to get json with the list of last 10 inputs  
•	Cover functionality with unit- and integration tests  
•	Provide scripts to populate DB (please, use in-memory DB)  

## Optional, not compulsory (none, any or all):   
•	Basic authorization for both client and sensor  
•	Extend the application by adding a feature to filter by city. The city should be retrieved automatically from the given coordinates  
•	Implement basic UI (use your favorite JS framework)  
You can describe, how would you do optional tasks in words in case it seems difficult or long for you  
