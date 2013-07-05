package ro.fortsoft.wicket.plugin;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.injection.Injector;

import ro.fortsoft.pf4j.PluginManager;

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
