package applusvelosi.projects.android.salt.utils.interfaces;

import java.io.File;

/**
 * Created by Velosi on 11/17/15.
 */
public interface FileSelectionInterface {

    void onFileSelectionSuccess(File file);
    void onFileSelectionFailed();
}
