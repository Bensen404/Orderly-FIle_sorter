import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileSorter {
    private final FileUtility fileUtility;
    private final CategoryManager categoryManager;

    public FileSorter(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
        this.fileUtility = new FileUtility();
    }

    public String sortFiles(File directory, int choice) {
        File[] files = directory.listFiles();
        if (files == null) {
            return "Error: Could not read directory contents.";
        }

        boolean hasFilesToSort = false;
        for (File file : files) {
            if (file.isFile()) {
                hasFilesToSort = true;
                break;
            }
        }

        if (!hasFilesToSort) {
            return "Info: The folder contains no files to sort.";
        }

        try {
            switch (choice) {
                case 1: sortByType(directory); break;
                case 2: sortByDate(directory); break;
                case 3: sortBySize(directory); break;
                case 4: sortByTypeAndDate(directory); break;
                default: return "Error: Invalid sorting choice!";
            }
        } catch (Exception e) {
            return "Error during sorting: " + e.getMessage();
        }
        return "Sorting completed successfully!";
    }

    private void sortByType(File directory) throws IOException {
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                String ext = fileUtility.getExtension(file);
                String category = categoryManager.getCategory(ext);
                Path destFolder = Paths.get(directory.getPath(), category);
                Files.createDirectories(destFolder);
                Files.move(file.toPath(), destFolder.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private void sortByDate(File directory) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                Date lastMod = new Date(file.lastModified());
                String folderName = sdf.format(lastMod);
                Path destFolder = Paths.get(directory.getPath(), folderName);
                Files.createDirectories(destFolder);
                Files.move(file.toPath(), destFolder.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private void sortBySize(File directory) throws IOException {
            for (File file : directory.listFiles()) {
                if (file.isFile()) {
                    long size = file.length();
                    String sizeCategory;
                    if (size < 1_000_000) sizeCategory = "Small (Under 1MB)";
                    else if (size < 10_000_000) sizeCategory = "Medium (1-10MB)";
                    else sizeCategory = "Large (Over 10MB)";
                    Path destFolder = Paths.get(directory.getPath(), sizeCategory);
                    Files.createDirectories(destFolder);
                    Files.move(file.toPath(), destFolder.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }

    private void sortByTypeAndDate(File directory) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                String ext = fileUtility.getExtension(file);
                String category = categoryManager.getCategory(ext);
                Date lastMod = new Date(file.lastModified());
                String dateFolder = sdf.format(lastMod);
                Path destFolder = Paths.get(directory.getPath(), category, dateFolder);
                Files.createDirectories(destFolder);
                Files.move(file.toPath(), destFolder.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}