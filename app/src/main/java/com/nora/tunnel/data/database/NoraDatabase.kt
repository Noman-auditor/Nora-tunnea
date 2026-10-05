package com.nora.tunnel.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM TunnelProfile ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<TunnelProfile>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(p: TunnelProfile)
    @Delete suspend fun delete(p: TunnelProfile)
    @Query("SELECT * FROM TunnelProfile WHERE id=:id") suspend fun getById(id: String): TunnelProfile?
}

@Database(entities = [TunnelProfile::class, ConnectionSession::class, RoutingRule::class], version = 1)
abstract class NoraDatabase: RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}

@Entity
data class RoutingRule(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val domain: String? = null,
    val cidr: String? = null,
    val appPackage: String? = null,
    val action: String, // PROXY, DIRECT, BLOCK
    val enabled: Boolean = true,
    val priority: Int = 0
)
