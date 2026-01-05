import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WebViewController : ViewModel() {
    private val _events = MutableSharedFlow<WebCommand>()
    val events = _events.asSharedFlow()

    fun send(command: WebCommand) {
        viewModelScope.launch {
            _events.emit(command)
        }
    }
}

sealed class WebCommand {
    object BackPressed : WebCommand()
    object Play : WebCommand()
    object Pause : WebCommand()
    object Stop : WebCommand()
    object Right : WebCommand()
    object Left : WebCommand()
    object FastForward : WebCommand()
    object FastRewind : WebCommand()

    data class SetFocusedImage(val index: Int) : WebCommand()
}
