import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Sudoku extends JPanel {
    static final int SIZE = 9;
    static final int BOX = 3;
    static int dimension = 500;

    JLabel[][] board;
    int[][] solution;
    int[][] puzzle;

    int selectedNumber = -1; // -1 means nothing selected
    static int[] numCounts = new int[SIZE + 1];
    int maxNumCount = 9;

    private boolean gameFinished = false;

    // COLORS
    private final Color cream = new Color(243, 230, 208);
    private final Color green = new Color(140, 220, 140);
    private final Color blue = new Color(180, 200, 240);
    private final Color beige = new Color(223, 208, 183);
    private final Color red = new Color(220, 140, 140);

    public Sudoku() {
        this.setBounds(350, 60, dimension, dimension);
        this.setLayout(new GridLayout(SIZE, SIZE));
        this.setBackground(Color.BLACK);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        initSudoku();
        this.setVisible(true);
    }

    private void initSudoku() {
        board = new JLabel[SIZE][SIZE];
        solution = new int[SIZE][SIZE];
        puzzle = new int[SIZE][SIZE];

        generateSolution(0, 0);

        // copy solution to puzzle
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                puzzle[r][c] = solution[r][c];
            }
        }

        // remove numbers (balanced per box)
        Random rand = new Random();
        int blanksPerBox = 4;
        for (int boxR = 0; boxR < BOX; boxR++) {
            for (int boxC = 0; boxC < BOX; boxC++) {
                int removed = 0;
                while (removed < blanksPerBox) {
                    int r = boxR * BOX + rand.nextInt(BOX);
                    int c = boxC * BOX + rand.nextInt(BOX);
                    if (puzzle[r][c] != 0) {
                        puzzle[r][c] = 0;
                        removed++;
                    }
                }
            }
        }

        // build board
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int val = puzzle[r][c];
                board[r][c] = makeCell(r, c, val);
                this.add(board[r][c]);
            }
        }

        updateNumCounts();
    }

    private boolean generateSolution(int row, int col) {
        if (row == SIZE) return true;

        int nextRow = (col == SIZE - 1) ? row + 1 : row;
        int nextCol = (col == SIZE - 1) ? 0 : col + 1;

        Integer[] nums = new Integer[SIZE];
        for (int i = 0; i < SIZE; i++) nums[i] = i + 1;
        java.util.Collections.shuffle(java.util.Arrays.asList(nums));

        for (int n : nums) {
            if (isValid(row, col, n)) {
                solution[row][col] = n;
                if (generateSolution(nextRow, nextCol)) return true;
                solution[row][col] = 0; // backtrack
            }
        }
        return false;
    }

    private boolean isValid(int r, int c, int n) {
        for (int x = 0; x < SIZE; x++) {
            if (solution[r][x] == n) return false;
            if (solution[x][c] == n) return false;
        }
        int startR = r - r % BOX, startC = c - c % BOX;
        for (int i = startR; i < startR + BOX; i++) {
            for (int j = startC; j < startC + BOX; j++) {
                if (solution[i][j] == n) return false;
            }
        }
        return true;
    }

    private JLabel makeCell(int r, int c, int n) {
        JLabel cell = new JLabel(n == 0 ? "" : Integer.toString(n), SwingConstants.CENTER);
        cell.setOpaque(true);
        cell.setBackground(cream);

        int top = (r % 3 == 0) ? 2 : 1;
        int left = (c % 3 == 0) ? 2 : 1;
        int bottom = (r == SIZE - 1) ? 2 : 1;
        int right = (c == SIZE - 1) ? 2 : 1;
        cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

        if (n == 0) {
            cell.putClientProperty("locked", false);
            cell.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    boolean locked = (boolean) cell.getClientProperty("locked");

                    if (!cell.getText().equals("")) {
                        int val = Integer.parseInt(cell.getText());
                        setSelectedNumber(val);
                        return;
                    }

                    if (selectedNumber != -1 && !locked) {
                        if (selectedNumber == solution[r][c]) {
                            cell.setText(Integer.toString(selectedNumber));
                            cell.setBackground(green);
                            cell.putClientProperty("locked", true);
                            updateNumCounts();
                            if (isSolved()) finishGame();
                        } else {
                            flashRed(cell);
                        }
                    }
                }
            });
        }
        return cell;
    }

    private void flashRed(JLabel cell) {
        Color original = cell.getBackground();
        cell.setBackground(red);
        new javax.swing.Timer(300, evt -> {
            cell.setBackground(original);
            ((javax.swing.Timer) evt.getSource()).stop();
        }).start();
    }

    public void setSelectedNumber(int n) {
        selectedNumber = n;
        refreshColors();
    }

    private void refreshColors() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                JLabel cell = board[r][c];
                String text = cell.getText();
                if (text.equals("")) continue;

                int val = Integer.parseInt(text);
                if (cell.getBackground().equals(green)) continue;

                if (selectedNumber != -1 && val == selectedNumber) {
                    cell.setBackground(blue);
                } else if (numCounts[val] == maxNumCount) {
                    cell.setBackground(beige);
                } else {
                    cell.setBackground(cream);
                }
            }
        }
    }

    private void updateNumCounts() {
        for (int i = 1; i <= SIZE; i++) numCounts[i] = 0;

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                String text = board[r][c].getText();
                if (text.equals("")) continue;
                int val = Integer.parseInt(text);
                numCounts[val]++;
            }
        }
        refreshColors();
    }

    private boolean isSolved() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                String text = board[r][c].getText();
                if (text.equals("")) return false;
                int val = Integer.parseInt(text);
                if (val != solution[r][c]) return false;
            }
        }
        return true;
    }

    private void finishGame() {
        if (gameFinished) return;
        gameFinished = true;

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                board[r][c].putClientProperty("locked", true);
            }
        }
        JOptionPane.showMessageDialog(this, "Game Over! You solved it!", "Sudoku", JOptionPane.INFORMATION_MESSAGE);
    }
}
