package org.elasticsearch.plugin.stats;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Jakub Podeszwik
 * Date: 09/08/14
 */
public class StatusUpdateJob implements Job {
    private ESLogger logger = ESLoggerFactory.getLogger("plugin.stats.StatusUpdateJob");
    private HttpHelper httpHelper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing job");

        httpHelper = new HttpHelper();
        try {
            JSONObject nodesStats = httpHelper.getJsonFromUrl("http://localhost:9200/_nodes/stats?all");
            JSONObject clusterStats = httpHelper.getJsonFromUrl("http://localhost:9200/_cluster/stats");
            putStats(nodesStats, clusterStats);

        } catch (Exception e) {
            logger.error("Error during getting stats", e);
        }
    }

    private void putStats(JSONObject nodesStats, JSONObject clusterStats) throws IOException {

        String indexName = IndexNameFactory.getIndexName();

        StringBuilder sb = new StringBuilder();
        if (nodesStats.has("nodes")) {
            JSONObject nodes = nodesStats.getJSONObject("nodes");

            for (String key : (Set<String>) nodes.keySet()) {
                JSONObject node_data = nodes.getJSONObject(key);
                if (node_data.has("name")) {
                    String nodeName = node_data.getString("name");

                    JSONObject nodeData = new JSONObject();
                    nodeData.put("_index", indexName);
                    nodeData.put("_type", nodeName);

                    JSONObject metadata = new JSONObject();
                    metadata.put("index", nodeData);

                    sb.append(metadata);
                    sb.append('\n');
                    sb.append(node_data.toString());
                    sb.append('\n');
                }
            }
        }

        JSONObject clusterData = new JSONObject();
        clusterData.put("_index", indexName);
        clusterData.put("_type", "cluster");
        JSONObject metadata = new JSONObject();
        metadata.put("index", clusterData);
        sb.append(metadata);
        sb.append('\n');
        sb.append(clusterStats.toString());
        sb.append('\n');


        httpHelper.postString("http://localhost:9200/_bulk", sb.toString());
    }
}
