
/**
 Argument usage:
 width, height, mode?, output path?, output file name?, output file type?
 */
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;
import java.text.DecimalFormat;
public class main {
    private static float flip(float n) {
        return 1 - n;
    }
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int width = 25;
        int height = 25;
        if (args.length == 0) {
            System.out.println("Insufficient number of arguments provided. Run \"help\" to see documentation.");
            System.exit(1);
        }
        String lc = args[0].toLowerCase();
        if (lc.equals("help")) {
            try {
                Runtime.getRuntime().exec("write.exe Documentation.rtf");
            } catch (IOException e) {
                System.out.println("Could not open help file.");
            }
            System.exit(0);
        } else if (lc.equals("hd") || lc.equals("720p")) {
            width = 1280;
            height = 720;
        } else if (lc.equals("fhd") || lc.equals("1080p")) {
            width = 1920;
            height = 1080;
        } else if (lc.equals("qhd") || lc.equals("1440p")) {
            width = 2560;
            height = 1440;
        } else if (lc.equals("uhd") || lc.equals("4k") || lc.equals("2160p")) {
            width = 3840;
            height = 2160;
        } else {
            width = Integer.parseInt(args[0]);
            height = Integer.parseInt(args[1]);
        }
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        String filename = "generated";
        String type = "png";
        String pathset = "";
        String mode = "random";
        if (args.length > 2) {
            mode = args[2].toLowerCase();
            if (args.length > 3) {
                pathset = args[3];
                if (args.length > 4) {
                    filename = args[4];
                    if (args.length > 5) {
                        type = args[5];
                    }
                }
            }
        }
        String full = pathset + filename + "." + type;
        Random rand = new Random();
        float flipcol = 0;
        float fliprow = 0;
        int last = 0;
        System.out.println("Generating...");
        for (int i=0; i < height; i++) {
            flipcol = 0;
            for (int z=0; z < width; z++) {
                if (mode.equals("random")) {
                    img.setRGB(z, i, new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()).getRGB());
                } else if (mode.equals("color")) {
                    img.setRGB(z, i, new Color((float)z / width, (float)i / height, 1).getRGB());
                } else if (mode.equals("static")) {
                    float f = rand.nextFloat();
                    img.setRGB(z, i, new Color(f, f, f).getRGB());
                } else if (mode.equals("lines")) {
                    img.setRGB(z, i, new Color(flipcol, flipcol, flipcol).getRGB());
                    flipcol = flip(flipcol);
                } else if (mode.equals("checkered")) {
                    float flipnow = flipcol;
                    if (fliprow == 1) {
                        flipnow = flip(flipcol);
                    }
                    img.setRGB(z, i, new Color(flipnow, flipnow, flipnow).getRGB());
                    flipcol = flip(flipcol);
                } else {
                    System.out.println("Invalid mode specification \"" + mode + "\", exiting.");
                    System.exit(1);
                }
                float iterations = ((i * width) + z);
                float percent = ((iterations / (width * height)) * 100);
                String progress = "\r" + (int)percent + "%";
                try {
                    if ((int)percent != last) {
                        System.out.write(progress.getBytes());
                        last = (int)percent;
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            fliprow = flip(fliprow);
        }
        String abs = new File(full).getAbsolutePath();
        System.out.println("\nWriting to " + abs);
        float diff = System.currentTimeMillis() - start;
        DecimalFormat formatter = new DecimalFormat("##.00");
        try {
            File out = new File(abs);
            ImageIO.write(img, type, out);
        } catch (IOException e) {
            System.out.println(e);
        }
        float mins = (diff / 60000);
        System.out.println("Generated in " + formatter.format(diff / 1000) + "s (" + formatter.format(mins) + "min)");
    }
}
