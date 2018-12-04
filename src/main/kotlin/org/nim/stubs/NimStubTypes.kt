package org.nim.stubs

import com.intellij.psi.stubs.IStubElementType

object NimStubTypes {
    fun stubFactory(name: String) : IStubElementType<*, *> = when(name){

        else -> error("Unknown element $name")
    }
}