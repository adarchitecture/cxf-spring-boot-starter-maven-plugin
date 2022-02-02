package org.ada.datapi.domain.customization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class JSONMapTest {

    private JSONMap map;
    
    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        String jsonToParse = "{" +
                "    \"glossary\": {" +
                "        \"title\": \"example glossary\"," +
                "    \"GlossDiv\": {" +
                "            \"title\": \"S\"," +
                "      \"GlossList\": {" +
                "                \"GlossEntry\": {" +
                "                    \"ID\": \"SGML\"," +
                "          \"SortAs\": \"SGML\"," +
                "          \"GlossTerm\": \"Standard Generalized Markup Language\"," +
                "          \"Acronym\": \"SGML\"," +
                "          \"Abbrev\": \"ISO 8879:1986\"," +
                "          \"GlossDef\": {" +
                "                        \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\"," +
                "            \"GlossSeeAlso\": [\"GML\", \"XML\"]" +
                "                    }," +
                "          \"GlossSee\": \"markup\"" +
                "                }" +
                "            }" +
                "        }" +
                "    }" +
                "}";

        this.map = new ObjectMapper().readValue(jsonToParse, JSONMap.class);
    }
    
    @Test
    public void getEndValue_null_key() throws Exception {
        assertThat(this.map.getEndValue(null), nullValue());
    }

    @Test
    public void getEndValue_invalid_route() throws Exception {
        assertThat(this.map.getEndValue("this.does.not.exist"), nullValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getEndValue_not_end_of_path() throws Exception {
        this.map.getEndValue("glossary");
    }

    @Test
    public void getEndValue() throws Exception {
        assertThat(this.map.getEndValue("glossary.title"), equalTo("example glossary"));
    }

}