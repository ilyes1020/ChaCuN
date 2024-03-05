package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static java.util.List.of;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public class MyAreaTest {

    @Test
    public void sortingOccupantsTest() {
        Area<Zone> area = new Area<>(null, List.of(PlayerColor.BLUE, PlayerColor.RED, PlayerColor.GREEN), 1);
    }
}
