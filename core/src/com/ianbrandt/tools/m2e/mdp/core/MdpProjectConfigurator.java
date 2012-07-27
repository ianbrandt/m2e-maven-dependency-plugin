package com.ianbrandt.tools.m2e.mdp.core;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;

public class MdpProjectConfigurator extends AbstractProjectConfigurator {

	@Override
	public AbstractBuildParticipant getBuildParticipant(IMavenProjectFacade projectFacade, MojoExecution execution,
			IPluginExecutionMetadata executionMetadata) {

		return new MdpBuildParticipant(execution);
	}

	@Override
	public void configure(ProjectConfigurationRequest projectConfigurationRequest, IProgressMonitor iProgressMonitor)
			throws CoreException {
		// Nothing to configure
	}

}
