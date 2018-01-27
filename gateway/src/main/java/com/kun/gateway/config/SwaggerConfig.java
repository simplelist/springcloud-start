package com.kun.gateway.config;

import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Primary
@EnableSwagger2
public class SwaggerConfig implements SwaggerResourcesProvider {
    private final RouteLocator routeLocator;

    public SwaggerConfig(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();
        List<Route> routes = routeLocator.getRoutes();
        System.out.println(Arrays.toString(routes.toArray()));
        routes.forEach(route -> {
            resources.add(swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs"),"2.0"));
        });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }

}
