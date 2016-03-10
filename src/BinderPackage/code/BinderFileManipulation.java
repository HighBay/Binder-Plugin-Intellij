package BinderPackage.code;

import BinderPackage.code.utils.ConsolePrintUtils;
import BinderPackage.code.utils.FileUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Aaron on 3/2/2016.
 */
public class BinderFileManipulation {

    //Built because I didn't know how to do it when this was made.
    public static void addTextDependenciesForBinderToGradleFile(String gradlePath) throws IOException {
        //find gradle file in project.
        File gradlefile = new File(gradlePath);
        //needed because when i remove the lines they disapear?
        List<String> linesForDep = Files.readAllLines(gradlefile.toPath(), Charset.defaultCharset());
        List<String> lines = Files.readAllLines(gradlefile.toPath(), Charset.defaultCharset());
        List<String> projectDependencies = getAllDependenciesInGradle(linesForDep);
        //remove all dependencies from build.gradle file.
        lines = removeLinesInBetweenStartAndEndLine(lines, "dependencies {", "}");
        //find depencies in file ( create if not there )

        for (int i = 0; i < lines.size(); i++) {
            //add gson dependecy and  volley dependency
            if (lines.get(i).equals("dependencies {")) {
                //remove all dependencies from build.gradle file.
                ArrayList<String> dependencies = addGradleDependencyIfNotThere(projectDependencies);
                for (int j = 0; j < dependencies.size(); j++) {
                    lines.add((i + 1 + j), "\t" + dependencies.get(j));
                }
                break;
            }
        }
        //write new gradle file.
        FileUtils.writeFileForLinesList(lines, new File(gradlePath));
    }

