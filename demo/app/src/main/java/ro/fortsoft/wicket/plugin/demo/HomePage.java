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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import ro.fortsoft.pf4j.PluginManager;
import ro.fortsoft.wicket.plugin.demo.api.Section;
import ro.fortsoft.wicket.plugin.demo.api.SimpleSection;
import ro.fortsoft.wicket.plugin.demo.api.tab.ImageTabbedPanel;

/**
 * @author Decebal Suiu
 */
public class HomePage extends WebPage {

	private static final long serialVersionUID = 1L;

	public HomePage(final PageParameters parameters) {		
		List<Section> sections = new ArrayList<Section>();

		// add default/main section
		ResourceReference imagereReference = new PackageResourceReference(HomePage.class, "dashboard.png");
		String imageUrl = RequestCycle.get().urlFor(imagereReference, null).toString();
//		System.out.println("imageUrl = " + imageUrl);
		IModel<String> imageModel = Model.of(imageUrl);
		Section defaultSection = new SimpleSection(new Model<String>("first tab"), imageModel);
		sections.add(defaultSection);
		
		// add section extensions
		PluginManager pluginManager = WicketApplication.get().getPluginManager();
		List<Section> sectionExtensions = pluginManager.getExtensions(Section.class);
//		System.out.println("sectionExtensions = " + sectionExtensions);
		sections.addAll(sectionExtensions);

		List<ITab> tabs = new ArrayList<ITab>();
		tabs.addAll(sections);
		add(new ImageTabbedPanel("tabs", tabs));
	}
	
}
