package br.com.brunoxkk0.dfs.server.protocol.http.router.model;

public class Rule {

    public enum RuleType {
        AUTH, CONTENT_TYPE, INDEX
    }

    private String name;
    private String description;

    private RuleType type;
    private RuleArguments arguments;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public RuleType getType() {
        return type;
    }

    public RuleArguments getArguments() {
        return arguments;
    }
}
