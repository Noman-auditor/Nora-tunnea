package com.nora.tunnel.core.parser

import com.nora.tunnel.core.model.*
import com.nora.tunnel.data.database.TunnelProfile
import kotlinx.serialization.json.Json

sealed class ParseResult {
    data class Success(val profile: TunnelProfile, val preview: String): ParseResult()
    data class Failure(val field: String, val reason: String, val action: String): ParseResult()
}

object ConfigParser {
    fun parse(text: String, fileName: String? = null): ParseResult {
        return when {
            text.trim().startsWith("[Interface]") -> parseWireGuard(text)
            text.contains("client") && text.contains("remote ") -> parseOpenVpn(text)
            text.trim().startsWith("{") -> parseJson(text)
            text.startsWith("vless://") || text.startsWith("vmess://") || text.startsWith("trojan://") || text.startsWith("ss://") -> parseUri(text)
            else -> ParseResult.Failure("format", "Unknown configuration format", "Check file content and try QR import")
        }
    }

    private fun parseWireGuard(text: String): ParseResult {
        val address = Regex("Address\\s*=\\s*(.+)").find(text)?.groupValues?.get(1) ?: return fail("Address","Missing Address","Add Address = 10.0.0.2/32")
        val endpoint = Regex("Endpoint\\s*=\\s*(.+):(\\d+)").find(text) ?: return fail("Endpoint","Missing Endpoint","Add Endpoint = host:port")
        return success("WireGuard", Protocol.WIREGUARD, Core.WIREGUARD, Transport.UDP, endpoint.groupValues[1], endpoint.groupValues[2].toInt(), text)
    }

    private fun parseOpenVpn(text: String): ParseResult {
        val remote = Regex("remote\\s+(\\S+)\\s+(\\d+)").find(text) ?: return fail("remote","Missing remote line","Add: remote example.com 1194")
        return success("OpenVPN", Protocol.OPENVPN, Core.OPENVPN, Transport.UDP, remote.groupValues[1], remote.groupValues[2].toInt(), text)
    }

    private fun parseJson(text: String): ParseResult {
        return try {
            val json = Json.parseToJsonElement(text)
            // Validate Xray / sing-box structure without silently modifying
            if(!text.contains("outbounds") && !text.contains("outbound")) return fail("outbounds","Missing outbounds","Provide valid Xray/sing-box JSON")
            success("Imported JSON", Protocol.VLESS, Core.XRAY, Transport.TCP, "example.com", 443, text.take(500))
        } catch(e: Exception) { fail("JSON", e.message ?: "Invalid JSON", "Validate JSON syntax") }
    }

    private fun parseUri(text: String): ParseResult {
        return try {
            val uri = java.net.URI(text)
            val port = if(uri.port==-1) 443 else uri.port
            success(uri.scheme.uppercase(), mapProto(uri.scheme), mapCore(uri.scheme), Transport.TCP, uri.host ?: "unknown", port, text)
        } catch(e: Exception) { fail("URI", "Malformed URI: ${e.message}", "Re-copy URI or use QR") }
    }

    private fun mapProto(s: String) = when(s.lowercase()) { "vless"->Protocol.VLESS; "vmess"->Protocol.VMESS; "trojan"->Protocol.TROJAN; "ss"->Protocol.SHADOWSOCKS else->Protocol.SOCKS }
    private fun mapCore(s: String) = when(s.lowercase()) { "ss"->Core.SINGBOX else->Core.XRAY }
    private fun success(name: String, p: Protocol, c: Core, t: Transport, host: String, port: Int, preview: String) =
        ParseResult.Success(TunnelProfile(name=name, protocol=p, core=c, transport=t, security=Security.TLS, serverAddress=host, port=port), preview)
    private fun fail(f: String, r: String, a: String) = ParseResult.Failure(f,r,a)
}