    private static List<String> getAllDependenciesInGradle(List<String> lines) {
        int start = -1, end = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals("dependencies {")) {
                start = i + 1;
                List<String> newList = lines.subList(i, lines.size());
                for (int j = 0; j < newList.size(); j++) {
                    if (newList.get(j).contains("}")) {
                        end = (start + j - 1);
                        break;
                    }
                }
                break;
            }
        }
        List<String> allCurrentDependencies = lines.subList(start, end);
        ConsolePrintUtils.printToConsole("Start " + start + "End " + end,"IMPORTS");
        return allCurrentDependencies;
    }

    public static List<String> removeLinesInBetweenStartAndEndLine(List<String> lines, String startLine, String endLine) {
        int start = -1, end = -1;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains(startLine)) {
                start = i + 1;
                List<String> newList = lines.subList(i, lines.size());
                for (int j = 0; j < newList.size(); j++) {
                    if (newList.get(j).contains(endLine)) {
                        end = start + j - 1;
                        break;
                    }
                }
                break;
            }
        }
        List<String> listBeg = lines.subList(0, start);
        int StartIndex = end;
        int LastIndex = lines.size() - 1;
        List<String> listEnd = new ArrayList<>();
        if (StartIndex == LastIndex) {
            listEnd.add(lines.get(StartIndex));
        } else {
            listEnd = lines.subList(StartIndex, LastIndex);
        }

        //add the end to the beginning
        listBeg.addAll(listEnd);
        //make lines equal the listBeg because it is now the full list.
        lines = listBeg;
        return lines;
    }

    public static List<String> removeLines(List<String> lines, String startLine, String endLine) {
        int start = -1, end = -1;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains(startLine)) {
                start = i;
                List<String> newList = lines.subList(i, lines.size());
                for (int j = 0; j < newList.size(); j++) {
                    if (newList.get(j).contains(endLine)) {
                        end = start + j - 1;
                        break;
                    }
                }
                break;
            }
        }
        List<String> listBeg = lines.subList(0, start);
        int StartIndex = end;
        int LastIndex = lines.size() - 1;
        List<String> listEnd = new ArrayList<>();
        if (StartIndex == LastIndex) {
            listEnd.add(lines.get(StartIndex));
        } else {
            //Start index is + 2 so e get the line after the end tag
            //LastIndex is +1 so we get the actual last index
            listEnd = lines.subList(StartIndex + 2, LastIndex + 1);
        }

        //add the end to the beginning
        listBeg.addAll(listEnd);
        //make lines equal the listBeg because it is now the full list.
        lines = listBeg;
        return lines;
    }

    public static List<String> replaceLineInBetweenStartAndEndLine(List<String> lines, List<String> replacementLines, String startLine, String endLine) {
        lines = removeLinesInBetweenStartAndEndLine(lines, startLine, endLine);
        for (int i = 0; i < lines.size(); i++) {
            //add gson dependecy and  volley dependency
            if (lines.get(i).equals(startLine)) {
                //remove all dependencies from build.gradle file.
                for (int j = 0; j < replacementLines.size(); j++) {
                    lines.add((i + 1 + j), "\t" + replacementLines.get(j));
                }
                break;
            }
        }
        return lines;
    }

    @Nullable
    public static ArrayList<String> getLinesWithStartAndEndLine(List<String> lines, String startLine, String endLine) {
        int Start = -1, End = -1;
        //Find the start and end
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains(startLine)) {
                Start = i;
                List<String> theRest = lines.subList(i, lines.size());
                for (int j = 0; j < theRest.size(); j++) {
                    String lineTheRest = theRest.get(j);
                    if (lineTheRest.contains(endLine)) {
                        End = Start + j + 1;
                        break;
                    }
                }
            }
        }
        if (Start != -1 && End != -1) {
            return new ArrayList<>(lines.subList(Start, End));
        } else {
            return null;
        }
    }

    @Nullable
    public static ArrayList<String> getLinesInBetweenStartAndEndLine(List<String> lines, String startLine, String endLine) {
        int Start = -1, End = -1;
        //Find the start and end
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains(startLine)) {
                Start = i;
                List<String> theRest = lines.subList(i, lines.size());
                for (int j = 0; j < theRest.size(); j++) {
                    String lineTheRest = theRest.get(j);
                    if (lineTheRest.contains(endLine)) {
                        End = i + j;
                        break;
                    }
                }
            }
        }
        if (Start != -1 && End != -1) {
            return new ArrayList<>(lines.subList(Start, End));
        } else if (Start == End) {
            ArrayList<String> singleLine = new ArrayList<>();
            singleLine.add(lines.get(Start));
            return singleLine;
        } else {
            return null;
        }
    }

    public static void changeUrlToInWebServiceClass(String webServicesPath, String url) throws IOException {
        //create file for webservice.
        File file = new File(webServicesPath);
        List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("<#WebServiceAddress#>")) {
                String find = "<#WebServiceAddress#>";
                String replace = "\"" + url + "\";";

                line = line.replace(find,replace );
                //remove previous
                lines.remove(i);
                //add new
                lines.add(i, line);
                break;
            }
        }
        FileUtils.writeFileForLinesList(lines, file);
    }

    public static void addImports(String path, String packageName, ArrayList<String> allFiles) throws IOException {
        //Data sources doesn't need any imports.
        File file = new File(path);
        if (!path.contains("DataSources")) {
            HashSet<String> importsToAdd = new HashSet<>();
            List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
            for (int i = 0; i < lines.size(); i++) {
                for (int j = 0; j < allFiles.size(); j++) {
                    //allFiles.get(j) == SomeClass.java we only want the part before the .java
                    String FileName = allFiles.get(j).split("\\.", -1)[0];
                    //check for the fileName in the file if it is there add it to the list to add. Unless it extends the class or implements it those are not needed.
                    if (lines.get(i).contains(FileName) &&
                            !path.contains(FileName) &&
                            !lines.get(i).contains("extends") &&
                            !lines.get(i).contains("implements")) {
                        importsToAdd.add(FileName);
                    }
                }
            }
            //System.out.print(path + " needs " + importsToAdd.size() + " imports\n\n\n\n\n");
            //Add the lines
            int lineToAddImportTo = 2;
            for (String myImport : importsToAdd) {
                //if it contains any of these lines dont add line like normal add line line using other conditions.
                if (!myImport.contains("DataSources") && !myImport.contains("TestData") && !myImport.contains("WebService") && !myImport.contains("DataInterface")) {
                    lines.add(lineToAddImportTo, addImportLine(packageName, myImport));
                } else if (myImport.contains("DataSources")) {
                    //add in the other two that are needed.
                    lines.add(lineToAddImportTo, "import " + packageName + ".Binder.Libraries.DataSources;");
                } else if (myImport.contains("TestData")) {
                    //add in the other two that are needed.
                    lines.add(lineToAddImportTo, "import " + packageName + ".Binder.TestData;");
                } else if (myImport.contains("WebServices")) {
                    //add in the other two that are needed.
                    lines.add(lineToAddImportTo, "import " + packageName + ".Binder.WebServices;");
                } else if (myImport.contains("DataInterface")) {
                    //add in the other two that are needed.
                    lines.add(lineToAddImportTo, "import " + packageName + ".Binder.DataInterface;");
                }
            }
            //Save file.
            FileUtils.writeFileForLinesList(lines, file);
        }
    }

    //add import line.
       private static String addImportLine(String packageName, String fileName) {
        return "import " + packageName + ".Binder.Data.Dtos." + fileName + ";";
    }

    private static ArrayList<String> addGradleDependencyIfNotThere(List<String> otherDependencies) {
        //adding volley and gson dependency
        ArrayList<String> dependencies = new ArrayList<>();
        String volleyDependency = "compile 'com.mcxiaoke.volley:library:1.0.19'";
        String gsonDependency = "compile 'com.google.code.gson:gson:2.6.2'";
        if (otherDependencies != null && otherDependencies.size() > 0) {
            for (String dependecy : otherDependencies) {
                dependencies.add(dependecy.replaceAll("^\\s+", "").replaceAll("^\\t+", ""));
            }
        }
        if (!dependencies.contains(volleyDependency)) {
            dependencies.add(0, volleyDependency);
        }
        if (!dependencies.contains(gsonDependency)) {
            dependencies.add(0, gsonDependency);
        }
        return dependencies;
    }

    //packagePath = com.some.thing & filePath = com/some/thing/file.java
    public static void addPackageNameToBeginningOfFile(String packagePath, String filePath) throws IOException {
        File file = new File(filePath);
        List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        //add package line at the beginning of the file.
        lines.add(0, "package " + packagePath + ";\n");
        FileUtils.writeFileForLinesList(lines, file);
        ConsolePrintUtils.printToConsole("Package Addition Done","PACKAGE WRITE");
    }


}
