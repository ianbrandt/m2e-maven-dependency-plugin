package com.ianbrandt.tools.m2e.mdp.tests;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2e.tests.common.AbstractMavenProjectTestCase;

@SuppressWarnings("restriction")
public class M2eMdpConnectorTest extends AbstractMavenProjectTestCase {

	public void testShouldNotUnpackDependency() throws Exception {
		// given
		final IProject project = importProject("projects/smoketest/pom.xml");
		waitForJobsToComplete();
		project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
		waitForJobsToComplete();
		// make sure target/generated-resources is empty
		final IResource targetResourcesFolder = project.findMember(new Path("target").append("generated-resources"));
		if(targetResourcesFolder != null) {
			targetResourcesFolder.delete(true, new NullProgressMonitor());
		}
		// when further auto build is performed
		project.build(IncrementalProjectBuilder.AUTO_BUILD, monitor);
		waitForJobsToComplete();
		// then
		final IFile testResource = project
				.getFile("target/generated-resources/LICENSE.txt");
		assertFalse(testResource.getName() + " is missing", testResource.exists());
	}

	public void testShouldUnpackDependency() throws Exception {
		// given
		final IProject project = importProject("projects/smoketest/pom.xml");
		waitForJobsToComplete();
		// when
		project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
		waitForJobsToComplete();
		// then
		final IFile testResource = project
				.getFile("target/generated-resources/LICENSE.txt");
		assertTrue(testResource.getName() + " is missing", testResource.exists());
	}
}
