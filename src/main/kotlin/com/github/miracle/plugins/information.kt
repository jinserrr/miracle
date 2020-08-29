package com.github.miracle.plugins

import io.ktor.client.request.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeGroupMessages
import com.github.miracle.utils.network.KtorClient

fun Bot.information() {
    subscribeGroupMessages {
        Regex("""\s*一言|(five|废物|二次元)语录\s*""") matching regex@{
            val five = KtorClient.getInstance()?.get<String>("https://api.imjad.cn/hitokoto/")
            if (five != null) reply(five) else reply("获取失败")
        }
    }
}