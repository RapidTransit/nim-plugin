package org.nim.nimble;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * = Nimble Entry
 *
 * A NimbleEntry is part of a list of https://github.com/nim-lang/packages[Nimble Packages]
 *
 * [source,json]
 * ----
 *[
 *   {
 *     "name": "nimrun",
 *     "url": "https://github.com/lee-b/nimrun",
 *     "method": "git",
 *     "tags": [
 *       "shebang",
 *       "unix",
 *       "linux",
 *       "bsd",
 *       "mac",
 *       "shell",
 *       "script",
 *       "nimble",
 *       "nimcr",
 *       "compile",
 *       "run",
 *       "standalone"
 *     ],
 *     "description": "Shebang frontend for running nim code as scripts. Does not require .nim extensions.",
 *     "license": "MIT",
 *     "web": "https://github.com/lee-b/nimrun"
 *   },
 *   {
 *     "name": "sequtils2",
 *     "url": "https://github.com/Michedev/sequtils2",
 *     "method": "git",
 *     "tags": [
 *       "library",
 *       "sequence",
 *       "string",
 *       "openArray",
 *       "functional"
 *     ],
 *     "description": "Additional functions for sequences that are not present in sequtils",
 *     "license": "MIT",
 *     "web": "http://htmlpreview.github.io/?https://github.com/Michedev/sequtils2/blob/master/sequtils2.html"
 *   },
 *   {
 *     "name": "github_api",
 *     "url": "https://github.com/watzon/github-api-nim",
 *     "method": "git",
 *     "tags": [
 *       "library",
 *       "api",
 *       "github",
 *       "client"
 *     ],
 *     "description": "Nim wrapper for the GitHub API",
 *     "license": "WTFPL",
 *     "web": "https://github.com/watzon/github-api-nim"
 *   },
 * ----
 *
 */
public class NimbleEntry implements Serializable {

    /**
     * This May be bad in the future if another type of mechanism has a dash in it or something
     *
     * Taken From:
     * https://github.com/nim-lang/packages/blob/5ccf87564977f8f049daed9e9995cbf690111bce/package_scanner.nim#L51
     */
    enum VCS{ git, hg }

    /**
     * List of License Types from
     * https://github.com/nim-lang/packages/blob/5ccf87564977f8f049daed9e9995cbf690111bce/package_scanner.nim#L29
     */
    private static final List<String> LICENSE = Arrays.asList(
            "Allegro 4 Giftware",
            "Apache License 2.0",
            "BSD",
            "BSD2",
            "BSD3",
            "CC0",
            "GPL",
            "GPLv2",
            "GPLv3",
            "LGPLv2",
            "LGPLv3",
            "MIT",
            "MS-PL",
            "MPL",
            "WTFPL",
            "libpng",
            "zlib",
            "ISC",
            "Unlicense");

    protected String name;
    protected String alias;
    protected String url;
    protected VCS method;
    protected List<String> tags = new ArrayList<>();
    protected String description;
    protected String license;
    protected String web;
}
