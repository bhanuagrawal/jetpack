package agrawal.bhanu.jetpack

import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object TestUtil {
    suspend fun <T> fetchLiveData(livedata: LiveData<T?>, waitTime: Long = 5000, isValid: ((T?) -> Boolean)? ) = suspendCoroutine<T?> { continuation ->
        val waitingJob = GlobalScope.launch {
            delay(waitTime)
            continuation.resume(null)
        }

        livedata.observeForever {
            if(isValid == null ||
                isValid(it)){
                waitingJob.cancel()
                continuation.resume(it)
            }
        }
    }


}