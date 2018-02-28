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
package org.pf4j.wicket.demo;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.pf4j.wicket.demo.api.Section;
import org.pf4j.wicket.demo.api.SimpleSection;
import org.pf4j.wicket.demo.api.tab.ImageTabbedPanel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Decebal Suiu
 */
public class HomePage extends WebPage {

	@Inject
	private List<Section> sectionExtensions;

	public HomePage() {
		List<Section> sections = new ArrayList<>();

		// add default/main section
		ResourceReference imageReference = new PackageResourceReference(HomePage.class, "dashboard.png");
		Section defaultSection = new SimpleSection(new Model<>("Default"), imageReference);
		sections.add(defaultSection);

		// add section extensions
		sections.addAll(sectionExtensions);

        // add tabbed panel to page
		add(new ImageTabbedPanel<>("tabs", sections));
	}

}
