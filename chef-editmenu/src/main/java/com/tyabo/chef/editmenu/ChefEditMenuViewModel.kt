package com.tyabo.chef.editmenu

import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChefEditMenuViewModel @Inject constructor(): ViewModel(){

    /*
    val ff = flow {
        emit(EditMenuViewState.Edit("","","",0L))
    }

    val editMenuState: StateFlow<EditMenuViewState> = combine (
        ff
    ){ bro ->
        EditMenuViewState.Edit("","","",0L)
    }.stateIn(
        scope = viewModelScope
    )

     */

    val _editMenuState = MutableStateFlow<EditMenuViewState>(EditMenuViewState.Edit(
        name = "",
        numberPersons = "",
        description = "",
        price = 0L
    )
    )
    val editMenuState = _editMenuState.asStateFlow()

    fun editState() = editMenuState.value as? EditMenuViewState.Edit ?: EditMenuViewState.Edit(
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

}