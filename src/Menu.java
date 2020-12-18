import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    //TODO faire chargement delta save txt et phi
    // TODO LOU : signature fonction commentaring
    //TODO afficher abrbre et les 3 menus bien
    //TODO expliquer eqm dans le meu, faire saut à la ligne pour que ce soit propre

    public static final String RED   = "\033[0;31m"; //Color red
    public static final String GREEN = "\033[0;32m"; //Color green
    public static final String RESET = "\033[0m";  //Reset color

    private final ArrayList<String> listMenu;
    private boolean compressMenu, saveMenu, comparator;
    private QuadTree tree;

    //Constructor
    public Menu() {

        this.compressMenu = false;
        this.saveMenu     = false;
        this.comparator   = false;

        this.listMenu     = new ArrayList<>();

        this.tree         = null;

    }

    //---------------------------------------- START PROGRAMM
    /** @role This function catch if in terminal we have argument. If we don't have argument,
     * programm() starts interractive mode, else, starts non interractive mode.
     *
     * @param args It catch argument in the main class.
     * @throws Exception catch a global error.
     * */
    public void programm(String[] args) throws Exception {

        //Begin
        if ( args.length == 0 ) { //If we are in interactive mode.
            startProgramme();

        } else if ( args.length == 3 ) { //If we are in noninteractive mode.
            noInteractiveProgramme(args);

        } else {
            throw new Exception("Le nombre d'argument n'est pas le bon ! ");
        }
    }

    //---------------------------------------- INTERACTIVE MODE

    /** @role This function starts programm. The goal is to display the introductory sentence before any operation
     * and it scans the input (name of the image) to starts programm.
     *
     * @throws IOException catch a global input or output error.
     */
    public void startProgramme() throws IOException {

        createMenu();
        if (!this.compressMenu && !this.saveMenu) { //compressMenu and saveMenu they are not unlocked
            try {
                System.out.println("Bonjour, bienvenue dans notre outil de compression d'image !\n" +
                        "→ Nous vous invitons à charger une image (indiquez le nom du fichier ou chemin d'accès du fichier) en mémoire pour acceder au menu.\nNous vous souhaitons une bonne découverte !\n");

                Scanner scan = new Scanner(System.in);
                String imagePath = scan.nextLine(); //we scan the user's keyboard input

                //TODO préciser dans le readMe soit indiquer chemin (mettre exemple) soit juste le nom de l'image si elle se trouve dans pngs
                this.tree = new QuadTree(imagePath);
                this.compressMenu = true;//So, we can access the menu.

                startProgramme();

            } catch (IOException e) {
                System.out.println(RED + "/!\\ ERREUR : Le nom du fichier est incorrect ! " + RESET);
                programmeError(); //This error the error is processed in the called function.
                throw e;
            }

        } else {
            displayMenu();
            choiceOption();
        }
    }

    /** @role Serves as an error case when launching the menu. If the user enter Serves as an error case when launching the menu,
     * this function it will deal with the problem and ask until the user enters a correct name.
     *
     *  @throws IOException catch a global input or output error.
     */
    private void programmeError() throws IOException {
        try {
            System.out.println("\n→ Nous vous invitons à re-donner le nom de votre image pour acceder au menu.");
            Scanner scan = new Scanner(System.in);

            String imagePath = scan.nextLine();
            this.tree = new QuadTree(imagePath); //We load image.

        } catch (IOException e) {
            System.out.println(RED + "/!\\ ERREUR : Le nom du fichier est incorrect ! " + RESET);
            programmeError(); //if the error persists
            throw e;
        }
        this.compressMenu = true;
        startProgramme();
    }


    /** @role Some of the functions cannot be performed without some being done first.
     * It displays the functions that can be performed and blocks the others. */
    private void createMenu(){

        if (this.compressMenu && !this.saveMenu && !this.comparator) {
            listMenu.add("\n************************************* MENU *************************************\n\n");
            listMenu.add("\t 0. Quitter le programme.\n");
            listMenu.add("\t 1. Recharger une image PNG en mémoire dans un quadtree.\n");
            listMenu.add("\t 2. Appliquer une compression Delta pour un ∆ donné.\n");
            listMenu.add("\t 3. Appliquer une compression Phi pour un Φ donné.\n");
            listMenu.add("\n\t A. Afficher à l'écran l'arbre.\n");

        } else if (this.compressMenu && this.saveMenu && !this.comparator && this.listMenu.size() < 8 ) {
            listMenu.remove(5);
            listMenu.add("\t 4. Sauvegarder le quadtree dans un fichier PNG.\n");
            listMenu.add("\t 5. Sauvegarder la représentation textuelle du quadtree dans un fichier TXT.\n");
            listMenu.add("\n\t A. Afficher à l'écran l'arbre.\n");

        } else if (this.compressMenu && this.saveMenu && this.comparator && this.listMenu.size() < 9) {
            listMenu.remove(7);
            listMenu.add("\t 6. Donner les mesures comparative de deux fichiers images PNG.\n\n");
            listMenu.add("\t A. Afficher à l'écran l'arbre.\n");
        }
    }

    /** @role Display the menu into console. */
    private void displayMenu() {
        for (String option : this.listMenu) {
            System.out.print(option);
        }
    }



    /** @role from the user's choices, the function performs the correct calculations and displays the correct display, then it recalls the menu.
     *
     * @throws IOException catch a global input or output error.
     */
    private void choiceOption() throws IOException {
        System.out.println("\n→ A présent, choissisez quelle option vous intérresse.");

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        switch (choice) {
            case "0": //exit the program
                exit();
                break;
            case "1": //Load another image
                this.compressMenu = false;
                this.saveMenu = false;
                startProgramme();
                break;
            case "2": //Compress with delta
                this.saveMenu = true;
                compressDelta();
                break;
            case "3": //Compress with phi
                this.saveMenu = true;
                compressPhi();
                break;
            case "4": //Save a image in PNG
                if (saveMenu) {
                    savePNG();
                    this.comparator = true;
                } else {
                    problemmeChoice();
                }
                break;
            case "5": //Save a image in TXT
                if (saveMenu) {
                    saveTXT();
                } else {
                    problemmeChoice();
                }
                break;
            case "6": //Compare the gap between 2 trees
                if (this.saveMenu && this.comparator) {
                    displayEQM(this.tree);
                } else {
                    problemmeChoice();
                }
                break;
            case "A": //Display tree
                System.out.println("\nArbre : \n");
                System.out.println(this.tree.toString());
                break;
            default: //Invamide input
                System.out.println(RED + "/!\\ ERREUR : Entrée invalide, veuillez refaire votre choix." + RESET);
                choiceOption();
        }
        startProgramme();
    }

    private void problemmeChoice() throws IOException {
        System.out.println(RED + "/!\\ ERREUR : Le choix est incorrect ! " + RESET);
        choiceOption();
    }


    /** @role To finish the programm */
    private void exit() {
        System.out.println(GREEN + "\nMerci, au revoir et à la prochaîne !\n" + RESET);
        System.exit(0);

    }

    //---------------------------------------- COMPRESSION
    /** @role the function requests an integer to performs a delta compression corresponding to the input caught by the scanner.
     *
     *  @throws InputMismatchException captures if the user's input is not the correct type of input expected.
     *  @throws IOException catch a global input or output error.
     */
    public void compressDelta() throws InputMismatchException, IOException {
        Scanner scan = new Scanner(System.in);
        int delta;

        System.out.println("Choissisez votre delta (un entier entre 0 et 255) pour la compression : ");

        try {
            delta = scan.nextInt();
            while (delta < 0 || delta > 255) {
                System.out.println(RED + "/!\\ ERREUR : Entrée invalide, veuillez re-donner un delta." + RESET);
                delta = scan.nextInt();
            }

            this.tree.compressDelta(delta);//We applied compression.

        } catch (InputMismatchException eDelta) {
            System.out.println(RED + "/!\\ ERREUR : Il faut un entier entre 0 et 255 ! " + RESET);
            compressDelta(); //While the error persist, we restrat the function.
            throw eDelta;
        }
        startProgramme();
    }


    /** @role the function requests an integer to performs a phi compression corresponding to the input caught by the scanner.
     *
     * @throws InputMismatchException captures if the user's input is not the correct type of input expected.
     * @throws IOException catch a global input or output error.
     */
    public void compressPhi() throws InputMismatchException, IOException {
        Scanner scan = new Scanner(System.in);
        int phi;

        System.out.println("Choissisez votre phi (un entier strictement supérieur à 0) pour la compression : ");

        try {
            phi = scan.nextInt();
            while (phi < 0 || phi > 255) {
                System.out.println(RED + "/!\\ ERREUR : Entrée invalide, veuillez re-donner un phi." + RESET);
                phi = scan.nextInt();
            }

            this.tree.compressPhi(phi); //We applied compression.

        } catch (InputMismatchException ePhi) { //We caught an error.
            System.out.println(RED + "/!\\ ERREUR : Il faut un entier strictement supérieur à 0 ! " + RESET);
            compressPhi(); //While the error persist, we restrat the function.
            throw ePhi;
        }
        startProgramme();
    }


    /** @role Save a compressed image in PNG file.
     *
     *  @throws IOException catch a global input or output error.
     */
    private void savePNG() throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Choissisez votre nom de fichier PNG ou le chemin ou vous voulez enregistrer : ");
        String namePNG = scan.nextLine();
        this.tree.savePNG(namePNG);
    }

    /** @role Save a compressed tree in TXT file.
     *
     *  @throws IOException catch a global input or output error.
     */
    private void saveTXT() throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Choissisez votre nom de fichier TXT : ");
        String nameTXT = scan.nextLine();
        this.tree.saveTXT("SaveTXT/" + nameTXT + ".txt");
        startProgramme();
    }



    //---------------------------------------- NON INTERACTIVE MODE
    /** @role This function starts non interactive mode.
     *
     *  @param args It catch argument in terminal.
     *  @throws Exception catch a global error.
     */
    public void noInteractiveProgram(String[] args) throws Exception {

        System.out.println("Chargement de l'image...");

        int delta = Integer.parseInt(args[1]);
        int phi = Integer.parseInt(args[2]);

        System.out.println("\nCréation des fichiers \n"
                + "DELTA : SaveTXT/" + args[0] + "-delta" + delta + ".txt" + " et " + "SavePNG/" + args[0] + "-delta" + delta + ".png"
                + " PHI :   SaveTXT/ " + args[0] + "-phi" + phi + ".txt" + " et " + "SavePNG/" + args[0] + "-phi" + phi + ".png");

        createDeltaFile(delta, args[0], args[0]); //creation of delta PNG and text files.
        createPhiFile(phi, args[0], args[0]); //creation of phi PNG and text files.
    }


    /** @role This function load a new image.
     *
     *  @param imagePath path of the future image.
     *  @throws IOException catch a global input or output error.
     *  @return ImagePNG
     */
    public static ImagePNG loadImagePNG(String imagePath) throws IOException {
        if (imagePath.contains("\\") || imagePath.contains("/")) {
            return new ImagePNG(imagePath);
        } else if (imagePath.contains(".png")) {
            return new ImagePNG("pngs/" + imagePath);
        } else {
            return new ImagePNG("pngs/" + imagePath + ".png");
        }
    }

    //---------------------------------------- CREATE FILE
    /** @role This function create delta PNG and text files.
     *
     *  @param delta compression index.
     *  @param name name of the future image.
     *  @param imagePath path of the future image.
     *  @throws IOException catch a global input or output error.
     */
    private void createDeltaFile(int delta, String name, String imagePath) throws IOException {
        QuadTree deltaTree = new QuadTree(imagePath);
        deltaTree.compressDelta(delta);

        deltaTree.saveTXT("SaveTXT/" + name + "-delta" + delta + ".txt");
        deltaTree.savePNG("SavePNG/" + name + "-delta" + delta + ".png");

        System.out.print("\n Comparaison fichiers Delta : ");
        displayEQM(deltaTree);
    }


    /** @role This function create phi PNG and text files.
     *
     * @param phi compression index.
     * @param name name of the future image.
     * @param imagePath  path of the future image.
     * @throws IOException catch a global input or output error.
     */
    private void createPhiFile(int phi, String name, String imagePath) throws IOException {
        QuadTree phiTree = new QuadTree(imagePath);
        phiTree.compressPhi(phi);

        phiTree.saveTXT("SaveTXT/" + name + "-phi" + phi + ".txt");
        phiTree.savePNG("SavePNG/" + name + "-phi" + phi + ".png");

        System.out.print("\n Comparaison fichiers Phi : ");
        displayEQM(phiTree);

    }

    /** @role Calculate and display EQM.
     *
     *  @param tree Tree comparared against.
     *  @throws IOException catch a global input or output error.
     */
    public void displayEQM(QuadTree tree) throws IOException {
        tree.EQM();
    }


}//END
