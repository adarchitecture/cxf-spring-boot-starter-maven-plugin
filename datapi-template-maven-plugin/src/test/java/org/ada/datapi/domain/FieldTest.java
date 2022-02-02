package org.ada.datapi.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FieldTest {

    @Test
    public void getTemplate() throws Exception {
        TypeDeclaration declaration = TypeDeclarationTest.buildTypeDeclaration("Class");

        Field field = new Field(declaration, "varName");

        assertThat(field.getTemplate(), equalTo("\tprivate Class varName;"));
    }

}