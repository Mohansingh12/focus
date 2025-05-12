package com.focus.focus;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import java.util.Calendar;
import java.util.List;

public class UsageStatsHelper {

    public static long getScreenTimeToday(Context context) {
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long end = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        long start = calendar.getTimeInMillis();

        List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end);
        long totalTime = 0;
        if (stats != null) {
            for (UsageStats usage : stats) {
                totalTime += usage.getTotalTimeInForeground();
            }
        }
        return totalTime;
    }
}
