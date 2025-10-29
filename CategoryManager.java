import java.util.HashMap;

public class CategoryManager {
    private HashMap<String, String> categoryMap;

    public CategoryManager() {
        categoryMap = new HashMap<>();
        initializeCategories();
    }

    private void initializeCategories() {
        // Studio Image Formats
        categoryMap.put("jpg", "Images");
        categoryMap.put("jpeg", "Images");
        categoryMap.put("png", "Images");
        categoryMap.put("gif", "Images");
        categoryMap.put("bmp", "Images");
        categoryMap.put("psd", "Photoshop"); 
        
        // RAW formats
        categoryMap.put("cr2", "RAW_Images"); 
        categoryMap.put("nef", "RAW_Images"); 
        categoryMap.put("arw", "RAW_Images"); 

        // Media
        categoryMap.put("mp4", "Videos");
        categoryMap.put("avi", "Videos");
        categoryMap.put("mkv", "Videos");
        categoryMap.put("mp3", "Audio");
        categoryMap.put("wav", "Audio");

        // Document Folders
        categoryMap.put("docx", "Word");
        categoryMap.put("doc", "Word");
        
        categoryMap.put("pptx", "PPT");
        categoryMap.put("ppt", "PPT");
        
        categoryMap.put("xlsx", "Excel");
        categoryMap.put("xls", "Excel");

        // General Documents Folder
        categoryMap.put("pdf", "Documents");
        categoryMap.put("txt", "Documents");
    }

    public String getCategory(String extension) {
        return categoryMap.getOrDefault(extension.toLowerCase(), "Others");
    }
}