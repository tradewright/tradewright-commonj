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
import java.util.Objects;

/**
* <p>
* Provides facilities to determine the number and values of text elements in a 
* string. The elements are interpreted either as arguments (which have 
* positional significance) or switches (which have an explicit identifier).
* </p>
* <p>
* To create an instance of the <code>CommandParser</code> class, use the 
* <code>CommandParser</code>'s static <code>getBuilder</code> method to create 
* a <code>Builder</code> object, passing it the string to be parsed, then use 
* the <code>Builder</code> object's methods to set the required attributes; 
* finally use the <code>Builder</code> object's <code>build</code> method to create the 
* <code>CommandParser</code> object.
* </p>
* 
* <p>
* Typically the supplied string is the arguments part of
* the command used to start an application, but there are many other uses.
* </p>
* 
* <p>
* The format of the string passed to the constructor is as follows:
</p>
* <pre>
*   [&lt;argument&gt; | &lt;switch&gt;] [&lt;element-separator&gt;... (&lt;argument&gt; | &lt;switch&gt;)]...
* </pre>
<p>
 ie, there is a sequence of arguments or switches, separated by one or more 
 element separator characters. The element separator character is specified 
 in the getBuilder constructor.
 </p>
* 
* <p>
* Arguments that contain the element separator character must be enclosed in 
* double quotes. Double quotes appearing within an argument must be repeated.
* </p>
* 
* <p>
* Switches have the following format:
</p>
* <pre>
*   [&lt;switch-prefix&gt;]&lt;identifier&gt;[&lt;value-separator&gt;&lt;switchValue&gt;]
*   
*   OR
* 
*   &lt;switch-prefix&gt;&lt;switch-prefix&gt;
* </pre>
*<p>
 ie a switch starts with a prefix (a single character specified in the getBuilder
 constructor) followed by an identifier, and optionally followed by a value 
 separator character (specified in the getBuilder constructor) followed by the 
 switch value. Switch identifiers must have a letter or digit as their first 
 character, and can be specified to be case-sensitive in the getBuilder 
 constructor. Switch values that contain the element separator character must 
 be enclosed in double quotes. Double quotes appearing within a switch value 
 must be repeated.
 </p>
* <p>
* A null switch prefix can be specified, in which case any 
* element that consists only of:
* </p>
* <pre>
* <code>&lt;identifier&gt;[&lt;value-separator&gt;&lt;switchValue&gt;]</code>
* </pre>
* <p>
* is interpreted as a switch rather than an argument.
* </p>
* <p>
* Alternatively a switch may consist simply of two occurrences of the switch 
* separator character: all elements following this are interpreted as arguments.
* </p>
* 
* <p>Note that the element separator character, the switch prefix character, 
* and the value separator character must all be different.
* </p>
* <b>Examples</b>
* 
* <p>
* Examples 1-3 use a space as the element separator character, a hyphen as 
* the switch prefix character, and a colon as the value separator character:
* </p>
* <p>
* Example 1: contains two arguments and two switches. Note that the first 
* switch is between the two arguments: this does not affect the order of the 
* arguments:
* </p>
* <pre>
*   anArgument -sw1 anotherArg -sw2:42
* </pre>
* <p>
* Example 2: contains a single argument and a single switch. The argument is
* enclosed in double quotes because it contains the argument separator 
* character. Note that the switch contains the switch value separator character,
* but this does not require the switch value to be quoted:
* </p>
* <pre>
*   "C:\Program Files\MyApp\myapp.ini" -out:C:\MyLogs\myapp.Log
* </pre>
* <p>
* Example 3: the last two elements are interpreted as arguments because of the
* -- element, even though the last one looks like a switch:
* </p>
* <pre>
*   -a -out:C:\MyLogs\myapp.Log -- C:"\Program Files\MyApp\myapp.ini" -in:inputFile
* </pre>
* <p>
* Example 4: this example uses a comma as the element separator character, a 
* null switch prefix character, and an equals sign as the value separator 
* character:
* </p>
* <pre>
*   name=Jane Doe,  age=41, address="123 Railway Cuttings, Camberwick Green"
* </pre>
*/
public final class CommandParser {

