package com.example.app_iliyan.repository.background_task

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.app_iliyan.helpers.NotificationService
import com.example.app_iliyan.helpers.Utils
import kotlinx.coroutines.delay

class ServerRequest(appContext: Context, workerParams: WorkerParameters) :
  CoroutineWorker(appContext, workerParams) {
  override suspend fun doWork(): Result {
    // Make a request to the server
    // For the sake of this example, we'll just delay for 5 seconds
    delay(5000L)

    Utils.logger.error("Server request finished Custom")
    NotificationService.showNotification(applicationContext, "hi", "hi", 0)

    // Indicate whether the work finished successfully with the Result
    return Result.success()
  }
}
