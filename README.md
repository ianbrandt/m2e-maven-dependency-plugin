M2E connector for the Maven Dependency Plugin
=============================================

This m2e connector for the Maven Dependency Plugin adds a lifecycle mapping for the
[Unpack Mojo](http://maven.apache.org/plugins/maven-dependency-plugin/unpack-mojo.html).  It will be executed during
incremental builds, and the configured outputDirectory will then be refreshed.

Many thanks to [Fred Bricon](https://community.jboss.org/people/fbricon "Fred Bricon at JBoss") for his help on IRC;
his work on [m2e](http://www.eclipse.org/m2e/) and [m2e-wtp](http://www.eclipse.org/m2e-wtp/); and his
[m2e-wro4j connector](https://github.com/jbosstools/m2e-wro4j), which I cribbed from heavily to create this plugin.
