m2e connector for the Maven Dependency Plugin
=============================================

This m2e connector for the Maven Dependency Plugin adds a lifecycle mapping for the
[Unpack Mojo](http://maven.apache.org/plugins/maven-dependency-plugin/unpack-mojo.html).  It will be executed during
full and incremental builds, and configured outputDirectory(s) will then be refreshed.

[![Build Status](https://buildhive.cloudbees.com/job/ianbrandt/job/m2e-maven-dependency-plugin/badge/icon)](https://buildhive.cloudbees.com/job/ianbrandt/job/m2e-maven-dependency-plugin/)

## FAQ ##

### How do I use it? ###

First off, note that this is currently Beta code.  It has been minimally tested, and all the usual early adopter
warnings apply.  That said if you're willing to help test the connector all you have to do is:

1. Add the following
[update site](http://help.eclipse.org/juno/topic/org.eclipse.platform.doc.user/tasks/tasks-127.htm?cp=0_3_15_5):

    http://ianbrandt.github.io/m2e-maven-dependency-plugin/
1. Install it into Eclipse like any other
[new feature](http://help.eclipse.org/juno/topic/org.eclipse.platform.doc.user/tasks/tasks-124.htm?cp=0_3_15_1).
1. Remove any [lifecycle mapping metadata](http://wiki.eclipse.org/M2E_plugin_execution_not_covered#ignore_plugin_goal)
you might have had in your POMs for the dependency:unpack goal.

That's it!  The connector will run on full and incremental builds, leaving it to the Maven Dependency Plugin's
[overwrite rules](http://maven.apache.org/plugins/maven-dependency-plugin/usage.html#aOverwrite_Rules) to unpack the
artifacts when appropriate.

### Are there any performance tips I should be aware of? ###

* Be mindful of your overwrite rules.  Forcing an unpack for every IDE build could slow things down quite a bit.
* If you unpack into an `outputDirectory` that generally also contains a lot of other resources, the workspace refresh
could take longer than you'd want.  Try to unpack artifacts into distinct output directories.

### How can I help the project? ###

Thanks for asking...

* If you're a dependency:unpack user:
	* Test this out.  [File an issue](https://github.com/ianbrandt/m2e-maven-dependency-plugin/issues) if it doesn't
	work for you.  File an issue if you think it should do something more, or something different.
* If you're a user of any other Maven Dependency Plugin goal:
	* File an issue to request mappings for additional goals.  Support for copy, copy-dependencies, and
	unpack-dependencies is currently planned to be similar to that of unpack.  Perhaps you have different needs for
	those goals?  Be as specific as possible in describing the behavior you would expect.  If you think the behavior
	might warrant configuration, either via files or a UI, share your vision for this as well.
* If you're a Tycho expert:
	* Skim the POMs.  File an issue or submit a pull request if there is something that could be done better.
* If you're an Eclipse Plugin or m2e expert:
	* Skim the source and suggest improvements via issues or pull requests.
	* Contribute new test cases.
* If you're a representative of the Eclipse Foundation or Apache Software Foundation or similar:
	* I'd be happy to consider donating this plugin&mdash;do get in touch.

### Why not just make the Maven Dependency Plugin itself m2e compatible? ###

Connector-less [m2e compatibility](http://wiki.eclipse.org/M2E_compatible_maven_plugins) may very well be the end goal
for this project.  First this connector's behavior should be deemed universally desirable, and not in need of any
configuration.  As it stands if you find the behavior of this connector unsuitable you're free to uninstall it, or
swap it out for another solution.  That wouldn't be the case if the behavior was baked into the Maven Dependency Plugin
proper.

## Thanks ##

Many thanks to [Fred Bricon](https://community.jboss.org/people/fbricon "Fred Bricon at JBoss") for his help on IRC;
his work on [m2e](http://www.eclipse.org/m2e/) and [m2e-wtp](http://www.eclipse.org/m2e-wtp/); and his
[m2e-wro4j connector](https://github.com/jbosstools/m2e-wro4j), which I cribbed from heavily to create this plugin.
