import ch.epfl.chacun.Base32;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public class MyBase32Test {

    @Test
    void isValidWorks() {
        assertFalse(Base32.isValid("1"));
        assertFalse(Base32.isValid("@"));
        assertTrue(Base32.isValid("2"));
        assertTrue(Base32.isValid("abcdefg"));
    }

    @Test
    void encodeBits5Works() {
        assertEquals("J", Base32.encodeBits5(9));
        assertEquals("7", Base32.encodeBits5(31));
        assertEquals("A", Base32.encodeBits5(0));
    }

    @Test
    void encodeBits5Throws() {
        assertThrows(IllegalArgumentException.class, () -> Base32.encodeBits5(32));
        assertThrows(IllegalArgumentException.class, () -> Base32.encodeBits5(-5));
    }

    @Test
    void encodeBits10Works() {
        assertEquals("CH", Base32.encodeBits10(71));
        assertEquals("EW", Base32.encodeBits10(150));
        assertEquals("A7", Base32.encodeBits10(31));
        assertEquals("AA", Base32.encodeBits10(0));
        assertEquals("6U", Base32.encodeBits10(980));
    }

    @Test
    void encodeBits10Throws() {
        assertThrows(IllegalArgumentException.class, () -> Base32.encodeBits10(1024));
        assertThrows(IllegalArgumentException.class, () -> Base32.encodeBits10(-5));
    }

    @Test
    void decodeWorks() {
        assertEquals(150, Base32.decode("EW"));
        assertEquals(980, Base32.decode("6U"));
        assertEquals(0, Base32.decode("AA"));
        assertEquals(31, Base32.decode("A7"));
        assertEquals(71, Base32.decode("CH"));
    }

    @Test
    void decodeThrows() {
        assertThrows(IllegalArgumentException.class, () -> Base32.decode("ew"));
        assertThrows(IllegalArgumentException.class, () -> Base32.decode("ABC"));
    }
}