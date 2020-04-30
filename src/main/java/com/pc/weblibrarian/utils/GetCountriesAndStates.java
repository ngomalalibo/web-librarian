package com.pc.weblibrarian.utils;

import com.pc.weblibrarian.model.Country;
import com.pc.weblibrarian.model.State;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

//Consuming a json array
@Slf4j
public class GetCountriesAndStates
{
    private static Client client;
    private static WebTarget target;
    
    @Value("${weblibrarian.countryCodes}")
    private static String appId;
    
    // private static final Logger log = LoggerFactory.getLogger(GetCountriesAndStates.class);
    
    //@PostConstruct
    protected static void init()
    {
        client = ClientBuilder.newClient();
        //query params: ?q=Turku&cnt=10&mode=json&units=metric
        target = client.target("http://battuta.medunes.net/api/country/all/?key=e62b4a753ecab1669cea0e47b8ba18b5");
    }
    
    public static List<Country> getCountries()
    {
        List<Country> cc = new ArrayList<>();
        try
        {
            URL url = new URL("http://battuta.medunes.net/api/country/all/?key=e62b4a753ecab1669cea0e47b8ba18b5");
            URLConnection yc = url.openConnection();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("ACCEPT", "application/json");
            
            if (conn.getResponseCode() == 200)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String output;
                Country country;
                JSONArray jsonArray;
                if ((output = in.readLine()) != null)
                {
                    //How to consume a json array string. Quite useful
                    jsonArray = new JSONArray(output);
                    int i = 0;
                    while (i < jsonArray.length())
                    {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String name = jsonObj.getString("name");
                        String code = jsonObj.getString("code");
                        country = new Country(name, code);
//                        System.out.println(country.toString());
                        
                        cc.add(country);
                        i++;
                    }
                }
            }
        }
        catch (IOException io)
        {
            io.getMessage();
            io.printStackTrace();
        }
        
        return cc;
    }
    
    public static List<State> getStates(String countryCode)
    {
        List<State> states = new ArrayList<>();
        
        try
        {
            String webService = "http://battuta.medunes.net/api/region/" + countryCode + "/all/?key=e62b4a753ecab1669cea0e47b8ba18b5";
            URL url = new URL(webService);
            URLConnection yc = url.openConnection();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("ACCEPT", "application/json");
            
            if (conn.getResponseCode() == 200)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String output;
                State state;
                
                if ((output = in.readLine()) != null)
                {
                    JSONArray jsonArray = new JSONArray(output);
                    int i = 0;
                    while (i < jsonArray.length())
                    {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String region = jsonObj.getString("region");
                        state = new State(region, countryCode);
                        // System.out.println("state = " + state.toString());
                        states.add(state);
                        i++;
                    }
                    
                }
            }
        }
        catch (IOException io)
        {
            io.getMessage();
            io.printStackTrace();
        }
        
        
        return states;
    }
    
    public static void main(String[] args)
    {
        init();
        //getCountries();
        getStates("ng");
    }
    
    
    /*public static Collection<String> getCountries()
    {
        Set<String> list = new LinkedHashSet<>(250);
        list.add("United States");
        for (String countryCode : Locale.getISOCountries())
        {
            list.add(new Locale("", countryCode).getDisplayCountry());
        }
        return list;
    }
    
    public static Collection<String> getStates(String country)
    {
        Set<String> list = new LinkedHashSet<>(250);
        list.add("United States");
        for (String countryCode : Locale.getISOCountries())
        {
            list.add(new Locale("", countryCode).getDisplayCountry());
        }
        return list;
    }*/
    /*
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder)
    {
        return builder.build();
    }
    
    @Bean
    public List<Country> run(RestTemplate restTemplate) throws Exception
    {
        
        
        Country country = restTemplate.getForObject("http://battuta.medunes.net/api/country/all/?key=e62b4a753ecab1669cea0e47b8ba18b5"
                , Country.class);
        log.info("Country: " + country.toString());
        
    }*/
}
