# 📂 Orderly: The Java File Sorter

Orderly is a simple and clean desktop utility built with **Java Swing**. It helps you organize a messy folder by sorting all its files into clean, categorized subdirectories based on your chosen criteria.

This project was built as a clean, self-contained example of a Java Swing application that follows basic separation of concerns (UI vs. Logic) and uses a `SwingWorker` for responsive background processing.

## 📸 Screenshot

![A screenshot of the Orderly application UI](placeholder-image.png)
*(You should replace the file `placeholder-image.png` with an actual screenshot of your application)*

## ✨ Features

* **Clean & Simple UI:** A straightforward graphical interface built with Java Swing.
* **Directory Chooser:** Easily browse and select any folder on your system.
* **Multiple Sorting Strategies:**
    * **By File Type:** Sorts files into folders like `Images`, `Videos`, `Word`, `Excel`, etc.
    * **By Date Modified:** Sorts files into folders based on their last modified date (e.g., `2025-10`).
    * **By File Size:** Sorts files into `Small (<1MB)`, `Medium (1-10MB)`, and `Large (>10MB)`.
    * **By Type and Then Date:** Creates nested folders (e.g., `Images/2025-10`).
* **Responsive Application:** Uses a `SwingWorker` to run the sorting process on a background thread. This prevents the UI from freezing, even when sorting thousands of files.
* **Extensible Categories:** Easily add new file extensions and categories by modifying the `CategoryManager.java` class.

## 🚀 How to Run

You can run this project from a compiled JAR file or directly from your IDE.

### From an IDE (Recommended)

1.  Clone this repository: `git clone https://github.com/your-username/orderly.git`
2.  Open the project in your favorite Java IDE (like IntelliJ IDEA, Eclipse, or VS Code with Java extensions).
3.  Locate and run the `FileSorterSwingUI.java` file (it contains the `main` method).

### From the Command Line

1.  Ensure you have a Java Development Kit (JDK) 11 or newer installed.
2.  Clone the repository and navigate into the project directory.
3.  Compile all the Java files:
    ```bash
    javac *.java
    ```
4.  Run the main UI class:
    ```bash
    java FileSorterSwingUI
    ```

## 🛠️ Project Structure

The project is broken down into four simple classes to separate responsibilities:

* `FileSorterSwingUI.java`: The **View/Controller**. This class builds the entire Swing GUI, handles button clicks, and manages the user-facing state. It launches the `SwingWorker` to start the sorting task.
* `FileSorter.java`: The **Sorting Logic**. This class contains all the business logic for moving files. It receives the directory and the user's choice, then executes the correct sorting method (`sortByType`, `sortByDate`, etc.).
* `CategoryManager.java`: The **Configuration**. This class uses a `HashMap` to map file extensions (like `.jpg` or `.pdf`) to their corresponding category folder names (like `Images` or `Documents`).
* `FileUtility.java`: A **Helper Class**. This provides simple methods for getting file properties like its extension, size, or last modified time.

## 📄 License

This project is open-source and available as a learning resource. You can consider adding an [MIT License](https://opensource.org/licenses/MIT) if you wish to allow others to freely use and modify your code.
