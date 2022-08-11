package com.tyabo.service.firebase.implemetations

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.tyabo.data.Menu
import com.tyabo.data.NumberPersons
import com.tyabo.data.UserType
import com.tyabo.service.firebase.di.CollectionReferences.CHEFS
import com.tyabo.service.firebase.di.CollectionReferences.MENUS
import com.tyabo.service.firebase.di.CollectionReferences.RESTAURANTS
import com.tyabo.service.firebase.interfaces.MenuDataSource
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class MenuDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MenuDataSource {

    override suspend fun editMenu(menu: Menu, userType: UserType, userId: String): Result<Unit> {
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

    override suspend fun fetchMenus(
        userType: UserType,
        userId: String,
        menusIds: List<String>
    ): Result<List<Menu>> {
         try {
            val menuRef: CollectionReference = when (userType){
                UserType.Chef -> firestore.collection("$CHEFS/$userId/$MENUS")
                UserType.Restaurant -> firestore.collection("$RESTAURANTS/$userId/$MENUS")
                UserType.Client -> return Result.failure(Exception())
            }
            val fetchedMenus: MutableList<Menu> = mutableListOf()
            menusIds.forEach { id ->
                val menuSnapshot = menuRef.document(id).get().await()
                val remoteMenu = menuSnapshot.toObject(RemoteMenu::class.java)
                if (remoteMenu == null){
                    return Result.failure(Exception())
                }
                else {
                    fetchedMenus.add(remoteMenu.toMenu())
                }
            }
            return Result.success(fetchedMenus)
        }
        catch (e: Exception){
            return Result.failure(e)
        }
    }

    private data class RemoteMenu(
        var id: String = "",
        var name: String = "",
        var numberPersons: NumberPersons = NumberPersons.ONE,
        var description: String = "",
        var price: Double = 0.0,
        var menuPictureUrl: String? = null,
        var menuVideoUrl: String? = null,
        var isHidden: Boolean = false
    )

    private fun RemoteMenu.toMenu() = Menu(
        id = this.id,
        name = this.name,
        numberPersons = this.numberPersons,
        description = this.description,
        price = this.price,
        menuPictureUrl = this.menuPictureUrl,
        menuVideoUrl = this.menuVideoUrl,
        isHidden = this.isHidden
    )
}