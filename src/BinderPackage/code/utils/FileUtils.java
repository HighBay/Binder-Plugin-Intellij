package BinderPackage.code.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Aaron on 3/7/2016.
 */
public class FileUtils {


    //save file.
    public static void writeFileForLinesList(List<String> lines, File file) throws IOException {
        FileWriter writer = new FileWriter(file.getPath());
        for (String str : lines) {
            writer.write(str + "\n");
        }
        writer.close();
    }

    //Finding a file in the project.
    public static File findFileInProject(Project project, String name) {
        File result = null;
        if (project.getBasePath() != null) {
            File dir = new File(project.getBasePath());
            result = findFile(dir, name);
        }
        return result;
    }


    //Finding a file in the
    private static File findFile(File dir, String name) {
        File result = null; // no need to store result as String, you're returning File anyway
        File[] dirlist = dir.listFiles();
        for (int i = 0; i < dirlist.length; i++) {
            if (dirlist[i].isDirectory()) {
                result = findFile(dirlist[i], name);
                if (result != null) break; // recursive call found the file; terminate the loop
            } else if (dirlist[i].getName().matches(name)) {
                return dirlist[i]; // found the file; return it
            }
        }
        return result; // will return null if we didn't find anything
    }


    public static File getAndroidManifestFileForApp(Project mProject) {
        File manifest = null;
        VirtualFile[] vfs = mProject.getBaseDir().getChildren();
        for (VirtualFile vf : vfs) {
            if (vf.getName().equals("app")) {
                VirtualFile[] appvfs = vf.getChildren();
                for (VirtualFile appvf : appvfs) {
//                    System.out.print("VIRTUAL FILE APP ***********************************" + appvf.getName() + "\n");
                    if (appvf.getName().equals("src")) {
                        VirtualFile[] srcvfs = appvf.getChildren();
                        for (VirtualFile srcvf : srcvfs) {
//                            System.out.print("VIRTUAL FILE SRC ***********************************" + srcvf.getName() + "\n");
                            if (srcvf.getName().equals("main")) {
                                VirtualFile[] mainvfs = srcvf.getChildren();
                                for (VirtualFile mainvf : mainvfs) {
//                                    System.out.print("VIRTUAL FILE MAIN ***********************************" + mainvf.getName() + "\n");
                                    if (mainvf.getName().equals("AndroidManifest.xml")) {
                                        //print manifest.
                                        manifest = new File(mainvf.getCanonicalPath());
                                    }
                                }
                            }
                        }
                    }
                }
            }
//            System.out.print("VIRTUAL FILE  ***********************************" + vf.getName() + "\n");
        }
        return manifest;
    }

    //Get the path to the build.gradle file.
    public static String getAppGradleFilePath(Project mProject) throws IOException {
        String gradlePath = null;
        VirtualFile[] vfs = mProject.getBaseDir().getChildren();
        for (VirtualFile vf : vfs) {
            if (vf.getName().equals("app")) {
                VirtualFile[] appvfs = vf.getChildren();
                for (VirtualFile appvf : appvfs) {
                    if (appvf.getName().equals("build.gradle")) {
                        //get gradle path
                        gradlePath = appvf.getCanonicalPath();
                    }
                }
            }
        }
        return gradlePath;
    }

}
