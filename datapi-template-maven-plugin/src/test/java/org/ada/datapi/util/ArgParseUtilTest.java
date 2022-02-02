package org.ada.datapi.util;

import com.google.common.collect.Lists;
import org.ada.datapi.data.Arg;
import org.ada.datapi.exception.DatapiException;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class ArgParseUtilTest {

    @Test
    public void testCannotInstantiate() throws Exception {
        assertTrue(TestUtil.testUtilClass(ArgParseUtil.class));
    }

    @Test(expected = ArgumentParserException.class)
    public void getArgParser_empty() throws Exception {
        ArgumentParser parser = ArgParseUtil.getArgParser();

        parser.parseArgs(new String[] {""});
    }

    @Test(expected = ArgumentParserException.class)
    public void getArgParser_wrong_gen_type() throws Exception {
        ArgumentParser parser = ArgParseUtil.getArgParser();

        parser.parseArgs(new String[] {"x", "scaffold", "Something", "x:int"});
    }

    @Test(expected = ArgumentParserException.class)
    public void getArgParser_wrong_gen_scope() throws Exception {
        ArgumentParser parser = ArgParseUtil.getArgParser();

        parser.parseArgs(new String[] {"g", "blah", "Something", "x:int"});
    }

    @Test
    public void getArgParser() throws Exception {
        ArgumentParser parser = ArgParseUtil.getArgParser();

        Namespace parsedArgs = parser.parseArgs(new String[] {"g", "scaffold", "-k", "z:string", "-d", "somewhere", "Something", "x:int", "y:date"});

        assertThat(parsedArgs.get(Arg.MODE.key), equalTo("g"));
        assertThat(parsedArgs.get(Arg.MODE_SCOPE.key), equalTo("scaffold"));
        assertThat(parsedArgs.get(Arg.CLASS_NAME.key), equalTo("Something"));
        assertThat(parsedArgs.get(Arg.FIELD.key), equalTo(Lists.newArrayList("x:int", "y:date")));
        assertThat(parsedArgs.get(Arg.KEY.key), equalTo("z:string"));
        assertThat(parsedArgs.get(Arg.DIR.key), equalTo("somewhere"));
    }

    @Test(expected = DatapiException.class)
    public void validateParsedArgs_weird_class_name() throws Exception {
        ArgumentParser parser = ArgParseUtil.getArgParser();

        ArgParseUtil.validateParsedArgs(parser.parseArgs(new String[] {"g", "scaffold", "Something*Weird", "x:int"}));

    }

    @Test(expected = DatapiException.class)
    public void validateParsedArgs_invalid_name_type_pair() throws Exception {
        ArgumentParser parser = ArgParseUtil.getArgParser();

        ArgParseUtil.validateParsedArgs(parser.parseArgs(new String[] {"g", "scaffold", "SomeClass", "x-int"}));
    }

    @Test(expected = DatapiException.class)
    public void validateParsedArgs_invalid_custom_path() throws Exception {
        ArgumentParser parser = ArgParseUtil.getArgParser();

        ArgParseUtil.validateParsedArgs(parser.parseArgs(new String[] {"g", "scaffold", "-d", "this+is-aWrongPath", "SomeClass", "x:int"}));
    }

    @Test(expected = DatapiException.class)
    public void validateParsedArgs_invalid_name_type_pair_id() throws Exception {
        ArgumentParser parser = ArgParseUtil.getArgParser();

        ArgParseUtil.validateParsedArgs(parser.parseArgs(new String[] {"g", "scaffold", "SomeClass", "x:int", "-k", "x int"}));
    }

    @Test
    public void validateParsedArgs() throws Exception {
        ArgumentParser parser = ArgParseUtil.getArgParser();

        ArgParseUtil.validateParsedArgs(parser.parseArgs(new String[] {"g", "scaffold", "SomeClass", "x:int", "-k", "y:int", "-d", "z/somewhere"}));
    }

}