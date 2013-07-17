#!/bin/sh

#
# This script creates and run the wicket-plugin demo.
#

# create and install artifacts using maven
mvn clean install

# create 'plugins' folder
cd demo/app
rm -fr plugins
mkdir plugins
cp ../plugin1/target/wicket-plugin-demo-plugin1-*.zip plugins/
cp ../plugin2/target/wicket-plugin-demo-plugin2-*.zip plugins/

# run demo
mvn jetty:run
cd -
