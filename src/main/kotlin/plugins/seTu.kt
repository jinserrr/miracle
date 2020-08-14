package plugins

import com.google.gson.Gson
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeGroupMessages
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import utils.network.Requests
import utils.network.model.LoliconSeTuModel
import java.io.IOException

fun Bot.seTu() {
    subscribeGroupMessages {
        Regex("(?:来一?[点张份]?)?[色瑟涩]图") matchingReply {
            reply("少女祈祷中")
            Requests.get("https://api.lolicon.app/setu/",
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        logger.warning("色图 api onFailure")
                        launch { reply("api 获取失败") }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Gson().fromJson(
                            response.body?.string(), LoliconSeTuModel::class.java
                        ).let {
                            val url = it.data.first().url
                            Requests.get(url,
                                object : Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        logger.warning("色图 onFailure")
                                        launch { reply("图片获取失败") }
                                    }

                                    override fun onResponse(call: Call, response: Response) {
                                        launch {
                                            if (response.isSuccessful) {
                                                response.body?.byteStream()?.sendAsImage()
                                            }
                                            "网络请求失败".let { info ->
                                                logger.warning("色图 $info")
                                                reply(info)
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            )
        }
    }
}