package org.nim.structure

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

class NimStructureViewModel(psiFile: PsiFile, editor: Editor?, root: StructureViewTreeElement) :
        StructureViewModelBase(psiFile, editor, root), StructureViewModel.ElementInfoProvider {


    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isAlwaysLeaf(element: StructureViewTreeElement?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}