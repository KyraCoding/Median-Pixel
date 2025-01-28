package com.example;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;

/**
 * Hello world!
 *
 */
public class App extends JPanel {
    private BufferedImage image;

    public void ImageDisplay(BufferedImage image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            // Draw the BufferedImage
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }

    public BufferedImage capture() {
        Webcam webcam = Webcam.getWebcams().get(1);
        webcam.setViewSize(webcam.getViewSizes()[webcam.getViewSizes().length - 1]);
        webcam.open();
        BufferedImage image = webcam.getImage();
        System.out.println("Image dimensions: " + image.getWidth() + "x" + image.getHeight());
        webcam.close();
        return image;
    }

    public int[][] disassemble(BufferedImage image, int width, int height) {
        int[] red = new int[width * height];
        int[] green = new int[width * height];
        int[] blue = new int[width * height];
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                // if this is bitwise, what's bitdumb?
                red[index] = (rgb >> 16) & 0xff;
                green[index] = (rgb >> 8) & 0xff;
                blue[index] = rgb & 0xff;
                index++;
            }
        }
        return new int[][] { red, green, blue };
    }

    private void merge(int a[], int l, int m, int r) {

        int n1 = m - l + 1;
        int n2 = r - m;

        int L[] = new int[n1];
        int R[] = new int[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = a[l + i];

        for (int j = 0; j < n2; ++j)
            R[j] = a[m + 1 + j];

        int i = 0, j = 0;

        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                a[k] = L[i];
                i++;
            } else {
                a[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            a[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            a[k] = R[j];
            j++;
            k++;
        }
    }

    private void msort(int a[], int l, int r) {
        if (l < r) {

            int m = (l + r) / 2;

            // Sort first and second halves
            msort(a, l, m);
            msort(a, m + 1, r);

            // Merge the sorted halves
            merge(a, l, m, r);
        }
    }

    private int[] countSort(int[] inputArray) {
        int N = inputArray.length;
        int M = 0;

        for (int i = 0; i < N; i++) {
            M = Math.max(M, inputArray[i]);
        }

        int[] countArray = new int[M + 1];

        for (int i = 0; i < N; i++) {
            countArray[inputArray[i]]++;
        }

        for (int i = 1; i <= M; i++) {
            countArray[i] += countArray[i - 1];
        }

        int[] outputArray = new int[N];

        for (int i = N - 1; i >= 0; i--) {
            outputArray[countArray[inputArray[i]] - 1] = inputArray[i];
            countArray[inputArray[i]]--;
        }

        return outputArray;
    }

    public void bucketSort(float[] arr, int n) {
        if (n <= 0)
            return;
        float maxValue = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > maxValue) {
                maxValue = arr[i];
            }
        }
        @SuppressWarnings("unchecked")
        ArrayList<Float>[] bucket = new ArrayList[n];

        // Create empty buckets
        for (int i = 0; i < n; i++)
            bucket[i] = new ArrayList<Float>();

        // Add elements into the buckets
        for (int i = 0; i < n; i++) {
            int bucketIndex = (int) ((arr[i] / maxValue) * (n - 1));
            bucket[bucketIndex].add(arr[i]);
        }

        // Sort the elements of each bucket
        for (int i = 0; i < n; i++) {
            Collections.sort((bucket[i]));
        }

        // Get the sorted array
        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0, size = bucket[i].size(); j < size; j++) {
                arr[index++] = bucket[i].get(j);
            }
        }
    }
    private void shellsort(int arr[])
    {
        int n = arr.length;

        // Start with a big gap, then reduce the gap
        for (int gap = n/2; gap > 0; gap /= 2)
        {
            // Do a gapped insertion sort for this gap size.
            // The first gap elements a[0..gap-1] are already
            // in gapped order keep adding one more element
            // until the entire array is gap sorted
            for (int i = gap; i < n; i += 1)
            {
                // add a[i] to the elements that have been gap
                // sorted save a[i] in temp and make a hole at
                // position i
                int temp = arr[i];

                // shift earlier gap-sorted elements up until
                // the correct location for a[i] is found
                int j;
                for (j = i; j >= gap && arr[j - gap] > temp; j -= gap)
                    arr[j] = arr[j - gap];

                // put temp (the original a[i]) in its correct
                // location
                arr[j] = temp;
            }
        }
    }

    private int median(int[] a) {
        int n = a.length;
        if (n % 2 == 0) {
            return (a[n / 2 - 1] + a[n / 2]) / 2;
        } else {
            return a[n / 2];
        }
    }

    public static void main(String[] args) {
        App cam = new App();
        BufferedImage image = cam.capture();
        int[][] data = cam.disassemble(image, image.getWidth(), image.getHeight());

        int runs = 1;

        // Merge sort
        int[][] mdata = data.clone();
        long mavg = 0;
        long mbest = 0;
        for (int v=0;v<runs;v++) {
            mdata = data.clone();
            long start = System.nanoTime();
            for (int i = 0; i < 3; i++) {
                cam.msort(mdata[i], 0, mdata[i].length - 1);
            }
            long end = System.nanoTime();
            long time = end - start;
            mavg += time;
            if (v == 0 || time < mbest) {
                mbest = time;
            }
        }
        mavg /= 100;
        System.out.println("Merge sort: " + mavg + " ns average, " + mbest + " ns best");
        

        // Counting sort
        int[][] cdata = data.clone();
        long cavg = 0;
        long cbest = 0;
        for (int v=0;v<runs;v++) {
            cdata = data.clone();
            long start = System.nanoTime();
            for (int i = 0; i < 3; i++) {
                cdata[i] = cam.countSort(cdata[i]);
            }
            long end = System.nanoTime();
            long time = end - start;
            cavg += time;
            if (v == 0 || time < cbest) {
                cbest = time;
            }
        }
        cavg /= 100;
        System.out.println("Counting sort: " + cavg + " ns average, " + cbest + " ns best");

        // Bucket sort
        long bavg = 0;
        long bbest = 0;
        int[][] bdata = data.clone();
        for (int v=0;v<runs;v++) {
            bdata = data.clone();
            long start = System.nanoTime();
            for (int i = 0; i < 3; i++) {
                float[] fdata = new float[data[i].length];
                for (int j = 0; j < data[i].length; j++) {
                    fdata[j] = (float)(data[i][j]);
                }
                cam.bucketSort(fdata, fdata.length);
                bdata[i] = new int[fdata.length];
                for (int j = 0; j < fdata.length; j++) {
                    bdata[i][j] = (int) fdata[j];
                }
            }
            long end = System.nanoTime();
            long time = end - start;
            bavg += time;
            if (v == 0 || time < bbest) {
                bbest = time;
            }
        }
        bavg /= 100;
        System.out.println("Bucket sort: " + bavg + " ns average, " + bbest + " ns best");

        // Shell sort
        int[][] sdata = data.clone();
        long savg = 0;
        long sbest = 0;
        for (int v=0;v<runs;v++) {
            sdata = data.clone();
            long start = System.nanoTime();
            for (int i = 0; i < 3; i++) {
                cam.shellsort(sdata[i]);
            }
            long end = System.nanoTime();
            long time = end - start;
            savg += time;
            if (v == 0 || time < sbest) {
                sbest = time;
            }
        }
        savg /= 100;
        System.out.println("Shell sort: " + savg + " ns average, " + sbest + " ns best");

        // Answer key
        int[][] jdata = data.clone();
        for (int i = 0; i < 3; i++) {
            java.util.Arrays.sort(jdata[i]);
        }

        // Verification
        String mergeres = cam.median(mdata[0]) + "" + cam.median(mdata[1]) + "" + cam.median(mdata[2]);
        String countres = cam.median(cdata[0]) + "" + cam.median(cdata[1]) + "" + cam.median(cdata[2]);
        String bucketres = cam.median(bdata[0]) + "" + cam.median(bdata[1]) + "" + cam.median(bdata[2]);
        String shellres = cam.median(sdata[0]) + "" + cam.median(sdata[1]) + "" + cam.median(sdata[2]);
        String javares = cam.median(jdata[0]) + "" + cam.median(jdata[1]) + "" + cam.median(jdata[2]);
        if (!mergeres.equals(javares)) {
            System.out.println("Merge sort and Java sort do not match");
        }
        if (!countres.equals(javares)) {
            System.out.println("Counting sort and Java sort do not match");
        }
        if (!bucketres.equals(javares)) {
            System.out.println("Bucket sort and Java sort do not match");
        }
        if (!shellres.equals(javares)) {
            System.out.println("Shell sort and Java sort do not match");
        }

        System.out.println("Median color: rgb(" + cam.median(jdata[0]) + ", " + cam.median(jdata[1]) + ", "
                + cam.median(jdata[2]) + ")");
        JFrame frame = new JFrame("RainingPixels");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(image.getWidth(), image.getHeight());
        App panel = new App();
        panel.ImageDisplay(image);
        frame.add(panel);
        frame.getRootPane().setBorder(javax.swing.BorderFactory.createMatteBorder(20, 20, 20, 20,
                new java.awt.Color(cam.median(jdata[0]), cam.median(jdata[1]), cam.median(jdata[2]))));
        frame.setVisible(true);

    }
}
