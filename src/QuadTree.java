import java.awt.*;
import java.io.*;
import java.util.*;

public class QuadTree {

    private QuadTree northWest, northEast, southEast, southWest, father;
    private Color color;
    private boolean leaf;

    private ImagePNG image;
    private ImagePNG compressImage;

    //Constructor 1
    public QuadTree(ImagePNG image) {
        this.northWest      = null;
        this.northEast      = null;
        this.southEast      = null;
        this.southWest      = null;

        this.father         = null;

        this.leaf           = false;

        this.color          = null;

        this.image          = image;
        this.compressImage  = null;

        this.createQuadTree(image, 0, 0, image.width());

    }

    //Constructor 2
    private QuadTree(ImagePNG image, int x, int y, int sizeImage, QuadTree father) {
        this.northWest      = null;
        this.northEast      = null;
        this.southEast      = null;
        this.southWest      = null;

        this.father         = father;

        this.leaf           = false;

        this.color          = null;

        this.image          = image;
        this.compressImage  = null;

        this.createQuadTree(image, x, y, sizeImage);

    }

    /** @role : Create the complete tree of the image.
     *  @param image image in 2^N format, with N = number of pixels per side.
     *  @param x position x of pixel.
     *  @param y position y of pixel.
     *  @param sizeImage number pixel per side.
     */
    private void createQuadTree(ImagePNG image, int x, int y, int sizeImage) {

        if (sizeImage == 1) {
            this.color = image.getPixel(x, y);
            this.leaf = true; //We arrived to a leaf, so we stop the function

        } else {
            int newSizeImage = sizeImage / 2; //Calculation of new size of childrens (North West, North East, South East and South West).

            int newXNW = x;                 //Coordinate X of cutting North West.
            int newYNW = y;                 //Coordinate Y of cutting North West.

            int newXNE = x + newSizeImage;  //Coordinate X of cutting North East.
            int newYNE = y;                 //Coordinate Y of cutting North East.

            int newXSE = x + newSizeImage;  //Coordinate X of cutting South East.
            int newYSE = y + newSizeImage;  //Coordinate Y of cutting South East.

            int newXSW = x;                 //Coordinate X of cutting South West.
            int newYSW = y + newSizeImage;  //Coordinate Y of cutting South West.

            this.northWest = new QuadTree(image, newXNW, newYNW, newSizeImage, this); //Recursive of cutting North West.
            this.northEast = new QuadTree(image, newXNE, newYNE, newSizeImage, this); //Recursive of cutting North East.
            this.southEast = new QuadTree(image, newXSE, newYSE, newSizeImage, this); //Recursive of cutting South East.
            this.southWest = new QuadTree(image, newXSW, newYSW, newSizeImage, this); //Recursive of cutting South West.


            if (this.northWest.isLeaf() && this.northEast.isLeaf() && this.southEast.isLeaf() && this.southWest.isLeaf()) {
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

    public boolean verificationBound() {
        return !this.isLeaf() && this.getNorthEast().isLeaf() && this.getNorthWest().isLeaf() && this.getSouthWest().isLeaf() && this.getSouthEast().isLeaf();
    }

    // ----------------------------------------------- TO STRING -----------------------------------------------

    /** @role : Display tree in docs format.
     *  @return display in format String.
     */
    public String toString() {
        String display = " ";

        if (this.leaf) {
            return ImagePNG.colorToHex(this.color);
        } else {
            display += "(" + this.northWest.toString() + ") ";
            display += "(" + this.northEast.toString() + ") ";
            display += "(" + this.southEast.toString() + ") ";
            display += "(" + this.southWest.toString() + ") ";

            return display + " ";
        }
    }

    // ----------------------------------------------- COLORIMETRIC DIFFERENCE -----------------------------------------------

    /**
     * Average color between north east, north west, south west and south east.
     * @return average color.
     */
    public Color colorimetricAverage() {
        int Rm = (this.northWest.getColor().getRed()   + this.northEast.getColor().getRed()   + this.southEast.getColor().getRed()   + this.southWest.getColor().getRed()) / 4; //Average red color between north east, north west, south west and south east.
        int Gm = (this.northWest.getColor().getGreen() + this.northEast.getColor().getGreen() + this.southEast.getColor().getGreen() + this.southWest.getColor().getGreen()) / 4; //Average green color between north east, north west, south west and south east.
        int Bm = (this.northWest.getColor().getBlue()  + this.northEast.getColor().getBlue()  + this.southEast.getColor().getBlue()  + this.southWest.getColor().getBlue()) / 4; //Average blue color between north east, north west, south west and south east.

        return new Color(Rm, Gm, Bm);

    }

    /** @role : Calculate the colorimetric difference.
     *  @param average average color.
     *  @return calcule.
     */
    private int colorimetricDifference(Color average) {
        return (int) Math.sqrt(((this.color.getRed() - average.getRed()) * (this.color.getRed() - average.getRed()) +
                (this.color.getGreen() - average.getGreen()) * (this.color.getGreen() - average.getGreen()) +
                (this.color.getBlue() - average.getBlue()) * (this.color.getBlue() - average.getBlue())) / 3.0);
    }


    /** @role : choose the maximum direction between the 4 directions.
     *  @return //choose the maximum direction between south and north.
     */
    public int maxColorimetricDifference() {
        Color average = this.colorimetricAverage();
        int maxNorth = Math.max(this.northWest.colorimetricDifference(average), this.northEast.colorimetricDifference(average)); //choose the maximum direction between north east and north west.
        int maxSouth = Math.max( this.southWest.colorimetricDifference(average), this.southEast.colorimetricDifference(average)); //choose the maximum direction between south east and south west.

        return Math.max(maxNorth, maxSouth);
    }

    // ----------------------------------------------- COMPRESS DELTA -----------------------------------------------

    public void compressDelta(int delta) {
        compressDelta(delta, this);
    }
    /** @role :
     *  @param delta
     *  @param tree
     */
    private void compressDelta(int delta, QuadTree tree) {

        if (tree.isLeaf()) {
            return;
        } else {
            if (tree.verificationBound()) { //If all of sons are leaf

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

            } else {
                compressDelta(delta, tree.getNorthWest());
                compressDelta(delta, tree.getNorthEast());
                compressDelta(delta, tree.getSouthEast());
                compressDelta(delta, tree.getSouthWest());

                if (tree.verificationBound()) { //If all of sons are leaf

                    int colorimetricDifference = tree.maxColorimetricDifference();

                    if (colorimetricDifference <= delta) {
                        Color newColor = tree.colorimetricAverage();

                        tree.setColor(newColor);

                        tree.setNorthWest(null);//All of sons becomes null
                        tree.setNorthEast(null);
                        tree.setSouthWest(null);
                        tree.setSouthEast(null);


                        tree.setLeaf(true);
                    }
                }
           }
        }
    }



    // ----------------------------------------------- COMPRESS PHI -----------------------------------------------

    public void compressPhi(int phi) {
        compressPhi(this, phi);
    }

    /**
     * Compress the number of leaves until reaching phi.
     * @param tree Shaft to compress
     * @param phi leaf limit to be reached.
     */
    private void compressPhi(QuadTree tree, int phi) {
        int numberLeaf = tree.numberLeafs(tree); //Calculate numbers leaf in quadtree.

        Comparator<QuadTree> comparator = new QuadTreeComparator();
        TreeSet<QuadTree> listLeaf = new TreeSet<QuadTree>(comparator);

        //ArrayList<QuadTree> listLeaf = new ArrayList<>();
        //ArrayList<QuadTree> listNewLeaf = new ArrayList<>();

        this.compressPhiTri(tree, listLeaf); //fill listLeaf with leafs.

         //initialize comparator of QuadTree.

        //Collections.sort(listLeaf, comparator);//Sort automatically listLeaf.

        while (phi < numberLeaf && listLeaf.size() > 0) {

            QuadTree saveTree = listLeaf.first();
            listLeaf.remove(saveTree);
            // Displaying the values after iterating through the set
            //System.out.println("The iterator values are: ");

            //Output the first element of the array

            if (saveTree != null) {
                saveTree = this.crushLeaf(saveTree); //and crush (see crush fonction).

                 //Delete elemente of array.


                if (saveTree.getFather() != null && saveTree.getFather().verificationBound()) {
                    listLeaf.add(saveTree.father); //Add in new listLeaf if the father has as a result of overwriting 4 sons.
                }
                numberLeaf -= 3; //the number of leaves decreases by 3 because 4 leaves disappear but 1 new one is created by the father (4 - 1 = 3).

            }
        }
    }


    /**
     * Adds all the leaves of a tree to a list.
     * @param tree
     * @param list list of leafs.
     */
    private void compressPhiTri( QuadTree tree, TreeSet<QuadTree> list) {
        if (tree != null) {
            if (tree.verificationBound()) {
                list.add(tree);
            } else {
                tree.compressPhiTri(tree.getNorthWest(), list);
                tree.compressPhiTri(tree.getNorthEast(), list);
                tree.compressPhiTri(tree.getSouthEast(), list);
                tree.compressPhiTri(tree.getSouthWest(), list);
            }
        }
    }

    /**
     *
     * @param tree
     * @return
     */
    private QuadTree crushLeaf(QuadTree tree) {
        Color newColor =  tree.colorimetricAverage();

        tree.setColor(newColor);

        tree.setNorthWest(null);//All of sons becomes null
        tree.setNorthEast(null);
        tree.setSouthWest(null);
        tree.setSouthEast(null);


        tree.setLeaf(true);

        return tree;
    }


        /** @role : This function count the number of leaves in the tree
         * @param tree
         * @return
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


    /**
     *
     * @param filename
     * @throws IOException
     */
    public void savePNG(String filename) throws IOException {
        ImagePNG imageClone = this.image.clone();
        this.compressionPNG(imageClone, this, 0, 0, imageClone.width());

        this.compressImage = imageClone;

        imageClone.save(filename);
    }

    /**
     *
     * @param image
     * @param arbre
     * @param x
     * @param y
     * @param sizeImage
     */
    private void compressionPNG(ImagePNG image, QuadTree arbre, int x, int y, int sizeImage) {
        if (arbre.isLeaf()) {
            compressionBlockPNG(image, x, y, sizeImage, arbre.getColor());
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

    ///TODO CHANGER NOM FONCTION
    private void compressionBlockPNG(ImagePNG image, int x, int y, int sizeImage, Color rgb) {

        /*if (x == x + sizeImage) {
            //image.setPixel(x, y, new Color(0,0,0));
            //compressionBlockPNG(image, x, y + 1, sizeImage, maxXY, rgb);
        } else if (y == y + sizeImage) {
            //image.setPixel(x, y, new Color(0,0,0));
            //compressionBlockPNG(image, x + 1, y, sizeImage, maxXY, rgb);
        } else {
            System.out.println("X = " + x + " / Y = " + y + " block" );

            image.setPixel(x, y, new Color(0, 0, 0));

            compressionBlockPNG(image, x + 1, y, sizeImage, rgb);
            compressionBlockPNG(image, x, y + 1, sizeImage, rgb);
        }*/

        for (int i = x ; i < x + sizeImage ; ++i) {
            for (int j = y ; j < y + sizeImage ; j++) {
                image.setPixel(i, j, rgb);
            }
        }
    }



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

    public double EQM() {
        return ImagePNG.computeEQM(this.image, this.compressImage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuadTree quadTree = (QuadTree) o;
        return leaf == quadTree.leaf &&
                Objects.equals(northWest, quadTree.northWest) &&
                Objects.equals(northEast, quadTree.northEast) &&
                Objects.equals(southEast, quadTree.southEast) &&
                Objects.equals(southWest, quadTree.southWest) &&
                Objects.equals(color, quadTree.color) &&
                Objects.equals(image, quadTree.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(northWest, northEast, southEast, southWest, color, leaf, image);
    }
}
