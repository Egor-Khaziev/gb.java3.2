package Client1.Interfaces;

import java.io.IOException;

public interface History {

    public void saveMessages (String st);

    public void loadMessages () throws IOException;

}
