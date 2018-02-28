/*
 * Copyright 2012 Decebal Suiu
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
package org.pf4j.wicket;

import org.apache.wicket.application.AbstractClassResolver;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 * This resolver uses the PLuginclassLoader to resolve any classes that
 * cannot directly be loaded by the Wicket classloader.
 *
 * @author Decebal Suiu
 */
public class PluginClassResolver extends AbstractClassResolver {

	private PluginWrapper plugin;

	public PluginClassResolver(PluginWrapper plugin) {
		this.plugin = plugin;
	}

	@Override
	public ClassLoader getClassLoader() {
		return plugin.getPluginClassLoader();
	}

}
