package app.config;

import java.net.URI;
import java.net.URISyntaxException;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

 @Profile("heroku")
@Configuration
public class HerokuDbConfig {

    @Bean
    public DataSource dataSource()  {    

        URI dbUri;
        try {
            // herokun dburl format:
            // postgres://user3123:passkja83kd8@ec2-117-21-174-214.compute-1.amazonaws.com:6212/db982398
            String username = "username";
            String password = "password";
            String url = "jdbc:postgresql://localhost/dbname";
            String dbProperty = System.getProperty("database.url");
            if(dbProperty != null) {
                dbUri = new URI(dbProperty);

                username = dbUri.getUserInfo().split(":")[0];
                password = dbUri.getUserInfo().split(":")[1];
                url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            }     

            DataSource basicDataSource = new SimpleDriverDataSource(null, url, username, password);
            return basicDataSource;
        } catch (URISyntaxException e) {
            //Deal with errors here.
        }
        return null;
    }
}