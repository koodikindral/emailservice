# Introduction

E-Mail template configuration, translation and parser service. 

Ready to go microservice, using:
- Gradle for build process
- PostgreSQL as persistence layer
- Dagger for dependency injection
- Rosetta for JDBI <-> Jackson mapping
- Docker for deployment

# How to start

```bash
# clone application from Github
git clone --depth 1 https://github.com/koodikindral/emailservice.git

# Running the application
nohup java -jar email-<version>.jar server configuration.yml > serverlog.log &
```
# Used technologies & tools

- [Dropwizard](http://www.dropwizard.io/1.0.2/docs/)
- [Rosetta JDBI Mapper](https://github.com/HubSpot/Rosetta)
- [Dagger](https://github.com/google/dagger)

# License

MIT
