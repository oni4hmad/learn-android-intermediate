package com.dicoding.picodiploma.mywidgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class RandomNumberWidget : AppWidgetProvider() {
    /* metode yang akan dipanggil ketika widget pertama kali dibuat.
    Metode ini juga akan dijalankan ketika updatePeriodMillis yang
    di dalam random_numbers_widget_info.xml mencapai waktunya.*/
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

/* metode yang dipanggil di setiap perulangan appWidgetIds,
di mana hampir seluruh proses update ada di dalam metode ini. */
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
//    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.random_number_widget)
    val lastUpdate = "Random: " + NumberGenerator.generate(100)
    views.setTextViewText(R.id.appwidget_text, lastUpdate)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}