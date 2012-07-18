M2E connector for the Maven Dependency Plugin
=============================================

This m2e connector for the Maven Dependency Plugin adds a lifecycle mapping for the
[Unpack Mojo](http://maven.apache.org/plugins/maven-dependency-plugin/unpack-mojo.html).  It will be executed during
incremental builds, and configured outputDirectory(s) will then be refreshed.

## FAQ ##

### How do I use it? ###

_Coming soon..._

> I need to sort out how to regularly publish this to a P2 repository somewhere.  Once I've done that
you'll be able to install this into Eclipse via an update site.  Early adopters could clone the project, build it with
Maven, and install it from "./com.ianbrandt.m2e.mdp.site/target/site/" as a local update site.

### How can I help the project? ###

Thanks for asking...

* If you're a dependency:unpack user:
	* Test this out.  [File an issue](https://github.com/ianbrandt/m2e-maven-dependency-plugin/issues) if it doesn't
	work for you.  File an issue if you think it should do something more or something different.
* If you're a user of any other Maven Dependency Plugin goal:
	* File an issue to request mappings for additional goals.  Be as specific as possible in describing the behavior
	you would expect.  If you think the behavior might warrant configuration, either via files or a UI, share your
	vision for this as well.
* If you're a Tycho expert:
	* Skim the POMs.  File an issue or submit a pull request if there is something that could be done better.
* If you're an Eclipse Plugin or M2E expert:
	* Skim the source and suggest improvements via issues or pull requests.
	* Contribute new test cases.
* If you're a representative of the Eclipse Foundation or Apache Software Foundation or similar:
	* I'd be happy to consider donating this plugin--do get in touch.

## Thanks ##

Many thanks to [Fred Bricon](https://community.jboss.org/people/fbricon "Fred Bricon at JBoss") for his help on IRC;
his work on [m2e](http://www.eclipse.org/m2e/) and [m2e-wtp](http://www.eclipse.org/m2e-wtp/); and his
[m2e-wro4j connector](https://github.com/jbosstools/m2e-wro4j), which I cribbed from heavily to create this plugin.
