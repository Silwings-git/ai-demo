package cn.silwings.langchain4j.boot.std13_spring_boot_integration.declarative;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MathTools {

    @Tool("计算两数之和")
    public BigDecimal add(@P("第一个数") BigDecimal num1,@P("第二个数") BigDecimal num2) {
        return num1.add(num2);
    }

}
