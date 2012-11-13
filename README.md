Plugin framework for Wicket
=====================

A simple plugin framework for wicket based on [PF4J] (https://github.com/decebals/pf4j).

Components
-------------------

Using Maven
-------------------

In your pom.xml you must define the dependencies to wicket plugin artifacts with:

```xml
<dependency>
    <groupId>ro.fortsoft.wicket.plugin</groupId>
    <artifactId>wicket-plugin/artifactId>
    <version>${wicket-plugin.version}</version>
</dependency>
```

where ${wicket-plugin.version} is the last wicket plugin version.

How to use
-------------------

For more information please see the demo sources.

Demo
-------------------

I have a tiny demo application. The demo application is in demo folder.
In demo/api folder I declared an extension point (_Section_) that is a tab in an TabbedPanel.
In demo/plugin* I implemented two plugins: plugin1, plugin2 (each plugin adds an extension for _Section_).  

To run the demo application use:  
 
    ./run-demo.sh
    
In the internet browser type http://localhost:8081/.

License
--------------
  
Copyright 2012 Decebal Suiu
 
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
the License. You may obtain a copy of the License in the LICENSE file, or at:
 
http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.
