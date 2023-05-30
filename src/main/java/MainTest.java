import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSenderImpl;

import java.util.*;

class MainTest {
    private GeoService geoService;
    private LocalizationService localizationService;
    private MessageSenderImpl messageSender;
    private Map<String, String> map = new HashMap<String, String>();
    private Map<String, String> ipMap = new HashMap<String, String>();
    private String USAIp = "96.";
    private String RUSIp = "172.";

    @BeforeEach
    void init() {
        geoService = Mockito.Mock(GeoService.class);
        localizationService = Mockito.Mock(LocalizationService.class);
        messageSender = new MessageSenderImpl(geoService, localizationService);
    }

    @Test
    void test_messageSender_returns_USA() {
        map.put(MessageSenderImpl.IP_ADDRESS_HEADER, USAIp);
        Set<Country> countries = new HashSet<Country>(Arrays.asList(Country.BRAZIL, Country.USA, Country.GERMANY, Country.RUSSIA));

        Location location = geoService.byIp(USAIp);
        Country country = Country.USA;

        Mockito.when(geoService.byIp(USAIp)).thernReturn(countries);
        Mockito.when(localizationService.locale(location.getCountry())).thenReturn(country);

        String result = messageSender.send(map);
        messageSender.send(map);

        Assertions.assertEquals(result, localizationService.locale(Country.USA));
    }

    @Test
    void test_messageSender_returns_Russia() {
        map.put(MessageSenderImpl.IP_ADDRESS_HEADER, RUSIp);
        Set<Country> countries = new HashSet<Country>(Arrays.asList(Country.BRAZIL, Country.USA, Country.GERMANY, Country.RUSSIA));

        Location location = geoService.byIp(RUSIp);
        Country country = Country.RUSSIA;

        Mockito.when(geoService.byIp(RUSIp)).thernReturn(countries);
        Mockito.when(localizationService.locale(location.getCountry())).thenReturn(country);

        String result = messageSender.send(map);
        messageSender.send(map);

        Assertions.assertEquals(result, localizationService.locale(Country.RUSSIA));
    }

    @Test
    void test_geo_byIP() {
        ipMap.put("moscowIP", "172.0.32.11");
        ipMap.put("newYorkIP", "96.44.183.149");
        ipMap.put("localHost", "127.0.0.1");
        ipMap.put("USAIp", "96.");
        ipMap.put("RUSIp", "172.");

        Location loc = geoService.byIp(ipMap.get("moscowIP"));
        Country ctr = Country.RUSSIA;

        Assertions.assertTrue(loc.getCountry().equals(ctr));
    }

    @Test
    void test_locate() {
        List<Country> list = new ArrayList<Country>();
        list.addAll(Arrays.asList(Country.RUSSIA, Country.GERMANY, Country.BRAZIL, Country.USA));

        for (Country ctr : list) {
            String answer = localizationService.locale(ctr);

            if (ctr.equals(Country.RUSSIA)) {
                Assertions.assertEquals(answer, "Добро пожаловать");
            } else {
                Assertions.assertEquals(answer, "Welcome");
            }
        }
    }
}