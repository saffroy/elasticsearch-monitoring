#elasticsearch-stats-plugin
This plugin is experimental. Don't use it on production yet :)

##Build:
mvn clean package

##Instalation:
###quartz plugin (dependency):
$ES_PATH/bin/plugin --install org.codelibs/elasticsearch-quartz/1.0.1

###elasticsearch-stats-plugin:
$ES_PATH/bin/plugin --url file://$PATH_TO_PROJECT_DIR/target/releases/stats-plugin-1.0-SNAPSHOT.zip --install stats-plugin

##Possible settings:
###Index rotation
In elasticsearch.yml:
* monitoring.index.rotation: "none"
* possible values: "yearly", "monthly", "daily", "none"

###Cron frequency
monitoring.cron.string: "0 * * * * ?"
* run every minute "0 * * * * ?"
* run every five seconds "0/5 * * * * ?"
* more examples: [quartz cron examples](http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger)
