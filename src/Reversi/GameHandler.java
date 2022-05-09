package Reversi;

import java.awt.event.*;

public class GameHandler extends MouseAdapter{
    GameScreen gameScn;
    private boolean PlayerWhite;
    private boolean TurnWhite;
    // 1: 흰색, 0: 돌이 없는 상태, -1: 검은색
    private int[][] board = new int[9][9]; // 8 x 8
    private int[][] snapshot = new int[9][9];

    GameHandler(GameScreen gameScn) {
        this.gameScn = gameScn;
        PlayerWhite = true;
        TurnWhite = true;

        initBoard();
    }

    public void changeTurn() {
        TurnWhite = !TurnWhite;
        PlayerWhite = !PlayerWhite; // 임시
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (TurnWhite != PlayerWhite) // 내 턴이 아닌 입력 무시
            return;

        int x = getCell(e.getX()), y = getCell(e.getY());
        if ((x <= 0 || x >= 9) || (y <= 0 || y >= 9)) // == (x < 70 || x >= 630) || (y < 70 || y >= 630)
            return;

        saveToSnap();
        board[y][x] = PlayerWhite ? 1 : -1;
        changeTurn();
        gameScn.repaint();
    }

    public int getCell(int p) {
        return p / 70;
    }

    public int getStone(int x, int y) {
        return board[y][x];
    }

    public void initBoard() {
        for (int i=1; i<=8; i++) // 보드와 스냅샷 초기화
            for (int j=1; j<=8; j++) {
                board[i][j] = 0;
                snapshot[i][j] = 0;
            }

        board[4][4] = 1; snapshot[4][4] = 1;
        board[4][5] = -1; snapshot[4][5] = -1;
        board[5][4] = -1; snapshot[5][4] = -1;
        board[5][5] = 1; snapshot[5][5] = 1;
    }

    public void saveToSnap() {
        for (int i=1; i<=8; i++)
            for (int j=1; j<=8; j++)
                snapshot[i][j] = board[i][j];
    }

    public void loadFromSnap() {
        for (int i=1; i<=8; i++)
            for (int j=1; j<=8; j++)
                board[i][j] = snapshot[i][j];
    }

    public void checkColor() {

    }
}
