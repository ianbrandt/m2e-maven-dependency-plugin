package com.ianbrandt.m2e.mdp.tests;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.m2e.tests.common.AbstractMavenProjectTestCase;

@SuppressWarnings("restriction")
public class M2eMdpConnectorTest extends AbstractMavenProjectTestCase {

	public void testM2eMdpForSmoke() throws Exception {

		IProject project = importProject("projects/smoketest/pom.xml");

		waitForJobsToComplete();

		project.build(IncrementalProjectBuilder.AUTO_BUILD, monitor);

		waitForJobsToComplete();

		IFile testResource = project
				.getFile("target/generated-resources/plugin.xml");

		assertTrue(testResource.getName() + " is missing", testResource.exists());
	}
}
