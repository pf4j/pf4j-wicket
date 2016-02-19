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
package ro.fortsoft.wicket.plugin;

import org.apache.wicket.injection.IFieldValueFactory;
import ro.fortsoft.pf4j.PluginManager;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * @author Decebal Suiu
 */
public class PluginFieldValueFactory implements IFieldValueFactory {

	private PluginManager pluginManager;

	public PluginFieldValueFactory(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	@Override
	public Object getFieldValue(Field field, Object fieldOwner) {
		if (field.isAnnotationPresent(Inject.class)) {
			// field.getType().getName() must be java.util.List
//			System.out.println(">>>> " + field.getType().getName());

			ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
			Class<?> type = (Class<?>) parameterizedType.getActualTypeArguments()[0];

			return pluginManager.getExtensions(type) ;
        }

        return null;
	}

	@Override
	public boolean supportsField(Field field) {
		return field.isAnnotationPresent(Inject.class);
	}

}
