package database;

import java.io.File;
import java.io.IOException;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.crypt.CryptCodecProvider;

import net.ucanaccess.jdbc.JackcessOpenerInterface;

public class CryptCodecOpener implements JackcessOpenerInterface {

    @Override
    public Database open(File file, String s) throws IOException {

        DatabaseBuilder databaseBuilder = new DatabaseBuilder(file);

        databaseBuilder.setAutoSync(false);
        databaseBuilder.setCodecProvider(new CryptCodecProvider(s));
        databaseBuilder.setReadOnly(false);

        return databaseBuilder.open();
    }
}