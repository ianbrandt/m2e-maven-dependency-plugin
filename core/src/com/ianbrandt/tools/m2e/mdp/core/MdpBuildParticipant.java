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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
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

		final File globalOutputDirectory = maven.getMojoParameterValue(getSession(), mojoExecution,
				OUTPUT_DIRECTORY_PROPERTY, File.class);

		final List<?> artifactItems = maven.getMojoParameterValue(getSession(), mojoExecution, ARTIFACT_ITEMS_PROPERTY,
				List.class);

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
