package org.elasticsearch.plugin.stats;

import org.elasticsearch.common.settings.Settings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Jakub Podeszwik
 * Date: 13/08/14
 */
public class IndexNameFactory {
    private static String indexRotationSetting;
    private static final String BASE_INDEX_NAME = "nodesstats";

    public static void initSettings(final Settings settings) {
        String settingsValue = settings.get("monitoring.index.rotation");
        if (settingsValue != null) {
            indexRotationSetting = settingsValue;
        } else {
            indexRotationSetting = "none";
        }
    }

    public static String getIndexName() {
        switch (indexRotationSetting) {
            case "daily":
                return BASE_INDEX_NAME + getFormattedDate(new SimpleDateFormat("-yyyy.MM.dd"));
            case "monthly":
                return BASE_INDEX_NAME + getFormattedDate(new SimpleDateFormat("-yyyy.MM"));
            case "yearly":
                return BASE_INDEX_NAME + getFormattedDate(new SimpleDateFormat("-yyyy"));
            case "none":
            default:
                return BASE_INDEX_NAME;
        }
    }

    private static String getFormattedDate(DateFormat dateFormat) {
        Date date = new Date();
        return dateFormat.format(date);
    }
}
