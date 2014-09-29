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
package com.ianbrandt.tools.m2e.mdp.core;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.eclipse.osgi.util.NLS;
import org.sonatype.plexus.build.incremental.BuildContext;

public class MdpBuildParticipant extends MojoExecutionBuildParticipant {

	private static final String ARTIFACT_ITEMS_PROPERTY = "artifactItems";

	private static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";

	public MdpBuildParticipant(MojoExecution execution) {

		super(execution, true);
	}

	@Override
	public Set<IProject> build(final int kind, final IProgressMonitor monitor) throws Exception {

		final IMaven maven = MavenPlugin.getMaven();
		final MojoExecution mojoExecution = getMojoExecution();
		final BuildContext buildContext = getBuildContext();

		if (mojoExecution == null) {
			return null;
		}

		final IFile pomFile = (IFile) getMavenProjectFacade().getProject().findMember("pom.xml");
		
		// skipping the build if not a Full Build or if pom.xml has not changed
		if(kind != IncrementalProjectBuilder.FULL_BUILD && !buildContext.hasDelta(pomFile.getLocation().toFile())) {
			return null;
		}
		
		setTaskName(monitor);

		final Set<IProject> result = executeMojo(kind, monitor);

		final Set<File> outputDirectories = getOutputDirectories(maven, mojoExecution);

		for (File outputDirectory : outputDirectories) {

			refreshOutputDirectory(buildContext, outputDirectory);
		}

		return result;
	}

	private Set<File> getOutputDirectories(final IMaven maven, final MojoExecution mojoExecution) throws CoreException,
			IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		final Set<File> outputDirectories = new HashSet<File>();
		final MavenProject mavenProject = getMavenProjectFacade().getMavenProject();

		final File globalOutputDirectory = maven.getMojoParameterValue(mavenProject, mojoExecution,
				OUTPUT_DIRECTORY_PROPERTY, File.class, new NullProgressMonitor());

		final List<?> artifactItems = maven.getMojoParameterValue(mavenProject, mojoExecution, ARTIFACT_ITEMS_PROPERTY,
				List.class, new NullProgressMonitor());

		Method getOutputDirectoryMethod = null;

		for (Object artifactItem : artifactItems) {

			if (getOutputDirectoryMethod == null) {

				getOutputDirectoryMethod = new PropertyDescriptor(OUTPUT_DIRECTORY_PROPERTY, artifactItem.getClass())
						.getReadMethod();
			}

			File artifactItemOutputDirectory = (File) getOutputDirectoryMethod.invoke(artifactItem);

			if (artifactItemOutputDirectory != null) {

				outputDirectories.add(artifactItemOutputDirectory);

			} else {

				outputDirectories.add(globalOutputDirectory);
			}
		}

		if (outputDirectories.size() == 0) {

			outputDirectories.add(globalOutputDirectory);
		}

		return outputDirectories;
	}

	private void setTaskName(IProgressMonitor monitor) {

		if (monitor != null) {

			final String taskName = NLS.bind("Invoking {0} on {1}", getMojoExecution().getMojoDescriptor()
					.getFullGoalName(), getMavenProjectFacade().getProject().getName());

			monitor.setTaskName(taskName);
		}
	}

	private Set<IProject> executeMojo(final int kind, final IProgressMonitor monitor) throws Exception {
		return super.build(kind, monitor);
	}

	private void refreshOutputDirectory(final BuildContext buildContext, final File outputDirectory) {

		if (outputDirectory != null && outputDirectory.exists()) {

			buildContext.refresh(outputDirectory);
		}
	}
}