    private final ArrayList<String> mArgs = new ArrayList<>();
    private final ArrayList<SwitchEntry> mSwitches = new ArrayList<>();
    private final String mArgSep;
    private final String mValueSep;
    private final String mSwitchPrefix;
    private final String mInputString;
    private final boolean mCaseSensitive;
    
    private boolean mTreatRemainingArgsAsArgs;
    
    public static final char SWITCH_PREFIX_NULL = 0;

    /** 
     * Initialises a new instance of the <code>CommandLineParser</code> class.
     * @param inputString
     * The command arguments to be parsed.
     * @param argSeparator
     * A single printable ASCII character used as the separator between command 
     * arguments.
     * @param switchPrefix 
     * A single printable ASCII character used to indicate the start of a 
     * switch. Alternatively SWITCH_PREFIX_NULL may be used to indicate that 
     * there is no switch prefix: in this case, any argument that contains the
     * value separator character as its second or later character is interpreted
     * as a switch.
     * @param valueSeparator
     * A single printable ASCII character used as the separator between option 
     * names and values.
     * @param caseSensitive
     * Specifies whether option names are case-sensitive.
     */
    private CommandParser(Builder builder) {
        mInputString = builder.mInputString.trim();
        mArgSep = String.valueOf(builder.mArgumentSeparatorChar);
        mSwitchPrefix = builder.mSwitchPrefixChar == 0 ? "" : String.valueOf(builder.mSwitchPrefixChar);
        mValueSep = String.valueOf(builder.mValueSeparatorChar);
        mCaseSensitive = builder.mCaseSensitive;
        getAllArgs();
    }
    
    /**
     * Returns an instance of the <code>Builder</code> class that is used to 
     * construct a <code>CommandParser</code> instance.
     * @param inputString
     * A <code>Builder</code> instance.
     * @return 
     * An instance of the <code>Builder</code> class.
     */    
    public static Builder getBuilder(String inputString) {
        return new Builder(inputString);
    }

    /**
     * Gets the argument specified by <code>index</code>.
     * @param index
     * Identifies the argument to return.
     * @return 
     * The requested argument.
     */
    public String getArg(int index) {
        if (index < 0 || index >= mArgs.size()) {
            throw new IllegalArgumentException("index out of range");
        }
        return mArgs.get(index);
    }

    /**
     * Gets a List&lt;String&gt; containing all the arguments.
     * @return 
     * A List&lt;String&gt; containing the arguments.
     */
    public ArrayList<String> getArgs() {
        return mArgs;
    }

    /**
    * Gets the number of arguments.
    * @return
    * The number of arguments.
    */
    public int getNumberOfArgs() {
        return mArgs.size();
    }

    /**
    * Gets the number of switches.
    * @return
    * The number of switches.
    */
    public int getNumberOfSwitches() {
        return mSwitches.size();
    }

    /**
    * Gets a <code>List&lt;SwitchEntry&gt;</code> containing the
    * switch identifiers and values.
    * @return
    * A <code>List&lt;SwitchEntry&gt;</code> containing the
    * switch identifiers and values.
    */
    public ArrayList<SwitchEntry> getSwitches() {
        return mSwitches;
    }

    /**
     * Gets the zero-based index of the specified switch.
     * @param name
     * The name of the required switch.
     * @return 
     * The specified switch's index.
     */
    public int getSwitchIndex(String name) {
        return findSwitch(name);
    }

    /**
     * Gets the value of the specified switch.
     * @param name
     * The name of the required switch.
     * @return 
     * The specified switch's value.
     */
    public String getSwitchValue(String name) {
        int index = findSwitch(name);
        if (index == -1) throw new IllegalArgumentException("Switch is not set");
        return mSwitches.get(index).getValue();
    }

