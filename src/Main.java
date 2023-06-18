import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main extends JFrame implements ActionListener {
    static JLabel filePathCipherOpenLabel = new JLabel("");
    static JLabel filePathCipherSaveLabel = new JLabel("");
    static JLabel filePathBruteForceFileLabel = new JLabel("");
    static JLabel filePathBruteForceWordlistLabel = new JLabel("");
    static JRadioButton cipherModeEncryptRadioButton;
    static JRadioButton cipherModeDecryptRadioButton;
    static JButton encryptDecryptButton;
    static JTextField cipherKeyTextField;
    static JSpinner wordlistKeySizeJSpinner;
    static JTextField wordlistCharactersTextField;
    static JLabel errorLabel = new JLabel(" ", SwingConstants.LEFT);
    static JLabel crackLabel = new JLabel(" ");
    static JFrame frame;

    Main() {
    }

    public static void main(String[] args) {
        Main m = new Main();
        frame = new JFrame();

        JTabbedPane tp = new JTabbedPane();
        JPanel encryptPanel = new JPanel();
        JPanel wordlistPanel = new JPanel();
        JPanel bruteForcePanel = new JPanel();
        tp.setBounds(50, 50, 200, 200);
        tp.add("Encrypt/Decrypt File", encryptPanel);
        tp.add("Create Wordlist", wordlistPanel);
        tp.add("Brute Force", bruteForcePanel);

        tp.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                errorLabel.setText(" ");
            }
        });


        // ENCRYPT PANEL WIDGETS
        encryptPanel.setLayout(new GridLayout(5, 2, 20, 25));
        encryptPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel cipherModeGroup = new JPanel();
        cipherModeGroup.setLayout(new BoxLayout(cipherModeGroup, BoxLayout.X_AXIS));
        cipherModeEncryptRadioButton = new JRadioButton("Encrypt");
        cipherModeEncryptRadioButton.doClick();
        cipherModeDecryptRadioButton = new JRadioButton("Decrypt");

        ActionListener radioButtonActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                encryptDecryptButton.setText(((AbstractButton) actionEvent.getSource()).getText());
                errorLabel.setText(" ");
            }
        };
        cipherModeEncryptRadioButton.addActionListener(radioButtonActionListener);
        cipherModeDecryptRadioButton.addActionListener(radioButtonActionListener);


        ButtonGroup cipherModeGroupButton = new ButtonGroup();
        cipherModeGroupButton.add(cipherModeEncryptRadioButton);
        cipherModeGroupButton.add(cipherModeDecryptRadioButton);
        cipherModeGroup.add(cipherModeEncryptRadioButton);
        cipherModeGroup.add(cipherModeDecryptRadioButton);
        encryptPanel.add(cipherModeGroup);

        JPanel fileCipherOpenGroup = new JPanel();
        fileCipherOpenGroup.setLayout(new BoxLayout(fileCipherOpenGroup, BoxLayout.Y_AXIS));
        JButton selectFileCipherButton = new JButton("Select File");

        selectFileCipherButton.addActionListener(m);
        fileCipherOpenGroup.add(selectFileCipherButton);
        fileCipherOpenGroup.add(filePathCipherOpenLabel);

        JPanel fileCipherSaveGroup = new JPanel();
        fileCipherSaveGroup.setLayout(new BoxLayout(fileCipherSaveGroup, BoxLayout.Y_AXIS));

        JButton saveFileCipherButton = new JButton("Select Destination File");

        saveFileCipherButton.addActionListener(m);
        fileCipherSaveGroup.add(saveFileCipherButton);
        fileCipherSaveGroup.add(filePathCipherSaveLabel);

        encryptPanel.add(fileCipherOpenGroup);
        encryptPanel.add(fileCipherSaveGroup);

        JPanel cipherKeyGroup = new JPanel();
        cipherKeyGroup.setLayout(new FlowLayout());
        cipherKeyTextField = new JTextField(10);
        cipherKeyTextField.setBounds(50, 50, 50, 20);
        cipherKeyTextField.setDocument(new JTextFieldWithLimit(8));
        encryptDecryptButton = new JButton("Encrypt");
        encryptDecryptButton.addActionListener(m);

        cipherKeyGroup.add(cipherKeyTextField);
        cipherKeyGroup.add(new JLabel("Exactly 8 chars for DES"));
        cipherKeyGroup.add(encryptDecryptButton);

        encryptPanel.add(cipherKeyGroup);

        // WORDLIST PANEL WIDGETS
        wordlistPanel.setLayout(new GridLayout(3, 2, 20, 25));
        wordlistPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel wordlistCharactersPanel = new JPanel();
        wordlistCharactersPanel.setLayout(new BoxLayout(wordlistCharactersPanel, BoxLayout.Y_AXIS));
        wordlistCharactersPanel.add(new JLabel("Enter characters here:"));
        wordlistCharactersTextField = new JTextField(15);
        wordlistCharactersPanel.add(wordlistCharactersTextField);

        JPanel wordlistFileSaveGroup = new JPanel();
        wordlistFileSaveGroup.setLayout(new BoxLayout(wordlistFileSaveGroup, BoxLayout.Y_AXIS));

        wordlistKeySizeJSpinner = new JSpinner(new SpinnerNumberModel(8, 2, 15, 1));
        JPanel keySizeEnterPanel = new JPanel();
        keySizeEnterPanel.setLayout(new FlowLayout());
        keySizeEnterPanel.add(new Label("Set word length (DES requires exact 8) :"));
        keySizeEnterPanel.add(wordlistKeySizeJSpinner);

        JButton wordlistFileSaveButton = new JButton("Generate Wordlist");
        wordlistFileSaveButton.addActionListener(m);
        keySizeEnterPanel.add(wordlistFileSaveButton);

        wordlistPanel.add(wordlistCharactersPanel);
        wordlistPanel.add(keySizeEnterPanel);
        wordlistPanel.add(wordlistFileSaveGroup);


        // BRUTE FORCE PANEL WIDGETS
        bruteForcePanel.setLayout(new GridLayout(3, 2, 20, 25));
        bruteForcePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel bruteForceSelectEncryptedFileGroup = new JPanel();
        bruteForceSelectEncryptedFileGroup.setLayout(new BoxLayout(bruteForceSelectEncryptedFileGroup, BoxLayout.Y_AXIS));
        bruteForceSelectEncryptedFileGroup.setBorder(BorderFactory.createTitledBorder("Encrypted File"));

        JButton bruteForceSelectEncryptedFileButton = new JButton("Select Encrypted File");
        bruteForceSelectEncryptedFileButton.addActionListener(m);
        bruteForceSelectEncryptedFileGroup.add(bruteForceSelectEncryptedFileButton);
        bruteForceSelectEncryptedFileGroup.add(filePathBruteForceFileLabel);

        JPanel bruteForceSelectWordlistFileGroup = new JPanel();
        bruteForceSelectWordlistFileGroup.setLayout(new BoxLayout(bruteForceSelectWordlistFileGroup, BoxLayout.Y_AXIS));
        bruteForceSelectWordlistFileGroup.setBorder(BorderFactory.createTitledBorder("Wordlist File"));

        JButton bruteForceSelectWordlistFileButton = new JButton("Select Wordlist File");
        bruteForceSelectWordlistFileButton.addActionListener(m);
        bruteForceSelectWordlistFileGroup.add(bruteForceSelectWordlistFileButton);
        bruteForceSelectWordlistFileGroup.add(filePathBruteForceWordlistLabel);

        bruteForcePanel.add(bruteForceSelectEncryptedFileGroup);
        bruteForcePanel.add(bruteForceSelectWordlistFileGroup);

        JPanel bruteForceCrackButtonLayout = new JPanel();
        bruteForceCrackButtonLayout.setLayout(new FlowLayout());
        JButton bruteForceCrackButton = new JButton("Crack");
        bruteForceCrackButton.addActionListener(m);
        bruteForceCrackButtonLayout.add(bruteForceCrackButton);

        JPanel bruteForceCrackGroup = new JPanel();
        bruteForceCrackGroup.setLayout(new GridLayout(2, 1, 25, 25));


        bruteForceCrackGroup.add(bruteForceCrackButtonLayout);
        bruteForceCrackGroup.add(crackLabel);

        bruteForcePanel.add(bruteForceCrackGroup);

        // MAIN WINDOW

        frame.setLayout(new GridLayout());
        frame.setVisible(true);
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(tp);

        errorLabel.setForeground(Color.red);
        mainPanel.add(errorLabel);

        frame.add(mainPanel);
    }

    public void actionPerformed(ActionEvent evt) {
        errorLabel.setText(" ");
        JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        String actionCommand = evt.getActionCommand();
        int res;
        String key;
        DESCrypto crypto;
        switch (actionCommand) {
            case "Select File":
                res = j.showOpenDialog(null);
                if (res == JFileChooser.APPROVE_OPTION) {
                    filePathCipherOpenLabel.setText(j.getSelectedFile().getAbsolutePath());
                }
                break;
            case "Select Destination File":
                res = j.showSaveDialog(null);
                if (res == JFileChooser.APPROVE_OPTION) {
                    filePathCipherSaveLabel.setText(j.getSelectedFile().getAbsolutePath());
                }
                break;
            case "Select Encrypted File":
                res = j.showOpenDialog(null);
                if (res == JFileChooser.APPROVE_OPTION) {
                    filePathBruteForceFileLabel.setText(j.getSelectedFile().getAbsolutePath());
                }
                break;
            case "Select Wordlist File":
                res = j.showOpenDialog(null);
                if (res == JFileChooser.APPROVE_OPTION) {
                    filePathBruteForceWordlistLabel.setText(j.getSelectedFile().getAbsolutePath());
                }
                break;
            case "Generate Wordlist":
                // small validation
                // characters should be at least 2 different chars
                String wordlistText = wordlistCharactersTextField.getText();
                HashSet<Character> charSet = convertToSet(wordlistText.toCharArray());
                if (charSet.size() < 2) {
                    errorLabel.setText("Please enter at least 2 different chars to generate wordlist");
                    return;
                }
                errorLabel.setText(" ");

                res = j.showSaveDialog(null);
                if (res == JFileChooser.APPROVE_OPTION) {
                    try {
                        String fp = j.getSelectedFile().getAbsolutePath();
                        saveWordlistToFile(
                                fp,
                                StringUtils.generateWordlist(convertToCharArr(charSet), (Integer) wordlistKeySizeJSpinner.getValue())
                        );
                        JOptionPane.showMessageDialog(frame, "Wordlist has been generated and saved into " + fp);
                    } catch (Exception e) {
                        errorLabel.setText(e.getMessage());
                    }

                }
                break;
            case "Encrypt":
                if (filePathCipherOpenLabel.getText().equals("") || filePathCipherSaveLabel.getText().equals("")) {
                    errorLabel.setText("Please select source and destination files");
                    return;
                }
                key = cipherKeyTextField.getText();
                if (key.length() != 8) {
                    errorLabel.setText("Key size should be 8 characters");
                    return;
                } else {
                    crypto = new DESCrypto(key, "encrypt");
                    try {
                        crypto.encryptFile(filePathCipherOpenLabel.getText(), filePathCipherSaveLabel.getText());
                        JOptionPane.showMessageDialog(frame, "Encrypted file saved!");
                    } catch (RuntimeException e) {
                        errorLabel.setText(e.getCause().getMessage());
                    }
                }

                break;
            case "Decrypt":
                if (filePathCipherOpenLabel.getText().equals("") || filePathCipherSaveLabel.getText().equals("")) {
                    errorLabel.setText("Please select source and destination files");
                    return;
                }
                key = cipherKeyTextField.getText();
                if (key.length() != 8) {
                    errorLabel.setText("Key size should be 8 characters long");
                } else {
                    crypto = new DESCrypto(key, "decrypt");
                    try {
                        crypto.decryptFile(filePathCipherOpenLabel.getText(), filePathCipherSaveLabel.getText());
                        JOptionPane.showMessageDialog(frame, "Decrypted file saved!");
                    } catch (RuntimeException e) {
                        errorLabel.setText(e.getCause().getMessage());
                    }
                }
                break;
            case "Crack":
                String filePath = filePathBruteForceFileLabel.getText();
                String wordlistPath = filePathBruteForceWordlistLabel.getText();
                if (filePath.equals("") || wordlistPath.equals("")) {
                    errorLabel.setText("Please select both of encrypted file and wordlist file");
                    return;
                }

                try {
                    crackFile(filePath, wordlistPath);
                } catch (Exception e) {
                    errorLabel.setText(e.getMessage());
                }
                break;
            default:
                break;
        }
    }

    public static HashSet<Character> convertToSet(char[] charArray) {
        HashSet<Character> resultSet = new HashSet<Character>();

        for (char c : charArray) {
            resultSet.add(c);
        }

        return resultSet;
    }

    public static char[] convertToCharArr(HashSet<Character> charSet) {
        StringBuilder str = new StringBuilder();

        for (char c : charSet) {
            str.append(c);
        }

        return str.toString().toCharArray();
    }

    public static void saveWordlistToFile(String fileName, ArrayList<String> wordlist) throws IOException {
        Files.write(Path.of(fileName), wordlist);
    }

    public static void crackFile(String encryptedFilePath, String wordlistPath) throws RuntimeException {
        String outfile = "./decrypted.txt";
        java.util.List<String> wordlist;
        DESCrypto cipher;
        boolean cracked = false;
        try {
            wordlist = Files.readAllLines(Path.of(wordlistPath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Could not parse wordlist file");
        }
        String key = "";
        for (String str : wordlist) {
            cipher = new DESCrypto(str, "decrypt");
            cipher.decryptFile(encryptedFilePath, outfile);
            cracked = checkFileContainsUTF(outfile);
            crackLabel.setText("Trying: " + str);
            frame.repaint();
            if (cracked) {
                key = str;
                break;
            }
        }
        if (!cracked) {
            JOptionPane.showMessageDialog(frame, "Could not crack it", ":(", JOptionPane.ERROR_MESSAGE);
            (new File(outfile)).delete();
            crackLabel.setText(" ");
        } else {
            String message = "Found key: " + key;
            crackLabel.setText(message);
            JOptionPane.showMessageDialog(frame, message + "\nDecrypted file saved: " + Path.of(outfile), ":)", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static boolean checkFileContainsUTF(String filepath) {
        try {
            Files.readAllLines(Path.of(filepath), StandardCharsets.UTF_8);
            return true;
        } catch (IOException ignored) {
        }
        return false;
    }

}