
import java.util.ArrayList;
import java.util.Scanner;

public class NQueenProblem_1005072 {

    public static int N;
    public static int[] X;
    public static int totalSolution;
    public static ArrayList<int[]> validSolution;
    public static ArrayList<int[]> validSolutionMRV;

    public static int[][] F;
    public static int[] RV;

    public static void init() {
        Scanner scn = new Scanner(System.in);
        N = scn.nextInt();
        X = new int[N];
        F = new int[N][N];
        RV = new int[N];

        for (int i = 0; i < N; i++) {
            X[i] = -1;
            RV[i] = N;
        }
    }

    public static void printSolution(int[] x) {
        for (int i = 0; i < x.length; i++) {
            System.out.print((x[i] + 1) + "  ");
        }
        System.out.println("");
    }

    public static void printBoard() {
        updateBoard();
        System.out.println("_________________________");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (F[i][j] == 1) {
                    System.out.print("Q ");
                } else if (F[i][j] == -1) {
                    System.out.print("X ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println("");
        }
        System.out.println("_________________________");
    }

    public static boolean placeNQueens_1(int i) {
        boolean flag = false;
        boolean succ = false;
        if (i == N) {
            printSolution(X);
            return true;
        }
        for (int j = 0; j < N; j++) {
            X[i] = j;
            flag = checkConstraints(i);
            if (!flag) {
                continue;
            }
            succ = placeNQueens_1(i + 1);
            if (succ) {
                return true;
            }
        }
        return false;
    }

    public static boolean placeNQueens_2(int i) {
        boolean flag = false;
        boolean succ = false;
        if (i == N) {
            int[] temp = new int[N];
            System.arraycopy(X, 0, temp, 0, X.length);
            validSolution.add(temp);
            return true;
        }
        for (int j = 0; j < N; j++) {
            X[i] = j;
            flag = checkConstraints(i);
            if (!flag) {
                continue;
            }
            succ = placeNQueens_2(i + 1);
        }
        return false;
    }

    public static boolean checkConstraints(int r) {
        for (int i = 0; i <= r - 1; i++) {
            if (X[i] == X[r] || X[r] - (r - i) == X[i] || X[r] + (r - i) == X[i]) {
                return false;
            }
        }
        return true;
    }

    public static void updateBoard() {
        F = new int[N][N];
        for (int i = 0; i < N; i++) {
            if (X[i] >= 0) {
                for (int j = 0; j < N; j++) {
                    F[i][j] = -1;
                    F[j][X[i]] = -1;
                }

                F[i][X[i]] = 1;

                // diagonally top-left
                for (int j = i - 1, k = X[i] - 1; j >= 0 && k >= 0; j--, k--) {
                    F[j][k] = -1;
                }
                // diagonally top-right
                for (int j = i - 1, k = X[i] + 1; j >= 0 && k < N; j--, k++) {
                    F[j][k] = -1;
                }
                // diagonally bottom-left
                for (int j = i + 1, k = X[i] - 1; j < N && k >= 0; j++, k--) {
                    F[j][k] = -1;
                }
                // diagonally bottom-right
                for (int j = i + 1, k = X[i] + 1; j < N && k < N; j++, k++) {
                    F[j][k] = -1;
                }
            }
        }
    }

    public static boolean forwardCheck() {
        updateBoard();
        boolean isValid = false;
        for (int i = 0; i < N; i++) {
            isValid = false;
            for (int j = 0; j < N; j++) {
                if (F[i][j] == 0 || F[i][j] == 1) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                return false;
            }
        }

        return true;
    }

    public static void updateRV() {
        RV = new int[N];
        for (int i = 0; i < N; i++) {
            RV[i] = N;
            for (int j = 0; j < N; j++) {
                if (F[i][j] == 1 || F[i][j] == -1) {
                    RV[i]--;
                }
            }
        }
    }

    public static int selectRowMRV() {
        updateBoard();
        updateRV();
        int minV = N + 1;
        int minR = 0;
        for (int i = 0; i < N; i++) {
            if (RV[i] != 0 && RV[i] < minV) {
                minV = RV[i];
                minR = i;
            }
        }
        return minR;
    }

    public static boolean placeNQueensFCandMRV() {
        boolean noMoreRows = true;
        boolean flag = false;
        boolean succ = false;

        for (int i = 0; i < N; i++) {
            if (X[i] == -1) {
                noMoreRows = false;
                break;
            }
        }
       
        if (noMoreRows) {
            int[] temp = new int[N];
            System.arraycopy(X, 0, temp, 0, X.length);
            validSolutionMRV.add(temp);
            return true;
        }

        int i = selectRowMRV();
        for (int j = 0; j < N ; j++) {
            if(F[i][j] != 0){
                continue;
            }
            
            X[i] = j;
            flag = forwardCheck();
            if (!flag) {
                X[i] = -1;
                updateBoard();
                continue;
            }
            succ = placeNQueensFCandMRV();
            
            X[i] = -1;
            updateBoard();
        }

        return false;
    }

    public static void main(String[] args) {
        init();
        System.out.println("");
        placeNQueens_1(0);

        validSolution = new ArrayList<>();
        placeNQueens_2(0);
        System.out.println("");
        System.out.println("Total valid solution : " + validSolution.size());
        System.out.println("");
        System.out.println("They are as follows --- ");
        System.out.println("");
        for (int i = 0; i < validSolution.size(); i++) {
            printSolution(validSolution.get(i));
        }
        
        System.out.println("");
        System.out.println("-------------------------------");
        System.out.println("-------------------------------");
        System.out.println("");
        
        X = new int[N];
        for (int i = 0; i < N; i++) {
            X[i] = -1;
        }
        
        validSolutionMRV = new ArrayList<>();
        placeNQueensFCandMRV();        
        placeNQueens_2(0);
        System.out.println("");
        System.out.println("Total valid solution applying FC and MRV: " + validSolutionMRV.size());
        System.out.println("");
        System.out.println("They are as follows --- ");
        System.out.println("");
        for (int i = 0; i < validSolutionMRV.size(); i++) {
            printSolution(validSolutionMRV.get(i));
        }
    }
}
