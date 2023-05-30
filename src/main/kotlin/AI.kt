import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.ProxyConfig
import com.aallam.openai.client.RetryStrategy
import kotlin.time.Duration.Companion.minutes

val openAI = OpenAI(
    OpenAIConfig(
        token = System.getenv("OPENAI_SECRET_KEY"),
        timeout = Timeout(socket = 2.minutes),
        proxy = ProxyConfig.Http("http://127.0.0.1:7890"),
        retry = RetryStrategy(10, 2.0, 3.minutes),
    )
)


@OptIn(BetaOpenAI::class)
object ShengLiuPromptTemplate {
    fun get(messages: String): List<ChatMessage> = listOf(
        ChatMessage(
            role = ChatRole.Assistant, content = """请总结以下聊天记录："""
        ),
        ChatMessage(
            role = ChatRole.User, content = messages
        ),
    )
}

@OptIn(BetaOpenAI::class)
suspend fun getShengLiu(messages: String): String? = openAI.chatCompletion(
    ChatCompletionRequest(
        model = ModelId("gpt-3.5-turbo"), messages = ShengLiuPromptTemplate.get(messages),
        temperature = 0.45,
        maxTokens = 512,
        frequencyPenalty = 0.25,
        presencePenalty = 0.5,
    )
).choices[0].message?.content
