---
id: contribute-building
title: Building the code
---

## Using Maven

To build from source, you need the following installed and available in your `$PATH:`

* [Java 11](https://openjdk.java.net/projects/jdk/11/)

* [Apache maven 3.3.6 or greater](https://maven.apache.org/)

After cloning the project, you can build it from source with this command:

```bash
mvn clean install
```

If you don't have maven installed, you may directly use the included [maven wrapper](https://github.com/takari/maven-wrapper), and build with the command:

```bash
./mvnw clean install
```

