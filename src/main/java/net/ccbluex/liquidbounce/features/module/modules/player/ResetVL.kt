/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/laoshuikaixue/FDPClient
 */
package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(name = "ResetVL", category = ModuleCategory.PLAYER)
class ResetVL : Module() {
    private var jumped = 0
    private var y = 0.0

    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (mc.thePlayer.onGround) {
            if (jumped <= 25) {
                mc.thePlayer.motionY = 0.11
                jumped++
            }
        }
        if (jumped <= 25) {
            mc.thePlayer.posY = y
            mc.timer.timerSpeed = 2.25f
        } else {
            mc.timer.timerSpeed = 1F
            toggle()
        }
    }

    override fun onEnable() {
        jumped = 0
        y = mc.thePlayer.posY
    }

    override fun onDisable() {
        mc.timer.timerSpeed = 1F
    }
}