package org.ada.datapi.service.it;

import org.ada.datapi.service.ConfigurationServiceTest;
import org.ada.datapi.service.SearchService;
import org.ada.datapi.service.ServiceProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isIn;

public class SearchServiceIt {

    private SearchService classUnderTest = ServiceProvider.getSearchService();

    @Before
    public void setUp() {
        ConfigurationServiceTest.transformToItService(ServiceProvider.getConfigurationService());
    }

    @Test
    public void findClassWithName() {
        List<String> filesFound = this.classUnderTest.findClassesWithName("duplicate");

        assertThat(filesFound, hasSize(2));

        assertThat("com/example/app/dir1/Duplicate.java", isIn(filesFound));
        assertThat("com/example/app/dir2/Duplicate.java", isIn(filesFound));
    }

}
