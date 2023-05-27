import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.newClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


object Repo {
    private object Utils {
        fun keyValueListToMap(keyValueList: List<String>): Map<String, String> {
            val resultMap = mutableMapOf<String, String>()

            if (keyValueList.isNotEmpty()) {
                var index = 0
                while (index < keyValueList.size) {
                    val userId = keyValueList[index]
                    val nickname = keyValueList[index + 1]
                    resultMap[userId] = nickname
                    index += 2
                }
            }

            return resultMap
        }
    }

    private val client = newClient(Endpoint.from(redisUrl))

    suspend fun saveWarningEvents(event: String, reason: String, stack: String) {
        val timestamp = System.currentTimeMillis().toString()
        client.hset(
            "warning_event:$timestamp", "event" to event, "reason" to reason, "stack" to stack, "timestamp" to timestamp
        )
    }

    suspend fun saveUserNickname(groupId: String, userId: String, nickname: String) {
        val key = "user_nickname:$groupId"
        client.hset(key, userId to nickname)
    }

    suspend fun readUserNickname(groupId: String, userId: String): String? {
        val key = "user_nickname:$groupId"
        return client.hget(key, userId)
    }

    suspend fun readUserNicknames(groupId: String): Map<String, String> {
        val key = "user_nickname:$groupId"
        val keyValueList = client.hgetAll(key)
        return Utils.keyValueListToMap(keyValueList)
    }

    suspend fun saveMessage(groupId: String, name: String, content: String) {
        val key = "message:$groupId"
        val value = "$name: $content"
        client.rpush(key, value)
        client.ltrim(key, -SHENGLIU_BOOTSTRAP_MIN, -1)
    }

    suspend fun readMessages(groupId: String): List<String> {
        val key = "message:$groupId"
        val messages = client.lrange(key, 0, -1)
        return messages
    }

    suspend fun clearMessages(groupId: String) {
        val key = "message:$groupId"
        client.del(key)
    }

    suspend fun countMessages(groupId: String): Long {
        val key = "message:$groupId"
        return client.llen(key)
    }

    suspend fun saveShengLiu(groupId: String, shengLiu: String) {
        val key = "shengliu:$groupId"
        client.lpush(key, shengLiu)
        client.ltrim(key, 0, 2)
        client.set(
            "$key:last",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy'年'M'月'd'日'H'点'm'分'", Locale.CHINA))
        )
    }

    suspend fun countShengLiu(groupId: String): Long {
        val key = "shengliu:$groupId"
        return client.llen(key)
    }


    suspend fun readShengLiuLastTime(groupId: String): String? {
        val key = "shengliu:$groupId"
        return client.get("$key:last")
    }

    suspend fun readShengliu(groupId: String): List<String> {
        val key = "shengliu:$groupId"
        return client.lrange(key, 0, -1)
    }

    suspend fun save_shengliu_logs(shengliu_input: String, shengliu_output: String) {
        val timestamp = System.currentTimeMillis().toString()
        val key = "shengliu_logs:${timestamp}"
        client.hset(key, "input" to shengliu_input, "output" to shengliu_output)
    }
}