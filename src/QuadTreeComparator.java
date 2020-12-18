import java.util.Comparator;

public class QuadTreeComparator implements Comparator<QuadTree> {

    //TODO Faire Exeption du cas ou 01 ou 02 n'est pas père de 4 feuilles.
    @Override
    public int compare(QuadTree o1, QuadTree o2) {
        if (o1.verificationBound() && o2.verificationBound()) {
            int delta1 = o1.maxColorimetricDifference();
            int delta2 = o2.maxColorimetricDifference();

            if (delta1 < delta2) {
                return -1;
            } else if (delta1 > delta2) {
                return 1;
            } else if (o1 == o2) {
                return 0;
            }
        }

        return -1;
    }
}
