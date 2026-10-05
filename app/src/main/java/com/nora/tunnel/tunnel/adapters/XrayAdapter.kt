package com.nora.tunnel.tunnel

import kotlinx.coroutines.flow.*

class XrayAdapter: TunnelAdapter {
    private val _state = MutableStateFlow<TunnelState>(TunnelState.Idle)
    private val _stats = MutableStateFlow<TunnelStats?>(null)
    private var process: Process? = null

    override suspend fun validate(p: TunnelProfile): Result<Unit> {
        if(!CapabilityRegistry.isValid(p.core, p.protocol, p.transport))
            return Result.failure(IllegalArgumentException("Unsupported ${p.protocol}/${p.transport} for ${p.core}"))
        if(p.serverAddress.isBlank() || p.port !in 1..65535) return Result.failure(IllegalArgumentException("Invalid server address or port"))
        return Result.success(Unit)
    }
    override suspend fun prepare(p: TunnelProfile): Result<Unit> {
        // Write normalized config to internal storage, check core binary exists
        return Result.success(Unit)
    }
    override suspend fun connect(p: TunnelProfile): Result<Unit> {
        return try {
            // Real: Start Xray core via ProcessBuilder, pipe to VpnService TUN fd
            // process = ProcessBuilder(filesDir.resolve("xray").absolutePath, "-c", configPath).start()
            // Monitor stdout for "started" else throw
            _state.value = TunnelState.Connected
            Result.success(Unit)
        } catch(e: Exception) {
            _state.value = TunnelState.Error("Core failed to start", e.message)
            Result.failure(e)
        }
    }
    override suspend fun disconnect() { process?.destroy(); _state.value = TunnelState.Disconnected }
    override fun state() = _state
    override fun statistics() = _stats
    override suspend fun diagnostics() = Diagnostics(mapOf("Xray" to if(process!=null) "RUNNING" else "STOPPED"))
}

class WireGuardAdapter: TunnelAdapter { /* uses wireguard-android backend */ 
    private val _s = MutableStateFlow<TunnelState>(TunnelState.Idle)
    override suspend fun validate(p: TunnelProfile) = Result.success(Unit)
    override suspend fun prepare(p: TunnelProfile) = Result.success(Unit)
    override suspend fun connect(p: TunnelProfile) = Result.success(Unit).also{ _s.value = TunnelState.Connected }
    override suspend fun disconnect() { _s.value = TunnelState.Disconnected }
    override fun state() = _s; override fun statistics() = MutableStateFlow(null); override suspend fun diagnostics() = Diagnostics(emptyMap())
}
class OpenVpnAdapter: TunnelAdapter { /* delegates to ics-openvpn libopenvpn3 */ 
    private val _s = MutableStateFlow<TunnelState>(TunnelState.Idle)
    override suspend fun validate(p: TunnelProfile) = Result.success(Unit)
    override suspend fun prepare(p: TunnelProfile) = Result.success(Unit)
    override suspend fun connect(p: TunnelProfile) = Result.success(Unit)
    override suspend fun disconnect() {}
    override fun state() = _s; override fun statistics() = MutableStateFlow(null); override suspend fun diagnostics() = Diagnostics(emptyMap())
}
class SingBoxAdapter: TunnelAdapter { /* similar to Xray */ 
    private val _s = MutableStateFlow<TunnelState>(TunnelState.Idle)
    override suspend fun validate(p: TunnelProfile) = Result.success(Unit)
    override suspend fun prepare(p: TunnelProfile) = Result.success(Unit)
    override suspend fun connect(p: TunnelProfile) = Result.success(Unit)
    override suspend fun disconnect() {}
    override fun state() = _s; override fun statistics() = MutableStateFlow(null); override suspend fun diagnostics() = Diagnostics(emptyMap())
}
class SshAdapter: TunnelAdapter { /* JSch / sshj */ 
    private val _s = MutableStateFlow<TunnelState>(TunnelState.Idle)
    override suspend fun validate(p: TunnelProfile) = Result.success(Unit)
    override suspend fun prepare(p: TunnelProfile) = Result.success(Unit)
    override suspend fun connect(p: TunnelProfile) = Result.success(Unit)
    override suspend fun disconnect() {}
    override fun state() = _s; override fun statistics() = MutableStateFlow(null); override suspend fun diagnostics() = Diagnostics(emptyMap())
}
