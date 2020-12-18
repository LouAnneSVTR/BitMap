import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import javax.imageio.ImageIO;

public class ImagePNG
{
    private BufferedImage img; // the bitmap

    // Default constructor: load image from file
    public ImagePNG(String filename) throws IOException {
        File fic = new File(filename);
        img = ImageIO.read(fic);
    }

    // Copy constructor
    public ImagePNG(ImagePNG png) {
        ColorModel cm = png.img.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = png.img.copyData(null);
        img = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    // Clone method
    public ImagePNG clone() {
        return new ImagePNG(this);
    }

    // Saving the png image to a file
    public void save(String filename) throws IOException {
        File fic = new File(filename);
        ImageIO.write(img,"png",fic);
    }

    // Number of pixels in X dimension
    public int width() {
        return img.getWidth();
    }

    // Number of pixels in Y dimension
    public int height() {
        return img.getHeight();
    }

    // Pixel color getter
    public Color getPixel(int x, int y) {
        return new Color(img.getRGB(x,y));
    }

    // Pixel color setter
    public void setPixel(int x, int y, Color col) {
        img.setRGB(x,y,col.getRGB());
    }

    // Function (static) that converts a color to an hexadecimal code
    public static String colorToHex(Color col) {
        return Integer.toHexString(col.getRGB()).substring(2);
    }

    // Function (static) that computes a similarity index between images ref and cpy
    // based on the mean squared error measure (see https://fr.wikipedia.org/wiki/Peak_Signal_to_Noise_Ratio)
    public static double computeEQM(ImagePNG ref, ImagePNG cpy) {
        double w = ref.width(), h = ref.height(),  eqm = 0;
        for( int x = 0 ; x < w ; x++ ) {
            for( int y = 0 ; y < h ; y++ ) {
                // Pixel (x,y) retrieval
                Color Cref = ref.getPixel(x,y), Ccpy = cpy.getPixel(x,y);
                // Components normalization
                double Rref = Cref.getRed()/255.0, Gref = Cref.getGreen()/255.0, Bref = Cref.getBlue()/255.0;
                double Rcpy = Ccpy.getRed()/255.0, Gcpy = Ccpy.getGreen()/255.0, Bcpy = Ccpy.getBlue()/255.0;
                // squared error contribution
                eqm += ((Rref-Rcpy)*(Rref-Rcpy) + (Gref-Gcpy)*(Gref-Gcpy) + (Bref-Bcpy)*(Bref-Bcpy))/(3*h*w);
            }
        }
        return Math.ceil(10000*(1-eqm))/100.0; // percentage rounded to the hundredth
    }

    /**************** À RETIRER DANS VOTRE PROJET *********************/
    /** Code de démonstration de l'usage de cette classe **/
    /*public static void main( String[] args )
    {
        try {
            if (args.length!=1) throw new IllegalArgumentException("Préciser le nom (et chemin) d'un unique fichier png en paramètre");

            ImagePNG png = new ImagePNG(args[0]);
            ImagePNG pngNB = new ImagePNG(png); // une copie
            ImagePNG pngR = new ImagePNG(png); // une autre copie

            // 1. MODIFICATION DES COULEURS

            Color col,nb,red;
            int r,g,b,l;
            for (int x=0 ; x<png.width(); x++)
            {
                for (int y=0 ; y<png.height() ; y++)
                {
                    // extraction de la couleur du pixel (x,y)
                    col = png.getPixel(x,y);
                    // modification de la couleur
                    r = col.getRed();
                    g = col.getGreen();
                    b = col.getBlue();
                    l = (r+g+b)/3;
                    nb = new Color(l,l,l);
                    red = new Color(l,0,0);
                    // inscription de la nouvelle couleur des pixels (x,y)
                    pngNB.setPixel(x,y,nb);
                    pngR.setPixel(x,y,red);
                }
            }
            png.save(args[0]);
            pngNB.save(args[0]+"NB");
            pngR.save(args[0]+"R");

            // 2. COMPARAISON DES RÉSULTATS

            // calcul des indices de similarité
            double siNB = ImagePNG.computeEQM(png,pngNB);
            double siR = ImagePNG.computeEQM(png,pngR);

            // chargement des fichiers
            File fic = new File(args[0]);
            File ficNB =  new File(args[0]+"NB");
            File ficR =  new File(args[0]+"R");

            // rapport des tailles
            double wNB = Math.ceil(10000.0*ficNB.length() / fic.length())/100.0;
            double wR = Math.ceil(10000.0*ficR.length() / fic.length())/100.0;

            // affichage des critères
            System.out.println("NB: taille="+wNB+"% / qualité="+siNB+"%");
            System.out.println("R: taille="+wR+"% / qualité="+siR+"%");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
