

public class Main {

    public static void main(String[] args) throws Exception {
        //Menu menuProgramme = new Menu();
        //menuProgramme.programm(args);

        long testCreationArbre = System.currentTimeMillis();
        QuadTree t = new QuadTree("1024-cube");
        System.out.println("creation : " + (System.currentTimeMillis() - testCreationArbre) / 1000.0);


        /*QuadTree t2 = new QuadTree("1024-cube");
        QuadTree t3 = new QuadTree("1024-cube");

        long testCompressionDelta = System.currentTimeMillis();
        t2.compressDelta(128);
        System.out.println("Delta : " + (System.currentTimeMillis() - testCompressionDelta) / 1000.0);
*/
        long testCompressionPhi = System.currentTimeMillis();
        t.compressPhi(150);
        System.out.println("Phi : " + (System.currentTimeMillis() - testCompressionPhi) / 1000.0);
/*
        long testSavePNG = System.currentTimeMillis();
        t.savePNG("test.png");
        System.out.println("savePNG : " + (System.currentTimeMillis() - testSavePNG) / 1000.0);

        long testSaveTXT = System.currentTimeMillis();
        t.saveTXT("test");
        System.out.println("saveTXT : " + (System.currentTimeMillis() - testSaveTXT) / 1000.0);

        long testToString = System.currentTimeMillis();
        t.toString();
        System.out.println("toString : " + (System.currentTimeMillis() - testToString) / 1000.0);*/




    }
}