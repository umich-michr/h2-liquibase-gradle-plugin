package edu.umich.med.michr.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.liquibase.gradle.LiquibasePlugin

class H2LiquibasePlugin implements Plugin<Project> {
	static final String H2_CONFIGURATION_NAME = 'h2'
	static final String LIQUIBASE_CONFIGURATION_NAME = 'liquibase'
	
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
		StopH2Task stopH2 = project.task('stopH2', type: StopH2Task)
		
		startH2.description = 'Starts an embedded h2 database.'
		startH2.group = H2_CONFIGURATION_NAME
		
		stopH2.description = 'Stops an embedded h2 database.'
		stopH2.group = H2_CONFIGURATION_NAME
	}
}