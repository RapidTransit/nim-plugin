/**
 * = Stubs
 *
 * == _What are Stubs?_
 *
 * The Parsers AST (Abstract Syntax Tree) is *resource expensive* Stubs are a way to hold important information about
 * an AST node for instance we have a class and it has methods, we don't really care what is inside the method but
 * what we do care about is the names, parameters, and return types. These get serialized and are a compact way to look
 * up information without having to have a huge AST in memory.
 *
 * == Stub Style Guide
 *
 * * Stub's Should be placed in package {@link org.nim.stubs.impl}
 * * Stub's first field should be `public static final String NAME = "THE_NAME";`
 * * The Second field should be the adapter example:
 *  `public static final NimStubAdapter<NimTypeStub, NimTypeDeclaration> ADAPTER =`
 * ** The Adapter should be an anonymous inner class separated by one space
 * ** The {@link com.intellij.psi.stubs.ObjectStubSerializer#deserialize(com.intellij.psi.stubs.StubInputStream, com.intellij.psi.stubs.Stub)}
 *    method should rename the the StubInputStream to `ds`
 * *** Each Constructor Parameter should be placed on a new line
 * *** When reading from the stream a comment should be above what parameter it is deserializing
 *
 */
package org.nim.stubs;