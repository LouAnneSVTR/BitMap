import java.awt.*;
import java.awt.image.*;

public class ImageUtil
{
    public static int ALPHA_BIT_MASK = 0xFF000000;

    public static BufferedImage imageToBufferedImage(Image image, int width, int height)
    {
        return imageToBufferedImage(image, width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public static BufferedImage imageToBufferedImage(Image image, int width, int height, int type)
    {
        BufferedImage dest = new BufferedImage(width, height, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return dest;
    }

    public static BufferedImage convertRGBAToIndexed(BufferedImage srcImage)
    {
        // Create a non-transparent palletized image
        Image flattenedImage = transformTransparencyToMagenta(srcImage);
        BufferedImage flatImage = imageToBufferedImage(flattenedImage,
                srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
        BufferedImage destImage = makeColorTransparent(flatImage, 0, 0);
        return destImage;
    }

    private static Image transformTransparencyToMagenta(BufferedImage image)
    {
        ImageFilter filter = new RGBImageFilter()
        {
            @Override
            public final int filterRGB(int x, int y, int rgb)
            {
                int pixelValue = 0;
                int opacity = (rgb & ALPHA_BIT_MASK) >>> 24;
                if (opacity < 128)
                {
                    // Quite transparent: replace color with transparent magenta
                    // (traditional color for binary transparency)
                    pixelValue = 0x00FF00FF;
                }
                else
                {
                    // Quite opaque: get pure color
                    pixelValue = (rgb & 0xFFFFFF) | ALPHA_BIT_MASK;
                }
                return pixelValue;
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public static BufferedImage makeColorTransparent(BufferedImage image, int x, int y)
    {
        ColorModel cm = image.getColorModel();
        if (!(cm instanceof IndexColorModel))
            return image; // No transparency added as we don't have an indexed image

        IndexColorModel originalICM = (IndexColorModel) cm;
        WritableRaster raster = image.getRaster();
        int colorIndex = raster.getSample(x, y, 0); // colorIndex is an offset in the palette of the ICM'
        // Number of indexed colors
        int size = originalICM.getMapSize();
        byte[] reds = new byte[size];
        byte[] greens = new byte[size];
        byte[] blues = new byte[size];
        originalICM.getReds(reds);
        originalICM.getGreens(greens);
        originalICM.getBlues(blues);
        IndexColorModel newICM = new IndexColorModel(8, size, reds, greens, blues, colorIndex);
        return new BufferedImage(newICM, raster, image.isAlphaPremultiplied(), null);
    }
}