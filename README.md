# ScalatraEmail #

## Build & Run ##

First add a file with your config settings ( look at src/main/resources/reference.conf for the keys) and then assign the
location to fileLocation variable in ScalatraBootstrap class
```sh
$ cd ScalatraEmail
$ ./sbt
> container:start
```

Then call the service as follows:
```sh
$ curl -m 3600 -isb -X POST -H "Content-Type: application/json" -d '{"person": {"firstName":"xyz","lastName":"xyz", "emailAddress":"abc@ced.com"}, "emailType": "Birthday"}' http://localhost:8080/emails
```

Replace Birthday with Holidays for holidays email