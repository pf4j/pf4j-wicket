# PF4J - Wicket integration
=====================
[![Travis CI Build Status](https://travis-ci.org/pf4j/pf4j-wicket.png)](https://travis-ci.org/pf4j/pf4j-wicket)
[![Maven Central](http://img.shields.io/maven-central/v/org.pf4j/pf4j-wicket.svg)](http://search.maven.org/#search|ga|1|pf4j-wicket)

A simple plugin framework for wicket based on [PF4J](https://github.com/pf4j/pf4j). You can view pf4j-wicket as a wrapper over PF4J (that is more general and can be used to create a modular Swing application for example).  

Features/Benefits
-------------------
This framework is lightweight (around 10KB) with minimal dependencies (only PF4J).  

The beauty of this framework is that you can start with a monolithic application and as the application grows in complexity you can split the code (without modifications) in multiple plugins.  
First create a package for each future plugin in your monolithic application. After this move each package in a plugin structure. You can play in each plugin with `PackageResoure`, `PackageResourceReference`, ... 
from wicket without be aware that your code is located in a plugin.

Components
-------------------
- **WicketPlugin** is a Plugin that implements IInitializer (hook for init/destroy application).
- **PluginManagerInitializer** creates the plugin manager and register the created plugin manager in application using MetaDataKey.
This class load, init, start, stop and destroy plugins (using the plugin manager object).
- **PluginComponentInjector** scans the wicket component class for fields annotated by @javax.inject.Inject, 
looks up extensions of the required type for the given field from the plugin manager, and injects the extensions.

Using Maven
-------------------
In your pom.xml you must define the dependencies to wicket plugin artifacts with:

```xml
<dependency>
    <groupId>org.pf4j</groupId>
    <artifactId>pf4j-wicket</artifactId>
    <version>${pf4j-wicket.version}</version>
</dependency>
```

where ${pf4j-wicket.version} is the last wicket plugin version.

You may want to check for the latest released version using [Maven Search](http://search.maven.org/#search%7Cga%7C1%7Cpf4j-wicket)

How to use
-------------------
It's very easy to use pf4j-wicket. All you need to do is to add a dependency to pf4j-wicket in your pom.xml.
The main challenge for you to transform a monolithic wicket application in a modular wicket application is to identify what's your extension points and 
to write extensions for these extension point in your plugins.

The plugins are stored in a folder. You can specify the plugins folder in many way:
- set `pf4j.pluginsDir` system property
- servlet \<context-param> with name `pluginsDir` (in your web.xml file)
- filter \<init-param> with name `pluginsDir` (in your web.xml file)   

The default value for plugins folder is 'plugins'.

You can define an extension point in your application using **ExtensionPoint** interface marker.

```java
public abstract class Section extends AbstractImageTab implements ExtensionPoint {

    public Section(IModel<String> title) {
        super(title);
    }

}
```

In below code I supply an extension for the `Section` extension point.

```java
public class WelcomePlugin extends WicketPlugin {

    private static WelcomePlugin instance;
    
    public WelcomePlugin(PluginWrapper wrapper) {
        super(wrapper);
        
        instance = this;
    }

    public static WelcomePlugin get() { // for a quick access to this plugin (it's optional)
        return instance;
    }
    
    @Extension
    public static class WelcomeSection extends SimpleSection {

        public WelcomeSection() {
            super(Model.of("Welcome Plugin"));
        }

        @Override
        public ResourceReference getImage() {
            return new PackageResourceReference(WelcomePlugin.class, "res/datasource.png");
        }

        @Override
        public WebMarkupContainer getPanel(String panelId) {
            return new WelcomePanel(panelId, Model.of("This plugin contributes with a css file to the head of page."));
        }

    }

}

public class WelcomePanel extends SimplePanel {
    
    public WelcomePanel(String id, IModel<String> model) {
        super(id, model);
        
        messageLabel.add(AttributeModifier.append("class", "welcome"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        
        response.render(CssHeaderItem.forReference(new PackageResourceReference(WelcomePanel.class, "res/welcome.css")));
    }

}
```
	
You can use **@Inject** to retrieve all extensions for `Section` extension point (see demo/app/.../HomePage.java).

```java
public class HomePage extends WebPage {

    @Inject
    private List<Section> sectionExtensions; // this field is populate by pf4j-wicket

    public HomePage() {     
        ...

        // add section extensions
        sections.addAll(sectionExtensions);

        // add tabbed panel to page
        add(new ImageTabbedPanel<Section>("tabs", sections));        
    }
    
}
```

Another option (without annotation) to retrieves all extensions for an extension point is **pluginManager.getExtensions(Section.class)**.   
For example:
	
```java
PluginManager pluginManager = Application.get().getMetaData(PluginManagerInitializer.PLUGIN_MANAGER_KEY);
List<Section> sectionExtensions = pluginManager.getExtensions(Section.class);
```

If you want to supply a custom PluginManager than your Application must implements PluginManagerFactory.

For more information please see the demo sources.

Demo
-------------------
I have a tiny demo application. The demo application is in demo folder.
In demo/api folder I declared an extension point (_Section_) that is a tab in a wicket TabbedPanel.
Each section has an title, an icon and a content (a simple text message in my demo).
In demo/plugins/* I implemented two plugins: plugin1, plugin2 (each plugin adds an extension for _Section_).

The first plugin contributes with some JavaScript files to the head of page and the second plugin contributes with a Css file to the head of page.  

To run the demo application use:  

```bash 
./run-demo.sh
```
    
In the internet browser type http://localhost:8081/.

Mailing list
--------------

Much of the conversation between developers and users is managed through [mailing list] (http://groups.google.com/group/wicket-plugin).

Versioning
------------
PF4J Wicket will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

`<major>.<minor>.<patch>`

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org/.
