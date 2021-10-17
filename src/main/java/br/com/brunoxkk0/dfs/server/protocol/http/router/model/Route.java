package br.com.brunoxkk0.dfs.server.protocol.http.router.model;

public class Route {

    public enum RouteMode {
        READ,
        WRITE,
        BOTH
    }

    public Route(String name, String description, String location, RouteMode routeMode, Rule ... rules) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.routeMode = routeMode;
        this.rules = rules;
    }

    private final String name;
    private final String description;
    private final String location;
    private final RouteMode routeMode;
    private final Rule[] rules;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public RouteMode getRouteMode() {
        return routeMode;
    }

    public Rule[] getRules() {
        return rules;
    }
}
