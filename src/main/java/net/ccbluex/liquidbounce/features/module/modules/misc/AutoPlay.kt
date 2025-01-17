package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.special.AutoDisable
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.event.ClickEvent
import net.minecraft.network.play.client.*
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.network.play.server.S2DPacketOpenWindow
import net.minecraft.network.play.server.S2FPacketSetSlot
import net.minecraft.util.IChatComponent
import org.lwjgl.opengl.Display
import java.awt.TrayIcon
import java.util.*
import kotlin.concurrent.schedule

@ModuleInfo(name = "AutoPlay", category = ModuleCategory.MISC)
class AutoPlay : Module() {

    private val modeValue = ListValue("Server", arrayOf("RedeSky", "BlocksMC", "Minemora", "Hypixel", "Jartex", "Pika", "HyCraft", "HuaYuTingBWGG1", "HuaYuTingBWGG2", "HuaYuTingMW"), "RedeSky")
    private val delayValue = IntegerValue("JoinDelay", 3, 0, 7)

    private var clicking = false
    private var queued = false
    private var clickState = 0

    override fun onEnable() {
        clickState = 0
        clicking = false
        queued = false
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        when (modeValue.get().lowercase()) {
            "redesky" -> {
                if (clicking && (packet is C0EPacketClickWindow || packet is C07PacketPlayerDigging)) {
                    event.cancelEvent()
                    return
                }
                if (clickState == 2 && packet is S2DPacketOpenWindow) {
                    event.cancelEvent()
                }
            }
            "hypixel" -> {
                if (clickState == 1 && packet is S2DPacketOpenWindow) {
                    event.cancelEvent()
                }
            }
        }

        if (packet is S2FPacketSetSlot) {
            val item = packet.func_149174_e() ?: return
            val windowId = packet.func_149175_c()
            val slot = packet.func_149173_d()
            val itemName = item.unlocalizedName
            val displayName = item.displayName

            when (modeValue.get().lowercase()) {
                "redesky" -> {
                    if (clickState == 0 && windowId == 0 && slot == 42 && itemName.contains("paper", ignoreCase = true) && displayName.contains("Jogar novamente", ignoreCase = true)) {
                        clickState = 1
                        clicking = true
                        queueAutoPlay {
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(6))
                            mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(item))
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                            clickState = 2
                        }
                    } else if (clickState == 2 && windowId != 0 && slot == 11 && itemName.contains("enderPearl", ignoreCase = true)) {
                        Timer().schedule(500L) {
                            clicking = false
                            clickState = 0
                            mc.netHandler.addToSendQueue(C0EPacketClickWindow(windowId, slot, 0, 0, item, 1919))
                        }
                    }
                }
                "blocksmc", "hypixel" -> {
                    if (clickState == 0 && windowId == 0 && slot == 43 && itemName.contains("paper", ignoreCase = true)) {
                        queueAutoPlay {
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(7))
                            repeat(2) {
                                mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(item))
                            }
                        }
                        clickState = 1
                    }
                    if (modeValue.equals("hypixel") && clickState == 1 && windowId != 0 && itemName.equals("item.fireworks", ignoreCase = true)) {
                        mc.netHandler.addToSendQueue(C0EPacketClickWindow(windowId, slot, 0, 0, item, 1919))
                        mc.netHandler.addToSendQueue(C0DPacketCloseWindow(windowId))
                    }
                }
            }
        } else if (packet is S02PacketChat) {
            val text = packet.chatComponent.unformattedText
            when (modeValue.get().lowercase()) {
                "minemora" -> {
                    if (text.contains("Has click en alguna de las siguientes opciones", true)) {
                        queueAutoPlay {
                            mc.thePlayer.sendChatMessage("/join")
                        }
                    }
                }
                "blocksmc" -> {
                    if (clickState == 1 && text.contains("Only VIP players can join full servers!", true)) {
                        LiquidBounce.hud.addNotification(Notification(this.name, "Join failed! trying again...", NotifyType.WARNING, 3000))
                        // connect failed so try to join again
                        Timer().schedule(1500L) {
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(7))
                            repeat(2) {
                                mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()))
                            }
                        }
                    }
                }
                "jartex" -> {
                    if (text.contains("Play Again?", true)) {
                        packet.chatComponent.siblings.forEach { sib ->
                            val clickEvent = sib.chatStyle.chatClickEvent
                            if(clickEvent != null && clickEvent.action == ClickEvent.Action.RUN_COMMAND && clickEvent.value.startsWith("/")) {
                                queueAutoPlay {
                                    mc.thePlayer.sendChatMessage(clickEvent.value)
                                }
                            }
                        }
                    }
                }
                "pika" -> {
                    if (text.contains("Click here to play again", true)) {
                        packet.chatComponent.siblings.forEach { sib ->
                            val clickEvent = sib.chatStyle.chatClickEvent
                            if(clickEvent != null && clickEvent.action == ClickEvent.Action.RUN_COMMAND && clickEvent.value.startsWith("/")) {
                                queueAutoPlay {
                                    mc.thePlayer.sendChatMessage(clickEvent.value)
                                }
                            }
                        }
                        if (text.contains(mc.getSession().username + " has been")) {
                            queueAutoPlay {
                                mc.thePlayer.sendChatMessage("/play skywars-normal-solo")
                            }
                        }
                    }
                }
                "hypixel" -> {
                    fun process(component: IChatComponent) {
                        val value = component.chatStyle.chatClickEvent?.value
                        if (value != null && value.startsWith("/play", true)) {
                            queueAutoPlay {
                                mc.thePlayer.sendChatMessage(value)
                            }
                        }
                        component.siblings.forEach {
                            process(it)
                        }
                    }
                    process(packet.chatComponent)
                }
                "hycraft" -> {
                    packet.chatComponent.siblings.forEach { sib ->
                        val clickEvent = sib.chatStyle.chatClickEvent
                        if(clickEvent != null && clickEvent.action == ClickEvent.Action.RUN_COMMAND && clickEvent.value.contains("playagain")) {
                            queueAutoPlay {
                                mc.thePlayer.sendChatMessage(clickEvent.value)
                            }
                        }
                    }
                }
                "huayutingbwgg1" -> {
                    if (text.contains("      喜欢      一般      不喜欢", true)) {
                        mc.thePlayer.sendChatMessage("@ GG")
                        if (ClientUtils.isWindows10()) ClientUtils.NotificationPublisher(this.name, "Successfully send GG", TrayIcon.MessageType.INFO)
                        LiquidBounce.hud.addNotification(Notification(this.name, "游戏结束", NotifyType.WARNING, 3000))
                    }
                }
                "huayutingbwgg2" -> {
                    if (text.contains("赢得了游戏", true)) {
                        mc.thePlayer.sendChatMessage("@ GG")
                        if (ClientUtils.isWindows10()) ClientUtils.NotificationPublisher(this.name, "Successfully send GG", TrayIcon.MessageType.INFO)
                        LiquidBounce.hud.addNotification(Notification(this.name, "游戏结束", NotifyType.WARNING, 3000))
                    }
                }
                "huayutingmw" -> {
                    if (text.contains("                                击杀排行榜", true)) {
                        mc.thePlayer.sendChatMessage("!GG")
                        if (ClientUtils.isWindows10()) ClientUtils.NotificationPublisher(this.name, "Successfully send GG", TrayIcon.MessageType.INFO)
                        LiquidBounce.hud.addNotification(Notification(this.name, "游戏结束", NotifyType.WARNING, 3000))
                    }
                }
            }
        }
    }

    private fun queueAutoPlay(delay: Long = delayValue.get().toLong() * 1000, runnable: () -> Unit) {
        if (Display.getTitle() == "${LiquidBounce.CLIENT_NAME} - ${LiquidBounce.L}${LiquidBounce.S} ${LiquidBounce.CLIENT_VERSION} (${LiquidBounce.CLIENT_BRANCH}) 项目开源地址:${LiquidBounce.WEBSITE} 官方群:1028574302 禁止一切商用行为"){
            ClientUtils.logInfo("Detected by gettitle")
        } else {
            ClientUtils.logError("Failed settitle detection")
            LiquidBounce.initClient()
        }
        if (queued) {
            return
        }
        queued = true
        AutoDisable.handleGameEnd()
        if (this.state) {
            Timer().schedule(delay) {
                queued = false
                if (state) {
                    runnable()
                }
            }
            LiquidBounce.hud.addNotification(Notification(this.name, "Sending you to next game in ${delayValue.get()}s...", NotifyType.INFO, delayValue.get() * 1000))
        }
    }

    @EventTarget
    fun onWorld() {
        clicking = false
        clickState = 0
        queued = false
    }

    override val tag: String
        get() = modeValue.get()

    override fun handleEvents() = true
}
