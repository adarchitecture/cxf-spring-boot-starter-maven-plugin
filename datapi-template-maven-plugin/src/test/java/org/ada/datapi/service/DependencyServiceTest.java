package org.ada.datapi.service;

import com.google.common.collect.Lists;
import org.ada.datapi.domain.Dependency;
import org.ada.datapi.exception.DatapiException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DependencyServiceTest {

    @Mock
    private SearchService searchService;

    @InjectMocks
    private DependencyService classUnderTest;

    @Before
    public void setUp() {
        initMocks(this);

        this.classUnderTest = new DependencyService(searchService);
    }

    @Test
    public void fetchCustomDependencies_null() throws Exception {
        assertThat(this.classUnderTest.fetchDependency(null), equalTo(null));
    }

    @Test
    public void fetchCustomDependencies_empty() throws Exception {
        assertThat(this.classUnderTest.fetchDependency(""), equalTo(null));
    }

    @Test
    public void fetchCustomDependencies_stored_no_deps() throws Exception {
        assertThat(this.classUnderTest.fetchDependency("int"), equalTo(null));
    }

    @Test
    public void fetchCustomDependencies_stored() throws Exception {
        assertThat(this.classUnderTest.fetchDependency("List").getPath(), equalTo("java.util.List"));
    }

    @Test(expected = DatapiException.class)
    public void fetchCustomDependencies_custom_too_many() throws Exception {
        String customClassName = "Something";

        when(this.searchService.findClassesWithName(customClassName)).thenReturn(Lists.newArrayList("", ""));

        this.classUnderTest.fetchDependency(customClassName);
    }

    @Test(expected = DatapiException.class)
    public void fetchCustomDependencies_custom_not_found() throws Exception {
        String customClassName = "Something";
        String className = String.format("Map<List<Integer>, %s>", customClassName);

        when(this.searchService.findClassesWithName(customClassName)).thenReturn(Lists.newArrayList());

        this.classUnderTest.fetchDependency(className);
    }

    @Test
    public void fetchCustomDependencies() throws Exception {
        String absPath = "com/example/Something.java";
        String customClassName = "Something";

        when(this.searchService.findClassesWithName(customClassName)).thenReturn(Lists.newArrayList(absPath));

        Dependency foundDependency = this.classUnderTest.fetchDependency(customClassName);

        assertThat(foundDependency.getPath(), equalTo("com.example.Something"));
    }

}