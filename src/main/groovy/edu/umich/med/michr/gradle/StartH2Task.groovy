package edu.umich.med.michr.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.TaskAction
import org.h2.tools.Server

public class StartH2Task extends DefaultTask{
	static final Logger LOGGER = Logging.getLogger(StartH2Task.class)
	
	@TaskAction
	void start(){
        LOGGER.debug("Starting h2 database at port: ${project.h2.tcpPort} and web server at port: ${project.h2.webPort}");
        Server.main("-tcp", "-tcpAllowOthers", "-web", "-webAllowOthers", "-ifNotExists", "-tcpPort", "${project.h2.tcpPort}", "-webPort", "${project.h2.webPort}")
    }
}
