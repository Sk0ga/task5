package com.company.gui;

import com.company.generator.GenerationAlgorithm;

import com.company.solver.SolvingAlgorithm;
import com.company.util.ColorScheme;
import com.company.util.ImagePanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Objects;

import static com.company.generator.GenerationAlgorithm.PRIM;
import static com.company.solver.SolvingAlgorithm.LEE;

public class MainFrame {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    private static final int DEFAULT_GRID = 17;
    public static int cellWidth = 20;
    public static int cellHeight = 20;
    public static int speed = 50;
    public static boolean gridDisplayed;
    public static boolean manualEntranceExit;
    public static boolean generated;
    public static boolean solved;
    public static boolean autoSolve;
    private MazeGridPanel grid;
    private ImagePanel mazeBorder;
    private int cols;
    private int rows;
    private JSpinner rowsSpinner;
    private JSpinner colsSpinner;
    private JPanel genAlgPanel;
    private JPanel entranceExitSettingsPanel;
    private JLabel entExitLabel;
    private JRadioButton exitAutoRadioButton;
    private JRadioButton exitManualRadioButton;
    private JButton createMazeButton;
    private JLabel solveModeLabel;
    private JRadioButton autoSolveRadioButton;
    private JRadioButton manualSolveRadioButton;
    private JPanel visualizationPanel;
    private JLabel visualSpeed;
    private JSlider visualizationSlider;
    private JButton solveMazeButton;

