package Reversi;

import java.awt.event.*;
import java.util.Random;

public class GameHandler extends MouseAdapter{
    GameScreen gameScn;
    DataHandler dataHdr;
    private boolean PlayerWhite;
    private boolean TurnWhite;
    private boolean savedTurn;
    private boolean isLoadedBefore;
    private int StoneNum = 4;
    // 1: 흰색, 0: 돌이 없는 상태, -1: 검은색
    private final int White = 1;
    private final int Black = -1;
    private final int Empty = 0;

    private int[][] board = new int[9][9]; // 8 x 8
    private int[][] snapshot = new int[9][9];
    // [[x, y] * 64]
    private int[][] possibleCells = new int[64][2];
    private int pCellCount = 0;
    // 메소드가 공유할 수 있도록 클래스 변수로 변경, checkChangeColor 메소드로만 직접 값이 변경됨
    private boolean[] isPossible = new boolean[8];

    GameHandler(GameScreen gameScn, DataHandler dataHdr, boolean isServer) {
        this.gameScn = gameScn;
        this.dataHdr = dataHdr;
        // 서버인 경우 흰색, 클라이언트인 경우 검은색 배정
        PlayerWhite = isServer;
        TurnWhite = true;
        savedTurn = true;
        isLoadedBefore = false;

        initBoard();
    }

    // 돌을 놓는 3가지 함수
    @Override
    public void mousePressed(MouseEvent e) {
        if (TurnWhite != PlayerWhite) // 내 턴이 아닌 입력 무시
            return;

        int x = getCell(e.getX()), y = getCell(e.getY());
        if ((x <= 0 || x >= 9) || (y <= 0 || y >= 9)) // == (x < 70 || x >= 630) || (y < 70 || y >= 630)
            return;

        checkChangeColor(x, y, false);
        boolean isThereTrue = false;
        for  (int i=0; i<8; i++) {
            isThereTrue = isPossible[i];
            if (isThereTrue) break; // isPossible이 하나라도 true이면 중지
        }

        // 놓는 곳이 돌을 바꾸지 못하는 경우거나 돌이 이미 있는 경우
        if (!isThereTrue || board[y][x] != Empty)
            return;

        dataHdr.sender.send(Integer.toString(x) + Integer.toString(y), 'g');
        saveToSnap();
        board[y][x] = PlayerWhite ? White : Black;
        StoneNum++;
        for (int i=0; i<8; i++)
            if (isPossible[i])
                changeColor(x, y, i);
        changeTurn();
    }

    public int getCell(int p) {
        return p / 70;
    }

    public int getStone(int x, int y) {
        return board[y][x];
    }

    public void receiveCellData(int x, int y) {
        checkChangeColor(x, y, true);

        saveToSnap();
        board[y][x] = !PlayerWhite ? White : Black;
        StoneNum++;
        for (int i=0; i<8; i++)
            if (isPossible[i])
                changeColor(x, y, i);
        changeTurn();
    }

    public void selectRandomCell() {
        if (TurnWhite != PlayerWhite) // 내 턴이 아닌 경우 무시
            return;

        Random random = new Random();
        checkAllCells();
        int rand_idx = random.nextInt(pCellCount);
        int x = possibleCells[rand_idx][0], y = possibleCells[rand_idx][1];

        // 뽑힌 좌표에서 가능한 방향 탐색
        checkChangeColor(x, y, false);

        dataHdr.sender.send(Integer.toString(x)+Integer.toString(y), 'g');
        saveToSnap();
        board[y][x] = PlayerWhite ? White : Black;
        StoneNum++;
        for (int i=0; i<8; i++)
            if (isPossible[i])
                changeColor(x, y, i);
        changeTurn();
    }

    public void initBoard() {
        for (int i=1; i<=8; i++) // 보드와 스냅샷 초기화
            for (int j=1; j<=8; j++) {
                board[i][j] = Empty;
                snapshot[i][j] = Empty;
            }

        board[4][4] = White; snapshot[4][4] = White;
        board[4][5] = Black; snapshot[4][5] = Black;
        board[5][4] = Black; snapshot[5][4] = Black;
        board[5][5] = White; snapshot[5][5] = White;
    }

