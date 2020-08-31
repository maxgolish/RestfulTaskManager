> ### Project for Javaica Sect
# RESTful Task Manager API

The application uses:
* Spring Boot
* Spring JPA 
* H2 Database

Simple task manager to save and return your tasks 

# How to use it

Run Spring Boot:

    ./gradlew bootRun

It has 3 preloaded tasks for an example<br>


    http://localhost:8080/tasks/
    
    
Examples of requests:

    $ curl localhost:8080/tasks/
    $ curl -v -X POST localhost:8080/tasks -H 'Content-Type:application/json' -d '{"description": "Add some tasks"}'
    $ curl -X POST localhost:8080/tasks -H 'Content-type:application/json' -d '{"description": "Create API", "progress":"DONE}'
    
To complete the task:

    $ curl -v -X PUT localhost:8080/tasks/4/complete

To show completed or current tasks:

    $ curl localhost:8080/tasks/done
    $ curl localhost:8080/tasks/current
    
To delete the task:

    $ curl -X localhost:8080/tasks/1