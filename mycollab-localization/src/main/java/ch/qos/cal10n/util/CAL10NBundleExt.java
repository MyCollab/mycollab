package ch.qos.cal10n.util;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
public class CAL10NBundleExt extends CAL10NBundle {
    private boolean isChanged = false;

    public CAL10NBundleExt(Reader r, File file) throws IOException {
        super(r, file);
    }

    public boolean hasChanged() {
        return isChanged;
    }

    public void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }

    public File getHostFile() {
        return hostFile;
    }
}
