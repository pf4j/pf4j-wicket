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
package ro.fortsoft.wicket.plugin.demo;

import org.apache.wicket.protocol.http.WebApplication;

import ro.fortsoft.pf4j.PluginManager;
import ro.fortsoft.wicket.plugin.PluginManagerInitializer;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see ro.fortsoft.wicket.plugin.demo.Start#main(String[])
 * 
 * @author Decebal Suiu
 */
public class WicketApplication extends WebApplication {
	
	public static WicketApplication get() {
		return (WicketApplication) WebApplication.get();
	}
	
	@Override
	public void init() {
		super.init();
		
		// markup settings
		getMarkupSettings().setStripWicketTags(true);
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		
		// exception settings
		getResourceSettings().setThrowExceptionOnMissingResource(false);		
	}

	public Class<HomePage> getHomePage() {
		return HomePage.class;
	}

	public PluginManager getPluginManager() {
		return getMetaData(PluginManagerInitializer.PLUGIN_MANAGER_KEY);
	}

}
