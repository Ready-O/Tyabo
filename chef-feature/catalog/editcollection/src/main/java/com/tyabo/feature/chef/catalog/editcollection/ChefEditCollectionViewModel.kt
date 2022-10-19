package com.tyabo.feature.chef.catalog.editcollection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyabo.repository.interfaces.ChefCatalogRepository
import com.tyabo.repository.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChefEditCollectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val catalogRepository: ChefCatalogRepository,
) : ViewModel() {

    private val userId = userRepository.getUserId()

    private val isNew: Boolean = savedStateHandle["isNew"]!!
    private val collectionName: String = savedStateHandle["collectionName"]!!
    private val collectionId: String? = savedStateHandle["collectionId"]

    private val _collectionState = MutableStateFlow<String>(if (isNew) "" else collectionName!!)
    val collectionState : StateFlow<String> = _collectionState.asStateFlow()

    fun onCollectionUpdate(collection: String){
        viewModelScope.launch{
            _collectionState.value = collection
        }
    }

    fun onCtaClicked(navigateUp: ()-> Unit){
        viewModelScope.launch{
            if(isNew){
                catalogRepository.addCollection(
                    collectionName = collectionState.value,
                    userId = userId
                )
            }
            else{
                catalogRepository.editCollection(
                    collectionId = collectionId!!,
                    collectionName = collectionState.value,
                    userId = userId
                )
            }
            navigateUp()
        }
    }
}