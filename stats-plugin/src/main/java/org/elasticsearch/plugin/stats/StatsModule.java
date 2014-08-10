package org.elasticsearch.plugin.stats;

import org.elasticsearch.common.inject.AbstractModule;

public class StatsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(StatsUpdater.class).asEagerSingleton();
    }
}
