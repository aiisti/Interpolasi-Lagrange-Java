import java.util.Scanner;
import java.lang.Math;


public class Lagrange {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.useLocale(java.util.Locale.US); // Ensure '.' is used as decimal separator

        // Memasukkan jumlah titik data
        System.out.print("Masukkan jumlah titik data: ");
        int n = sc.nextInt();

        // Pilihan fungsi untuk y
        System.out.println("Pilih fungsi yang akan diterapkan pada y:");
        System.out.println("1. ln(x)");
        System.out.println("2. sin(x)");
        System.out.println("3. cos(x)");
        System.out.print("Masukkan pilihan (1/2/3): ");
        int fungsiPilihan = sc.nextInt();

        // Inisialisasi array x dan y
        double[] x = new double[n];
        double[] y = new double[n];

        // Memasukkan titik interpolasi
        System.out.print("Masukkan titik interpolasi: ");
        double xp = sc.nextDouble();

        // panjang nilai desimal
        System.out.print("Masukkan panjang desimal: ");
        int pd = sc.nextInt();

        // Memasukkan nilai x dan menghitung y
        System.out.println("Masukkan nilai x:");
        sc.nextLine(); // Consume the leftover newline after previous nextInt()
        for (int i = 0; i < n; i++) {
            while (true) {
                try {
                    System.out.print("x[" + i + "] = ");
                    String input = sc.nextLine().replace(',', '.'); // Accept both ',' and '.' as decimal separator
                    double xVal = Double.parseDouble(input);
                    boolean duplicate = false;
                    for (int j = 0; j < i; j++) {
                        if (xVal == x[j]) {
                            duplicate = true;
                            break;
                        }
                    }
                    if (duplicate) {
                        System.out.println("Nilai x[" + i + "] tidak boleh sama dengan nilai x sebelumnya.");
                        continue;
                    }
                    x[i] = xVal;
                    y[i] = applyFunc(xVal, fungsiPilihan);
                    if (fungsiPilihan == 1) {
                        System.out.printf("y[%d] = ln(X)%.4f = %s\n", i, xVal, String.format("%." + pd + "f", y[i]));
                    } else if (fungsiPilihan == 2) {
                        System.out.printf("y[%d] = sin(X)%.4f = %s\n", i, xVal, String.format("%." + pd + "f", y[i]));
                    } else if (fungsiPilihan == 3) {
                        System.out.printf("y[%d] = cos(X)%.4f = %s\n", i, xVal, String.format("%." + pd + "f", y[i]));
                    } else {
                        System.out.printf("y[%d] = %s\n", i, String.format("%." + pd + "f", y[i]));
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Masukkan angka yang valid untuk x. " + e.getMessage());
                }
            }
        }

        double yp = 0;
        System.out.println("\nLangkah-langkah perhitungan Lagrange:");
        for (int i = 0; i < n; i++) {
            double p = 1;
            System.out.println("\nSuku ke-" + i + ":");
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    double numerator = xp - x[j];
                    double denominator = x[i] - x[j];
                    if (denominator == 0) {
                        System.out.printf("  Peringatan: Penyebut nol pada x[%d] dan x[%d], lewati suku ini.\n", i, j);
                        continue;
                    }
                    System.out.printf("  (%.4f - x[%d]) / (x[%d] - x[%d]) = (%.4f - %.4f) / (%.4f - %.4f) = %s / %s = %s\n",
                            xp, j, i, j, xp, x[j], x[i], x[j],
                            String.format("%." + pd + "f", numerator),
                            String.format("%." + pd + "f", denominator),
                            String.format("%." + pd + "f", numerator / denominator));
                    p *= numerator / denominator;
                }
            }
            System.out.printf("  L_%d = %." + pd + "f\n", i, p);
            System.out.printf("  L_%d * y[%d] = %." + pd + "f * %." + pd + "f = %." + pd + "f\n", i, i, p, y[i], p * y[i]);
            yp += p * y[i];
        }

        // Menghitung nilai eksak dari titik interpolasi
        double eksak = 0;
        try {
            eksak = applyFunc(xp, fungsiPilihan);
            System.out.printf("\nNilai fungsi pada titik %.4f adalah %s.\n", xp, String.format("%." + pd + "f", yp));
            System.out.printf("Nilai eksak dari titik interpolasi adalah %s\n", String.format("%." + pd + "f", eksak));
        } catch (Exception e) {
            System.out.println("Tidak dapat menghitung nilai eksak: " + e.getMessage());
        }

        // Menghitung galat (error)
        double error = 0;
        if (eksak != 0) {
            error = Math.abs((yp - eksak) / eksak * 100);
            System.out.printf("Galat (error) pada titik %.4f adalah %." + pd + "f\n", xp, error);
            System.out.printf("Galat (error) dalam persen pada titik %.4f adalah %.2f%%\n", xp, error);
        } else {
            System.out.println("Nilai eksak adalah nol, tidak dapat menghitung galat relatif.");
        }

        sc.close();
    }

    static double applyFunc(double val, int fungsiPilihan) {
        if (fungsiPilihan == 1) {
            if (val <= 0) throw new IllegalArgumentException("ln(x) hanya untuk x > 0.");
            return Math.log(val);
        } else if (fungsiPilihan == 2) {
            return Math.sin(val);
        } else if (fungsiPilihan == 3) {
            return Math.cos(val);
        }
        return val;
    }
}
