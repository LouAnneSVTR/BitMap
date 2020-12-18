import java.util.Comparator;

public class QuadTreeComparator implements Comparator<QuadTree> {

    @Override
    public int compare(QuadTree o1, QuadTree o2) {
        if (o1.verificationBound() && o2.verificationBound()) {
            int delta1 = o1.maxColorimetricDifference();
            int delta2 = o2.maxColorimetricDifference();

            if (delta1 < delta2) {
                return -1;
            } else {
                return 1;
            }
        }
        return 0;
    }
}
