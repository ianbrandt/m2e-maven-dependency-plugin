package com.ianbrandt.m2e.mdp.core;

import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;

public class MdpBuildParticipant extends AbstractBuildParticipant {

	public MdpBuildParticipant(MojoExecution execution) {
	}

	@Override
	public Set<IProject> build(int arg0, IProgressMonitor arg1) throws Exception {

		return null;
	}
}
