package edu.umich.med.michr.gradle

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import static org.gradle.testkit.runner.TaskOutcome.*;

class H2PluginTest {
	
	def PLUGIN_ID = 'edu.umich.med.michr.gradle.h2-liquibase-gradle-plugin'
	
	@Rule 
	public final TemporaryFolder testProjectDir = new TemporaryFolder();
	private File buildFile;

	@Before
	public void setup() throws IOException {
		buildFile = testProjectDir.newFile("build.gradle");
	}
	
    @Test
    public void pluginAddsStartH2TaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply PLUGIN_ID

		assertTrue(project.tasks.startH2 instanceof StartH2Task)
		assertTrue(project.tasks.stopH2 instanceof StopH2Task)

        assertNotNull(project.tasks.each{it.name == 'update' && it.group=='Liquibase'})
	}
	
    @Test
    public void test(){
        String buildFileContent = "plugins { \n"+
			"    id '"+PLUGIN_ID+"'\n"+
			"}\n"+
			"h2 {\n"+
    		"    tcpPort 9095\n"+
    		"    webPort 8085\n"+
			"}\n"+
			"liquibase {\n"+
			"    activities {\n"+
			"        main {\n"+
			"            changeLogFile 'test.xml'\n"+
			"        }\n"+
		    "    }\n"+
			"}\n"
		writeFile(buildFile, buildFileContent)
		
		BuildResult result = GradleRunner.create()
		            .withProjectDir(testProjectDir.getRoot())
		            .withArguments("startH2","stopH2")
					.withPluginClasspath()
		            .build();

		//result.tasks.find{println it.path}
		assertEquals(result.task(":startH2").getOutcome(), SUCCESS);
		assertEquals(result.task(":stopH2").getOutcome(), SUCCESS);
	}
	
	private void writeFile(File destination, String content) throws IOException {
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(destination));
			output.write(content);
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}
}
