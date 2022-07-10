package com.tyabo.chef.editmenu

import androidx.lifecycle.SavedStateHandle
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
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val chefRepository: ChefRepository
) : ViewModel(){

    private val menuId: String? = extractArgId(savedStateHandle)

    private fun extractArgId(savedStateHandle: SavedStateHandle): String?
        {
            val arg: String? = savedStateHandle["menuId"]
            return if (arg == "null") {
                null
            } else {
                arg
            }
        }

    private val _editMenuState = MutableStateFlow<EditMenuViewState>(EditMenuViewState.Loading)

    val editMenuState = if (menuId != null) {
        combine(
            chefRepository.getMenus(chefId = userRepository.getUserId(), menusIds = listOf(menuId)),
            _editMenuState
        ) { result, actualState ->
            when (result){
                is UiResult.Success -> {
                    if (actualState is EditMenuViewState.Loading){
                        val menu = result.data.first()
                        _editMenuState.value = EditMenuViewState.Edit(
                            name = menu.name,
                            numberPersons = menu.numberPersons,
                            description = menu.description,
                            price = menu.price.toString(),
                            menuPictureUrl = menu.menuPictureUrl
                        )
                        actualState
                    }
                    else {
                        actualState
                    }
                }
                else -> EditMenuViewState.Loading
            }
        }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = EditMenuViewState.Loading
        )
    }
    else {
        _editMenuState.value = EditMenuViewState.Edit(
            "",
            NumberPersons.ONE,
            "",
            "0.0",
            null
        )
        _editMenuState.asStateFlow()
    }

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
        menuPictureUrl: String?,
        navigateUp: () -> Unit
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
            navigateUp()
        }
    }

}