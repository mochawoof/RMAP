
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
public class main {
    public static float flip(float num) {
        return 1 - num;
    }
    public static void main(String[] args) {
        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
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
        System.out.println("Generating...");
        for (int i=0; i < height; i++) {
            flipcol = 0;
            for (int z=0; z < width; z++) {
                if (mode == "random") {
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
                    System.out.write(progress.getBytes());
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            fliprow = flip(fliprow);
        }
        System.out.println("\nWriting to " + System.getProperty("user.dir") + File.separator + full);
        try {
            File out = new File(full);
            ImageIO.write(img, type, out);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