    public boolean getTurn() {
        return this.TurnWhite;
    }

    public void saveToSnap() {
        for (int i=1; i<=8; i++)
            for (int j=1; j<=8; j++)
                snapshot[i][j] = board[i][j];
        isLoadedBefore = false;
    }

    public boolean loadFromSnap() {
        // 되돌리기가 이미 한번 이루어진 경우
        if (isLoadedBefore)
            return false;

        for (int i=1; i<=8; i++)
            for (int j=1; j<=8; j++)
                board[i][j] = snapshot[i][j];

        TurnWhite = savedTurn;
        isLoadedBefore = true;
        gameScn.repaint();
        return true;
    }

    public void changeTurn() {
        savedTurn = TurnWhite;
        TurnWhite = !TurnWhite;
        // 놓을 수 있는 곳이 없는 경우 턴이 다시 돌아옴/
        if (!checkAllCells()) {
            TurnWhite = !TurnWhite;
        }
        gameScn.Turn.setText("Turn: " + (TurnWhite ? "White" : "Black"));

        gameScn.timer.resetTimer();
        gameScn.repaint();
        checkGameOver(false);
    }

    public void checkGameOver(boolean isSurrender) {
        // 판이 다 채워진 경우
        if (!isSurrender && StoneNum == 64) {
            int WhiteNum = 62, BlackNum;
            for (int i=1; i<=8; i++)
                for (int j=1; j<=8; j++)
                    // 검은색인 경우 -1, 흰색인 경우 변화 없음
                    WhiteNum += (board[i][j] == Black) ? -1 : 0;
            BlackNum = 64 - WhiteNum;

            if (WhiteNum >= BlackNum)
                System.out.println("White Win!");
            else
                System.out.println("Black Win!");

            gameScn.timer.stopTimer();
        }

        // /ff 커맨드가 입력된 경우
        else if (isSurrender) {
            if (!TurnWhite)
                System.out.println("White Win!");
            else
                System.out.println("Black Win!");

            gameScn.timer.stopTimer();
        }
    }

    public boolean checkAllCells() {
        boolean isThereTrue = false, isThereCell = false;
        // possibleCells 초기화
        pCellCount = 0;

        for (int y=1; y<=8; y++)
            for (int x=1; x<=8; x++) {
                checkChangeColor(x, y, false);
                for (int k=0; k<8; k++) {
                    isThereTrue = isPossible[k];
                    if (isThereTrue) break;
                }

                if (isThereTrue) {
                    possibleCells[pCellCount][0] = x;
                    possibleCells[pCellCount][1] = y;
                    pCellCount++;

                    isThereCell = true;
                    isThereTrue = false;
                }
            }
        return isThereCell;
    }

