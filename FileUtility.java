import java.io.File;

public class FileUtility {
    public String getExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf(".");
        if (lastDot == -1) return "";
        return name.substring(lastDot + 1);
    }

    public long getSize(File file) {
        return file.length();
    }

    public long getModifiedTime(File file) {
        return file.lastModified();
    }
}