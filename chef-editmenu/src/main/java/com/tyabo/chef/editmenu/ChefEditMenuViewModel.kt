package com.tyabo.chef.editmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyabo.common.UiResult
import com.tyabo.data.Menu
import com.tyabo.data.NumberPersons
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.repository.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.time.Duration


@HiltViewModel
class ChefEditMenuViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chefRepository: ChefRepository
) : ViewModel(){


    val ff: Flow<UiResult<EditMenuViewState>> = flow {
        emit(UiResult.Success(EditMenuViewState.Edit("bro", NumberPersons.ONE, "desc", "0.0",null)))
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
        numberPersons = NumberPersons.ONE,
        description = "",
        price = "0.0",
        menuPictureUrl = null
    )

    fun onNameUpdate(name: String){
        viewModelScope.launch{
            _editMenuState.value = editState().copy(name = name)
        }
    }

    fun onNumberPersonsUpdate(number: NumberPersons){
        viewModelScope.launch{
            _editMenuState.value = editState().copy(numberPersons = number)
        }
    }

    fun onDescriptionUpdate(description: String){
        viewModelScope.launch{
            _editMenuState.value = editState().copy(description = description)
        }
    }

    fun onPriceUpdate(price: String){
        viewModelScope.launch{
            _editMenuState.value = editState().copy(price = price)
        }
    }

    fun onPictureUpdate(url: String?){
        viewModelScope.launch{
            _editMenuState.value = editState().copy(menuPictureUrl = url)
        }
    }

    fun onCtaClicked(
        name: String,
        numberPersons: NumberPersons,
        description: String,
        price: String,
        menuPictureUrl: String?
    ) {
        viewModelScope.launch{
            val generatedId = UUID.randomUUID().toString()
            val menu = Menu(
                id = generatedId,
                name = name,
                numberPersons = numberPersons,
                description = description,
                price = price.toDouble(),
                menuPictureUrl = menuPictureUrl
            )
            chefRepository.addMenu(
                menu = menu,
                userId = userRepository.getUserId()
            )
        }
    }

}