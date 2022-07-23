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
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ChefEditMenuViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val chefRepository: ChefRepository
) : ViewModel(){

    private val userId = userRepository.getUserId()

    private val menuId: String? = extractArgId(savedStateHandle)

    private fun extractArgId(savedStateHandle: SavedStateHandle): String? {
        val arg: String? = savedStateHandle["menuId"]
        return if (arg == "null") {
            null
        } else {
            arg
        }
    }

    private val _videoState = MutableStateFlow<YoutubeVideoState>(YoutubeVideoState.Loading)

    val videoState = _videoState.asStateFlow()

    private val _editMenuState = MutableStateFlow<EditMenuViewState>(EditMenuViewState.Loading)

    val editMenuState = if (menuId != null) {
        combine(
            chefRepository.getMenus(chefId = userId, menusIds = listOf(menuId)),
            _editMenuState
        ) { result, actualState ->
            when (result){
                is UiResult.Success -> {
                    if (actualState is EditMenuViewState.Loading){
                        val menu = result.data.first { it.id == menuId }
                        _editMenuState.value = EditMenuViewState.Edit(
                            name = menu.name,
                            numberPersons = menu.numberPersons,
                            description = menu.description,
                            price = menu.price.toString(),
                            menuPictureUrl = menu.menuPictureUrl,
                            menuVideoUrl = menu.menuVideoUrl,
                        )

                        val url = menu.menuVideoUrl
                        if (url != null){
                            val video = chefRepository.getVideo(url)
                            if (video != null) {
                                _videoState.value = YoutubeVideoState.Video(
                                    title = video.title,
                                    thumbnailUrl = video.thumbnailUrl,
                                    videoUrl = video.videoUrl
                                )
                            }
                            else {
                                _videoState.value = YoutubeVideoState.ExportUrl("")
                            }
                        }
                        else {
                            _videoState.value = YoutubeVideoState.ExportUrl("")
                        }
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
            null,
            null,
        )
        _videoState.value = YoutubeVideoState.ExportUrl("")
        _editMenuState.asStateFlow()
    }

    private fun editState() = editMenuState.value as? EditMenuViewState.Edit ?: EditMenuViewState.Edit(
        name = "",
        numberPersons = NumberPersons.ONE,
        description = "",
        price = "0.0",
        menuPictureUrl = null,
        null,
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

    fun onVideoUrlUpdate(url: String){
        viewModelScope.launch {
            _videoState.value = YoutubeVideoState.ExportUrl(url)
        }
    }

    fun exportVideoUrl(){
        viewModelScope.launch {
            val state = videoState.value as YoutubeVideoState.ExportUrl
            val video = chefRepository.getVideo(state.url)
            if (video != null){
                _videoState.value = YoutubeVideoState.Video(
                    title = video.title,
                    thumbnailUrl = video.thumbnailUrl,
                    videoUrl = video.videoUrl
                )
                _editMenuState.value = editState().copy(menuVideoUrl = video.videoUrl)
            }
        }
    }

    fun onCtaClicked(
        navigateUp: () -> Unit
    ) {
        viewModelScope.launch{
            val editState = editMenuState.value as EditMenuViewState.Edit
            val menu = Menu(
                id = menuId ?: UUID.randomUUID().toString(),
                name = editState.name,
                numberPersons = editState.numberPersons,
                description = editState.description,
                price = editState.price.toDouble(),
                menuPictureUrl = editState.menuPictureUrl,
                menuVideoUrl = editState.menuVideoUrl
            )
            chefRepository.editMenu(
                menu = menu,
                userId = userId
            )
            navigateUp()
        }
    }

}