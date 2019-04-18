package edu.umich.med.michr.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS
import static org.junit.Assert.*

class H2PluginTest {
	
	def PLUGIN_ID = 'edu.umich.med.michr.gradle.h2-liquibase-gradle-plugin'
	def DB_CHANGELOG_FILE_NAME = 'db.changelog.xml'
	def BUILD_FILE_NAME = 'build.gradle'
	
	@Rule 
	public final TemporaryFolder testProjectDir = new TemporaryFolder();
	private File buildFile, changeLogFile;

	@Before
	public void setup() throws IOException {
		buildFile = testProjectDir.newFile(BUILD_FILE_NAME);
		changeLogFile = testProjectDir.newFile(DB_CHANGELOG_FILE_NAME);
	}
	
    @Test
    public void pluginAddsStartH2TaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply PLUGIN_ID
		project.pluginManager.apply 'liquibase'

		assertTrue(project.tasks.startH2 instanceof StartH2Task)
		assertTrue(project.tasks.stopH2 instanceof StopH2Task)

        assertNotNull(project.tasks.each{it.name == 'update' && it.group=='Liquibase'})
	}
	
    @Test
    public void test(){
		def buildFileSrc = new File(ClassLoader.getSystemResource(BUILD_FILE_NAME).getFile());
		def changeLogFileSrc = new File(ClassLoader.getSystemResource(DB_CHANGELOG_FILE_NAME).getFile());

		buildFile.write(buildFileSrc.text)
		changeLogFile.write(changeLogFileSrc.text)
		
		BuildResult result = GradleRunner.create()
		            .withProjectDir(testProjectDir.getRoot())
		            .withArguments("startH2","stopH2")
					.withPluginClasspath()
		            .build();

		//result.tasks.find{println it.path}
		assertEquals(result.task(":startH2").getOutcome(), SUCCESS);
		assertEquals(result.task(":stopH2").getOutcome(), SUCCESS);
	}
}