    public MainFrame() {
        cellWidth = Math.floorDiv(WIDTH, DEFAULT_GRID);
        cellHeight = Math.floorDiv(HEIGHT, DEFAULT_GRID);
        setRows(DEFAULT_GRID);
        setCols(DEFAULT_GRID);
        autoSolve = true;

        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                     | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
            createAndShowGUI();
        });

    }

    public static void main(String[] args) {
        new MainFrame();
    }

    public static void setGenerated(boolean generated) {
        MainFrame.generated = generated;
    }

    public static void setSolved(boolean solved) {
        MainFrame.solved = solved;
    }


    private void createAndShowGUI() {
        JFrame frame = new JFrame("Генератор лабиринтов");
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        frame.setContentPane(container);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        grid = new MazeGridPanel(rows, cols);
        grid.setBackground(ColorScheme.TRANSPARENT);

        mazeBorder = new ImagePanel();
        mazeBorder.setSize(WIDTH, HEIGHT);
        final int BORDER_SIZE = 10;
        mazeBorder.setBounds(0, 0, WIDTH + BORDER_SIZE, HEIGHT + BORDER_SIZE);
        mazeBorder.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

        mazeBorder.add(grid, BorderLayout.CENTER);

        JPanel leftPanel = initParamsPanel();
        Dimension d = leftPanel.getPreferredSize();
        d.height = mazeBorder.getPreferredSize().height;
        leftPanel.setPreferredSize(d);
        container.add(leftPanel);
        container.add(mazeBorder);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        grid.setVisible(false);
    }


    private void updateCellWidth() {
        cellWidth = Math.floorDiv(WIDTH, getRows());
        cellHeight = Math.floorDiv(HEIGHT, getCols());
    }

    private void updateGridSize() {
        mazeBorder.remove(grid);
        updateCellWidth();
        grid = new MazeGridPanel(rows, cols);
        grid.setBackground(ColorScheme.TRANSPARENT);
        mazeBorder.add(grid);
        gridDisplayed = true;
        generated = false;
        mazeBorder.revalidate();
    }

    @SuppressWarnings("unchecked")
    private JPanel initParamsPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setInheritsPopupMenu(true);
        leftPanel.setMaximumSize(new Dimension(300, 600));
        leftPanel.setMinimumSize(new Dimension(260, 600));
        leftPanel.setPreferredSize(new Dimension(270, 600));
        leftPanel.putClientProperty("html.disable", Boolean.FALSE);
        leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));

        JPanel mazeGeneration = new JPanel();
        mazeGeneration.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        leftPanel.add(mazeGeneration, gbc);

        mazeGeneration.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Параметры генерации", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mazeGeneration.add(sizePanel, gbc);

        rowsSpinner = new JSpinner();
        rowsSpinner.setModel(new SpinnerNumberModel(DEFAULT_GRID, 7, 37, 2));
        rowsSpinner.addChangeListener(e -> setRows((int) rowsSpinner.getValue()));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        sizePanel.add(rowsSpinner, gbc);

        JLabel rowsLabel = new JLabel();
        rowsLabel.setText("Ширина");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 0, 10);
        sizePanel.add(rowsLabel, gbc);

        JLabel colsLabel = new JLabel();
        colsLabel.setText("Высота");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        sizePanel.add(colsLabel, gbc);

        colsSpinner = new JSpinner();
        colsSpinner.setModel(new SpinnerNumberModel(DEFAULT_GRID, 7, 37, 2));
        colsSpinner.addChangeListener(e -> setCols((int) colsSpinner.getValue()));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        sizePanel.add(colsSpinner, gbc);

        genAlgPanel = new JPanel();
        genAlgPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mazeGeneration.add(genAlgPanel, gbc);

        JButton showGridButton = new JButton();
        showGridButton.setText("Показать сетку");

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        mazeGeneration.add(showGridButton, gbc);

        entranceExitSettingsPanel = new JPanel();
        entranceExitSettingsPanel.setLayout(new GridBagLayout());
        entranceExitSettingsPanel.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mazeGeneration.add(entranceExitSettingsPanel, gbc);

        entExitLabel = new JLabel();
        entExitLabel.setText("Вход/выход");
        entExitLabel.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 0, 10);
        entranceExitSettingsPanel.add(entExitLabel, gbc);

        exitAutoRadioButton = new JRadioButton();
        exitAutoRadioButton.setText("Авто");
        exitAutoRadioButton.setSelected(true);
        exitAutoRadioButton.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 0, 10);
        entranceExitSettingsPanel.add(exitAutoRadioButton, gbc);

        exitManualRadioButton = new JRadioButton();
        exitManualRadioButton.setText("Вручную");
        exitManualRadioButton.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        entranceExitSettingsPanel.add(exitManualRadioButton, gbc);

        ButtonGroup entExitGroup = new ButtonGroup();
        entExitGroup.add(exitAutoRadioButton);
        entExitGroup.add(exitManualRadioButton);

        createMazeButton = new JButton();
        createMazeButton.setText("Создать лабиринт");
        createMazeButton.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        mazeGeneration.add(createMazeButton, gbc);

        JPanel mazeSolving = new JPanel();
        mazeSolving.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        leftPanel.add(mazeSolving, gbc);
        mazeSolving
                .setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Параметры прохождения",
                        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));

        JPanel solvePanel = new JPanel();
        solvePanel.setLayout(new GridBagLayout());
        solvePanel.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mazeSolving.add(solvePanel, gbc);

        solveModeLabel = new JLabel();
        solveModeLabel.setText("Прохождение");
        solveModeLabel.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 0, 10);
        solvePanel.add(solveModeLabel, gbc);

        autoSolveRadioButton = new JRadioButton();
        autoSolveRadioButton.setText("Авто");
        autoSolveRadioButton.setSelected(true);
        autoSolveRadioButton.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 0, 10);
        solvePanel.add(autoSolveRadioButton, gbc);

        manualSolveRadioButton = new JRadioButton();
        manualSolveRadioButton.setText("Вручную");
        manualSolveRadioButton.setEnabled(true);

        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 0, 10);
        solvePanel.add(manualSolveRadioButton, gbc);

        ButtonGroup solveGroup = new ButtonGroup();
        solveGroup.add(autoSolveRadioButton);
        solveGroup.add(manualSolveRadioButton);

        JPanel solveAlgPanel = new JPanel();
        solveAlgPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mazeSolving.add(solveAlgPanel, gbc);

        visualizationPanel = new JPanel();
        visualizationPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mazeSolving.add(visualizationPanel, gbc);

        visualSpeed = new JLabel();
        visualSpeed.setText("Скорость визуализации");
        visualSpeed.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.05;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 0, 10);
        visualizationPanel.add(visualSpeed, gbc);

        visualizationSlider = new JSlider();
        visualizationSlider.setInverted(true);
        visualizationSlider.setMaximum(100);
        visualizationSlider.setMinimum(0);
        visualizationSlider.setMinorTickSpacing(50);
        visualizationSlider.setPaintLabels(true);
        visualizationSlider.setPaintTicks(true);
        visualizationSlider.setSnapToTicks(true);
        visualizationSlider.setValue(50);
        visualizationSlider.setValueIsAdjusting(true);
        visualizationSlider.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        visualizationPanel.add(visualizationSlider, gbc);

        manualSolveRadioButton.addActionListener(e -> {
            autoSolve = false;
            grid.setAutoSolve(false);
            grid.setClickable(true);
            grid.requestFocus();
        });

        autoSolveRadioButton.addActionListener(e -> {
            autoSolve = true;
        });

        solveMazeButton = new JButton();
        solveMazeButton.setText("Искать путь");
        solveMazeButton.setEnabled(true);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        mazeSolving.add(solveMazeButton, gbc);


        showGridButton.addActionListener(e -> {
            updateGridSize();
        });

        visualizationSlider.addChangeListener(e -> speed = visualizationSlider.getValue());

        exitAutoRadioButton.addActionListener(e -> {
            grid.setClickable(false);
            manualEntranceExit = false;
        });
        exitManualRadioButton.addActionListener(e -> {
            grid.setClickable(true);
            manualEntranceExit = true;
        });
        createMazeButton.addActionListener(e -> {
            if (gridDisplayed) {
                generated = true;
                solved = false;
                gridDisplayed = false;
                grid.generate(Objects.requireNonNull(PRIM));
            }
        });

        solveMazeButton.addActionListener(e -> {
            if (generated && autoSolve && !solved) {
                grid.solve(Objects.requireNonNull(LEE));
            } else if (generated && !autoSolve && !solved) {
                grid.setAutoSolve(false);
            } else if (!generated) {
                JOptionPane.showMessageDialog(leftPanel.getRootPane(), "Лабиринт ещё не готов. Пожалуйста, подождите.");
            }
        });

        return leftPanel;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

}
