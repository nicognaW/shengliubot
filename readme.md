# 省流bot

在 QQ 群中，大多数人并不是无时无刻都看着群内消息的，我们经常想要加入一场讨论，但需要手动翻阅聊天记录来获取上下文。

省流 bot 将群聊的消息记录下来，在有人想要加入聊天时利用 OpenAI 提供的 LLM API 对过去一小段时间内的聊天记录进行总结、摘要，从而节省时间。

## 效果

省流 bot 的最终目的时实现类似这样的效果：

> 以下文本为某真实聊天记录的人工总结

```
1.【用户1】的提到自己的电脑被一套带走了但幸好按原价赔了，【用户2】和【用户3】提到了水冷散热器漏液问题，所以【用户1】的电脑可能是水冷散热出了问题。【用户2】和【用户4】安慰【用户1】表示原价陪值得庆幸，而【用户1】认为刚配的电脑又要重新组很麻烦。【用户4】安慰【用户1】表示可以换个配置玩。结论：【用户1】的电脑可能因水冷散热器漏液出问题被一套带走，尽管得到了原价赔偿，但需要重新组装新电脑，引发了他的不满。
2.【用户2】认为水冷比风冷好装，风冷太大的话很挡事，双塔风冷还会挡内存和显卡，难以忍受，但【用户5】认为风扇换个地方双塔就不挡了；【用户5】则表达水冷相对较为危险，但如果不漏液则又好看又能压；【用户6】加入话题表示风冷得看机箱。结论：【用户2】和【用户5】对水冷和风冷的优缺点有不同看法，【用户6】则认为风冷的效果取决于机箱的设计。
3.【用户3】表示虽然大多数人认为风冷很吵，但他不觉得，【用户4】提到如果使用bios拉曲线，就不会吵，并提出了只要会拉曲线，20把扇子都安静的指标，【用户6】也认同这个观点，【用户7】和【用户8】则表示耳机对抗风扇噪音的功效，【用户3】调侃到娃娃会着火。结论：虽然风冷被认为噪音大，但通过调整BIOS曲线可以降低噪音，耳机也可以抵消噪音。【用户3】则以幽默的方式表达了对噪音的无所谓。
4.【用户5】坦白自己并不会拉曲线并且至今都是随便设置的，网上也没有作业可以抄，【用户6】对不拉曲线时风冷的噪音感同身受，之后提到自己七把扇子刚开始没拉曲线开机声音巨大甚至一度认为出问题了。结论：【用户5】和【用户6】都表示自己不擅长调整BIOS曲线，这导致他们的风冷系统噪音较大，甚至误以为电脑出现问题。
5.【用户4】举例说自己使用两个d5泵以及14个扇子仍然保持安静；【用户1】和【用户4】使用幽默风趣的方式表示 Intel CPU 散热难压。结论：【用户4】成功地通过调整使得其拥有两个D5泵和14个扇子的系统保持安静，而【用户1】和【用户4】则以幽默的方式表达了Intel CPU散热的难度。
```

在 2023 年 5 月份的一次技术预览测试中，省流 bot 达成了类似如下的效果：

```
1. 【用户1】、【用户2】和【用户3】讨论了老板或导员深夜发信息给他们的情况,【用户2】表示老板发语音信息给他他都会选择"收到"然后关闭,而【用户3】则在老板半夜2点找他时都是把信息震动起来。
2. 【用户2】抱怨小红书给他推送的都是垃圾信息,于是他把小红书卸载了。【用户2】也发了一张今天开会的图片。
3. 【用户4】和【用户5】对【用户6】发出疑问,【用户6】回应【用户5】问他有什么事。
4. 【用户2】评论【用户6】只吃嫩菜不吃肉,【用户6】解释是因为锅里只有嫩菜、黄瓜和紫甘蓝,【用户2】觉得只吃这样很痛苦,而【用户7】则表示想吃肉。
5. 【用户6】买的紫薯很苦,【用户8】和【用户9】表示苦的紫薯可能变质或有虫,不宜食用。【用户2】提醒【用户6】紫薯可以蒸。

结论:用户讨论了深夜情况下领导或导师发来的信息,以及推送垃圾广告和卸载软件的问题。用户【用户6】买的紫薯由于发苦被指可能变质或有虫,提醒他应蒸煮后食用。用户还讨论了【用户6】只吃青菜的餐桌情况。
```

## 起手

1. 同步依赖、构建

> POSIX 就是 macOS / Linux, NT 就是 Windows
>
> 如果你使用 Intellij Idea，直接打开项目即可，无需额外输入以下指令

- POSIX

  ```shell
  ./gradlew build
  ```

- NT

  ```shell
  ./gradlew.bat build
  ```

2. 本地启动一个 redis

3. 在 `OPENAI_SECRET_KEY` 环境变量中填入 OpenAI Secret Key

4. 在 `BOT_ID` 环境变量中填入QQ号

5. 启动bot

> 如果你使用 Intellij Idea，直接运行 `main.kt` 中的 `main` 函数即可

- POSIX

  ```shell
  ./gradlew run
  ```

- NT

  ```shell
  ./gradlew.bat run
  ```

默认使用手表协议，启动后扫码登录，详情请参考 Mirai 文档，Mirai 的使用方法本文不做介绍。

## 详细文档与其他功能实现

首先，我们的源代码是我们项目的核心。它包含了所有的功能和操作，是项目运行的基础。我们的源代码量并不大，这是因为我们注重代码的效率和优化。我们相信，优秀的代码不在于长度，而在于其能够高效、准确地执行任务。

我们鼓励所有的用户和开发者阅读我们的源代码。通过阅读源代码，你不仅可以了解到代码的运行机制，还可以学习到我们的编程风格和习惯。此外，阅读源代码也是理解我们项目的最好方式。你可以看到我们如何从零开始，逐步构建出这个项目的。

在阅读源代码的过程中，如果你有任何疑问或者不理解的地方，我们都欢迎你提出。我们的团队会尽力解答你的问题，帮助你理解我们的代码。

总的来说，我们的源代码是开放的，我们鼓励所有人阅读和学习。我们相信，这不仅可以帮助你理解我们的项目，还可以提升你的编程技能。

**省流：源代码不多，请自己阅读源代码**

## 使用更好的prompt

通常为了实现更好的效果，你需要替换[此处](src/main/kotlin/AI.kt#L27)的prompt

如果你有 promp 可以分享，欢迎发送 issues 或 pr

## 使用其他AI后端

如果你想使用别的 AI 后端来处理省流，你需要修改根作用域内的这个函数：

```kotlin
fun getShengLiu(messages: String): String?
```

输入参数 `messages` 格式类似:

```text
1:你听说了吗
2:听说什么
1:那件事
2:那件事啊，听说了
```

这个函数返回输入对话的省流（摘要）结果，例如：

```text
用户1询问用户2是否听说某事，用户2表示听说了
```
