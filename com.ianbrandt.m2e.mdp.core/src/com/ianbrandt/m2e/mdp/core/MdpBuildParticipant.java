package com.ianbrandt.m2e.mdp.core;

import java.io.File;
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

	private static final String OUTPUT_DIRECTORY = "outputDirectory";

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

		final File outputDirectory = getOutputDirectory(maven, mojoExecution);

		refreshOutputDirectory(buildContext, outputDirectory);

		return result;
	}

	private File getOutputDirectory(final IMaven maven, final MojoExecution mojoExecution) throws CoreException {

		return maven.getMojoParameterValue(getSession(), mojoExecution, OUTPUT_DIRECTORY, File.class);
	}

	private void setTaskName(IProgressMonitor monitor) {

		if (monitor != null) {

			final String taskName = NLS.bind("Invoking {0} on {1}", getMojoExecution().getMojoDescriptor().getFullGoalName(),
					getMavenProjectFacade().getProject().getName());

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
