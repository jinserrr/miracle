package com.github.miracle.plugins

import io.ktor.client.request.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeGroupMessages
import com.github.miracle.utils.network.KtorClient
import com.github.miracle.utils.network.model.ActivityModel
import com.github.miracle.utils.network.model.ShanBayDailyQuoteModel
import com.github.miracle.utils.network.model.ShiCiModel

fun Bot.information() {
    eventChannel.subscribeGroupMessages {
        Regex("""\s*一言|(five|废物|二次元)语录\s*""") matching regex@{
            val five = KtorClient.getInstance()?.get<String>("https://v1.hitokoto.cn/?encode=text")
            if (five != null) subject.sendMessage(five) else subject.sendMessage("获取失败")
        }
        Regex("""\s*今日诗词\s*""") matching regex@{
            val five = KtorClient.getInstance()?.get<ShiCiModel>("https://v1.jinrishici.com/all")
            if (five != null) subject.sendMessage(five.content) else subject.sendMessage("获取失败")
        }

        case("每日一句", trim = true) {
            val model = KtorClient.getInstance()
                ?.get<ShanBayDailyQuoteModel>("https://apiv3.shanbay.com/weapps/dailyquote/quote/")
            if (model != null) subject.sendMessage(
                "${model.content}\n${model.translation}\n     ——${model.author}"
            ) else subject.sendMessage("获取失败")
        }

        Regex("找点乐子|没事找事|找点事做") matching regex@{
            val client = KtorClient.getInstance() ?: return@regex

            val url = "http://www.boredapi.com/api/activity/"
            val model = client.get<ActivityModel>(url)

            subject.sendMessage(
                "你可以\n\t${model.activity}\n可行性:\t${model.accessibility}\n" +
                        "类型:\t${model.type}\n参与人数:\t${model.participants}\n花费:\t${model.price}" +
                        if (model.link.isNotEmpty()) "\n${model.link}" else ""
            )
        }
    }
}