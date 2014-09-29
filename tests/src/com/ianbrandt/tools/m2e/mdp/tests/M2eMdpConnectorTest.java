/*******************************************************************************
* Copyright (c) 2012 Ian Brandt
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Ian Brandt - initial API and implementation
*******************************************************************************/
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

	public void testShouldNotUnpackArtifact() throws Exception {
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

	public void testShouldUnpackArtifact() throws Exception {
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
	
	public void testShouldUnpackDependencies() throws Exception {
		// given
		final IProject project = importProject("projects/unpackdeps/pom.xml");
		waitForJobsToComplete();
		// when
		project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
		waitForJobsToComplete();
		// then
		final IFile junitResource = project
				.getFile("target/generated-resources/junit/framework/Assert.class");
		assertTrue(junitResource.getName() + " is missing", junitResource.exists());
		
		final IFile hamcrestResource = project
				.getFile("target/generated-resources/org/hamcrest/Matcher.class");
		assertTrue(hamcrestResource.getName() + " is missing", hamcrestResource.exists());
	}
	
	public void testShouldCopyDependencies() throws Exception {
		// given
		final IProject project = importProject("projects/copydeps/pom.xml");
		waitForJobsToComplete();
		// when
		project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
		waitForJobsToComplete();
		// then
		final IFile junitJar = project
				.getFile("target/generated-resources/junit-4.10.jar");
		assertTrue(junitJar.getName() + " is missing", junitJar.exists());
		final IFile hamcrestJar = project
				.getFile("target/generated-resources/hamcrest-core-1.1.jar");
		assertTrue(hamcrestJar.getName() + " is missing", hamcrestJar.exists());
	}
}
