package com.github.ringoame196_s_mcPlugin.notion

import java.net.HttpURLConnection
import java.net.URL

object NotifierServerStart {
    fun notifier(urlText: String) {
        Thread {
            try {
                val url = URL(urlText)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.connectTimeout = 3000 // 接続タイムアウト(ms)
                conn.readTimeout = 3000 // 読み込みタイムアウト(ms)

                val responseCode = conn.responseCode
                println("通知送信結果: $responseCode")
                conn.disconnect()
            } catch (e: Exception) {
                println("⚠️ Pythonサーバーへの通知に失敗しました: ${e.message}")
            }
        }.start()
    }
}