    public void checkChangeColor(int cell_x, int cell_y, boolean isReceived) {
        // 0-1-2-3 / 4-5-6-7 -> 동-서-남-북 / 왼쪽 위-왼쪽 아래-오른쪽 위-오른쪽 아래
        for (int i=0; i<8; i++)
            isPossible[i] = false;

        int my_color;
        if (isReceived)
            my_color = !PlayerWhite ? White : Black;
        else
            my_color = PlayerWhite ? White : Black;
        int cmp_color = -my_color;

        if (cell_x != 8 && board[cell_y][cell_x + 1] == cmp_color) { // 동쪽
            for (int k=2; cell_x + k <= 8; k++) {
                if (my_color == board[cell_y][cell_x + k])
                    isPossible[0] = true;
                else if (board[cell_y][cell_x + k] == Empty)
                    break;
            }
        }
        if (cell_x != 0 && board[cell_y][cell_x - 1] == cmp_color) { // 서쪽
            for (int k=2; cell_x - k >= 0; k++) {
                if (my_color == board[cell_y][cell_x - k])
                    isPossible[1] = true;
                else if (board[cell_y][cell_x - k] == Empty)
                    break;
            }
        }
        if (cell_y != 8 && board[cell_y + 1][cell_x] == cmp_color) { // 남쪽
            for (int k=2; cell_y + k <= 8; k++) {
                if (my_color == board[cell_y + k][cell_x])
                    isPossible[2] = true;
                else if (board[cell_y + k][cell_x] == Empty)
                    break;
            }
        }
        if (cell_y != 0 && board[cell_y - 1][cell_x] == cmp_color) { // 북쪽
            for (int k=2; cell_y - k >= 0; k++) {
                if (my_color == board[cell_y - k][cell_x])
                    isPossible[3] = true;
                else if (board[cell_y - k][cell_x] == Empty)
                    break;
            }
        }
        if ((cell_x != 0 && cell_y != 0) && board[cell_y - 1][cell_x - 1] == cmp_color) { // 왼쪽 위
            for (int k=2; (cell_x - k >= 0 && cell_y - k >= 0); k++) {
                if (my_color == board[cell_y - k][cell_x - k])
                    isPossible[4] = true;
                else if (board[cell_y - k][cell_x - k] == Empty)
                    break;
            }
        }
        if ((cell_x != 0 && cell_y != 8) && board[cell_y + 1][cell_x - 1] == cmp_color) { // 왼쪽 아래
            for (int k=2; (cell_x - k >= 0 && cell_y + k <= 8); k++) {
                if (my_color == board[cell_y + k][cell_x - k])
                    isPossible[5] = true;
                else if (board[cell_y + k][cell_x - k] == Empty)
                    break;
            }
        }
        if ((cell_x != 8 && cell_y != 0) && board[cell_y - 1][cell_x + 1] == cmp_color) { // 오른쪽 위
            for (int k=2; (cell_x + k <= 8 && cell_y - k >= 0); k++) {
                if (my_color == board[cell_y - k][cell_x + k])
                    isPossible[6] = true;
                else if (board[cell_y - k][cell_x + k] == Empty)
                    break;
            }
        }
        if ((cell_x != 8 && cell_y != 8) && board[cell_y + 1][cell_x + 1] == cmp_color) { // 오른쪽 아래
            for (int k=2; (cell_x + k <= 8 && cell_y + k <= 8); k++) {
                if (my_color == board[cell_y + k][cell_x + k])
                    isPossible[7] = true;
                else if (board[cell_y + k][cell_x + k] == Empty)
                    break;
            }
        }
    }

    public void changeColor(int cell_x, int cell_y, int d) {
        // Direction
        // 0-1-2-3 / 4-5-6-7 -> 동-서-남-북 / 왼쪽 위-왼쪽 아래-오른쪽 위-오른쪽 아래
        int k = 1;
        int my_color = board[cell_y][cell_x];
        switch (d) {
            case 0:
                do {
                    board[cell_y][cell_x + k] = my_color;
                    k++;
                } while (board[cell_y][cell_x + k] != my_color);
                break;
            case 1:
                do {
                    board[cell_y][cell_x - k] = my_color;
                    k++;
                } while (board[cell_y][cell_x - k] != my_color);
                break;
            case 2:
                do {
                    board[cell_y + k][cell_x] = my_color;
                    k++;
                } while (board[cell_y + k][cell_x] != my_color);
                break;
            case 3:
                do {
                    board[cell_y - k][cell_x] = my_color;
                    k++;
                } while (board[cell_y - k][cell_x] != my_color);
                break;
            case 4:
                do {
                    board[cell_y - k][cell_x - k] = my_color;
                    k++;
                } while (board[cell_y - k][cell_x - k] != my_color);
                break;
            case 5:
                do {
                    board[cell_y + k][cell_x - k] = my_color;
                    k++;
                } while (board[cell_y + k][cell_x - k] != my_color);
                break;
            case 6:
                do {
                    board[cell_y - k][cell_x + k] = my_color;
                    k++;
                } while (board[cell_y - k][cell_x + k] != my_color);
                break;
            case 7:
                do {
                    board[cell_y + k][cell_x + k] = my_color;
                    k++;
                } while (board[cell_y + k][cell_x + k] != my_color);
                break;
            default:
                break;
        }
    }
}
