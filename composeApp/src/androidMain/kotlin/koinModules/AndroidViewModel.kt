package koinModules

import androidx.lifecycle.ViewModel
import koinModules.`interface`.BaseViewModel

class AndroidViewModel : ViewModel(), BaseViewModel {

    override fun loadData() {
        // Android-specific ViewModel logic
    }

    override fun clear() {
        // Clear resources if needed
    }

    override fun onCleared() {
        super.onCleared()
        clear()
    }
}
