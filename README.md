# Gradle H2-Liquibase Plugin

This plugin lests you start an [H2 database](http://www.h2database.com) during gradle build cycle and then run [Liquibase](http://www.liquibase.org/) changesets on it to create schema and deve data.

Inspired by [James Carr](https://github.com/jamescarr)'s elegant and simple [h2-gradle-plugin](https://github.com/jamescarr/h2-gradle-plugin)

## Use Case

Need to run a web application that needs a relational database. The database migrations are already managed by liquibase. Use this plugin to start h2 in the background then run your changesets then bring you application up pointing to the embedded h2 database running on the background.

## Configuration

This plugin is not publicly published. To use the h2 plugin, you either need to publish the plugin to your own private repository until we publish it or you need to checkout this project, build it with running "gradle clean build" and then you can use the configuration below:

```groovy
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        //If you have your own private repo then you can specify it like below
        /*
        maven {
            url "https://<my nexus repo host>/nexus/content/groups/gradle-plugins/"
        }
        */
    }
    dependencies {
    	//The latest liquibase-gradle plugin version tested was 1.2.4
        classpath "org.liquibase:liquibase-gradle-plugin:$liquibaseVersion"
        //The latest h2 version tested was 1.4.193
        classpath group: 'com.h2database', name:'h2', version:"$h2Version"
        //If you used your own repo to host the plugin, you can use the line below
        //classpath group: 'edu.umich.med.michr.gradle', name: 'h2-liquibase-plugin',version: '1.0.0'
        classpath files('<The path to the plugin project after you checked out>/build/libs/h2-liquibase-plugin-1.0.0.jar')
    }
}
apply plugin: 'edu.umich.med.michr.gradle.h2-liquibase-gradle-plugin'
```
### h2 configuration'

There are only two ports you need to specify in your build.gradle for h2 to run. If you do not specify liquibase config then h2 will be started alone.

Under the hood this plugin is using [liquibase-gradle-plugin](https://github.com/liquibase/liquibase-gradle-plugin), refer to its documentation for further configuration options.

```groovy

h2 {
	tcpPort = 9092
	webPort = 8082
}

liquibase {
    activities {
        main {
            changeLogFile 'src/main/resources/liquibase/changelog/db.changelog-master.xml'
            driver 'org.h2.Driver'
            url 'jdbc:h2:mem:umcs;DB_CLOSE_DELAY=-1;TRACE_LEVEL_FILE=1;MODE=Oracle'
            username 'myappuser'
            password 'myappuserpassword'
            contexts 'schema,schema_staging,seed_data,dev_data'
            defaultSchemaName 'PUBLIC'
            logLevel 'info'
        }
    }
}

```

