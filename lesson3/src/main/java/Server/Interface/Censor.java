package Server.Interface;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Censor {

    String censored(String uncensoredMessage) throws IOException;

}
