package org.ada.datapi.domain;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TypeDeclarationTest {

    public static TypeDeclaration buildTypeDeclaration(String className) {
        return new TypeDeclaration(className, Lists.newArrayList(new Type(className, new Dependency(className))));
    }


    @Test
    public void getTemplate() throws Exception {
        String templateString = "List<Integer>";

        TypeDeclaration declaration = buildTypeDeclaration("List<Integer>");
        assertThat(declaration.getTemplate(), equalTo(templateString));
    }

    @Test
    public void constructor_normalizes_name() throws Exception {
        String templateString = "Map<List<    Integer    >,Integer>";

        TypeDeclaration declaration = new TypeDeclaration(templateString, Lists.newArrayList());

        assertThat(declaration.getDeclaration(), equalTo("Map<List<Integer>, Integer>"));
    }

}