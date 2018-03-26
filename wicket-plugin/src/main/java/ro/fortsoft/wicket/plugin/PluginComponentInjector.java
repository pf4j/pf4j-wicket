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
package org.pf4j.wicket.plugin;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.injection.Injector;
import org.pf4j.PluginManager;

/**
 * The injector scans the component class for fields annotated by @javax.inject.Inject,
 * looks up extensions of the required type for the given field from the plugin manager, and injects
 * the extensions.
 *
 * @author Decebal Suiu
 */
public class PluginComponentInjector extends Injector implements IComponentInstantiationListener {

	private IFieldValueFactory factory;

	public PluginComponentInjector(Application application) {
		PluginManager pluginManager = application.getMetaData(PluginManagerInitializer.PLUGIN_MANAGER_KEY);
		factory = new PluginFieldValueFactory(pluginManager);
	}

	@Override
	public void onInstantiation(Component component) {
		inject(component);
	}

	@Override
	public void inject(Object object) {
		inject(object, factory);
	}

}
