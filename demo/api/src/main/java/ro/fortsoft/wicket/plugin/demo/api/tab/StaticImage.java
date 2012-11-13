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
package ro.fortsoft.wicket.plugin.demo.api.tab;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;

/**
 * @author Decebal Suiu
 */
public class StaticImage extends WebComponent {

    private static final long serialVersionUID = 1L;

	public StaticImage(String id, IModel<String> model) {
        super(id, model);
    }

	@Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        
        checkComponentTag(tag, "img");
        tag.put("src", getDefaultModelObjectAsString());
    }

}
