package org.nim.structure

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.openapi.editor.Editor
import org.nim.NimFile

class NimTreeStructureViewBuilder(val nimFile: NimFile) : TreeBasedStructureViewBuilder() {
    override fun createStructureViewModel(editor: Editor?): StructureViewModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}