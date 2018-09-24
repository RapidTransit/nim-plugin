package org.nim.structure

import com.intellij.ide.structureView.StructureView
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.StructureViewFactory
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import org.nim.NimFile

class NimStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder? {
        if(psiFile is NimFile){

        }
    }

}