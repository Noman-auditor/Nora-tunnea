package com.nora.tunnel.core.model

enum class Protocol { VLESS, VMESS, TROJAN, SHADOWSOCKS, WIREGUARD, OPENVPN, IKEV2, SSH, SOCKS, HTTP, HYSTERIA2, TUIC }
enum class Core { XRAY, SINGBOX, WIREGUARD, OPENVPN, IKEV2, SSH }
enum class Transport { TCP, UDP, TLS, WS, GRPC, H2, QUIC, H3 }
enum class Security { NONE, TLS, REALITY, UTLS }

data class CoreCapability(val core: Core, val protocols: Set<Protocol>, val transports: Set<Transport>)

object CapabilityRegistry {
    val registry = mapOf(
        Core.XRAY to CoreCapability(Core.XRAY, setOf(Protocol.VLESS, Protocol.VMESS, Protocol.TROJAN, Protocol.SHADOWSOCKS), setOf(Transport.TCP, Transport.WS, Transport.GRPC, Transport.QUIC, Transport.TLS)),
        Core.SINGBOX to CoreCapability(Core.SINGBOX, setOf(Protocol.VLESS, Protocol.VMESS, Protocol.TROJAN, Protocol.SHADOWSOCKS, Protocol.HYSTERIA2, Protocol.TUIC), setOf(Transport.TCP, Transport.UDP, Transport.WS, Transport.GRPC, Transport.QUIC)),
        Core.WIREGUARD to CoreCapability(Core.WIREGUARD, setOf(Protocol.WIREGUARD), setOf(Transport.UDP)),
        Core.OPENVPN to CoreCapability(Core.OPENVPN, setOf(Protocol.OPENVPN), setOf(Transport.TCP, Transport.UDP)),
    )
    fun isValid(core: Core, protocol: Protocol, transport: Transport): Boolean {
        val cap = registry[core] ?: return false
        return protocol in cap.protocols && transport in cap.transports
    }
}
