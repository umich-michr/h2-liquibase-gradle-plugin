package edu.umich.med.michr.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.liquibase.gradle.LiquibasePlugin

class H2LiquibasePlugin implements Plugin<Project> {
	static final String H2_CONFIGURATION_NAME = 'h2'

    void apply(Project project) {
		project.plugins.apply(LiquibasePlugin.class)
		applyExtension(project)
		applyTasks(project)
    }

	void applyExtension(Project project) {
		project.extensions.create(H2_CONFIGURATION_NAME, H2PluginExtension)
	}

	void applyTasks(Project project) {
        StartH2Task startH2 = project.task('startH2', type: StartH2Task)
		if(project.liquibase){
			String taskName = 'liquibaseUpdate'
			if(project.hasProperty('liquibaseTaskPrefix')){
				taskName = project.liquibaseTaskPrefix+'Update'
			}
			//This is to make sure unit test will work because it was found that liquibasePlugin was not using liquibase prefix in front of task names by default when run in unit tests
			if(!project.hasProperty(taskName)){
				taskName = 'update'
			}
			startH2.finalizedBy(project.tasks[taskName])
		}

		StopH2Task stopH2 = project.task('stopH2', type: StopH2Task)
		
		startH2.description = 'Starts an embedded h2 database.'
		startH2.group = H2_CONFIGURATION_NAME
		
		stopH2.description = 'Stops an embedded h2 database.'
		stopH2.group = H2_CONFIGURATION_NAME
	}
}