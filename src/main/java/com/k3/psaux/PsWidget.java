package com.k3.psaux;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


public class PsWidget extends AppWidgetProvider {
    private static String ACTION_AUTOKILL = "AutoKill";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ps_widget);
        Intent intent = new Intent(context, PsWidget.class);
        intent.setAction(ACTION_AUTOKILL);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_image, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_AUTOKILL)) {
            Intent i = new Intent(context, PService.class);
            context.stopService(i);
            context.startService(i);
        }
    }
    @Override
    public void onEnabled(Context context) {}
    @Override
    public void onDisabled(Context context) {}
}

