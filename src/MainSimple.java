import java.io.IOException;

public class MainSimple {

    public static void main(String[] args) throws IOException {
        ImagePNG i = new ImagePNG("pngs/32-tux.png"); //CHARGE UN IMAGE PNG

        QuadTree t = new QuadTree(i);

        t.compressPhi(250);


        t.savePNG("SavePNG/test2.png");

        System.out.println(Main.displayEQM(t));

    }
}
