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
package org.pf4j.wicket.demo.hello;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.pf4j.wicket.demo.api.SimplePanel;

/**
 * @author Decebal Suiu
 */
public class HelloPanel extends SimplePanel {

	private ResourceReference raphaelReference = new PackageResourceReference(
			HelloPlugin.class, "res/raphael.2.1.0.min.js");
	private ResourceReference justgageReference = new PackageResourceReference(
			HelloPlugin.class, "res/justgage.1.0.1.min.js");

	public HelloPanel(String id, IModel<String> model) {
		super(id, model);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(JavaScriptHeaderItem.forReference(raphaelReference));
		response.render(JavaScriptHeaderItem.forReference(justgageReference));

		response.render(OnDomReadyHeaderItem.forScript(getJustGageJavaScript()));
	}

	private CharSequence getJustGageJavaScript() {
		/*
		var g = new JustGage({
		    id: "gauge",
		    value: 67,
		    min: 0,
		    max: 100,
		    title: "Visitors"
		  });
		*/

		return new StringBuilder()
		    .append("var g = new JustGage({")
		    .append("id: 'gauge',")
		    .append("value: " + Math.floor((Math.random() * 100) + 1) + ",")
		    .append("min: 0,")
		    .append("max: 100,")
		    .append("title: 'Visitors'")
		    .append("});");
	}

}
