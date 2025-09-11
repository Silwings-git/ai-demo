package cn.silwings.mcp.server.stdio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WeatherService {

    @Tool(description = "获取指定经纬度的天气预报")
    public String getWeather(@ToolParam(description = "经度") final double longitude, @ToolParam(description = "纬度") final double latitude) {
        log.info("获取天气,latitude:{},longitude:{}", latitude, longitude);
        return "阳光明媚";
    }

}
