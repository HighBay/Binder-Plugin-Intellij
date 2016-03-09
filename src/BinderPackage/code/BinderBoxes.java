package BinderPackage.code;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import org.apache.commons.validator.routines.UrlValidator;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Aaron on 3/1/2016.
 */
public class BinderBoxes extends AnAction {

    String AndroidApi = "api/Binder/distribution?platform=android";

    public BinderBoxes() {
        super("Binder _Boxes", "Start the Dialog that allows user to download binder code", IconLoader.getIcon("/BinderPackage/icons/icon-sm.png"));
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        String txt = Messages.showInputDialog(project, "What is the Base url to your C# project.\n" +
                "Be sure to add 'htts://' to the beginning if it is needed.\n" +
                "Plugin defaults to 'http://' if not provided.", "Enter Base Url.", Messages.getQuestionIcon());
        String BinderUrl = createBinderUrl(txt);
        if (checkIfBinderUrlIsValid(BinderUrl)) {
            showDownloadBinderDialog(project, BinderUrl);
        } else {
            showNotValidDialog(project, BinderUrl);
        }
    }

    private String createBinderUrl(String txt) {
        String BinderUrl = txt;
        if (BinderUrl.startsWith("http://")) {
            //do nothing something is already there
        } else if (BinderUrl.startsWith("https://")) {
            //do nothing something is already there
        } else {
            BinderUrl = "http://" + BinderUrl;
        }
        if (BinderUrl.endsWith("/")) {
            BinderUrl = BinderUrl.concat(AndroidApi);
        } else {
            BinderUrl = BinderUrl.concat("/" + AndroidApi);
        }
        return BinderUrl;
    }

    private void showNotValidDialog(Project project, String BinderUrl) {
        Messages.showMessageDialog(project, BinderUrl + " is not a valid URL. Please Enter a valid URL.", "Error", Messages.getErrorIcon());

    }

    private void showDownloadBinderDialog(Project project, String BinderUrl) {
        int DownloadDialog = Messages.showYesNoCancelDialog(project, "Binder Download URL is:\n" + BinderUrl, "Download Binder?", "Download", "Don't Download", "Cancel", Messages.getQuestionIcon());
        if (DownloadDialog == JOptionPane.YES_OPTION) {
            //Download Project
            DownloadBinderProject(BinderUrl, project);
        } else if (DownloadDialog == JOptionPane.NO_OPTION) {
            //User said no. Do nothing this is here to learn how this worked
        } else if (DownloadDialog == JOptionPane.CANCEL_OPTION) {
            //User canceled. Do Nothig this is here to learn how this worked
        }

    }

    private void DownloadBinderProject(String binderUrl, Project project) {
        //Show custom dialog with downloading project loader and stuff.

        //Download zip.
        BinderDownloadAndUnZipping download = new BinderDownloadAndUnZipping(project,binderUrl);
        Path binderPath = null;
        try {
           binderPath = download.DownloadBinderFile();
        } catch (IOException e) {
            e.printStackTrace();
            Messages.showMessageDialog(project,"Binder Download didn't work, IO Error","Error",Messages.getErrorIcon());
        }
        //Unzip project
        if(binderPath!=null) {
            try {
                download.UnZipBinderFile(binderPath, (Component) project.getComponent(""));
            } catch (IOException e) {
                e.printStackTrace();
                Messages.showMessageDialog(project, "Binder didn't unzip, IO Error", "Error", Messages.getErrorIcon());
            }
            try {
                download.DeleteBinderZipFile(binderPath);
            } catch (IOException e) {
                e.printStackTrace();
                Messages.showMessageDialog(project, "binder.zip didn't delete please delete manually.", "Error", Messages.getWarningIcon());
            }
        }else{
            Messages.showMessageDialog(project, "Binder did not download correctly", "Error", Messages.getErrorIcon());
        }
        //Save files to project to location set by user.
        //Delete Zip file.
    }

    private boolean checkIfBinderUrlIsValid(String binderUrl) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(binderUrl);
    }
}
