package com.tyabo.chef.editmenu

import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyabo.common.UiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration


@HiltViewModel
class ChefEditMenuViewModel @Inject constructor(): ViewModel(){


    val ff: Flow<UiResult<EditMenuViewState>> = flow {
        emit(UiResult.Success(EditMenuViewState.Edit("bro","3","desc",0L)))
    }

    private val _editMenuState = MutableStateFlow<EditMenuViewState>(EditMenuViewState.Loading)

    val editMenuState = combine (ff, _editMenuState){ fetchResult, actualState ->
        when(fetchResult){
            is UiResult.Failure -> {
                actualState
            }
            is UiResult.Success -> {
                if (actualState is EditMenuViewState.Loading){
                    val fetchedData = fetchResult.data
                    _editMenuState.value = fetchedData
                    actualState
                }
                else {
                    actualState
                }
            }
            is UiResult.Loading -> EditMenuViewState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = Duration.ZERO, replayExpiration = Duration.ZERO),
        initialValue = EditMenuViewState.Loading
    )

    private fun editState() = editMenuState.value as? EditMenuViewState.Edit ?: EditMenuViewState.Edit(
        name = "",
        numberPersons = "",
        description = "",
        price = 0L
    )

    fun onNameUpdate(name: String){
        viewModelScope.launch{
            _editMenuState.value = editState().copy(name = name)
        }
    }

    fun onNumberPersonsUpdate(number: String){
        viewModelScope.launch{
            _editMenuState.value = editState().copy(numberPersons = number)
        }
    }

    fun onDescriptionUpdate(description: String){
        viewModelScope.launch{
            _editMenuState.value = editState().copy(description = description)
        }
    }

    fun onPriceUpdate(price: Long){
        viewModelScope.launch{
            _editMenuState.value = editState().copy(price = price)
        }
    }

    fun onCtaClicked(){
        viewModelScope.launch{

        }
    }

}