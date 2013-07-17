/*
 * Copyright 2012 Decebal Suiu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
 * the License. You may obtain a copy of the License in the LICENSE file, or at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package ro.fortsoft.wicket.plugin;

import java.io.File;
import java.net.URL;

import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

import ro.fortsoft.pf4j.PluginWrapper;

/**
 * @author Decebal Suiu
 */
public class PluginResource extends ResourceStreamResource {

	private static final long serialVersionUID = 1L;
	
	private PluginWrapper plugin;
	private String name;
	private File resourceFile;
	
	public PluginResource(PluginWrapper plugin, String name) {
		super();
		
		this.plugin = plugin;
		this.name = name;
		
		File pluginPath = new File(System.getProperty("pf4j.pluginsDir"), plugin.getPluginPath());
		resourceFile = new File(pluginPath, name);
//		System.out.println("resourceFile = " + resourceFile);
	}

	@Override
	protected IResourceStream getResourceStream() {
//		System.out.println("[[[ " + name);
		URL resourceUrl = plugin.getPlugin().getClass().getResource(name);
//		System.out.println("resourceUrl = " + resourceUrl);
		if (resourceUrl != null) {
			return new UrlResourceStream(resourceUrl);
		} else {
			return new FileResourceStream(resourceFile);
		}
	}

	public String getName() {
		return name;
	}

	public File getResourceFile() {
		return resourceFile;
	}
	
}
