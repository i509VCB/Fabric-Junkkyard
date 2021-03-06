plugins {
	id("fabric-loom") version "0.4-SNAPSHOT"
	id("maven-publish")
}

configure<JavaPluginConvention> {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

val archivesBaseName: String by project
val version: String by project
val mavenGroup: String by project
val minecraft_version: String by project
val yarn_build: String by project
val loader_version: String by project
val fabric_version: String by project

repositories {
}

dependencies {
	minecraft("com.mojang:minecraft:$minecraft_version")
	mappings("net.fabricmc:yarn:$minecraft_version+build.$yarn_build:v2")
	modCompile("net.fabricmc:fabric-loader:$loader_version")

	modCompile("net.fabricmc.fabric-api:fabric-api:$fabric_version")

	implementation("org.checkerframework:checker-qual:3.0.1")
}

/*tasks.processResources {
	inputs.property "version", project.version

	from(sourceSets.main.resources.srcDirs) {
		include("fabric.mod.json")
		expand("version" to project.version)
	}

	from(sourceSets.main.resources.srcDirs) {
		exclude("fabric.mod.json")
	}
}*/
