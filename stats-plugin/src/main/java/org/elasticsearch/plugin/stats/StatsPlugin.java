package org.elasticsearch.plugin.stats;

import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.plugins.AbstractPlugin;

import java.util.Collection;

public class StatsPlugin extends AbstractPlugin {

    @Override
    public String name() {
        return "stats-plugin";
    }

    @Override
    public String description() {
        return "Stats plugin";
    }

    @Override
    public Collection<Class<? extends Module>> modules() {
        Collection<Class<? extends Module>> modules = Lists.newArrayList();
        modules.add(StatsModule.class);
        return modules;
    }
}
