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

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.apache.wicket.request.resource.IResource;

import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 * @author Decebal Suiu
 */
public class WicketPlugin extends Plugin implements IInitializer  {

	public WicketPlugin(PluginWrapper wrapper) {
		super(wrapper);
	}

	@Override
	public void init(Application application) {
	}

	@Override
	public void destroy(Application application) {
	}
	
	public final String getPluginResourceUrl(String name) {
		return PluginUtils.getPluginResourceUrl(getWrapper(), name);
	}
	
	public final IResource getPluginResource(String name) {
		return PluginUtils.getPluginResource(getWrapper(), name);
	}

}
