import java.awt.*;
import java.io.*;
import java.util.*;

public class QuadTree {

    private QuadTree northWest;
    private QuadTree northEast;
    private QuadTree southEast;
    private QuadTree southWest;

    private final QuadTree father;

    private Color color;
    private boolean leaf;

    private final String imagePath;
    private String compressImagePath;

    //Constructor 1
    public QuadTree(String imagePath) throws IOException {
        this.northWest          = null;
        this.northEast          = null;
        this.southEast          = null;
        this.southWest          = null;

        this.father             = null;

        this.leaf               = false;

        this.color              = null;



        this.imagePath          = constructionPath(imagePath);

        ImagePNG image          = Menu.loadImagePNG(imagePath);

        this.compressImagePath  = imagePath;

        this.createQuadTree(image, imagePath, 0, 0, image.width());

    }

    //Constructor 2
    private QuadTree(ImagePNG image, String imagePath, int x, int y, int sizeImage, QuadTree father) {
        this.northWest          = null;
        this.northEast          = null;
        this.southEast          = null;
        this.southWest          = null;

        this.father             = father;

        this.leaf               = false;

        this.color              = null;

        this.imagePath          = imagePath;
        this.compressImagePath  = imagePath;

        this.createQuadTree(image, imagePath, x, y, sizeImage);

    }

    /** @role : Create the complete tree of the image.
     *
     *  @param image image in 2^N format, with N = number of pixels per side.
     *  @param x position x of pixel.
     *  @param y position y of pixel.
     *  @param sizeImage number pixel per side.
     */
    private void createQuadTree(ImagePNG image, String imagePath, int x, int y, int sizeImage) {

        if (sizeImage == 1) {
            this.color = image.getPixel(x, y);
            this.leaf = true; //We arrived to a leaf, so we stop the function

        } else {
            int newSizeImage = sizeImage / 2; //Calculation of new size of childrens (North West, North East, South East and South West).

            int newXNE = x + newSizeImage;  //Coordinate X of cutting North East.

            int newXSE = x + newSizeImage;  //Coordinate X of cutting South East.
            int newYSE = y + newSizeImage;  //Coordinate Y of cutting South East.

            int newYSW = y + newSizeImage;  //Coordinate Y of cutting South West.

            this.northWest = new QuadTree(image, imagePath, x, y, newSizeImage, this); //Recursive of cutting North West.
            this.northEast = new QuadTree(image, imagePath, newXNE, y, newSizeImage, this); //Recursive of cutting North East.
            this.southEast = new QuadTree(image, imagePath, newXSE, newYSE, newSizeImage, this); //Recursive of cutting South East.
            this.southWest = new QuadTree(image, imagePath, x, newYSW, newSizeImage, this); //Recursive of cutting South West.


            if (this.northWest.isLeaf() && this.northEast.isLeaf() && this.southEast.isLeaf() && this.southWest.isLeaf()) { //lossless compression.
                if (this.northWest.getColor().equals(this.northEast.getColor()) &&
                    this.northEast.getColor().equals(this.southEast.getColor()) &&
                    this.southEast.getColor().equals(this.southWest.getColor()) &&
                    this.southWest.getColor().equals(this.northWest.getColor())) {

                    this.color = this.northWest.getColor();

                    this.northWest = null;
                    this.northEast = null;

                    this.southEast = null;
                    this.southWest = null;

                    this.leaf = true;
                }
            }
        }
    }

    // ----------------------------------------------- GETTERS -----------------------------------------------

    public Color getColor() {
        return color;
    }

    public QuadTree getNorthWest() {
        return this.northWest;
    }

    public QuadTree getNorthEast() {
        return this.northEast;
    }

    public QuadTree getSouthEast() {
        return this.southEast;
    }

    public QuadTree getSouthWest() {
        return this.southWest;
    }

    public QuadTree getFather() {
        return father;
    }

    public boolean isLeaf() {
        return leaf;
    }

    // ----------------------------------------------- SETTERS -----------------------------------------------

    public void setColor(Color color) {
        this.color = color;
    }

    public void setNorthWest(QuadTree northWest) {
        this.northWest = northWest;
    }

