package org.nim.sdk

import com.intellij.openapi.projectRoots.*
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import org.jdom.Element
import java.io.File

class NimSdkType : SdkType("NimSDK") {

    override fun getPresentableName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isValidSdkHome(path: String?): Boolean {
        return if (path != null) {
            val root = File(FileUtil.toSystemDependentName(path))
            File(root, "system.nim").isFile && File(root, "stdlib.nim").isFile
        } else {
            false
        }
    }

    override fun suggestSdkName(currentSdkName: String?, sdkHome: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun suggestHomePath(): String? {
        return when {
            SystemInfo.isLinux -> "/usr/lib/nim"
            else -> null
        }
    }

    override fun createAdditionalDataConfigurable(sdkModel: SdkModel, sdkModificator: SdkModificator): AdditionalDataConfigurable? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveAdditionalData(additionalData: SdkAdditionalData, additional: Element) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}