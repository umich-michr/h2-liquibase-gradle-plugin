package edu.umich.med.michr.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.h2.tools.Server
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

class StopH2Task extends DefaultTask {
    static final Logger LOGGER = Logging.getLogger(StopH2Task.class)
    @TaskAction
    void stopServer(){
        LOGGER.debug("Stopping h2 database at port: ${project.h2.tcpPort} and web server at port: ${project.h2.webPort}");
        Server.main("-tcpShutdown","tcp://localhost:${project.h2.tcpPort}")
    }
}
