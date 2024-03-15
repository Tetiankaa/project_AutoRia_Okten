package autoria.filter;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ProfanityFilter {

    private static final String PROFANITY_REGEX = "\\b(fuck|shit|damn|asshole|bitch|dick|bastard)\\b";

    public boolean containsProfanity(String text){
        Pattern pattern = Pattern.compile(PROFANITY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        return matcher.find();
    }

}
