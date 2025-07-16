import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import TicTacToe.Main;

public class Main {
    private static TicTacToe server;
    private static String playerName;
    private static String roomId;
    private static String[][] board = new String[3][3];
    private static JFrame frame;
    private static JButton[][] buttons = new JButton[3][3];
    private static JLabel statusLabel;

    public static void main(String[] args) {
        if (args.length < 4) {
            JOptionPane.showMessageDialog(null, "Usage: java TicTacToeClientGUI <server_ip> <rmi_port> <player_name>", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String serverIp = args[0];
        int rmiPort = Integer.parseInt(args[1]);
        playerName = args[2];

        try {
            String rmiUrl = "rmi://" + serverIp + ":" + rmiPort + "/TicTacToe";
            server = (TicTacToeService) Naming.lookup(rmiUrl);

            roomId = server.joinGame(playerName);
            JOptionPane.showMessageDialog(null, "Joined game room: " + roomId, "Info", JOptionPane.INFORMATION_MESSAGE);

            createGUI();

            new Thread(() -> {
                try {
                    while (true) {
                        GameState gameState = server.getGameState(roomId);
                        updateBoard(gameState);
                        if (!gameState.getCurrentTurn().equals(playerName)) {
                            statusLabel.setText("Opponent's turn...");
                        } else {
                            statusLabel.setText("Your turn!");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createGUI() {
        frame = new JFrame("Tic Tac Toe - " + playerName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel panel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.BOLD, 50));
                buttons[i][j] = button;
                final int x = i, y = j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        makeMove(x, y);
                    }
                });
                panel.add(button);
            }
        }

        statusLabel = new JLabel("Waiting for opponent...");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(statusLabel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void makeMove(int x, int y) {
        try {
            boolean success = server.makeMove(roomId, playerName, x, y);
            if (!success) {
                JOptionPane.showMessageDialog(frame, "Invalid move! Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateBoard(GameState gameState) {
        String[][] newBoard = gameState.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!newBoard[i][j].equals(board[i][j])) {
                    board[i][j] = newBoard[i][j];
                    buttons[i][j].setText(board[i][j] == null ? "" : board[i][j].equals(playerName) ? "X" : "O");
                    buttons[i][j].setEnabled(newBoard[i][j] == null);
                }
            }
        }
    }
}