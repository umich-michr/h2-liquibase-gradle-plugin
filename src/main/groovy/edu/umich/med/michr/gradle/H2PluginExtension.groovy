package edu.umich.med.michr.gradle

class H2PluginExtension {
    def tcpPort = 9092
    def webPort = 8082
	def H2PluginExtension(){}
	def H2PluginExtension(Closure c){
		c.resolveStrategy = Closure.DELEGATE_FIRST
		c.delegate = this
		c()
	}
}
