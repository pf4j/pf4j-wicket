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
package ro.fortsoft.wicket.plugin.demo.hello;

import org.apache.wicket.model.Model;

import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;
import ro.fortsoft.wicket.plugin.WicketPlugin;
import ro.fortsoft.wicket.plugin.demo.api.SimpleSection;

/**
 * A very simple plugin.
 *
 * @author Decebal Suiu
 */
public class HelloPlugin extends WicketPlugin {

	private static HelloPlugin instance;
	
    public HelloPlugin(PluginWrapper wrapper) {
        super(wrapper);
        
        instance = this;
    }

    public void start() {
        System.out.println("HelloPlugin.start()");
    }

    public void stop() {
        System.out.println("HelloPlugin.stop()");
    }

    public static HelloPlugin get() {
    	return instance;
    }
    
    @Extension
    public static class HelloSection extends SimpleSection {

    	private static final long serialVersionUID = 1L;

		public HelloSection() {
    		super(Model.of("Hello Plugin"), Model.of(HelloPlugin.get().getPluginResourceUrl("settings.png")));
    	}

    }

}
