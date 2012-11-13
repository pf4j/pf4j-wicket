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
package ro.fortsoft.wicket.plugin;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.AbstractMapper;
import org.apache.wicket.request.mapper.parameter.IPageParametersEncoder;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.caching.IResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.IStaticCacheableResource;
import org.apache.wicket.request.resource.caching.ResourceUrl;
import org.apache.wicket.util.string.Strings;

import ro.fortsoft.pf4j.PluginWrapper;

/**
 * @author Decebal Suiu
 */
public class PluginResourceMapper extends AbstractMapper {
	
	// encode page parameters into url + decode page parameters from url
	private final IPageParametersEncoder parametersEncoder;

	// mount path (= segments) the resource is bound to
	private final String[] mountSegments;

	private PluginWrapper plugin;
	
	public PluginResourceMapper(PluginWrapper plugin) {
		this(plugin, new PageParametersEncoder());
	}

	public PluginResourceMapper(PluginWrapper plugin, IPageParametersEncoder encoder) {
		this.plugin = plugin;
		
		// TODO URL normalization ?
		String path = "plugin/" + plugin.getDescriptor().getPluginId();
//		System.out.println("path = " + path);
		
		mountSegments = getMountSegments(path);
//		System.out.println("mountSegments = " + Arrays.asList(mountSegments));
		
		parametersEncoder = encoder;
	}

	@Override
	public IRequestHandler mapRequest(final Request request) {
//		System.out.println("PluginResourceMapper.mapRequest()");
//		System.out.println("##########################");
		final Url url = new Url(request.getUrl());

		// now extract the page parameters from the request url
		PageParameters parameters = extractPageParameters(request, mountSegments.length, parametersEncoder);

		// remove caching information from current request
		removeCachingDecoration(url, parameters);

		// check if url matches mount path
		if (urlStartsWith(url, mountSegments) == false) {
			return null;
		}
		
//		System.out.println(">>> url = " + url);
		
		// check if there are placeholders in mount segments
		for (int index = 0; index < mountSegments.length; ++index) {
			String placeholder = getPlaceholder(mountSegments[index]);
			if (placeholder != null) {
				// extract the parameter from URL
				if (parameters == null) {
					parameters = new PageParameters();
				}
				parameters.add(placeholder, url.getSegments().get(index));
			}
		}

		// fetch the resource name
		String name = getResourceName(url);
		System.out.println("resourceName = " + name);

		return new PluginResourceRequestHandler(plugin, name, parameters);
	}

	@Override
	public int getCompatibilityScore(Request request) {
		return 0; // pages always have priority over resources
	}

	@Override
	public Url mapHandler(IRequestHandler requestHandler) {
		if (!(requestHandler instanceof PluginResourceRequestHandler)) {
			return null;
		}

		PluginResourceRequestHandler handler = (PluginResourceRequestHandler) requestHandler;

		// see if request handler addresses the resource scope we serve
		if (!plugin.equals(handler.getPlugin())) {
			return null;
		}

		Url url = new Url();

		// add mount path segments
		for (String segment : mountSegments) {
			url.getSegments().add(segment);
		}

		// replace placeholder parameters
		PageParameters parameters = new PageParameters(handler.getPageParameters());

		for (int index = 0; index < mountSegments.length; ++index) {
			String placeholder = getPlaceholder(mountSegments[index]);

			if (placeholder != null) {
				url.getSegments().set(index, parameters.get(placeholder).toString(""));
				parameters.remove(placeholder);
			}
		}

		PluginResource pluginResource = handler.getResource();
		
		// append resource name
		List<String> nameSegments = Arrays.asList(pluginResource.getName().split("/"));
		url.getSegments().addAll(nameSegments);

		// add caching information
		addCachingDecoration(url, parameters, pluginResource);

		// create url
		return encodePageParameters(url, parameters, parametersEncoder);
	}

	private String getResourceName(Url url) {
		if (url.getSegments().size() > mountSegments.length) {
			return Strings.join("/", url.getSegments().subList(mountSegments.length, url.getSegments().size()));
		} else {
			return "";
		}
	}

	protected IResourceCachingStrategy getCachingStrategy() {
		return Application.get().getResourceSettings().getCachingStrategy();
	}

	protected void addCachingDecoration(Url url, PageParameters parameters, IResource resource) {
		final List<String> segments = url.getSegments();
		final int lastSegmentAt = segments.size() - 1;
		final String filename = segments.get(lastSegmentAt);

		if (Strings.isEmpty(filename) == false) {
			if (resource instanceof IStaticCacheableResource) {
				final IStaticCacheableResource cacheable = (IStaticCacheableResource) resource;
				final ResourceUrl cacheUrl = new ResourceUrl(filename, parameters);

				getCachingStrategy().decorateUrl(cacheUrl, cacheable);

				if (Strings.isEmpty(cacheUrl.getFileName())) {
					throw new IllegalStateException("caching strategy returned empty name for " + resource);
				}
				segments.set(lastSegmentAt, cacheUrl.getFileName());
			}
		}
	}

	protected void removeCachingDecoration(Url url, PageParameters parameters) {
		final List<String> segments = url.getSegments();
		
		if (segments.isEmpty() == false) {
			// get filename (the last segment)
			final int lastSegmentAt = segments.size() - 1;
			String filename = segments.get(lastSegmentAt);

			// ignore requests with empty filename
			if (Strings.isEmpty(filename)) {
				return;
			}

			// create resource url from filename and query parameters
			final ResourceUrl resourceUrl = new ResourceUrl(filename, parameters);

			// remove caching information from request
			getCachingStrategy().undecorateUrl(resourceUrl);

			// check for broken caching strategy (this must never happen)
			if (Strings.isEmpty(resourceUrl.getFileName())) {
				throw new IllegalStateException("caching strategy returned empty name for " + resourceUrl);
			}

			segments.set(lastSegmentAt, resourceUrl.getFileName());
		}
	}

}
