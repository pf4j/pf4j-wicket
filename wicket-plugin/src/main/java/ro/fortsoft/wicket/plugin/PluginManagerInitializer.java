/*
 * Copyright (C) 2012-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.fortsoft.wicket.plugin;

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.application.CompoundClassResolver;
import org.apache.wicket.application.DefaultClassResolver;
import org.apache.wicket.protocol.http.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.PluginManager;
import ro.fortsoft.pf4j.PluginWrapper;
import ro.fortsoft.pf4j.RuntimeMode;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author Decebal Suiu
 */
public class PluginManagerInitializer implements IInitializer {

	private static final Logger log = LoggerFactory.getLogger(PluginManagerInitializer.class);

	@SuppressWarnings("serial")
	public static MetaDataKey<PluginManager> PLUGIN_MANAGER_KEY = new MetaDataKey<PluginManager>() {};

	private PluginManager pluginManager;

	@Override
	public void init(Application application) {
		pluginManager = createPluginManager(application);
        if (pluginManager == null) {
        	throw new WicketRuntimeException("Plugin manager cannot be null");
        }

        log.debug("Created plugin manager {}", pluginManager);

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

        // set class resolver
        CompoundClassResolver classResolver = new CompoundClassResolver();
        classResolver.add(new DefaultClassResolver());
        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
        for (PluginWrapper plugin : startedPlugins) {
        	classResolver.add(new PluginClassResolver(plugin));
        }
        application.getApplicationSettings().setClassResolver(classResolver);

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

	private PluginManager createPluginManager(Application application) {
		File pluginsDir = getPluginsDir(application);
        log.debug("Plugins directory is {} ", pluginsDir);

        // TODO check more locations for PluginManagerFactory similar with getPluginsDir();
        // now I checked only Application if it implements PluginManagerFactory ?!
        PluginManager pluginManager;
        if (application instanceof PluginManagerFactory) {
    		log.debug("Create custom plugin manager");
        	pluginManager = ((PluginManagerFactory) application).createPluginManager(pluginsDir);
        } else {
    		log.debug("Create default plugin manager");
        	pluginManager = new DefaultPluginManager(pluginsDir);
        }

        return pluginManager;
	}

	private File getPluginsDir(Application application) {
		String pluginsDir = System.getProperty("pf4j.pluginsDir");

		// if no system parameter check filter/servlet <init-param> and <context-param>
		if (pluginsDir == null) {
			pluginsDir = ((WebApplication) application).getInitParameter("pluginsDir");
		}

		if (pluginsDir == null) {
			pluginsDir = ((WebApplication) application).getServletContext().getInitParameter("pluginsDir");
		}

		if (pluginsDir == null) {
            // TODO: improve ?!
//   			pluginsDir = DefaultPluginManager.DEFAULT_PLUGINS_DIRECTORY;
            // retrieves the runtime mode from system
            String modeAsString = System.getProperty("pf4j.mode", RuntimeMode.DEPLOYMENT.toString());
            RuntimeMode runtimeMode = RuntimeMode.byName(modeAsString);
            if (RuntimeMode.DEVELOPMENT.equals(runtimeMode)) {
                pluginsDir = DefaultPluginManager.DEVELOPMENT_PLUGINS_DIRECTORY;
            } else {
                pluginsDir = DefaultPluginManager.DEFAULT_PLUGINS_DIRECTORY;
            }
        }

		return new File(pluginsDir);
	}

}
