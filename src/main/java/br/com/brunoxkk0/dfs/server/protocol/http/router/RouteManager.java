package br.com.brunoxkk0.dfs.server.protocol.http.router;

import br.com.brunoxkk0.dfs.server.protocol.http.router.model.Route;

import java.util.ArrayList;

public class RouteManager {

    private static final ArrayList<Route> routes = new ArrayList<>();

    public static ArrayList<Route> routes() {
        return routes;
    }
}
