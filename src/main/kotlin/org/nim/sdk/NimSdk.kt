package org.nim.sdk

import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.projectRoots.SdkAdditionalData
import com.intellij.openapi.projectRoots.SdkModificator
import com.intellij.openapi.projectRoots.SdkTypeId
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.RootProvider
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile

class NimSdk : Sdk, SdkModificator {
    override fun <T : Any?> getUserData(key: Key<T>): T? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSdkModificator(): SdkModificator {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRootProvider(): RootProvider {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : Any?> putUserData(key: Key<T>, value: T?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHomePath(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getCompilerPath(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getVersionString(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun getSdkAdditionalData(): SdkAdditionalData? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun clone(): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSdkType(): SdkTypeId {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHomeDirectory(): VirtualFile? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addRoot(root: VirtualFile, rootType: OrderRootType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun commitChanges() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeAllRoots() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isWritable(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setName(name: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun getRoots(rootType: OrderRootType): Array<VirtualFile> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeRoots(rootType: OrderRootType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setVersionString(versionString: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setHomePath(path: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeRoot(root: VirtualFile, rootType: OrderRootType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setSdkAdditionalData(data: SdkAdditionalData?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}