    /**
     * Indicates whether the specified switch is set.
     * @param name
     * The name of the switch.
     * @return 
     * <code>true</code> if the switch is set, otherwise <code>false</code>
     */
    public boolean isSwitchSet(String name) {
        return findSwitch(name) != -1;
    }

    private void addSwitchOrArg(String value) {
        if ((mSwitchPrefix + mSwitchPrefix).equals(value)) {
            // an argument consisting only of two switch prefix characters
            // means that none of the remainder of the command is interpeted
            // as switches
            mTreatRemainingArgsAsArgs = true;
            return;
        }
        
        if (mTreatRemainingArgsAsArgs) {
            mArgs.add(trimQuotes(value));
        } else if ("".equals(mSwitchPrefix) &&
                value.length() > 2 && 
                Character.isLetterOrDigit(value.charAt(0)) &&
                value.indexOf(mValueSep) >= 1 ) {
            mSwitches.add(new SwitchEntry(value, mValueSep));
        } else if (!"".equals(mSwitchPrefix) && 
                value.startsWith(mSwitchPrefix) && 
                value.length() > 1) {
            mSwitches.add(new SwitchEntry(value.substring(1), mValueSep));
        } else {
            mArgs.add(trimQuotes(value));
        }
    }

    static boolean containsUnbalancedQuotes(String inString) {
        int num = inString.lastIndexOf("\"");
        boolean flag = false;
        for (; num != -1; num = inString.lastIndexOf("\"", num - 1)) {
            flag = !flag;
            if (num == 0) {
                break;
            }
        }
        return flag;
    }

    private int findSwitch(String name) {
        int index = 0;
        while (index <= mSwitches.size() - 1) {
            String s = mSwitches.get(index).getName();
            if ((mCaseSensitive ? s.equals(name) : s.equalsIgnoreCase(name))) {
                return index;
            }
            ++index;
        }
        return -1;
    }

    private void getAllArgs() {
        if ("".equals(mInputString)) {
            return;
        }
        String partialArg = "";
        
        for (String argument : mInputString.split(mArgSep)){
            if ("".equals(argument) && "".equals(partialArg) && " ".equals(mArgSep)) {
                //discard spaces when the separator is a space and we don't have unbalanced quotes
            } else {
                if (!"".equals(partialArg)) {
                    partialArg += mArgSep;
                }
                partialArg += argument;
                if (!containsUnbalancedQuotes(partialArg)) {
                    addSwitchOrArg(partialArg.trim());
                    partialArg = "";
                }
            }
        }

        if (!"".equals(partialArg)) {
            addSwitchOrArg(partialArg);
        }
    }

    static boolean isPrintableAscii(char c) {
      return c >= 32 && c < 126;
    }

