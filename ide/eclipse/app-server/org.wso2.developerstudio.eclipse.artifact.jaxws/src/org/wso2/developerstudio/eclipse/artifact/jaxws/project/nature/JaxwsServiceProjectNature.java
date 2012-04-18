/*
 * Copyright (c) 2011, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.developerstudio.eclipse.artifact.jaxws.project.nature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Repository;
import org.apache.maven.model.RepositoryPolicy;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.runtime.CoreException;
import org.wso2.developerstudio.eclipse.artifact.jaxws.Activator;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.maven.util.MavenUtils;
import org.wso2.developerstudio.eclipse.maven.util.ProjectDependencyConstants;
import org.wso2.developerstudio.eclipse.platform.core.nature.AbstractWSO2ProjectNature;
import org.wso2.developerstudio.eclipse.utils.jdt.JavaLibraryBean;
import org.wso2.developerstudio.eclipse.utils.jdt.JavaLibraryUtil;

public class JaxwsServiceProjectNature extends AbstractWSO2ProjectNature {
	private static IDeveloperStudioLog log=Logger.getLog(Activator.PLUGIN_ID);
	
	public void configure() throws CoreException {
		try {
			setupAsJaxwsService();
			updatePom();
		} catch (Exception e) {
			log.error("Cannot configure JAX-WS project",e);
		}
	}

	public void updatePom() throws Exception {
		File mavenProjectPomLocation = getProject().getFile("pom.xml").getLocation().toFile();
		MavenProject mavenProject = MavenUtils.getMavenProject(mavenProjectPomLocation);
		
		addMavenCompilerPlugin(mavenProject);
		addMavenWarPlugin(mavenProject);
		
		Properties properties = mavenProject.getModel().getProperties();
		
		properties.put("CApp.type", "webapp/jaxws");
		
		mavenProject.getModel().setProperties(properties);
		
		Repository repo = new Repository();
		repo.setUrl("http://maven.wso2.org/nexus/content/groups/wso2-public/");
		repo.setId("wso2-nexus");
		
		RepositoryPolicy releasePolicy=new RepositoryPolicy();
		releasePolicy.setEnabled(true);
		releasePolicy.setUpdatePolicy("daily");
		releasePolicy.setChecksumPolicy("ignore");
		
		repo.setReleases(releasePolicy);
		
		if (!mavenProject.getRepositories().contains(repo)) {
	        mavenProject.getModel().addRepository(repo);
        }
		if (!mavenProject.getPluginArtifactRepositories().contains(repo)) {
	        mavenProject.getModel().addPluginRepository(repo);
        }
		List<Dependency> dependencyList = new ArrayList<Dependency>();
		
		Map<String, JavaLibraryBean> dependencyInfoMap = JavaLibraryUtil.getDependencyInfoMap(getProject());
		Map<String, String> map = ProjectDependencyConstants.dependencyMap;
		for (JavaLibraryBean bean : dependencyInfoMap.values()) {
			if (bean.getVersion().contains("${")){
				for(String path: map.keySet()) {
					bean.setVersion(bean.getVersion().replace(path, map.get(path)));
				}
			}
			Dependency dependency = new Dependency();
			dependency.setArtifactId(bean.getArtifactId());
			dependency.setGroupId(bean.getGroupId());
			dependency.setVersion(bean.getVersion());
			dependencyList.add(dependency);
		}
		MavenUtils.addMavenDependency(mavenProject, dependencyList);
		MavenUtils.saveMavenProject(mavenProject, mavenProjectPomLocation);
		
	}

	private void setupAsJaxwsService() throws CoreException, IOException {
			
	}

	
	public void deconfigure() throws CoreException {
		
		
	}
	
	private void addMavenCompilerPlugin(MavenProject mavenProject) {
		List<Plugin> plugins = mavenProject.getBuild().getPlugins();
		for (Plugin plg : plugins) {
			if (plg.getId().equals("maven-compiler-plugin")) {
				return;
			}
		}
		
		Plugin plugin = MavenUtils.createPluginEntry(mavenProject, "org.apache.maven.plugins",
				"maven-compiler-plugin", "2.3.2", false);
		Xpp3Dom createMainConfigurationNode = MavenUtils.createMainConfigurationNode(plugin);
		Xpp3Dom createSourceNode = MavenUtils.createXpp3Node(createMainConfigurationNode, "source");
		createSourceNode.setValue("1.5");
		Xpp3Dom createTargetNode = MavenUtils.createXpp3Node(createMainConfigurationNode, "target");
		createTargetNode.setValue("1.5");
	}
	
	private void addMavenWarPlugin(MavenProject mavenProject) {
		List<Plugin> plugins = mavenProject.getBuild().getPlugins();
		for (Plugin plg : plugins) {
			if (plg.getId().equals("maven-war-plugin")) {
				return;
			}
		}
		
		Plugin plugin = MavenUtils.createPluginEntry(mavenProject, "org.apache.maven.plugins",
				"maven-war-plugin", "2.2", false);
		Xpp3Dom createMainConfigurationNode = MavenUtils.createMainConfigurationNode(plugin);
		Xpp3Dom createWebXmlNode = MavenUtils.createXpp3Node(createMainConfigurationNode, "webXml");
		createWebXmlNode.setValue("${basedir}/src/main/webapp/WEB-INF/web.xml");
		Xpp3Dom createExcludesNode = MavenUtils.createXpp3Node(createMainConfigurationNode,
				"packagingExcludes");
		createExcludesNode.setValue("WEB-INF/lib/*.jar");
		Xpp3Dom createWarNameNode = MavenUtils.createXpp3Node(createMainConfigurationNode,
				"warName");
		createWarNameNode.setValue("${project.artifactId}");
	}

}
