package com.tyabo.service.implemetations

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.tyabo.data.Menu
import com.tyabo.data.UserType
import com.tyabo.service.di.CollectionReferences.CHEFS
import com.tyabo.service.di.CollectionReferences.MENUS
import com.tyabo.service.di.CollectionReferences.RESTAURANTS
import com.tyabo.service.interfaces.MenuDataSource
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class MenuDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MenuDataSource {

    override suspend fun addMenu(menu: Menu, userType: UserType, userId: String): Result<Unit> {
        return try {
            val menuRef: CollectionReference = when (userType){
                UserType.Chef -> firestore.collection("$CHEFS/$userId/$MENUS")
                UserType.Restaurant -> firestore.collection("$RESTAURANTS/$userId/$MENUS")
                UserType.Client -> return Result.failure<Unit>(Exception())
            }
            menuRef.document(menu.id).set(menu).await()
            Result.success(Unit)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }
}