import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    //Variable
    static QuadTree tree, deltaTree, phiTree;
    static ArrayList listMenu, menuChoiceMade;
    private static boolean firstStage = false, secondStage = false, thirdStage = false, thirdStage = false, fouthStage = false ;


    //---------------------------------------- INTERACTIVE MODE : MENU
    private static void welcome() throws IOException {
        String str = ("Bienvenue dans notre outil de compression d'image !\n"
                + "→ Nous vous invitons à presser une touche du clavier pour acceder au menu. Nous vous souhaitons une bonne découverte ! ");

        System.out.println(str + "\n\n");
        Scanner scan = new Scanner(System.in);
        String enter = scan.next();
        createMenu();
    }

    private static void createMenu() throws IOException {
        listMenu = new ArrayList<String>();

        listMenu.add("************************************* MENU *************************************\n\n");
        listMenu.add("\t 1. Charger une image PNG en mémoire dans un quadtree.\n");
        listMenu.add("\t 2. Appliquer une compression Delta pour un ∆ donné.\n");
        listMenu.add("\t 3. Appliquer une compression Phi pour un Φ donné.\n");
        listMenu.add("\t 4. Sauvegarder le quadtree dans un fichier PNG.\n");
        listMenu.add("\t 5. Sauvegarder la représentation textuelle du quadtree dans un fichier TXT.\n");
        listMenu.add("\t 6. Donner les mesures comparative de deux fichiers images PNG.\n\n");

        //System.out.println(listMenu.toString());
        choiceOption();
    }

    private static void displayCorrectMenu(){

        if ( menuChoiceMade.size() != 0 ) {
            if ( firstStage == true && secondStage == false ){
                System.out.println(listMenu.get(0));//Display
                System.out.println(listMenu.get(1));
                System.out.println(listMenu.get(2));//Display
                System.out.println(listMenu.get(3));

            } else if ( firstStage == true && secondStage == true && thirdStage == false  ) {

            } else if ( firstStage == true && secondStage == true && thirdStage == true  ) {

            }

        } else {
            System.out.println(listMenu.get(0));//Display
            System.out.println(listMenu.get(1));

        }
    }

    //TODO voir si on eut sans appuyer sur la touche entrer selectionner une option !!
    private static void choiceOption() throws IOException {
        System.out.println("→ A présent, choissisez quelle option entre 1 et 6 vous intérresse.");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        menuChoiceMade.add(choice);//We store the selection to display the correct options in the menu

        switch(choice){
            case 1:
                firstStage = true;
                boolean b = false;
                loadImage(b);
                break;
            case 2:
                compressDelta();
                break;
            case 3:
                compressPhi();
                break;
            case 4:
                savePNG();
                break;
            case 5:
                saveTXT();
                break;
            case 6:
                displayEQM(tree);
                break;
            default:
                System.out.println("Entrée invalide, veuillez refaire votre choix.");
                choiceOption();
        }
        continueProgramm();
    }


    private static void continueProgramm() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n→ Voulez-vous continuer ? ");
        String responce = scanner.next();

        if(responce.equals("oui") || responce.equals("Oui") || responce.equals("OUI")){
            //TODO CONTINUER LE PROGRAMME EN FONCTION DES CHOIX EFFECTUES
            createMenu();
        } else if (responce.equals("non") || responce.equals("Non") || responce.equals("NON")) {
            System.out.println("\nMerci, au revoir et à la prochaîne !\n");
            return;
        } else {
            //TODO APPLER FONCTION
            continueProgramm();
        }
    }


    //---------------------------------------- NON INTERACTIVE MODE : MENU
    private static ImagePNG loadImage(String str) throws IOException {
        String begin = "pngs/";
        String end = ".png";
        ImagePNG i = new ImagePNG(begin + str + end); //CHARGE UN IMAGE PNG
        return i;
    }

    //---------------------------------------- CREATE FILE
    ///TODO BLOQUER DELTA ENTRE 0 ET 255 !!!!!!!!!!!!!!!!!!!!!!!!!
    private static void createDeltaFile(int delta, String name, ImagePNG i) throws IOException {
        deltaTree = new QuadTree(i);
        deltaTree.compressDelta(delta);

        deltaTree.saveTXT("SaveTXT/" + name + "-delta" + delta + ".txt");
        deltaTree.savePNG("SavePNG/" + name + "-delta" + delta + ".png");
    }

    ///TODO PHI > 0 !!!!!!!!!!!!!!!!!!!!!!!!!
    private static void createPhiFile(int phi, String name, ImagePNG i) throws IOException {
        phiTree = new QuadTree(i);
        phiTree.compressPhi(phi);

        phiTree.saveTXT("SaveTXT/" + name + "-phi" + phi + ".txt");
        phiTree.savePNG("SavePNG/" + name + "-phi" + phi + ".png");

    }

    //---------------------------------------- EQM DISPLAY
    public static String displayEQM(QuadTree tree) throws IOException {
        String displayEQM = "";

        displayEQM = "\nECART QUATRADIQUE MOYEN : " + tree.EQM() + "%";

        return displayEQM;
    }

    //------------------------------------------------------------ MAIN
    // TODO ****************************************************************************************************
    //TODO ENREGISTRER LES SYSTEM DANS UNE VARIABLE
    // FAIRE SOU FONCTION DE CE MAIN ET RENVOYER LE STRING POUR AFFICHAGE DANS LE MAIN
    public static void main(String[] args) throws Exception {

        //Begin
        if ( args.length == 0 ) { //If we are in interactive mode
            welcome();

        } else if ( args.length == 3 ) { //If we are in noninteractive mode

            System.out.println("Chargement de l'image...");
            ImagePNG i = loadImage(args[0]); //TODO VERIFIER EXCEPTION BON NOM DE FICHIER

            int delta = Integer.parseInt(args[1]);
            int phi = Integer.parseInt(args[2]);
            System.out.println("\nCréation des fichiers \n"
                               + "DELTA : SaveTXT/" + args[0] + "-delta" + delta + ".txt" + " et " + "SavePNG/" + args[0] + "-delta" + delta + ".png"
                               + "PHI :   SaveTXT/ " + args[0] + "-phi" + phi + ".txt" + " et " +  "SavePNG/" + args[0] + "-phi" + phi + ".png" );

            createDeltaFile(delta, args[0], i ); //creation of delta PNG and text files
            createPhiFile( phi, args[0], i); //creation of phi PNG and text files


            System.out.println("\n Comparaison fichiers Delta : " + displayEQM(deltaTree));
            System.out.println("\n Comparaison fichiers Phi : " + displayEQM(phiTree));

        } else {
            throw new Exception("Le nombre d'argument n'est pas le bon ! ");
        }

    }


    public static void loadImage(boolean b) throws IOException {
        Scanner scan = new Scanner(System.in);

        System.out.println("Veuillez entrer le nom du fichier à tester. Pour cela, entrez uniquement le nom du fichier sans son extention.");
        ///TODO EXPCETION VERIF BON NOM DE FICHIER +
        ///TODO VOIR SI ON APPELLE CETTE FONCTION DANS LE MODE NN INTERACTIF

        try {
            String fileName = scan.next();

            String begin = "pngs/";
            String end = ".png";


            ImagePNG i = new ImagePNG(begin+fileName+end); //CHARGE UN IMAGE PNG

            tree = new QuadTree(i);
            System.out.println("\nAFFICHAGE ARBRE DE LA PHOTO : " + fileName + "\n" );
            b = true;
            System.out.println(tree.toString());

        }
        catch(Exception e ){
            System.out.println("Le nom du fichier est incorrect ! ");

            throw e;
        } finally {
            if(b == false ){
                loadImage(b);
            }
        }
    }

    public static void compressDelta() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Choissisez votre delta pour la compression : ");
        int delta = scan.nextInt();
        tree.compressDelta(delta);
        //scan.close();
    }

    public static void compressPhi() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Choissisez votre phi pour la compression : ");
        int phi = scan.nextInt();
        tree.compressPhi(phi);
        //scan.close();
    }

    private static void savePNG() throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Choissisez votre nom de fichier PNG : ");
        String namePNG = scan.next();
        tree.savePNG("SavePNG/" + namePNG + ".png");
    }

    private static void saveTXT() throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Choissisez votre nom de fichier TXT : ");
        String nameTXT = scan.next();
        tree.savePNG("SavePNG/" + nameTXT + ".png");
    }
}
