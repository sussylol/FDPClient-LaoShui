package net.ccbluex.liquidbounce.features.module.modules.misc

import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EntityKilledEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.file.FileManager
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.FileUtils
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
import net.ccbluex.liquidbounce.value.TextValue
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.opengl.Display
import java.io.File

@ModuleInfo(name = "KillInsults", category = ModuleCategory.MISC)
object KillInsults : Module() {
    private val prefixValue = TextValue("Prefix", "@")

    val modeValue = ListValue(
        "Mode", arrayOf(
            "Clear",
            "WithWords",
            "RawWords"
        ), "Clear"
    )
    private val clientname = BoolValue("ClientName", true)
    private val waterMarkValue = BoolValue("WaterMark", true)
    private val waterMark2Value = BoolValue("Add front watermark", false)

    private val insultFile = File(LiquidBounce.fileManager.dir, "insult.json")
    var insultWords = mutableListOf<String>()

    init {
        loadFile()
    }

    fun loadFile() {
        fun convertJson() {
            insultWords.clear()
            insultWords.addAll(insultFile.readLines(Charsets.UTF_8).filter { it.isNotBlank() })

            val json = JsonArray()
            insultWords.map { JsonPrimitive(it) }.forEach(json::add)
            insultFile.writeText(FileManager.PRETTY_GSON.toJson(json), Charsets.UTF_8)
        }

        try {
            // check file exists
            if (!insultFile.exists()) {
                FileUtils.unpackFile(insultFile, "assets/minecraft/fdpclient/misc/insult.json")
            }
            // read it
            val json = JsonParser().parse(insultFile.readText(Charsets.UTF_8))
            if (json.isJsonArray) {
                insultWords.clear()
                json.asJsonArray.forEach {
                    insultWords.add(it.asString)
                }
            } else {
                // not jsonArray convert it to jsonArray
                convertJson()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            convertJson()
        }
    }

    fun getRandomOne(): String {
        return insultWords[RandomUtils.nextInt(0, insultWords.size - 1)]
    }

    @EventTarget
    fun onKilled(event: EntityKilledEvent) {
        if (Display.getTitle() == "${LiquidBounce.CLIENT_NAME} - ${LiquidBounce.L}${LiquidBounce.S} ${LiquidBounce.CLIENT_VERSION} (${LiquidBounce.CLIENT_BRANCH}) 项目开源地址:${LiquidBounce.WEBSITE} 官方群:1028574302 禁止一切商用行为"){
            ClientUtils.logInfo("Detected by gettitle")
        } else {
            ClientUtils.logError("Failed settitle detection")
            LiquidBounce.initClient()
        }

        val target = event.targetEntity

        if (target !is EntityPlayer) {
            return
        }

        when (modeValue.get().lowercase()) {
            "clear" -> {
                sendInsultWords("L ${target.name} kill within ${LiquidBounce.CLIENT_NAME} for ${LiquidBounce.L}${LiquidBounce.S} ", target.name)
            }
            "withwords" -> {
                sendInsultWords("L ${target.name} kill within ${LiquidBounce.CLIENT_NAME} for ${LiquidBounce.L}${LiquidBounce.S} " + getRandomOne(), target.name)
            }
            "rawwords" -> {
                sendInsultWords(getRandomOne(), target.name + "kill within ${LiquidBounce.CLIENT_NAME} for ${LiquidBounce.L}${LiquidBounce.S}")
            }
        }
    }

    private fun sendInsultWords(msg: String, name: String) {
        var message = msg.replace("%name%", name)

        if (waterMarkValue.get() && clientname.get()) {
            message = "[${LiquidBounce.CLIENT_NAME} for ${LiquidBounce.L}${LiquidBounce.S}] $message"
        }
        else{
            message = "$message"
        }

        if (waterMark2Value.get() && clientname.get()) {
            message = prefixValue.get() + "[${LiquidBounce.CLIENT_NAME} for ${LiquidBounce.L}${LiquidBounce.S}] " + " $message"
        }
        else{
            message = prefixValue.get() + " $message"
        }

        if (Display.getTitle() == "${LiquidBounce.CLIENT_NAME} - ${LiquidBounce.L}${LiquidBounce.S} ${LiquidBounce.CLIENT_VERSION} (${LiquidBounce.CLIENT_BRANCH}) 项目开源地址:${LiquidBounce.WEBSITE} 官方群:1028574302 禁止一切商用行为"){
            ClientUtils.logInfo("Detected by gettitle")
        } else {
            ClientUtils.logError("Failed settitle detection")
            LiquidBounce.initClient()
        }

        mc.thePlayer.sendChatMessage(message)
    }

    override val tag: String
        get() = modeValue.get()
}