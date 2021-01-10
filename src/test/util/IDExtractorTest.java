package test.util;

import main.data.IDNumbers;
import main.exception.ProvisioningException;
import main.util.IDExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class IDExtractorTest {

    private IDExtractor idExtractor;

    @BeforeEach
    public void setup() {
        idExtractor = new IDExtractor("./testData/sampleID.txt");
    }

    @Test
    public void testRetrieveIDsStartOfList() {
        try {
            IDNumbers idNums = idExtractor.extractIDs("asdf", "lolol");
            assertEquals(1928371, idNums.getAccountID());
            assertEquals(345231, idNums.getCharID());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testRetrieveIDsMidList() {
        try {
            IDNumbers idNums = idExtractor.extractIDs("zxcvb", "jeicn");
            assertEquals(2984123, idNums.getAccountID());
            assertEquals(139841, idNums.getCharID());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testRetrieveIdsEndOfList() {
        try {
            IDNumbers idNums = idExtractor.extractIDs("qwerty", "lolol");
            assertEquals(9812498, idNums.getAccountID());
            assertEquals(1383109, idNums.getCharID());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUserNotFoundInList() {
        try {
            idExtractor.extractIDs("notAUser", "notAClass");
            fail();
        } catch (ProvisioningException e) {
            //Check that the account character is not provisioned, should not be present in list
            System.out.println("success");
        } catch (Exception e) {
            //File should be valid
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMalformedLineRead() {
        try {
            idExtractor.extractIDs("malformed", "line");
            fail();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
