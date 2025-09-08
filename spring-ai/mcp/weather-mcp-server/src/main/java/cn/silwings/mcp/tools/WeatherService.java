package cn.silwings.mcp.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class WeatherService {

    private static final Random random = new Random();

    @Tool(description = "Get weather information by city name")
    public String getWeather(String cityName) {
        return "风和日丽,28摄氏度";
    }

}