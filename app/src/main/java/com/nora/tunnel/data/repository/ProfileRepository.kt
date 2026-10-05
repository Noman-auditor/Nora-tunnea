package com.nora.tunnel.data.repository

import com.nora.tunnel.data.database.*
import com.nora.tunnel.data.secure.SecureStorage
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val dao: ProfileDao,
    private val secure: SecureStorage
) {
    fun observe() = dao.observeAll()
    suspend fun create(p: TunnelProfile, secret: String?) {
        secret?.let { secure.put("cred_${p.id}", it) }
        dao.upsert(p.copy(credentialRef = if(secret!=null) "cred_${p.id}" else null))
    }
    suspend fun delete(p: TunnelProfile) { dao.delete(p) }
}
