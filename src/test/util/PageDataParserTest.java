package test.util;

import main.util.PageDataParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class PageDataParserTest {

    private PageDataParser defaultParser;
    private PageDataParser invalidParser;

    @BeforeEach
    public void setup() {
        defaultParser = new PageDataParser("auronflk1212</div></div></div></a> aisdoinds3232</div></div></div></a>" +
                "asddoicicnrownf3422</div></div></div></a>ainovaoirenv1321</div></div></div></a>elijvnslef3940" +
                "</div></div></div></a>9284huefvjdsv1200</div></div></div></a>uivrvnreionf2442</div></div></div>" +
                "</a>unfdibseroivnreubsuonbfd9999</div></div></div></a>iweondc<spanclass=\"power-symbol\">&#x2727;" +
                "</span>6372eunveroif");

        invalidParser = new PageDataParser("onerovineoirnieorsfkdmv1231soinvdlfs43r4oiernv2094t0g9rnv02)(*)NEVW");
    }

    @Test
    public void checkValidParse() {
        int[] results = defaultParser.obtainStats();
        Assertions.assertEquals(1212, results[0]);
        Assertions.assertEquals(3232, results[1]);
        Assertions.assertEquals(3422, results[2]);
        Assertions.assertEquals(1321, results[3]);
        Assertions.assertEquals(3940, results[4]);
        Assertions.assertEquals(1200, results[5]);
        Assertions.assertEquals(2442, results[6]);
        Assertions.assertEquals(9999, results[7]);

        Assertions.assertEquals(6372, defaultParser.obtainPowerLevel());
    }

    @Test
    public void testInvalidParse() {
        try {
            invalidParser.obtainStats();
            fail();
        } catch (StringIndexOutOfBoundsException ignored) {}
    }
}