    public void setNorthEast(QuadTree northEast) {
        this.northEast = northEast;
    }

    public void setSouthEast(QuadTree southEast) {
        this.southEast = southEast;
    }

    public void setSouthWest(QuadTree southWest) {
        this.southWest = southWest;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    // ----------------------------------------------- VERIFICATION -----------------------------------------------

    /** @role checking if the node is a father of 4 leaves and if it is not a leaf.
     *
     *  @return boolean
     */
    public boolean verificationBound() {
        return !this.isLeaf() && this.getNorthEast().isLeaf() && this.getNorthWest().isLeaf() && this.getSouthWest().isLeaf() && this.getSouthEast().isLeaf();
    }

    // ----------------------------------------------- TO STRING -----------------------------------------------

    /** @role Display tree in docs format.
     *
     *  @return display in format String.
     */
    public String toString() {
        String display = "(";

        if (this.leaf) {
            return ImagePNG.colorToHex(this.color);
        } else {
            display += "" + this.northWest.toString() + " ";
            display += "" + this.northEast.toString() + " ";
            display += "" + this.southEast.toString() + " ";
            display += "" + this.southWest.toString();

            return display + ")";
        }
    }

    // ----------------------------------------------- COLORIMETRIC DIFFERENCE -----------------------------------------------

    /** @role : Average color between north east, north west, south west and south east.
     *
     *  @return average color.
     */
    public Color colorimetricAverage() {
        int Rm = (this.northWest.getColor().getRed()   + this.northEast.getColor().getRed()   + this.southEast.getColor().getRed()   + this.southWest.getColor().getRed()) / 4; //Average red color between north east, north west, south west and south east.
        int Gm = (this.northWest.getColor().getGreen() + this.northEast.getColor().getGreen() + this.southEast.getColor().getGreen() + this.southWest.getColor().getGreen()) / 4; //Average green color between north east, north west, south west and south east.
        int Bm = (this.northWest.getColor().getBlue()  + this.northEast.getColor().getBlue()  + this.southEast.getColor().getBlue()  + this.southWest.getColor().getBlue()) / 4; //Average blue color between north east, north west, south west and south east.

        return new Color(Rm, Gm, Bm);

    }

    /** @role Calculate the colorimetric difference.
     *
     *  @param average average color.
     *  @return calcule.
     */
    private int colorimetricDifference(Color average) {
        return (int) Math.sqrt(((this.color.getRed() - average.getRed()) * (this.color.getRed() - average.getRed()) +
                (this.color.getGreen() - average.getGreen()) * (this.color.getGreen() - average.getGreen()) +
                (this.color.getBlue() - average.getBlue()) * (this.color.getBlue() - average.getBlue())) / 3.0);
    }


    /** @role choose the maximum direction between the 4 directions.
     *
     *  @return choose the maximum direction between south and north.
     */
    public int maxColorimetricDifference() {
        Color average = this.colorimetricAverage();
        int maxNorth = Math.max(this.northWest.colorimetricDifference(average), this.northEast.colorimetricDifference(average)); //choose the maximum direction between north east and north west.
        int maxSouth = Math.max( this.southWest.colorimetricDifference(average), this.southEast.colorimetricDifference(average)); //choose the maximum direction between south east and south west.

        return Math.max(maxNorth, maxSouth);
    }

    /** @role Calculate the maximum colorimetric for a father of 4 leaves.
     *
     *  @param delta limit value of the colorimetric difference.
     *  @param tree Father of 4 sheets to calculate the maximum colorimetric difference.
     */
    private void maxColorimetricDifference(int delta, QuadTree tree) {
        int colorimetricDifference = tree.maxColorimetricDifference();

        if (colorimetricDifference <= delta) {
            Color newColor =  tree.colorimetricAverage();

            tree.setColor(newColor);

            tree.setNorthWest(null);//All of sons becomes null
            tree.setNorthEast(null);
            tree.setSouthWest(null);
            tree.setSouthEast(null);


            tree.setLeaf(true);
        }
    }

    // ----------------------------------------------- COMPRESS DELTA -----------------------------------------------

    /** @role call version of compressDelta for the menu.
     *
     *  @param delta limit value of the colorimetric difference.
     */
    public void compressDelta(int delta) {
        compressDelta(delta, this);
    }
    /** @role Go through the tree and compress if the colorimetric interpretation is lower than the delta.
     *
     *  @param delta limit value of the colorimetric difference.
     *  @param tree Shaft to compress
     */
    private void compressDelta(int delta, QuadTree tree) {

        if (!tree.isLeaf()) {
            if (tree.verificationBound()) { //If all of sons are leaf
                maxColorimetricDifference(delta, tree);
            } else {
                compressDelta(delta, tree.getNorthWest());
                compressDelta(delta, tree.getNorthEast());
                compressDelta(delta, tree.getSouthEast());
                compressDelta(delta, tree.getSouthWest());

                if (tree.verificationBound()) { //If all of sons are leaf
                    maxColorimetricDifference(delta, tree);
                }
           }
        }
    }


    // ----------------------------------------------- COMPRESS PHI -----------------------------------------------

    /** @role call version of compressPhi for the menu.
     *
     *  @param phi number of sheets that must remain at the end.
     */
    public void compressPhi(int phi) {
        compressPhi(this, phi);
    }


    /** @role Compress the number of leaves until reaching phi.
     *
     *  @param tree Shaft to compress
     *  @param phi leaf limit to be reached.
     */
    private void compressPhi(QuadTree tree, int phi) {
        int numberLeaf = tree.numberLeafs(tree); //Calculate numbers leaf in quadtree.

        Comparator<QuadTree> comparator = new QuadTreeComparator();
        TreeSet<QuadTree> listLeaf = new TreeSet<>(comparator);

        this.compressPhiTree(tree, listLeaf); //fill listLeaf with leafs.

        while (phi < numberLeaf) {

            QuadTree saveTree = listLeaf.first();
            listLeaf.remove(saveTree);

            crushLeaf(saveTree);//and crush (see crush fonction).

            if (saveTree.getFather() != null && saveTree.getFather().verificationBound()) {
                listLeaf.add(saveTree.getFather()); //Add in new listLeaf if the father has as a result of overwriting 4 sons.
            }

            numberLeaf -= 3;
        }
    }


    /** @role Adds all the leaves of a tree to a list.
     *
     *  @param tree Quadtree
     *  @param list list of leafs.
     */
    private void compressPhiTree( QuadTree tree, TreeSet<QuadTree> list) {
        if (tree != null) {
            if (tree.verificationBound()) {
                list.add(tree);
            } else {
                tree.compressPhiTree(tree.getNorthWest(), list);
                tree.compressPhiTree(tree.getNorthEast(), list);
                tree.compressPhiTree(tree.getSouthEast(), list);
                tree.compressPhiTree(tree.getSouthWest(), list);
            }
        }
    }

    // ----------------------------------------------- LEAFS OPERATION -----------------------------------------------

    /** @role turns a father of 4 leaves into leaves.
     *
     *  @param tree Father of 4 leaves.
     */
    private void crushLeaf(QuadTree tree) {
        Color newColor =  tree.colorimetricAverage();

        tree.setColor(newColor);

        tree.setNorthWest(null);//All of sons becomes null
        tree.setNorthEast(null);
        tree.setSouthWest(null);
        tree.setSouthEast(null);


        tree.setLeaf(true);

    }


        /** @role This function count the number of leaves in the tree.
         *
         *  @param tree QuadTree tree.
         *  @return the number of leaves in the tree.
         */
    public int numberLeafs(QuadTree tree){
        if(tree != null) {
            if (tree.isLeaf()) {
                return 1;
            } else {
                return (numberLeafs(tree.getNorthWest()) + numberLeafs(tree.getNorthEast()) + numberLeafs(tree.getSouthWest()) + numberLeafs(tree.getSouthEast()));
            }
        }

        return 0;
    }

    // ----------------------------------------------- SAVE PNG -----------------------------------------------
    /** @role Saves the tree in png format. Rewrite the image from the tree. By default saved in the SavePNG folder otherwise specify the path.
     *
     *  @param filename file name or save path.
     *  @throws IOException Exception if the file or path does not exist.
     */
    public void savePNG(String filename) throws IOException {
        ImagePNG image = Menu.loadImagePNG(this.imagePath);
         this.compressImagePath = constructionPathCompress(filename);

        this.compressionPNG(image, this, 0, 0, image.width());

        image.save(this.compressImagePath);
    }


    /** @role Recursively cycle through the tree and overwrite identical pixel packets with the help of crushPixelPNG.
     *
     *  @param image image to compress to PNG.
     *  @param arbre Tree to parcoured.
     *  @param x position X in image.
     *  @param y position Y in image.
     *  @param sizeImage number of pixels on one side of the image.
     */
    private void compressionPNG(ImagePNG image, QuadTree arbre, int x, int y, int sizeImage) {
        if (arbre.isLeaf()) {
            crushPixelPNG(image, x, y, sizeImage, arbre.getColor());
        } else {
            int newSizeImage = sizeImage / 2; //Calculation of new size of childrens (North West, North East, South East and South West).

            int newXNE = x + newSizeImage;  //Coordinate X of cutting North East.

            int newXSE = x + newSizeImage;  //Coordinate X of cutting South East.
            int newYSE = y + newSizeImage;  //Coordinate Y of cutting South East.

            int newYSW = y + newSizeImage;  //Coordinate Y of cutting South West.

            compressionPNG(image, arbre.getNorthWest(), x, y, newSizeImage);
            compressionPNG(image, arbre.getNorthEast(), newXNE, y, newSizeImage);
            compressionPNG(image, arbre.getSouthEast(), newXSE, newYSE, newSizeImage);
            compressionPNG(image, arbre.getSouthWest(), x, newYSW, newSizeImage);
        }
    }

    /** @role overwrite pixels of identical packet.
     *
     *  @param image image to overwrite.
     *  @param x top left position x of the pixel to be overwritten
     *  @param y top left position y of the pixel to be overwritten.
     *  @param sizeImage pixel packet size to be overwritten.
     *  @param rgb pixel color.
     */
    private void crushPixelPNG(ImagePNG image, int x, int y, int sizeImage, Color rgb) {
        for (int i = x ; i < x + sizeImage ; ++i) {
            for (int j = y ; j < y + sizeImage ; j++) {
                image.setPixel(i, j, rgb);
            }
        }
    }

    // ----------------------------------------------- SAVE TXT -----------------------------------------------

    /** @role Saves the QuadTree in SaveTXT in written form.
     *
     *  @param location image path.
     */
    public void saveTXT(String location) {
        String quadTreeTXT = this.toString();

        try {
            BufferedWriter write = new BufferedWriter(new FileWriter(location));

            write.write(quadTreeTXT);

            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------- EQM -----------------------------------------------

    /** @role Display of EQM values.
     *
     *  @throws IOException Exception if the path or image does not exist.
     */
    public void EQM() throws IOException {
        ImagePNG imageOrigine = Menu.loadImagePNG(this.imagePath);
        ImagePNG imageCompress = new ImagePNG(this.compressImagePath);

        File imageOrigineFile    =    new File(this.imagePath);
        File imageCompressFile   =    new File(this.compressImagePath);

        double EQMImage = ImagePNG.computeEQM(imageOrigine,imageCompress);
        double sizeImageComparaison = Math.ceil(10000.0*imageCompressFile.length() / imageOrigineFile.length())/100.0;

        System.out.println("Image: taille = " + sizeImageComparaison + "% / qualité = " + EQMImage + "%");
    }

    /** @role build the path from the pngs folder if it isn't.
     *
     *  @param path unbuilt path.
     *  @return build path.
     */
    private String constructionPath(String path) {

        if (!path.contains("\\") && !path.contains(".png")) {
            return "pngs/" + path + ".png";
        } else if (!path.contains("/")){
            return "pngs/" + path;
        } else {
            return path;
        }
    }
    private String constructionPathCompress(String path) {

        if (!path.contains("\\") && !path.contains(".png")) {
            return "SavePNG/" + path + ".png";
        } else if (!path.contains("/")){
            return "SavePNG/" + path;
        } else {
            return path;
        }
    }
}
