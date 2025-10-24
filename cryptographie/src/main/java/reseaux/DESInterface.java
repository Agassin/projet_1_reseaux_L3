package reseaux;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class DESInterface extends JFrame {
    private DES des;
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JRadioButton normalDES;
    private JRadioButton tripleDES;
    private JButton encryptBtn;
    private JButton decryptBtn;
    private JButton copyBtn;
    private JButton clearBtn;
    private JLabel statusLabel;
    private int[] currentEncrypted;

    public DESInterface() {
        des = new DES();
        initComponents();
    }

    private void initComponents() {
        setTitle("Cryptage DES - Outil de sécurité");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Couleurs modernes
        Color bgColor = new Color(240, 242, 245);
        Color primaryColor = new Color(59, 130, 246);
        Color secondaryColor = new Color(99, 102, 241);
        Color successColor = new Color(34, 197, 94);
        Color dangerColor = new Color(239, 68, 68);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // === HEADER ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(20, 25, 20, 25)
        ));

        JLabel titleLabel = new JLabel(" Cryptage DES");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(30, 41, 59));

        JLabel subtitleLabel = new JLabel("Chiffrement et déchiffrement sécurisé");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 116, 139));

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 5));
        titleBox.setBackground(Color.WHITE);
        titleBox.add(titleLabel);
        titleBox.add(subtitleLabel);

        headerPanel.add(titleBox, BorderLayout.WEST);

        // === OPTIONS PANEL ===
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(15, 25, 15, 25)
        ));

        JLabel modeLabel = new JLabel("Mode de cryptage:");
        modeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        modeLabel.setForeground(new Color(51, 65, 85));

        normalDES = new JRadioButton("DES Standard");
        normalDES.setSelected(true);
        normalDES.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        normalDES.setBackground(Color.WHITE);
        normalDES.setFocusPainted(false);

        tripleDES = new JRadioButton("Triple DES (3DES)");
        tripleDES.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tripleDES.setBackground(Color.WHITE);
        tripleDES.setFocusPainted(false);

        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(normalDES);
        modeGroup.add(tripleDES);

        optionsPanel.add(modeLabel);
        optionsPanel.add(normalDES);
        optionsPanel.add(tripleDES);

        // === CONTENT PANEL ===
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        contentPanel.setBackground(bgColor);

        // Input panel
        JPanel inputPanel = createTextPanel("Message à crypter / Données cryptées", primaryColor);
        inputArea = new JTextArea();
        inputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(null);
        inputPanel.add(inputScroll, BorderLayout.CENTER);

        // Output panel
        JPanel outputPanel = createTextPanel("Résultat", secondaryColor);
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(248, 250, 252));
        outputArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(null);
        outputPanel.add(outputScroll, BorderLayout.CENTER);

        contentPanel.add(inputPanel);
        contentPanel.add(outputPanel);

        // === BUTTONS PANEL ===
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setBackground(bgColor);

        encryptBtn = createStyledButton(" Crypter", successColor);
        decryptBtn = createStyledButton(" Décrypter", primaryColor);
        copyBtn = createStyledButton(" Copier", new Color(156, 163, 175));
        clearBtn = createStyledButton(" Effacer", dangerColor);

        buttonsPanel.add(encryptBtn);
        buttonsPanel.add(decryptBtn);
        buttonsPanel.add(copyBtn);
        buttonsPanel.add(clearBtn);

        // === STATUS BAR ===
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(241, 245, 249));
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 0, 0, new Color(226, 232, 240)),
                new EmptyBorder(10, 15, 10, 15)
        ));

        statusLabel = new JLabel("Prêt");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(100, 116, 139));
        statusPanel.add(statusLabel, BorderLayout.WEST);

        // === ASSEMBLY ===
        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(bgColor);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(optionsPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        // === EVENT LISTENERS ===
        encryptBtn.addActionListener(e -> encrypt());
        decryptBtn.addActionListener(e -> decrypt());
        copyBtn.addActionListener(e -> copyToClipboard());
        clearBtn.addActionListener(e -> clearAll());
    }

    private JPanel createTextPanel(String title, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(accentColor);
        panel.add(label, BorderLayout.NORTH);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    private void encrypt() {
        String input = inputArea.getText().trim();
        if (input.isEmpty()) {
            showStatus("⚠️ Veuillez entrer un message à crypter", new Color(245, 158, 11));
            return;
        }

        try {
            if (normalDES.isSelected()) {
                currentEncrypted = des.crypte(input);
                showStatus("✓ Message crypté avec DES Standard", new Color(34, 197, 94));
            } else if (tripleDES.isSelected()) {
                currentEncrypted = des.crypte3DES(input);
                showStatus("✓ Message crypté avec Triple DES", new Color(34, 197, 94));
            }
            outputArea.setText(Arrays.toString(currentEncrypted));
        } catch (Exception ex) {
            showStatus("✗ Erreur lors du cryptage: " + ex.getMessage(), new Color(239, 68, 68));
        }
    }

    private void decrypt() {
        String input = inputArea.getText().trim();
        if (input.isEmpty()) {
            showStatus("⚠️ Veuillez entrer des données à décrypter", new Color(245, 158, 11));
            return;
        }

        try {
            int[] encrypted;
            if (input.startsWith("[") && input.endsWith("]")) {
                String[] parts = input.substring(1, input.length() - 1).split(",");
                encrypted = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    encrypted[i] = Integer.parseInt(parts[i].trim());
                }
            } else {
                showStatus("✗ Format invalide. Utilisez le format: [0, 1, 0, ...]", new Color(239, 68, 68));
                return;
            }

            String decrypted;
            if (normalDES.isSelected()) {
                decrypted = des.decrypte(encrypted);
                showStatus("✓ Message décrypté avec DES Standard", new Color(34, 197, 94));
            } else if (tripleDES.isSelected()) {
                decrypted = des.decrypte3DES(encrypted);
                showStatus("✓ Message décrypté avec Triple DES", new Color(34, 197, 94));
            } else {
                decrypted = "";
            }
            outputArea.setText(decrypted);
        } catch (Exception ex) {
            showStatus("✗ Erreur lors du décryptage: " + ex.getMessage(), new Color(239, 68, 68));
        }
    }

    private void copyToClipboard() {
        String text = outputArea.getText();
        if (text.isEmpty()) {
            showStatus("⚠️ Aucun résultat à copier", new Color(245, 158, 11));
            return;
        }

        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new java.awt.datatransfer.StringSelection(text), null);
        showStatus("✓ Résultat copié dans le presse-papiers", new Color(34, 197, 94));
    }

    private void clearAll() {
        inputArea.setText("");
        outputArea.setText("");
        currentEncrypted = null;
        showStatus("Prêt", new Color(100, 116, 139));
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new DESInterface().setVisible(true);
        });
    }
}