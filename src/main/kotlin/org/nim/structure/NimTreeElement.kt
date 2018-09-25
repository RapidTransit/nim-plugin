package org.nim.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import org.nim.psi.impl.NimProcImpl
import org.nim.psi.impl.NimTypeImpl
import org.nim.psi.impl.NimType_Impl
import org.nim.psi.impl.NimVarInitImpl

class NimTreeElement(val element: NavigatablePsiElement) : StructureViewTreeElement, SortableTreeElement {

    override fun navigate(requestFocus: Boolean) = element.navigate(requestFocus)


    override fun getPresentation(): ItemPresentation {
        when(element){
            is NimProcImpl -> TODO("not implemented")
            is NimTypeImpl -> TODO("not implemented")
            else -> TODO("not implemented")
        }
    }

    override fun getChildren(): Array<TreeElement> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun canNavigate(): Boolean = element.canNavigate()


    override fun getValue(): Any = element

    override fun canNavigateToSource(): Boolean = element.canNavigateToSource()

    override fun getAlphaSortKey(): String = element.name.orEmpty()
}