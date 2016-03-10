package BinderPackage.code.utils;

import BinderPackage.code.BinderFileManipulation;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This utility extracts files and directories of a standard zip file to
 * a destination directory.
 *
 * @author www.codejava.net
 */
public class UnzipUtility {
    /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;
    private String mPackageName;

    public UnzipUtility(String packageName) {
        this.mPackageName = packageName;
    }

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     *
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public void unzip(String zipFilePath, String destDirectory) throws IOException {

        //create all folders in that are going to be in the zip.
        File destDir = new File(destDirectory);
        File destDataDir = new File(destDir + File.separator + "Data");
        File destDtoDir = new File(destDataDir + File.separator + "Dtos");
        File destLibDir = new File(destDir + File.separator + "Libraries");
        if (!destDir.exists()) {
            //Making Binder Folder
            ConsolePrintUtils.printToConsole(destDir.toString(),"DIR NAME");
            destDir.mkdir();
        }
        if (!destDataDir.exists()) {
            //Making Binder Data Folder
            ConsolePrintUtils.printToConsole(destDataDir.toString(),"DIR NAME");
            destDataDir.mkdir();
        }
        if (!destDtoDir.exists()) {
            //Making Binder Dto Folder
            ConsolePrintUtils.printToConsole(destDtoDir.toString(),"DIR NAME");
            destDtoDir.mkdir();
        }
        if (!destLibDir.exists()) {
            //Making Binder Libs Folder
            ConsolePrintUtils.printToConsole(destLibDir.toString(),"DIR NAME");
            destLibDir.mkdir();
        }
        // a list of all of the files in the zip.
        ArrayList<String> entries = getAllClassNamesInZip(zipFilePath);


        //get all of the files.
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String entryName = entry.getName();
            ConsolePrintUtils.printToConsole(entryName,"ENTRY NAME");

            String filePath = destDirectory + File.separator + entryName;

            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                ConsolePrintUtils.printToConsole(filePath,"CREATED FILE");
                extractFile(zipIn, filePath);
                BinderFileManipulation.addImports(filePath,mPackageName, entries);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                ConsolePrintUtils.printToConsole(dir.toString(),"CREATED DIR");
                dir.mkdir();
            }
            zipIn.closeEntry();

            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    /**
     * Extracts a zip entry (file entry)
     *
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
        //Add extra file paths if needed.
        String temp = mPackageName + ".Binder";
        String dataDtosCheck = File.separator + "Data/Dtos";
        if (filePath.contains(dataDtosCheck)) {
            temp = temp + ".Data.Dtos";
        } else if (filePath.contains(File.separator + "Libraries")) {
            temp = temp + ".Libraries";
        }
        BinderFileManipulation.addPackageNameToBeginningOfFile(temp, filePath);
    }

    public ArrayList<String> getAllClassNamesInZip(String zipFilePath) throws IOException {
        ArrayList<String> entries = new ArrayList<>();
        ZipInputStream zips = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry = zips.getNextEntry();
        while (zipEntry != null) {
            if (zipEntry.getName().contains(".")) {
                if (!zipEntry.getName().contains("Data/Dtos/String.java")) {
                    if (zipEntry.getName().contains("Data/Dtos/")) {
                        entries.add(zipEntry.getName().substring(("Data/Dtos/").length()));
                    } else if (zipEntry.getName().contains("Libraries/")) {
                        entries.add(zipEntry.getName().substring(("Libraries/").length()));
                    } else {
                        entries.add(zipEntry.getName());
                    }
                }
            }
            zips.closeEntry();
            zipEntry = zips.getNextEntry();
        }
        zips.close();
        return entries;
    }
}