    static String trimQuotes(String value){
        if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            return value.substring(1, value.length() - 1);
        } else {
            return value;
        }
    }

    /**
     * Contains details of a command switch.
     * 
     */
    public static class SwitchEntry {

        private final String mName;
        private final String mValue;

        private final String mValueSeparator;

        /**
         * Initialises a <code>SwitchEntry</code> with the specified name
         * and value taken from the <code>input</code> argument.
         * @param input
         * Specifies the name and value for this <code>SwitchEntry</code>
         * instance. This must be of the form:
         * <pre>
         * &lt;name&gt;&lt;sep&gt;&lt;value&gt;
         * </pre>
         * where <code>sep</code> is as specified in the 
         * <code>valueSeparator</code> argument.
         * @param valueSeparator 
         * The character that separates the name from the value.
         */
        SwitchEntry(String input, String valueSeparator) {
            mValueSeparator = valueSeparator;
            int i = input.indexOf(valueSeparator);
            if (i == 0) throw new IllegalArgumentException("Malformed switch: " + input);
            if (i > 0) {
                mName = input.substring(0, i);
                mValue = trimQuotes(input.substring(i + 1));
            } else {
                mName = input;
                mValue = "";
            }
        }

        /**
         * Gets the switch's Name.
         * @return 
         * The switch's Name.
         */
        public String getName() {
            return mName;
        }

        /**
         * Gets the switch's Value.
         * @return 
         * The switch's Value.
         */
        public String getValue() {
            return mValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return false;
            if (o == null) return false;
            if (getClass() != o.getClass()) return false;
            SwitchEntry s = (SwitchEntry) o;
            return Objects.equals(mName, s.getName()) &&
                    Objects.equals(mValue, s.getValue());
        }

        @Override
        public int hashCode() {
            int hash = 13;
            hash = 31 * hash + (null == mName ? 0 : mName.hashCode());
            hash = 31 * hash + (null == mValue ? 0 : mValue.hashCode());
            return hash;
        }

        @Override
        public String toString() {
            return mName + mValueSeparator + mValue;
        }

    }
    
    /**
     * Provides a means to construct an instance of the 
     * <code>CommandParser</code> class.
     */
    public static class Builder {
        
        private final String mInputString;

        private char mArgumentSeparatorChar = ' ';
        private char mSwitchPrefixChar = '-';
        private char mValueSeparatorChar = ':';
        private boolean mCaseSensitive = false;

        Builder(String inputString) {
            mInputString = inputString;
        }

        /**
         * Creates an instance of <code>CommandParser</code> initialised with
         * the values specified by the other <code>Builder</code> methods.
         * @return 
         * An instance of <code>CommandParser</code>.
         */
        public CommandParser build() {
        if (mArgumentSeparatorChar == mSwitchPrefixChar || 
                mArgumentSeparatorChar == mValueSeparatorChar || 
                mSwitchPrefixChar == mValueSeparatorChar)  throw new IllegalArgumentException("argumentSeparator, switchPrefix and valueSeparator must all be different");
            return new CommandParser(this);
        }

        /**
         * Specifies the argument separator character. This must be a printable
         * ASCII character and must be different from the switch prefix 
         * character and the value separator character at the time the 
         * <code>Build</code> method is called. The default value is ' ' 
         * (space).
         * @param argumentSeparator
         * The argument separator character.
         * @return 
         * The current <code>Builder</code> instance.
         */
        public Builder setArgumentSeparator(char argumentSeparator) {
            if (!isPrintableAscii(argumentSeparator)) throw new IllegalArgumentException("argumentSeparator must be printable char");
            mArgumentSeparatorChar = argumentSeparator;
            return this;
        }

        public Builder setCaseSensitive(boolean caseSensitive) {
            mCaseSensitive = caseSensitive;
            return this;
        }

        /**
         * Specifies the switch prefix character. This must be a printable
         * ASCII character and must be different from the argument separator 
         * character and the value separator character at the time the 
         * <code>Build</code> method is called. The default value is '-' 
         * (hyphen). 
         * <p>
         * If the value 0 is supplied, this indicates that there is 
         * no switch prefix, and elements of the form 
         * </p>
         * <pre>
         * <code>&lt;identifier&gt;[&lt;value-separator&gt;&lt;switchValue&gt;]</code>
         * </pre>
         * <p>
         * are interpreted as switches rather than arguments.
         * </p>
         * @param switchPrefix
         * The switch prefix character.
         * @return 
         * The current <code>Builder</code> instance.
         */
        public Builder setSwitchPrefix(char switchPrefix) {
            if (!isPrintableAscii(switchPrefix) & switchPrefix != 0) throw new IllegalArgumentException("switchPrefix must be printable char or 0");
            mSwitchPrefixChar = switchPrefix;
            return this;
        }

        /**
         * Specifies the value separator character. This must be a printable
         * ASCII character and must be different from the switch prefix 
         * character and the argument separator character at the time the 
         * <code>Build</code> method is called. The default value is ':' 
         * (colon).
         * @param valueSeparator
         * The value separator character.
         * @return 
         * The current <code>Builder</code> instance.
         */
        public Builder setValueSeparator(char valueSeparator) {
            if (!isPrintableAscii(valueSeparator)) throw new IllegalArgumentException("valueSeparator must be printable char");
            mValueSeparatorChar = valueSeparator;
            return this;
        }

    }
}

