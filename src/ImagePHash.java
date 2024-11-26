import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ImageObserver;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ImagePHash {
    private int size = 32;
    private int smallerSize = 8;
    private ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(1003), (RenderingHints)null);
    private double[] c;

    public ImagePHash() {
        this.initCoefficients();
    }

    public ImagePHash(int size, int smallerSize) {
        this.size = size;
        this.smallerSize = smallerSize;
        this.initCoefficients();
    }

    public int distance(String s1, String s2) {
        int counter = 0;

        for(int k = 0; k < s1.length(); ++k) {
            if (s1.charAt(k) != s2.charAt(k)) {
                ++counter;
            }
        }

        return counter;
    }

    public String getHash(InputStream is) throws Exception {
        BufferedImage img = ImageIO.read(is);
        img = this.resize(img, this.size, this.size);
        img = this.grayscale(img);
        double[][] vals = new double[this.size][this.size];

        for(int x = 0; x < img.getWidth(); ++x) {
            for(int y = 0; y < img.getHeight(); ++y) {
                vals[x][y] = (double)getBlue(img, x, y);
            }
        }

        long start = System.currentTimeMillis();
        double[][] dctVals = this.applyDCT(vals);
        System.out.println("DCT: " + (System.currentTimeMillis() - start));
        double total = 0.0;

        for(int x = 0; x < this.smallerSize; ++x) {
            for(int y = 0; y < this.smallerSize; ++y) {
                total += dctVals[x][y];
            }
        }

        total -= dctVals[0][0];
        double avg = total / (double)(this.smallerSize * this.smallerSize - 1);
        String hash = "";

        for(int x = 0; x < this.smallerSize; ++x) {
            for(int y = 0; y < this.smallerSize; ++y) {
                if (x != 0 && y != 0) {
                    hash = hash + (dctVals[x][y] > avg ? "1" : "0");
                }
            }
        }

        return hash;
    }

    private BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, 2);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, (ImageObserver)null);
        g.dispose();
        return resizedImage;
    }

    private BufferedImage grayscale(BufferedImage img) {
        this.colorConvert.filter(img, img);
        return img;
    }

    private static int getBlue(BufferedImage img, int x, int y) {
        return img.getRGB(x, y) & 255;
    }

    private void initCoefficients() {
        this.c = new double[this.size];

        for(int i = 1; i < this.size; ++i) {
            this.c[i] = 1.0;
        }

        this.c[0] = 1.0 / Math.sqrt(2.0);
    }

    private double[][] applyDCT(double[][] f) {
        int N = this.size;
        double[][] F = new double[N][N];

        for(int u = 0; u < N; ++u) {
            for(int v = 0; v < N; ++v) {
                double sum = 0.0;

                for(int i = 0; i < N; ++i) {
                    for(int j = 0; j < N; ++j) {
                        sum += Math.cos((double)(2 * i + 1) / (2.0 * (double)N) * (double)u * Math.PI) * Math.cos((double)(2 * j + 1) / (2.0 * (double)N) * (double)v * Math.PI) * f[i][j];
                    }
                }

                sum *= this.c[u] * this.c[v] / 4.0;
                F[u][v] = sum;
            }
        }

        return F;
    }
}
