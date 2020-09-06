package com.github.miracle.plugins

import com.github.miracle.utils.expand.sendToEveryGroup
import com.github.miracle.utils.tools.timer.calendarGen
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

fun Bot.scheduler() {
    Timer().apply {
        schedule(calendarGen(6).time, TimeUnit.DAYS.toMillis(1)) {
            launch { sendToEveryGroup("早") }
        }
        // need fix 立即执行了
        schedule(calendarGen(0).time, TimeUnit.DAYS.toMillis(1)) {
            launch { sendToEveryGroup("🕛") }
        }
    }
}