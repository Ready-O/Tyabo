package com.tyabo.feature.chef.editmenu

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyabo.data.Menu
import com.tyabo.data.NumberPersons
import com.tyabo.repository.interfaces.ChefCatalogRepository
import com.tyabo.repository.interfaces.ChefMenuRepository
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
    private val catalogRepository: ChefCatalogRepository,
    private val menuRepository: ChefMenuRepository
) : ViewModel(){

    private val userId = userRepository.getUserId()

    private val menuId: String? = savedStateHandle["menuId"]

    private val positionIndex: Int? = savedStateHandle["posIndex"]

    private val _videoState = MutableStateFlow<YoutubeVideoState>(YoutubeVideoState.Loading)

    val videoState = _videoState.asStateFlow()

    private val emptyEdit = EditMenuViewState.Edit(
        name = "",
        numberPersons = NumberPersons.ONE,
        description = "",
        price = "0.0",
        menuPictureUrl = null,
        null,
    )

    private val _editMenuState = MutableStateFlow<EditMenuViewState>(EditMenuViewState.Loading)

    val editMenuState = _editMenuState.asStateFlow()

    init {
        viewModelScope.launch {
            if (menuId != null) {
                menuRepository.getMenus(userId = userId, menusIds = listOf(menuId)).collectLatest{ result ->
                    if (result.isSuccess) {
                        val menu = result.getOrThrow().first { it.id == menuId }
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
                            menuRepository.getVideo(url).onSuccess {
                                _videoState.value = YoutubeVideoState.Video(
                                    title = it.title,
                                    thumbnailUrl = it.thumbnailUrl,
                                    videoUrl = it.videoUrl
                                )
                            }
                                .onFailure {
                                    _videoState.value = YoutubeVideoState.Empty
                                }
                        }
                        else {
                            _videoState.value = YoutubeVideoState.Empty
                        }
                    }
                }
            }
            else {
                _editMenuState.value = emptyEdit
                _videoState.value = YoutubeVideoState.Empty
            }
        }
    }

    private fun editState() = editMenuState.value as? EditMenuViewState.Edit ?: emptyEdit

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
                menuVideoUrl = editState.menuVideoUrl,
                isHidden = false
            )
            if (menuId == null){
                catalogRepository.addMenu(
                    menu = menu,
                    userId = userId,
                    posIndex = positionIndex!!
                )
            }
            else{
                menuRepository.editMenu(
                    menu = menu,
                    userId = userId
                )
            }
            navigateUp()
        }
    }

    // Youtube
    fun displayYoutubeScreen(){
        viewModelScope.launch{
            _editMenuState.value = EditMenuViewState.Youtube(
                url = "",
                savedEditState = editMenuState.value as EditMenuViewState.Edit
            )
        }
    }

    fun backToMain(){
        viewModelScope.launch {
            val state = editMenuState.value as EditMenuViewState.Youtube
            _editMenuState.value = state.savedEditState
        }
    }

    fun onVideoUrlUpdate(url: String){
        viewModelScope.launch {
            _editMenuState.value = EditMenuViewState.Youtube(
                url = url,
                savedEditState = (editMenuState.value as EditMenuViewState.Youtube).savedEditState
            )
        }
    }

    fun exportVideoUrl(){
        viewModelScope.launch {
            val state = editMenuState.value as EditMenuViewState.Youtube
            menuRepository.getVideo(state.url).onSuccess {
                _videoState.value = YoutubeVideoState.Video(
                    title = it.title,
                    thumbnailUrl = it.thumbnailUrl,
                    videoUrl = it.videoUrl
                )
                _editMenuState.value = state.savedEditState.copy(menuVideoUrl = it.videoUrl)
            }
        }
    }

    fun deleteVideo(){
        viewModelScope.launch {
            _editMenuState.value = editState().copy(menuVideoUrl = null)
            _videoState.value = YoutubeVideoState.Empty
        }
    }
}