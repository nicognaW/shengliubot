import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.buildForwardMessage
import net.mamoe.mirai.message.data.content


const val SHENGLIU_BOOTSTRAP_MIN = 80

suspend fun shengLiuPostProcess(shengLiuRawResult: String, groupId: String): String =
    Repo.readUserNicknames(groupId).entries.fold(shengLiuRawResult) { result, entry ->
        result.replace(entry.key, "【@${entry.value}】")
    }

suspend fun GroupMessageEvent.shengLiu() = with(group.id.toString()) {
    val currentMessagesCount = Repo.countMessages(this)
    when {
        message.content.trim() != "/省流" -> return@with
        currentMessagesCount < SHENGLIU_BOOTSTRAP_MIN && Repo.countShengLiu(this) < 1 -> {
            group.sendMessage(
                "记录的消息数量不足 $SHENGLIU_BOOTSTRAP_MIN 条，无法省流，当前 $currentMessagesCount 条"
            )
            return@with
        }

        currentMessagesCount >= SHENGLIU_BOOTSTRAP_MIN -> {
            group.sendMessage("开始处理，请稍等……")
            val shengLiuInputString = Repo.readMessages(this).joinToString("\n")
            getShengLiu(shengLiuInputString).let {
                if (it == null) {
                    group.sendMessage("未知错误")
                    return@with
                }
                Repo.saveShengLiu(this, shengLiuPostProcess(it, this))
                Repo.save_shengliu_logs(shengLiuInputString, it)
                Repo.clearMessages(this)
            }
        }
    }

    group.sendMessage(buildForwardMessage {
        if (currentMessagesCount < SHENGLIU_BOOTSTRAP_MIN) {
            add(
                botId, "省流bot", PlainText("本次无新增省流，进度：[$currentMessagesCount/$SHENGLIU_BOOTSTRAP_MIN]")
            )
        }
        add(
            botId, "省流bot", PlainText(
                "省流bot给你带来本群消息的省流（更新于${Repo.readShengLiuLastTime(this@with)}）："
            )
        )
        Repo.readShengliu(this@with).forEach { add(botId, "省流bot", PlainText(it)) }
    })
}
