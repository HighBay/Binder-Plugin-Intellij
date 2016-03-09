package BinderPackage.code;

import BinderPackage.code.utils.ConsolePrintUtils;
import BinderPackage.code.utils.FileUtils;
import BinderPackage.code.utils.UnzipUtility;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 3/2/2016.
 */
public class BinderDownloadAndUnZipping {

    String AndroidApi = "api/Binder/distribution?platform=android";
    private URL mBaseUrl, mUrl;
    private Project mProject;


    public BinderDownloadAndUnZipping(Project project, String Url) {
        try {
            //remove the Android Api part of the string.
            this.mBaseUrl = new URL(Url.replace(AndroidApi, ""));
            this.mUrl = new URL(Url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.mProject = project;
    }

    public Path DownloadBinderFile() throws IOException {
        if (mUrl != null) {
            String projectPath = mProject.getBasePath();
            return download(mUrl, projectPath);
        } else {
            return null;
        }
    }

    public void UnZipBinderFile(Path newBinderPath, Component parent) throws IOException {
        //String chooserPath = mProject.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        String chooserPath = "";
        try {
            chooserPath = getChooserPath();
            System.out.print(chooserPath + "\n");

        } catch (IOException e) {
            System.out.print("Chooser path did not work IO Exception");
        }
        chooserPath = mProject.getBasePath() + chooserPath;
        System.out.print("FULL PATH ***************************************************************" + chooserPath + "\n");
        JFileChooser chooser = new JFileChooser(chooserPath);

        // Set the tool tip
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showDialog(parent, "Choose");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //Un zip files
            File yourFolder = chooser.getSelectedFile();
            String appModuleFilePath = yourFolder.getPath();
            String projectPath = appModuleFilePath + File.separator + "Binder";
            String binderPath = newBinderPath.toString();
            UnzipUtility unzipThis = new UnzipUtility(getPackageName());
            unzipThis.unzip(binderPath, projectPath);
            //add gradle dependencies
            BinderFileManipulation.addTextDependenciesForBinderToGradleFile(FileUtils.getAppGradleFilePath(mProject));
            //change url in webservice class. (** currently not working. **)
            if (mProject.getBasePath() != null) {
                //Find the Webservice file and then put the base url in it.
                BinderFileManipulation.changeUrlToInWebServiceClass(FileUtils.findFileInProject(mProject, "WebService.java").getPath(), mBaseUrl.toString());
                String applicationName = ApplicationFileName();
                if (applicationName != null) {
                    //Edit Application File
                    Messages.showMessageDialog(mProject, "Application File decteded add these lines to The Application File " + applicationName + "\n" +
                            "\n" +
                            "imports:\n" +
                            "import com.example.myapplication.app.Binder.DataSingleton;\n" +
                            "import com.example.myapplication.app.Binder.Libraries.DataSources;\n" +
                            "\n" +
                            "add to onCreate:\n" +
                            "DataSingleton.sharedInstance().setNewDataSource(DataSources.Webservice, this);", "Add this to the " + applicationName + " file", Messages.getInformationIcon());
                } else {
                    //Create Application File
                    AddApplicationFile();
                }
            }
        }


        ///TODO WIP Figure out how to do a gradle sync to import the files that you add to the gradle file ( either java or script wise )
    }

    private void AddApplicationFile() throws IOException {
        File manifest = FileUtils.getAndroidManifestFileForApp(mProject);
        java.util.List<String> lines = Files.readAllLines(manifest.toPath(), Charset.defaultCharset());
        ArrayList<String> applicationTag = BinderFileManipulation.getLinesWithStartAndEndLine(lines, "<application", ">");
        ConsolePrintUtils.printListIntoConsole(applicationTag, "APPTAG*************************************************");
        //add text to manifest
        int Start = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("<application")) {
                Start = i;
                break;
            }
        }
        //if it has an applcation tag ( it will )
        String appName = addApplicationNameToManifest(manifest, Start, applicationTag, lines);
        //add file to project.
        addApplicationFileToProject(appName);
    }

    private String ApplicationFileName() throws IOException {
        File manifest = FileUtils.getAndroidManifestFileForApp(mProject);
        java.util.List<String> lines = Files.readAllLines(manifest.toPath(), Charset.defaultCharset());
        ArrayList<String> applicationTag = BinderFileManipulation.getLinesWithStartAndEndLine(lines, "<application", ">");
        ConsolePrintUtils.printListIntoConsole(applicationTag, "APPTAG*************************************************");
        String appName = null;
        for (String tagString : applicationTag) {
            if (tagString.contains("android:name=")) {
                String[] tags = tagString.replace("android:name=", "").replace("\"", "").split("\\.");
                appName = tags[tags.length - 1];
                break;
            }
        }
        return appName;
    }

    private void AddApplicationIfNotThereOtherWiseEditIt() throws IOException {
        File manifest = FileUtils.getAndroidManifestFileForApp(mProject);
        java.util.List<String> lines = Files.readAllLines(manifest.toPath(), Charset.defaultCharset());
        ArrayList<String> applicationTag = BinderFileManipulation.getLinesWithStartAndEndLine(lines, "<application", ">");
        ConsolePrintUtils.printListIntoConsole(applicationTag, "APPTAG*************************************************");
        boolean hasAppFile = false;
        String appName = null;
        for (String tagString : applicationTag) {
            if (tagString.contains("android:name=")) {
                hasAppFile = true;
                String[] tags = tagString.replace("android:name=", "").replace("\"", "").split("\\.");
                appName = tags[tags.length - 1];
                break;
            }
        }
        if (hasAppFile) {

        } else {


        }

    }

    private void addApplicationFileToProject(String AppName) throws IOException {
        //TODO CREATE FILE and add it to the project.
        File newAppFile = new File(mProject.getBasePath() + File.separator + getChooserPath(), AppName + ".java");
        if (newAppFile.toPath().toFile().createNewFile()) {
            ArrayList<String> lines = new ArrayList<>();
            lines.add(0, "package " + getPackageName() + ";\n");
            lines.add("import android.app.Application;");
            lines.add("import " + getPackageName() + ".Binder.DataSingleton;");
            lines.add("import " + getPackageName() + ".Binder.Libraries.DataSources;\n");
            lines.add("public class " + AppName + " extends Application {\n");
            //add onCreate
            lines.add("    @Override\n" +
                    "    public void onCreate() {\n" +
                    "        super.onCreate();\n" +
                    "        //This is how binder knows what Data Source to use.\n" +
                    "        DataSingleton.sharedInstance().setNewDataSource(DataSources.Webservice, this);\n" +
                    "    };\n");
            lines.add("}");
            FileUtils.writeFileForLinesList(lines, newAppFile);
        }
    }

    private String addApplicationNameToManifest(File manifest, int Start, ArrayList<
            String> applicationTag, List<String> lines) throws IOException {
        ConsolePrintUtils.printToConsole("No Application File!! Creating a file for user.", "APP");
        //Show dialog to Put in ApplicationName
        String txt = Messages.showInputDialog(mProject, "No application file detected please give us a name for your application file.", "Enter Application File Name.", Messages.getQuestionIcon());
        if (StringUtils.isAlphanumeric(txt)) {
            applicationTag.add(1, "android:name=\"." + txt + "\"");
        } else {
            Messages.showMessageDialog(mProject, "Name Not Valid so we named it BinderApplication", "File Name Not Valid", Messages.getErrorIcon());
            txt = "BinderApplication";
            applicationTag.add(1, "android:name=\"." + txt + "\"");
        }
        //remove the old application tag.
        lines = BinderFileManipulation.removeLines(lines, "<application", ">");
        //add new application tag.
        //app tag size + 1 because I added a line into the app tag for the name.
        for (int i = 0; i < applicationTag.size(); i++) {
            lines.add(Start + i, applicationTag.get(i));
        }
        //write over file in manifest.
        FileUtils.writeFileForLinesList(lines, manifest);
        ConsolePrintUtils.printListIntoConsole(lines, "New MANIFEST");
        return txt;
    }

    //Delete the zip folder after you are done with it.
    public void DeleteBinderZipFile(Path newBinderPath) throws IOException {
        Files.delete(newBinderPath);
    }

    //download the zip file from the web.
    private static Path download(URL sourceUrl, String targetDirectory) throws IOException {
        // save to this file.
        String fileName = File.separator + "binder.zip";
        Path targetPath = new File(targetDirectory + fileName).toPath();
        //download file.
        Files.copy(sourceUrl.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return targetPath;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Should go somewhere else just dont know where yet.


    @Nullable
    private String getPackageName() throws IOException {
        String packageName = null;
        File manifest = FileUtils.getAndroidManifestFileForApp(mProject);
        try (BufferedReader br = new BufferedReader(new FileReader(manifest))) {
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                String theLineIWant = "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\" package=\"";
                if (line.contains(theLineIWant)) {
                    packageName = line.substring(theLineIWant.length()).split("\"")[0];
//                      System.out.print("WHAT I WANT ***********************************" + packageName + "\n\n\n\n");
                }
            }
        }
        return packageName;
    }

    public boolean hasApplicationFile() {
        return true;
    }


    //TODO look at better way to do.
    private String getChooserPath() throws IOException {
        String getPackageName = getPackageName();
        assert getPackageName != null;
        return File.separator + "app" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + getPackageName.replace(".", File.separator);
    }


}
