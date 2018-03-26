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
package ro.fortsoft.wicket.plugin.demo;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import ro.fortsoft.wicket.plugin.demo.api.Section;
import ro.fortsoft.wicket.plugin.demo.api.SimpleSection;
import ro.fortsoft.wicket.plugin.demo.api.tab.ImageTabbedPanel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Decebal Suiu
 */
public class HomePage extends WebPage {

	private static final long serialVersionUID = 1L;

	@Inject
	private List<Section> sectionExtensions;

	public HomePage() {
		List<Section> sections = new ArrayList<Section>();

		// add default/main section
		ResourceReference imagereReference = new PackageResourceReference(HomePage.class, "dashboard.png");
		Section defaultSection = new SimpleSection(new Model<String>("Default"), imagereReference);
		sections.add(defaultSection);

		// add section extensions
		sections.addAll(sectionExtensions);

        // add tabbed panel to page
		add(new ImageTabbedPanel<Section>("tabs", sections));
	}

}
