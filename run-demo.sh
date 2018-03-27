#!/bin/sh

#
# This script creates and run the pf4j-wicket demo.
#

# create and install artifacts using maven
mvn clean install

# create 'plugins' folder
cd demo/app
rm -fr plugins
mkdir plugins
cp ../plugins/plugin1/target/pf4j-wicket-demo-plugin1-*.zip plugins/
cp ../plugins/plugin2/target/pf4j-wicket-demo-plugin2-*.zip plugins/

# run demo
mvn jetty:run
cd -
