package com.tyabo.repository.implementation

import com.tyabo.data.Chef
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.service.interfaces.ChefDataSource
import javax.inject.Inject

class ChefRepositoryImpl @Inject constructor(
    private val chefDataSource: ChefDataSource
) : ChefRepository {

    override fun addChef(chef: Chef) {
        chefDataSource.addChef(chef)
    }

    override suspend fun getChef(chefId: String): Result<Chef> {
        return chefDataSource.getChef(chefId)
    }
}