package com.studygenie.app.data.local.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.studygenie.app.R
import com.studygenie.app.StudyGenieApp

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): androidx.work.ListenableWorker.Result {
        val taskTitle = inputData.getString("taskTitle") ?: "Study Session"
        val subject = inputData.getString("subject") ?: "Time to study!"

        showNotification(taskTitle, subject)

        return androidx.work.ListenableWorker.Result.success()
    }

    private fun showNotification(title: String, text: String) {
        val builder = NotificationCompat.Builder(applicationContext, StudyGenieApp.REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Standard icon
            .setContentTitle("⏰ Reminder: $title")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        try {
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(System.currentTimeMillis().toInt(), builder.build())
            }
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }
}
