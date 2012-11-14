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
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.protocol.http.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.PluginManager;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 * @author Decebal Suiu
 */
public class PluginManagerInitializer implements IInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(PluginManagerInitializer.class);
	
	@SuppressWarnings("serial")
	public static MetaDataKey<PluginManager> PLUGIN_MANAGER_KEY = new MetaDataKey<PluginManager>() {};
	
	private PluginManager pluginManager;
	
	@Override
	public void init(Application application) {
		pluginManager = createPluginManager(application);
        if (pluginManager == null) {
        	throw new RuntimeException("Plugin manager cannot be null");
        }
        
        // load plugins        
        pluginManager.loadPlugins();
        
        // init plugins
        List<PluginWrapper> resolvedPlugins = pluginManager.getResolvedPlugins();
		for (PluginWrapper plugin : resolvedPlugins) {
			if (plugin.getPlugin() instanceof WicketPlugin) {
				((WicketPlugin) plugin.getPlugin()).init(application);
			}
		}

		// start plugins
        pluginManager.startPlugins();        
        
        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
        LOG.debug("startedPlugins = " + startedPlugins);        
        
        // mount resources for each started plugin
        for (PluginWrapper plugin : startedPlugins) {
        	((WebApplication) application).mount(new PluginResourceMapper(plugin));
        }
        
        // store plugin manager in application
        application.setMetaData(PLUGIN_MANAGER_KEY, pluginManager);
        
        // add PluginComponentInjector
        application.getComponentInstantiationListeners().add(new PluginComponentInjector(application));
	}

	@Override
	public void destroy(Application application) {
		List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
		Collections.reverse(startedPlugins);

		// stop plugins
		pluginManager.stopPlugins();
		
		// destroy started plugins in reverse order
		for (PluginWrapper plugin : startedPlugins) {
			if (plugin.getPlugin() instanceof WicketPlugin) {
				((WicketPlugin) plugin.getPlugin()).destroy(application);
			}
		}		
	}

	protected PluginManager createPluginManager(Application application) {
		String pluginsDirectory = System.getProperty("app.pluginsDir", "plugins");
        LOG.debug("pluginsDirectory = " + pluginsDirectory);
        if (pluginsDirectory != null) {
            return new DefaultPluginManager(new File(pluginsDirectory));
        }

		return null; 
	}

}
