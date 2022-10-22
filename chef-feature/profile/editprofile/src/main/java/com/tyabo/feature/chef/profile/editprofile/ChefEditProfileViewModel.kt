package com.tyabo.feature.chef.profile.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyabo.common.UiResult
import com.tyabo.data.NumberPersons
import com.tyabo.repository.interfaces.ChefProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChefEditProfileViewModel @Inject constructor(
    private val chefRepository: ChefProfileRepository
) : ViewModel(){

    private val _editViewState = MutableStateFlow<EditProfileViewState>(EditProfileViewState.Loading)
    val editViewState : StateFlow<EditProfileViewState> = _editViewState.asStateFlow()

    init {
        viewModelScope.launch{
            chefRepository.chefFlow.collectLatest { uiResult ->
                when(uiResult){
                    is UiResult.Success -> {
                        _editViewState.value = EditProfileViewState.Edit(
                            name = uiResult.data.name,
                            phoneNumber = uiResult.data.phoneNumber,
                            chefPictureUrl = uiResult.data.chefPictureUrl,
                            businessPictureUrl = uiResult.data.businessPictureUrl,
                            bio = uiResult.data.bio
                        )
                    }
                    else -> {
                        _editViewState.value = EditProfileViewState.Loading
                    }
                }
            }
        }
    }

    private val emptyEdit = EditProfileViewState.Edit(
        name = "",
        phoneNumber = "",
        chefPictureUrl = null,
        businessPictureUrl = null,
        bio = ""
    )

    private fun editState() = editViewState.value as? EditProfileViewState.Edit ?: emptyEdit

    fun onNameUpdate(name: String){
        viewModelScope.launch {
            _editViewState.value = editState().copy(name = name)
        }
    }

    fun onPhoneUpdate(phone: String){
        viewModelScope.launch {
            _editViewState.value = editState().copy(phoneNumber = phone)
        }
    }

    fun onChefPictureUpdate(pictureUrl: String?){
        viewModelScope.launch {
            _editViewState.value = editState().copy(chefPictureUrl = pictureUrl)
        }
    }

    fun onBusinessPictureUpdate(pictureUrl: String?){
        viewModelScope.launch {
            _editViewState.value = editState().copy(businessPictureUrl = pictureUrl)
        }
    }

    fun onBioUpdate(bio: String){
        viewModelScope.launch {
            _editViewState.value = editState().copy(bio = bio)
        }
    }
}