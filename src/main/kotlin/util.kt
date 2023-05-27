import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.content

fun GroupMessageEvent.getShengLiuMessage(): String = this.message.joinToString("") { it.content.replace("\\n", " ") }
