package org.nim.sdk;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Version;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NimVersionUtil {


    /**
     *   NimMajor* {.intdefine.}: int = 0
     *     ## is the major number of Nim's version.
     *
     *   NimMinor* {.intdefine.}: int = 18
     *     ## is the minor number of Nim's version.
     *
     *   NimPatch* {.intdefine.}: int = 0
     *     ## is the patch number of Nim's version.
     */
    private static final String START = "  NimMajor";
    private static final String BASE_PATTERN = "\\* \\{\\.intdefine\\.\\}: int = (\\d+)";
    private static final Pattern MAJOR = Pattern.compile("\\s*NimMajor" + BASE_PATTERN);
    private static final Pattern MINOR = Pattern.compile("\\s*NimMinor" + BASE_PATTERN);
    private static final Pattern PATCH = Pattern.compile("\\s*NimPatch" + BASE_PATTERN);


    @Nullable
    public static Version findVersion(@NotNull VirtualFile vf)  {
        return ApplicationManager.getApplication().runReadAction((Computable<Version>) ()->{
            try(InputStream inputStream = vf.getInputStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                // The version stuff happens around line 3500, 7000 chars should be a good number to skip without passing it
                reader.skip(7000);
                String line = reader.readLine();
                while (line != null) {
                    if (line.startsWith(START)) {
                        break;
                    } else {
                        line = reader.readLine();
                    }
                }
                if (line == null) {
                    return null;
                }
                Matcher matcher = MAJOR.matcher(line);
                Integer major = null;
                if (matcher.matches()) {
                    major = Integer.valueOf(matcher.group(1));
                } else {
                    return null;
                }
                var minor = read(reader, MINOR);
                var patch = read(reader, PATCH);
                reader.close();
                return new Version(major, minor.orElse(0), patch.orElse(0));
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        });

    }

    private static Optional<Integer> read(BufferedReader reader, Pattern pattern) throws IOException {
        String line = reader.readLine();
        while(line != null){
            Matcher matcher = pattern.matcher(line);
            if(matcher.matches()){
                return Optional.of(Integer.valueOf(matcher.group(1)));
            } else {
                line = reader.readLine();
            }
        }
        return Optional.empty();
    }


}
