import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.concurrent.ExecutionException;

public class FileSorterSwingUI extends JFrame {

    // UI Components
    private final JTextField directoryPathField;
    private final JButton browseButton;
    private final JRadioButton typeRadioButton;
    private final JRadioButton dateRadioButton;
    private final JRadioButton sizeRadioButton;
    private final JRadioButton typeAndDateRadioButton;
    private final JButton sortButton;
    private final JLabel statusLabel;

    // Logic Components
    private File selectedDirectory;
    private final FileSorter fileSorter;

    public FileSorterSwingUI() {
        // Frame Setup
        super("Orderly File Sorter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Backend Logic 
        CategoryManager categoryManager = new CategoryManager();
        this.fileSorter = new FileSorter(categoryManager);

        // Main Panel with Padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.weightx = 1;

        // Header 
        JLabel titleLabel = new JLabel("Orderly File Sorter", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitleLabel = new JLabel("Organize your files with a single click", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);

        mainPanel.add(titleLabel, gbc);
        gbc.insets = new Insets(5, 0, 20, 0); 
        mainPanel.add(subtitleLabel, gbc);

        //  Directory Selection Panel 
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel selectFolderLabel = new JLabel("Select Folder to Organize");
        selectFolderLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mainPanel.add(selectFolderLabel, gbc);

        JPanel directoryPanel = new JPanel(new BorderLayout(10, 0));
        directoryPathField = new JTextField("No folder selected...");
        directoryPathField.setEditable(false);
        browseButton = new JButton("Browse...");
        directoryPanel.add(directoryPathField, BorderLayout.CENTER);
        directoryPanel.add(browseButton, BorderLayout.EAST);
        mainPanel.add(directoryPanel, gbc);

        // Sorting Options Panel 
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(new TitledBorder("Sorting Method"));

        typeRadioButton = new JRadioButton("By File Type");
        dateRadioButton = new JRadioButton("By Date Modified");
        sizeRadioButton = new JRadioButton("By File Size");
        typeAndDateRadioButton = new JRadioButton("By Type and Then Date");
        typeRadioButton.setSelected(true);

        ButtonGroup sortingGroup = new ButtonGroup();
        sortingGroup.add(typeRadioButton);
        sortingGroup.add(dateRadioButton);
        sortingGroup.add(sizeRadioButton);
        sortingGroup.add(typeAndDateRadioButton);

        optionsPanel.add(createAlignedPanel(typeRadioButton));
        optionsPanel.add(createAlignedPanel(dateRadioButton));
        optionsPanel.add(createAlignedPanel(sizeRadioButton));
        optionsPanel.add(createAlignedPanel(typeAndDateRadioButton));
        
        gbc.insets = new Insets(20, 0, 20, 0);
        mainPanel.add(optionsPanel, gbc);

        // Action & Status
        sortButton = new JButton("Sort Files");
        sortButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sortButton.setEnabled(false); // Initially disabled
        gbc.ipady = 10; // Make button taller
        mainPanel.add(sortButton, gbc);

        statusLabel = new JLabel("Status: Ready", SwingConstants.CENTER);
        statusLabel.setForeground(Color.DARK_GRAY);
        gbc.ipady = 0;
        gbc.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(statusLabel, gbc);

        // Add Event Listeners
        addEventListeners();

        // Finalize Frame
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void addEventListeners() {
        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setDialogTitle("Select Folder to Organize");
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedDirectory = chooser.getSelectedFile();
                directoryPathField.setText(selectedDirectory.getAbsolutePath());
                statusLabel.setText("Status: Ready to sort '" + selectedDirectory.getName() + "'");
                sortButton.setEnabled(true);
            }
        });

        sortButton.addActionListener(e -> sortFiles());
    }
    
    private int getSelectedSortOption() {
        if (typeRadioButton.isSelected()) return 1;
        if (dateRadioButton.isSelected()) return 2;
        if (sizeRadioButton.isSelected()) return 3;
        if (typeAndDateRadioButton.isSelected()) return 4;
        return 0; 
    }

    private void sortFiles() {
        int choice = getSelectedSortOption();
        statusLabel.setText("Status: Sorting in progress...");
        statusLabel.setForeground(Color.BLUE);
        setUIEnabled(false);

        // Use SwingWorker for background processing
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                return fileSorter.sortFiles(selectedDirectory, choice);
            }

            @Override
            protected void done() {
                try {
                    String resultMessage = get();
                    statusLabel.setText("Status: " + resultMessage);
                    statusLabel.setForeground(resultMessage.startsWith("Error") ? Color.RED : new Color(0, 128, 0));
                    JOptionPane.showMessageDialog(FileSorterSwingUI.this, resultMessage, "Sort Complete", JOptionPane.INFORMATION_MESSAGE);
                } catch (InterruptedException | ExecutionException ex) {
                    String errorMessage = "An unexpected error occurred: " + ex.getCause().getMessage();
                    statusLabel.setText("Status: " + errorMessage);
                    statusLabel.setForeground(Color.RED);
                    JOptionPane.showMessageDialog(FileSorterSwingUI.this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    setUIEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void setUIEnabled(boolean enabled) {
        browseButton.setEnabled(enabled);
        sortButton.setEnabled(enabled);
        typeRadioButton.setEnabled(enabled);
        dateRadioButton.setEnabled(enabled);
        sizeRadioButton.setEnabled(enabled);
        typeAndDateRadioButton.setEnabled(enabled);
    }

    // Helper to left-align components in a panel
    private JPanel createAlignedPanel(Component comp) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.add(comp);
        return panel;
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> new FileSorterSwingUI().setVisible(true));
    }
}
