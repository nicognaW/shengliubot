import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.auth.BotAuthorization
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.events.MessageSyncEvent
import net.mamoe.mirai.utils.BotConfiguration
import org.slf4j.LoggerFactory

val botId: Long = System.getenv("BOT_ID").ifEmpty { throw RuntimeException("请提供 BOT_ID") }.toLong()
val redisUrl = System.getenv("REDIS_URL").ifEmpty { "127.0.0.1:6379" }

suspend fun main() {
    initMirai()
}

suspend fun initMirai() {

    val logger = LoggerFactory.getLogger("GroupMessage")
    val bot = BotFactory.newBot(botId, BotAuthorization.byQRCode()) {
        protocol = BotConfiguration.MiraiProtocol.ANDROID_WATCH
        enableContactCache()
        loginCacheEnabled = true
        fileBasedDeviceInfo("device.json")
    }
    try {
        bot.login()
        println("登陆成功")
    } catch (e: net.mamoe.mirai.network.LoginFailedException) {
        println("登录失败")
    }
    bot.eventChannel.subscribeAlways<MessageEvent> {
        if (this is MessageSyncEvent) return@subscribeAlways
        if (this is GroupMessageEvent) {
            spy()
            shengLiu()
        }
    }
}
