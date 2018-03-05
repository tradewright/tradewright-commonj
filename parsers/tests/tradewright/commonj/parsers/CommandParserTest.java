/*
 
The MIT License (MIT)

Copyright (c) 2018 Richard L King (TradeWright Software Systems)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

 */

package tradewright.commonj.parsers;

import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandParserTest {
    private final String COMMAND1 = "arg1 arg2 /loglevel:H \"arg3a arg3b arg3c\" /B: /C:\"Wiggly woo\" /D:D:\"\\My Folder\"";
    private final String COMMAND2 = "arg1 arg2 -loglevel:H -- ~docs/wiggly -A -B -C";
    private final String COMMAND3 = "arg1, arg2, a:1st, b:2nd, arg3, c:3rd";
    private final String COMMAND4 = "name=Jane Doe ,  age=41,   address=\"123 Railway Cuttings, Camberwick Green\"";

    private final char ARGUMENT_SEPARATOR_SPACE = ' ';
    private final char ARGUMENT_SEPARATOR_COMMA = ',';
    private final char SWITCH_PREFIX_HYPHEN = '-';
    private final char SWITCH_PREFIX_SLASH = '/';
    private final char VALUE_SEPARATOR_COLON = ':';
    private final char VALUE_SEPARATOR_EQUALS = '=';
    
    public CommandParserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }

    /*
     * Tests using COMMANDLINE1 -----------------------------------------------------------------------------------------
    */

    @Test
    public void testGetArg1_1() {
        System.out.println("getArg1_1");
        int index = 0;
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        String expResult = "arg1";
        String result = instance.getArg(index);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetArg1_2() {
        System.out.println("getArg1_2");
        int index = 1;
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        String expResult = "arg2";
        String result = instance.getArg(index);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetArg1_3() {
        System.out.println("getArg1_3");
        int index = 2;
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        String expResult = "arg3a arg3b arg3c";
        String result = instance.getArg(index);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetArgs1() {
        System.out.println("getArgs1");
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        ArrayList<String> expResult = new ArrayList<>();
        expResult.add("arg1");
        expResult.add("arg2");
        expResult.add("arg3a arg3b arg3c");
        ArrayList<String> result = instance.getArgs();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetNumberOfArgs1() {
        System.out.println("getNumberOfArgs1");
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        int expResult = 3;
        int result = instance.getNumberOfArgs();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetNumberOfSwitches1() {
        System.out.println("getNumberOfSwitches1");
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        int expResult = 4;
        int result = instance.getNumberOfSwitches();
        assertEquals(expResult, result);
    }

    @Test
    public void testIsSwitchSet1_1() {
        System.out.println("isSwitchSet1_1");
        String name = "loglevel";
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        boolean expResult = true;
        boolean result = instance.isSwitchSet(name);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsSwitchSet1_2() {
        System.out.println("isSwitchSet1_2");
        String name = "B";
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        boolean expResult = true;
        boolean result = instance.isSwitchSet(name);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsSwitchSet1_3() {
        System.out.println("isSwitchSet1_3");
        String name = "C";
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        boolean expResult = true;
        boolean result = instance.isSwitchSet(name);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSwitches1() {
        System.out.println("getSwitches1");
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        ArrayList<CommandParser.SwitchEntry> expResult = new ArrayList<>();
        expResult.add(new CommandParser.SwitchEntry("loglevel:H", String.valueOf(VALUE_SEPARATOR_COLON)));
        expResult.add(new CommandParser.SwitchEntry("B", String.valueOf(VALUE_SEPARATOR_COLON)));
        expResult.add(new CommandParser.SwitchEntry("C:\"Wiggly woo\"", String.valueOf(VALUE_SEPARATOR_COLON)));
        expResult.add(new CommandParser.SwitchEntry("D:D:\"\\My Folder\"", String.valueOf(VALUE_SEPARATOR_COLON)));
        ArrayList<CommandParser.SwitchEntry> result = instance.getSwitches();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSwitchIndex1_1() {
        System.out.println("getSwitchIndex1_1");
        String name = "loglevel";
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        int expResult = 0;
        int result = instance.getSwitchIndex(name);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSwitchIndex1_2() {
        System.out.println("getSwitchIndex1_2");
        String name = "B";
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        int expResult = 1;
        int result = instance.getSwitchIndex(name);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSwitchIndex1_3() {
        System.out.println("getSwitchIndex1_3");
        String name = "C";
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        int expResult = 2;
        int result = instance.getSwitchIndex(name);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSwitchValue1_1() {
        System.out.println("getSwitchValue1_1");
        String name = "loglevel";
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        String expResult = "H";
        String result = instance.getSwitchValue(name);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetSwitchValue1_2() {
        System.out.println("getSwitchValue1_2");
        String name = "B";
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        String expResult = "";
        String result = instance.getSwitchValue(name);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetSwitchValue1_3() {
        System.out.println("getSwitchValue1_3");
        String name = "C";
        CommandParser instance = CommandParser.getBuilder(COMMAND1)
                .setSwitchPrefix(SWITCH_PREFIX_SLASH)
                .setCaseSensitive(true)
                .build();
        String expResult = "Wiggly woo";
        String result = instance.getSwitchValue(name);
        assertEquals(expResult, result);
    }
    
    /*
     * Tests using COMMANDLINE2 -----------------------------------------------------------------------------------------
    */

    @Test
    public void testGetNumberOfArgs2() {
        System.out.println("getNumberOfArgs2");
        CommandParser instance = CommandParser.getBuilder(COMMAND2)
                .setCaseSensitive(true)
                .build();
        int expResult = 6;
        int result = instance.getNumberOfArgs();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetNumberOfSwitches2() {
        System.out.println("getNumberOfSwitches2");
        CommandParser instance = CommandParser.getBuilder(COMMAND2)
                .setCaseSensitive(true)
                .build();
        int expResult = 1;
        int result = instance.getNumberOfSwitches();
        assertEquals(expResult, result);
    }

    /*
     * Tests using COMMANDLINE3 -----------------------------------------------------------------------------------------
    */

    @Test
    public void testGetNumberOfArgs3() {
        System.out.println("getNumberOfArgs3");
        CommandParser instance = CommandParser.getBuilder(COMMAND3)
                .setArgumentSeparator(ARGUMENT_SEPARATOR_COMMA)
                .setSwitchPrefix(CommandParser.SWITCH_PREFIX_NULL)
                .setCaseSensitive(true)
                .build();
        int expResult = 3;
        int result = instance.getNumberOfArgs();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetNumberOfSwitches3() {
        System.out.println("getNumberOfSwitches3");
        CommandParser instance = CommandParser.getBuilder(COMMAND3)
                .setArgumentSeparator(ARGUMENT_SEPARATOR_COMMA)
                .setSwitchPrefix(CommandParser.SWITCH_PREFIX_NULL)
                .setCaseSensitive(true)
                .build();
        int expResult = 3;
        int result = instance.getNumberOfSwitches();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSwitches3() {
        System.out.println("getswitches3");
        CommandParser instance = CommandParser.getBuilder(COMMAND3)
                .setArgumentSeparator(ARGUMENT_SEPARATOR_COMMA)
                .setSwitchPrefix(CommandParser.SWITCH_PREFIX_NULL)
                .setCaseSensitive(true)
                .build();
        ArrayList<CommandParser.SwitchEntry> expResult = new ArrayList<>();
        expResult.add(new CommandParser.SwitchEntry("a:1st", String.valueOf(VALUE_SEPARATOR_COLON)));
        expResult.add(new CommandParser.SwitchEntry("b:2nd", String.valueOf(VALUE_SEPARATOR_COLON)));
        expResult.add(new CommandParser.SwitchEntry("c:3rd", String.valueOf(VALUE_SEPARATOR_COLON)));
        ArrayList<CommandParser.SwitchEntry> result = instance.getSwitches();
        assertEquals(expResult, result);
    }

    /*
     * Tests using COMMANDLINE4 -----------------------------------------------------------------------------------------
    */

    @Test
    public void testGetNumberOfArgs4() {
        System.out.println("getNumberOfArgs4");
        CommandParser instance = CommandParser.getBuilder(COMMAND4)
                .setArgumentSeparator(ARGUMENT_SEPARATOR_COMMA)
                .setSwitchPrefix(CommandParser.SWITCH_PREFIX_NULL)
                .setValueSeparator(VALUE_SEPARATOR_EQUALS)
                .setCaseSensitive(true)
                .build();
        int expResult = 0;
        int result = instance.getNumberOfArgs();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetNumberOfSwitches4() {
        System.out.println("getNumberOfSwitches4");
        CommandParser instance = CommandParser.getBuilder(COMMAND4)
                .setArgumentSeparator(ARGUMENT_SEPARATOR_COMMA)
                .setSwitchPrefix(CommandParser.SWITCH_PREFIX_NULL)
                .setValueSeparator(VALUE_SEPARATOR_EQUALS)
                .setCaseSensitive(true)
                .build();
        int expResult = 3;
        int result = instance.getNumberOfSwitches();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSwitches4() {
        System.out.println("getswitches4");
        CommandParser instance = CommandParser.getBuilder(COMMAND4)
                .setArgumentSeparator(ARGUMENT_SEPARATOR_COMMA)
                .setSwitchPrefix(CommandParser.SWITCH_PREFIX_NULL)
                .setValueSeparator(VALUE_SEPARATOR_EQUALS)
                .setCaseSensitive(true)
                .build();
        ArrayList<CommandParser.SwitchEntry> expResult = new ArrayList<>();
        expResult.add(new CommandParser.SwitchEntry("name=Jane Doe", String.valueOf(VALUE_SEPARATOR_EQUALS)));
        expResult.add(new CommandParser.SwitchEntry("age=41", String.valueOf(VALUE_SEPARATOR_EQUALS)));
        expResult.add(new CommandParser.SwitchEntry("address=\"123 Railway Cuttings, Camberwick Green\"", String.valueOf(VALUE_SEPARATOR_EQUALS)));
        ArrayList<CommandParser.SwitchEntry> result = instance.getSwitches();
        assertEquals(expResult, result);
    }

}
