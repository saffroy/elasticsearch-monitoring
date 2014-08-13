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
            JSONObject stats = getStats();
            putStats(stats);
        } catch (Exception e) {
            logger.error("Error during getting stats", e);
        }
    }

    private JSONObject getStats() throws IOException {
        return httpHelper.getJsonFromUrl("http://localhost:9200/_nodes/stats?all");
    }

    private void putStats(JSONObject stats) throws IOException {

        String indexName = IndexNameFactory.getIndexName();

        StringBuilder sb = new StringBuilder();
        if (stats.has("nodes")) {
            JSONObject nodes = stats.getJSONObject("nodes");

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

        httpHelper.postString("http://localhost:9200/_bulk", sb.toString());
    }
}
