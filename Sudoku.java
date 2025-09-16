import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Sudoku extends JPanel {
    static final int SIZE = 9;
    static final int BOX_SIZE = 3;
    static int dimension = 500;
    JLabel[][] board;
    int[][] solution;  // full solution
    int[][] puzzle;    // what is displayed to the user (blanks = 0)
    int selectedNumber = -1; // -1 means nothing selected
    static int[] numCounts = new int[SIZE + 1];
    int maxNumCount = 9;
    private boolean gameFinished = false;


    public Sudoku() {
        this.setBounds(350, 60, dimension, dimension);
        this.setLayout(new GridLayout(SIZE, SIZE));
        this.setBackground(Color.BLACK);
        this.setBorder(BorderFactory.createLineBorder(new Color(4, 3, 3), 2));
        this.sudoku();
        this.setVisible(true);
    }

    public void sudoku() {
        board = new JLabel[SIZE][SIZE];
        solution = new int[SIZE][SIZE];

        // fill solution using backtracking
        generate_solution(0, 0);

        // copy solution to puzzle array
        puzzle = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                puzzle[i][j] = solution[i][j];
            }
        }

        // create puzzle by removing numbers (balanced per box)
        Random rand = new Random();
        int blanksPerBox = 4; // adjust difficulty
        for (int boxRow = 0; boxRow < BOX_SIZE; boxRow++) {
            for (int boxCol = 0; boxCol < BOX_SIZE; boxCol++) {
                int removed = 0;
                while (removed < blanksPerBox) {
                    int r = boxRow * BOX_SIZE + rand.nextInt(BOX_SIZE);
                    int c = boxCol * BOX_SIZE + rand.nextInt(BOX_SIZE);
                    if (puzzle[r][c] != 0) {
                        puzzle[r][c] = 0;
                        removed++;
                    }
                }
            }
        }

        // add JLabels for display
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (puzzle[row][col] == 0) {
                    board[row][col] = newLabel(row, col, 0); // blank
                } else {
                    board[row][col] = newLabel(row, col, puzzle[row][col]);
                }
                this.add(board[row][col]);
            }
        }

        numCounts();
    }

    public boolean generate_solution(int row, int col) {
        if (row == SIZE) return true;

        int nextRow = (col == SIZE - 1) ? row + 1 : row;
        int nextCol = (col == SIZE - 1) ? 0 : col + 1;

        Integer[] numbers = new Integer[SIZE];
        for (int i = 0; i < SIZE; i++) numbers[i] = i + 1;

        // shuffle numbers
        java.util.Collections.shuffle(java.util.Arrays.asList(numbers));

        for (int num : numbers) {
            if (isValid(row, col, num)) {
                solution[row][col] = num;
                if (generate_solution(nextRow, nextCol)) return true;
                solution[row][col] = 0; // backtrack
            }
        }

        return false;
    }

    public boolean isValid(int row, int col, int num) {
        for (int c = 0; c < SIZE; c++)
            if (solution[row][c] == num) return false;
        for (int r = 0; r < SIZE; r++)
            if (solution[r][col] == num) return false;
        int startRow = row - row % BOX_SIZE;
        int startCol = col - col % BOX_SIZE;
        for (int r = startRow; r < startRow + BOX_SIZE; r++)
            for (int c = startCol; c < startCol + BOX_SIZE; c++)
                if (solution[r][c] == num) return false;
        return true;
    }

    public JLabel newLabel(int r, int c, int n) {
        JLabel pl;
        if (n == 0) {
            pl = new JLabel("", SwingConstants.CENTER); // blank
        } else {
            pl = new JLabel(Integer.toString(n), SwingConstants.CENTER);
        }
        pl.setOpaque(true);
        pl.setBackground(new Color(243, 230, 208));
        pl.setBorder(BorderFactory.createLineBorder(new Color(4, 3, 3), 1));
        // figure out border thickness
        int top = 1;
        int left = 1;
        int bottom = 1;
        int right = 1;
        // thicker line every 3 rows
        if (r % 3 == 0) {
            top = 2;
        }
        if (c % 3 == 0) {
            left = 2;
        }
        // bottom and right edges of the board
        if (r == SIZE - 1) {
            bottom = 2;
        }
        if (c == SIZE - 1) {
            right = 2;
        }

        pl.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

        if (n == 0) {
            // track locked state in JLabel
            pl.putClientProperty("locked", false);

            pl.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    boolean locked = (boolean) pl.getClientProperty("locked");

                    // If the cell already has a number, make it the selected number
                    if (!pl.getText().equals("")) {
                        int val = Integer.parseInt(pl.getText());
                        setSelectedNumber(val);
                        return;
                    }

                    // Otherwise, if blank, try to place the selected number
                    if (selectedNumber != -1 && !locked) {
                        if (selectedNumber == solution[r][c]) {
                            pl.setText(Integer.toString(selectedNumber));
                            pl.setBackground(new Color(140, 220, 140)); // green
                            pl.putClientProperty("locked", true);
                            numCounts();
                            //check if the entire puzzle is solved
                            if (checkGameOver()) {
                                finishGame();
                            }
                        } else {
                            Color original = pl.getBackground();
                            pl.setBackground(new Color(220, 140, 140));
                            new javax.swing.Timer(300, evt -> {
                                pl.setBackground(original);
                                ((javax.swing.Timer) evt.getSource()).stop();
                            }).start();
                        }
                    }
                }
            });
        }

        return pl;
    }
    public void setSelectedNumber(int n) {
        this.selectedNumber = n;

        Color highlightColor = new Color(180, 200, 240); // soft blue
        Color givenColor = new Color(243, 230, 208);     // default background
        Color correctGreen = new Color(140, 220, 140);   // locked correct
        Color usedUpColor = new Color(223, 208, 183);    // fully used

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                JLabel cell = board[r][c];
                String text = cell.getText();
                if (text.equals("")) continue;

                int val = Integer.parseInt(text);

                if (cell.getBackground().equals(correctGreen)) {
                    continue; // keep green
                }

                if (numCounts[val] == maxNumCount) {
                    cell.setBackground(usedUpColor);
                } else if (val == n) {
                    cell.setBackground(highlightColor); // highlight only chosen number
                } else {
                    cell.setBackground(givenColor); // reset others
                }
            }
        }
    }
    public void updateCellColors() {
        Color highlightColor = new Color(180, 200, 240); // blue
        Color givenColor = new Color(243, 230, 208);     // cream
        Color correctGreen = new Color(140, 220, 140);   // green
        Color usedUpColor = new Color(223, 208, 183);    // beige

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                JLabel cell = board[r][c];
                String text = cell.getText();
                if (text.equals("")) continue;

                int val = Integer.parseInt(text);

                if (cell.getBackground().equals(correctGreen)) {
                    continue; // keep green if locked correct
                }

                if (selectedNumber != -1 && val == selectedNumber) {
                    cell.setBackground(highlightColor); // ✅ keep blue highlight
                } else if (numCounts[val] == maxNumCount) {
                    cell.setBackground(usedUpColor);
                } else {
                    cell.setBackground(givenColor);
                }
            }
        }
    }


    public void numCounts() {
        for (int i = 1; i <= SIZE; i++) numCounts[i] = 0;
        Color correctGreen = new Color(140, 220, 140);

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                String text = board[r][c].getText();
                if (text.equals("")) continue;
                int val = Integer.parseInt(text);
                switch (val) {
                    case 1 -> numCounts[1]++;
                    case 2 -> numCounts[2]++;
                    case 3 -> numCounts[3]++;
                    case 4 -> numCounts[4]++;
                    case 5 -> numCounts[5]++;
                    case 6 -> numCounts[6]++;
                    case 7 -> numCounts[7]++;
                    case 8 -> numCounts[8]++;
                    case 9 -> numCounts[9]++;
                }
            }
        }
        // highlight overused numbers
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                String text = board[r][c].getText();
                if (text.equals("")) continue;
                int val = Integer.parseInt(text);
                if (numCounts[val] == maxNumCount) {
                    board[r][c].setBackground(new Color(223, 208, 183));
                }
                // remove the else that resets to base colour
            }
        }
        // after counts update, refresh cell colors properly
        updateCellColors();

        // print counts
        for (int i = 1; i <= SIZE; i++) {
            System.out.println("Number " + i + " appears " + numCounts[i] + " times");
        }
    }

    // returns true if all cells are filled and match the solution
    private boolean checkGameOver() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                String text = board[i][j].getText();
                if (text.equals("")) return false; // empty -> not finished

                int val;
                try {
                    val = Integer.parseInt(text);
                } catch (NumberFormatException ex) {
                    return false; // invalid -> not finished
                }

                if (val != solution[i][j]) return false; // mismatch -> not finished
            }
        }
        return true; // all cells present and correct
    }

    // call this once when the game is finished
    private void finishGame() {
        if (gameFinished) return; // guard: only once
        gameFinished = true;

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                board[r][c].putClientProperty("locked", true);
            }
        }

        // stop any timers here if you have one (example variable 'time')
        // if (time != null) time.stop();

        JOptionPane.showMessageDialog(this, "Game Over! You solved it!", "Sudoku", JOptionPane.INFORMATION_MESSAGE);
    }

}
