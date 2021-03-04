package com.rudiwijaya.crud.demo.config.rs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.HateoasSortHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.PagedResourcesAssemblerArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
//import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @author Rudi_W144
 */
@Configuration
@EnableHypermediaSupport(type={EnableHypermediaSupport.HypermediaType.HAL})
//@EnableEntityLinks
//@EnableSpringDataWebSupport
public class SpringHateoasConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);

        argumentResolvers.add(hateoasPageableResolver());
        argumentResolvers.add(hateoasSortResolver());
        argumentResolvers.add(pagedResourcesAssemblerArgumentResolver());
    }

    @Bean
    public HateoasPageableHandlerMethodArgumentResolver hateoasPageableResolver() {
        return new HateoasPageableHandlerMethodArgumentResolver(hateoasSortResolver());
    }

    @Bean
    public HateoasSortHandlerMethodArgumentResolver hateoasSortResolver() {
        return new HateoasSortHandlerMethodArgumentResolver();
    }

    @Bean
    public PagedResourcesAssemblerArgumentResolver pagedResourcesAssemblerArgumentResolver() {
        return new PagedResourcesAssemblerArgumentResolver(hateoasPageableResolver(), null);
    }

    @Bean
    public PagedResourcesAssembler<?> pagedResourcesAssembler() {
        return new PagedResourcesAssembler<Object>(hateoasPageableResolver(), null);
    }

}
