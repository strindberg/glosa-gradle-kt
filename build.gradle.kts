import org.gradle.api.JavaVersion.VERSION_1_8

group = "se.jh"
version = "1.0-SNAPSHOT"

apply<ApplicationPlugin>()

configure<ApplicationPluginConvention> {
    mainClassName = "se.jh.glosa.fw.Glosa"
}

configure<JavaPluginConvention> {
     sourceCompatibility = VERSION_1_8
     targetCompatibility = VERSION_1_8
}

repositories {
    jcenter()
}

dependencies {
    testCompile("junit:junit:4.12")
}
