package autoria.service;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProfanityFilterService {

    private static final String PROFANITY_REGEX = "\\b(fuck|shit|damn|asshole|bitch|dick|bastard)\\b";

    public boolean containsProfanity(String text){
        Pattern pattern = Pattern.compile(PROFANITY_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        return matcher.find();
    }

}
