public class RMatrix {
    final private double[][] m;
    final private int nrow, ncol;

    // A zero-matrix of any dimensions
    public RMatrix(int r, int c)
    {
        nrow = r;
        ncol = c;
        m = new double[nrow][ncol];
        for(int i = 0; i < nrow; i++)
            for(int j = 0; j < ncol; j++)
                m[i][j] = 0;
    }

    // A square identity matrix
    public RMatrix(int rc)
    {
        nrow = rc;
        ncol = rc;
        m = new double[nrow][ncol];
        for(int i = 0; i < nrow; i++)
        {
            for(int j = 0; j < ncol; j++) m[i][j] = (i == j)? 1:0;
        }
    }

    // A matrix of any dimensions filled with any values from an array
    public RMatrix(double[][] im)
    {
        nrow = im.length;
        ncol = im[0].length;
        m = im;
    }

    // A matrix for translation and scaling
    public RMatrix(String operation, double x, double y, double z) {
        nrow = 4;
        ncol = 4;
        switch (operation) {
            case "translate":
                double ta[][] = {
                        {1, 0, 0, x},
                        {0, 1, 0, y},
                        {0, 0, 1, z},
                        {0, 0, 0, 1}
                };
                m = ta;
                break;
            case "scale":
                double sa[][] = {
                        {x, 0, 0, 0},
                        {0, y, 0, 0},
                        {0, 0, z, 0},
                        {0, 0, 0, 1}
                };
                m = sa;
                break;
            default:
                double da[][] = {
                        {1, 0, 0, 0},
                        {0, 1, 0, 0},
                        {0, 0, 1, 0},
                        {0, 0, 0, 1}
                };
                m = da;
        }
    }

    // A matrix for rotation around a specified axis
    public RMatrix(String axis, double angle) {
        nrow = 4;
        ncol = 4;
        switch (axis) {
            case "x":
                double xa[][] = {
                        {1, 0, 0, 0},
                        {0, Math.cos(angle), -Math.sin(angle), 0},
                        {0, Math.sin(angle), Math.cos(angle), 0},
                        {0, 0, 0, 1}
                };
                m = xa;
                break;
            case "y":
                double ya[][] = {
                        {Math.cos(angle), 0, Math.sin(angle), 0},
                        {0, 1, 0, 0},
                        {-Math.sin(angle), 0, Math.cos(angle), 0},
                        {0, 0, 0, 1}
                };
                m = ya;
                break;
            case "z":
                double za[][] = {
                        {Math.cos(angle), -Math.sin(angle), 0, 0},
                        {Math.sin(angle), Math.cos(angle), 0, 0},
                        {0, 0, 1, 0},
                        {0, 0, 0, 1}
                };
                m = za;
                break;
            default:
                double da[][] = {
                        {1, 0, 0, 0},
                        {0, 1, 0, 0},
                        {0, 0, 1, 0},
                        {0, 0, 0, 1}
                };
                m = da;
        }
    }

    // A matrix for shearing
    public RMatrix(double xy, double xz, double yx, double yz, double zx, double zy) {
        nrow = 4;
        ncol = 4;
        double sa[][] = {
                {1, xy, xz, 0},
                {yx, 1, yz , 0},
                {zx, zy, 1, 0},
                {0, 0, 0, 1}
        };
        m = sa;
    }

    // Output the matrix with formatting
    public void show()
    {
        for(int i = 0; i < nrow; i++)
        {
            for(int j = 0; j < ncol; j++) { System.out.printf("%.5f" + "\t", m[i][j]); }
            System.out.println();
        }
    }

    // Get the element at (i, j)
    double get(int i, int j)
    {
        return m[i][j];
    }

    // Return the transposed matrix without altering the original
    public RMatrix transpose()
    {
        double[][] a = new double[ncol][nrow];
        for(int i = 0; i < nrow; i++)
            for(int j =0; j < ncol; j++)
                a[j][i] = m[i][j];
        return new RMatrix(a);
    }

    // Return the matrix multiplied by another matrix without altering the original
    public RMatrix mul(RMatrix im)
    {
        double[][] a = new double[nrow][im.ncol];
        int i, j, k;
        double sum;
        for(i = 0; i < nrow; i++) {
            for(j = 0; j < im.ncol; j++) {
                sum = 0;
                for(k = 0; k < ncol; k++) sum += m[i][k] * im.m[k][j];
                a[i][j] = sum;
            }
        }
        return new RMatrix(a);
    }

    // Return the matrix multiplied by a tuple (the result is a tuple) without altering the original
    public RTuple mul(RTuple it)
    {
        double[][] a = new double[nrow][1];
        double ia[][] = {
                {it.x},
                {it.y},
                {it.z},
                {it.w}
        };
        RMatrix im = new RMatrix(ia);
        int i, k;
        double sum;
        for(i = 0; i < nrow; i++) {
            sum = 0;
            for(k = 0; k < ncol; k++) sum += m[i][k] * im.m[k][0];
            a[i][0] = sum;
        }
        return new RTuple(a[0][0], a[1][0], a[2][0], a[3][0]);
    }

    // Return the matrix with a specified row and a specified column deleted, without altering the original
    RMatrix submat(int r, int c)
    {
        int cr, cc;
        double[][] a = new double[nrow-1][ncol-1];
        for(int mr = 0; mr < nrow; mr++)
            for(int mc = 0; mc < ncol; mc++)
                if(mr!=r && mc!=c)
                {
                    cr = (mr < r)? mr : mr - 1;
                    cc = (mc < c)? mc : mc - 1;
                    a[cr][cc] = m[mr][mc];
                }
        return new RMatrix(a);
    }

    // The determinant of the matrix
    double det()
    {
        if(nrow!=ncol) { System.out.println("The matrix is not square."); return 0; }
        if(nrow == 2) return m[0][0]*m[1][1] - m[0][1]*m[1][0];

        double sum = 0;
        for(int c = 0; c < ncol; c++)
        {
            sum += m[0][c]*Math.pow(-1, c)*(submat(0, c).det());
        }
        return sum;
    }

    // Return the inverse of the matrix without altering the original
    RMatrix inv()
    {
        double[][] a = new double[nrow][ncol];
        double d = det();

        for(int r = 0; r < nrow; r++)
            for(int c = 0; c < ncol; c++)
            {
                a[r][c] = Math.pow(-1, r+c)*(submat(r, c).det())/d;
            }
        RMatrix ma = new RMatrix(a);
        return ma.transpose();
    }
}