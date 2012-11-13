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

import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;

import ro.fortsoft.pf4j.PluginWrapper;

/**
 * @author Decebal Suiu
 */
public class PluginResourceRequestHandler implements IRequestHandler {

	private PluginWrapper plugin;
	private PageParameters pageParameters;
	private PluginResource resource;
	
	public PluginResourceRequestHandler(PluginWrapper plugin, String name, PageParameters pageParameters) {
		this.plugin = plugin;		
		this.resource = new PluginResource(plugin, name);
		this.pageParameters = (pageParameters != null) ? pageParameters : new PageParameters();
	}

	public PluginWrapper getPlugin() {
		return plugin;
	}

	public PageParameters getPageParameters() {
		return pageParameters;
	}

	public PluginResource getResource() {
		return resource;
	}

	@Override
	public void respond(IRequestCycle requestCycle) {
		IResource.Attributes a = new IResource.Attributes(requestCycle.getRequest(),
				requestCycle.getResponse(), pageParameters);
		resource.respond(a);
	}

	@Override
	public void detach(IRequestCycle requestCycle) {
	}

}
