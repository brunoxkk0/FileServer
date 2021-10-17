package br.com.brunoxkk0.dfs.server.protocol.http.router.validator;

import br.com.brunoxkk0.dfs.server.protocol.http.router.model.Rule;
import br.com.brunoxkk0.dfs.server.protocol.http.router.model.Rule.RuleType;
import br.com.brunoxkk0.dfs.server.protocol.http.router.model.RuleArguments.Keys;

import java.util.Set;

public class Validator {

    public static ValidRule Auth =          ValidRule.of(RuleType.AUTH, Keys.passwdFile, Keys.hashMode, Keys.authHandler);
    public static ValidRule ContentType =   ValidRule.of(RuleType.CONTENT_TYPE, Keys.acceptContent);
    public static ValidRule Index =         ValidRule.of(RuleType.INDEX, Keys.index);

    public interface ValidRule{

        RuleType rule();
        Set<Keys> expectedKeys();

        static ValidRule of(RuleType ruleType, Keys... expectedKeys){
            return new ValidRule() {
                public RuleType rule() {
                    return ruleType;
                }
                public Set<Keys> expectedKeys() {
                    return Set.of(expectedKeys);
                }
            };
        }

    }

    public static boolean isValid(ValidRule expected, Rule rule){

        if(expected.rule() != rule.getType())
            return false;

        return expected.expectedKeys().equals(rule.getArguments().toMap().keySet());

    }

}
