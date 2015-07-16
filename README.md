# ScalatraEmail #

## Build & Run ##

This service integrates with mandrill and mailgun. First setup accounts and get api keys for calling these services
using the API. Then add a file with your config settings ( look at src/main/resources/reference.conf for the keys)
and assign your file location to fileLocation variable in ScalatraBootstrap class
```sh
$ cd scalatraemail
$ ./sbt
> container:start
```

You can call the service as follows:
```sh
$ curl -m 3600 -isb -X POST -H "Content-Type: application/json" -d '{"person": {"firstName":"xyz","lastName":"xyz", "emailAddress":"abc@ced.com"}, "emailType": "Birthday"}' http://localhost:8080/emails
```

Replace Birthday with Holidays for holidays email
