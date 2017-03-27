package edu.umich.med.michr.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.h2.tools.Server

public class StartH2Task extends DefaultTask{
	static final Logger LOGGER = Logging.getLogger(StartH2Task.class)
	
	@TaskAction
	void start(){
        LOGGER.debug("Starting h2 database at port: ${project.h2.tcpPort} and web server at port: ${project.h2.webPort}");
        Server.main("-tcp", "-web", "-tcpPort", "${project.h2.tcpPort}", "-webPort", "${project.h2.webPort}")
        if(project.liquibase){
           liquibaseUpdate()
	    }
    }
    
    private void liquibaseUpdate(){
		   String taskName = 'liquibaseUpdate'
		   if(project.hasProperty('liquibaseTaskPrefix')){
		   		taskName = project.liquibaseTaskPrefix+'Update'
		   }
		   //This is to make sure unit test will work because it was found that liquibasePlugin was not usinng liquibase prefix in front of task names by default when run in unit tests
		   if(!project.hasProperty(taskName)){
		   		taskName = 'update'
		   }
    	   project.tasks[taskName].execute() 
    }
}
