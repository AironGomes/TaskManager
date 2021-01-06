import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airongomes.taskmanager.database.TaskListDao
import com.airongomes.taskmanager.overview.OverviewViewModel

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 * Provides the TaskDatabaseDao and context to the ViewModel.
 */
class OverviewViewMolderFactory(
        private val dataSource: TaskListDao,
        private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
            return OverviewViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}