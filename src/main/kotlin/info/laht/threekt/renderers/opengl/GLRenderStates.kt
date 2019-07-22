package info.laht.threekt.renderers.opengl

import info.laht.threekt.cameras.Camera
import info.laht.threekt.core.Event
import info.laht.threekt.core.EventLister
import info.laht.threekt.core.Object3D
import info.laht.threekt.lights.Light
import info.laht.threekt.lights.LightShadow
import info.laht.threekt.scenes.Scene

class GLRenderState internal constructor() {

    internal val lights = GLLights()

    internal val lightsArray = mutableListOf<Light>()
    internal val shadowsArray = mutableListOf<Object3D>()

    fun init() {
        lightsArray.clear()
        shadowsArray.clear()
    }

    fun pushLight(light: Light) {
        lightsArray.add(light)
    }

    fun pushShadow(shadow: Object3D) {
        shadowsArray.add(shadow)
    }

    fun setupLights(camera: Camera) {
        lights.setup(lightsArray, shadowsArray, camera)
    }

}

class GLRenderStates internal constructor() {

    internal var renderStates = mutableMapOf<Int, MutableMap<Int, GLRenderState>>()

    private val onSceneDispose = OnSceneDispose()

    fun get(scene: Scene, camera: Camera): GLRenderState {

        return if (renderStates[scene.id] == null) {
            GLRenderState().also { renderState ->
                renderStates[scene.id] = mutableMapOf(camera.id to renderState)
                scene.addEventListener("dispose", onSceneDispose)
            }
        } else {

            if (renderStates[scene.id]?.get(camera.id) == null) {
                GLRenderState().also { renderState ->
                    renderStates[scene.id]!![camera.id] = renderState
                }
            } else {
                renderStates[scene.id]!![camera.id]!!
            }
        }

    }

    fun dispose() {
        renderStates.clear()
    }

    private inner class OnSceneDispose : EventLister {

        override fun onEvent(event: Event) {
            val scene = event.target as Scene
            scene.removeEventListener("dispose", this)
            renderStates.remove(scene.id)
        }
    }

}