package test.util;


import main.exception.BadOrderException;
import main.exception.InvalidCommandLineException;
import main.util.ArgReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ArgReaderTest {

    ArgReader argReader;

    @Test
    public void testConstructorValid() {
        argReader = new ArgReader(new String[]{"-acc", "MGamer5525_Pown", "-T", "-id", "~/src/home/micha/lol"});
        assertEquals("MGamer5525_Pown", argReader.getAccountName());
        assertEquals("Titan", argReader.getGuardianClass());
        assertEquals("~/src/home/micha/lol", argReader.getIdFilePath());
    }

    @Test
    public void testConstructorAccountNameOptionAtEnd() {
        try {
            new ArgReader(new String[]{"-id", "~/src/home/micha", "MGamer5525_Pown", "-T", "-acc"});
            fail();
        } catch (BadOrderException e) {
            System.out.println("success");
        }
    }

    @Test
    public void testConstructorMoreThanOneCharClass() {
        try {
            new ArgReader(new String[]{"-acc", "MGamer5525_Pown", "-T", "-H", "-W", "-id", "~/src/home/micha"});
            fail();
        } catch (InvalidCommandLineException e) {
            System.out.println("success");
        }
    }

    @Test
    public void testConstructorMissingGuardianClass() {
        try {
            new ArgReader(new String[]{"-acc", "MGamer5525_Pown", "-id", "~/path"});
            fail();
        } catch (InvalidCommandLineException e) {
            System.out.println("success");
        }
    }

    @Test
    public void testConstructorTooManyArguments() {
        try {
            new ArgReader(new String[]{"--accountName", "MGamer5525_Pown", "-H", "1060", "1060", "1060", "1200"});
            fail();
        } catch (InvalidCommandLineException e) {
            System.out.println("success");
        }
    }

    @Test
    public void testConstructorMissingFilePath() {
        try {
            new ArgReader(new String[]{"--accountName", "MGamer5525_Pown", "-W"});
            fail();
        } catch (InvalidCommandLineException e) {
            System.out.println("success");
        }
    }

    @Test
    public void testConstructorFilePathFlagAtEnd() {
        try {
            new ArgReader(new String[]{"--accountName", "MGamer5525_Pown", "-H", "~/path", "--idFilePath"});
            fail();
        } catch (BadOrderException e) {
            System.out.println("success");
        }
    }


}
