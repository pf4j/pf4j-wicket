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
package ro.fortsoft.wicket.plugin.demo.api;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

/**
 * @author Decebal Suiu
 */
public class SimpleSection extends Section {

	private static final long serialVersionUID = 1L;

	IModel<String> imageModel;
	
	public SimpleSection(IModel<String> title, IModel<String> imageModel) {
		super(title);
		
		this.imageModel = imageModel;
	}
	
	@Override
	public IModel<String> getImageModel() {
		return imageModel;
	}

	@Override
	public WebMarkupContainer getPanel(String panelId) {
		return new SimplePanel(panelId, getTitle());
	}
	
	private class SimplePanel extends GenericPanel<String> {

		private static final long serialVersionUID = 1L;

		public SimplePanel(String id, IModel<String> model) {
			super(id, model);
			
			add(new Label("message", getTitle()));
		}
		
	}
	
}
