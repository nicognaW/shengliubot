import net.mamoe.mirai.contact.remarkOrNameCardOrNick
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.LightApp
import net.mamoe.mirai.message.data.content

private val filterKeywords = listOf("/省流", "草", "[图片]", "[动画表情]", "?")

private fun GroupMessageEvent.isNotFilterKeyword(): Boolean =
    (message.content.trim() !in filterKeywords) && (!this.message.contains(LightApp))


suspend fun GroupMessageEvent.spy() = if (isNotFilterKeyword()) Repo.run {
    saveUserNickname(
        group.id.toString(), sender.id.toString(), sender.remarkOrNameCardOrNick
    )
    saveMessage(group.id.toString(), sender.id.toString(), getShengLiuMessage())
} else Unit
