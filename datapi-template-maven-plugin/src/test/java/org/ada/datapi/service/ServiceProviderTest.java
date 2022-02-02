package org.ada.datapi.service;

import org.ada.datapi.util.TestUtil;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class ServiceProviderTest {

    @Test
    public void testCannotInstantiate() throws Exception {
        assertTrue(TestUtil.testUtilClass(ServiceProvider.class));
    }

    @Test
    public void getConfigurationService() throws Exception {
        assertThat(ServiceProvider.getConfigurationService(), sameInstance(ServiceProvider.getConfigurationService()));
    }

    @Test
    public void getTypeService() throws Exception {
        assertThat(ServiceProvider.getTypeService(), sameInstance(ServiceProvider.getTypeService()));
    }

    @Test
    public void getSearchService() throws Exception {
        assertThat(ServiceProvider.getSearchService(), sameInstance(ServiceProvider.getSearchService()));
    }

    @Test
    public void getDependencyService() throws Exception {
        assertThat(ServiceProvider.getDependencyService(), sameInstance(ServiceProvider.getDependencyService()));
    }

    @Test
    public void getModelTemplateService() throws Exception {
        assertThat(ServiceProvider.getModelTemplateService(), sameInstance(ServiceProvider.getModelTemplateService()));
    }

    @Test
    public void getControllerTemplateService() throws Exception {
        assertThat(ServiceProvider.getControllerTemplateService(), sameInstance(ServiceProvider.getControllerTemplateService()));
    }

    @Test
    public void getRepositoryTemplateService() throws Exception {
        assertThat(ServiceProvider.getRepositoryTemplateService(), sameInstance(ServiceProvider.getRepositoryTemplateService()));
    }


    @Test
    public void getServiceTemplateService() throws Exception {
        assertThat(ServiceProvider.getServiceTemplateService(), sameInstance(ServiceProvider.getServiceTemplateService()));
    }

}