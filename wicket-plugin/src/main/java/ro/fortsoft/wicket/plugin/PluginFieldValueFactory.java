package ro.fortsoft.wicket.plugin;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import javax.inject.Inject;

import org.apache.wicket.injection.IFieldValueFactory;

import ro.fortsoft.pf4j.PluginManager;

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
