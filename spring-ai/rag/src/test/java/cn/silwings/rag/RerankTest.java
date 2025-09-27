package cn.silwings.rag;

import com.alibaba.cloud.ai.advisor.RetrievalRerankAdvisor;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.rerank.DashScopeRerankModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RerankTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public VectorStore vectorStore(final OllamaEmbeddingModel embeddingModel) {
            return SimpleVectorStore.builder(embeddingModel).build();
        }
    }

    @BeforeEach
    public void init(@Autowired VectorStore vectorStore) {
        vectorStore.add(this.getDocuments());
    }

    @Test
    public void testRerank(@Autowired VectorStore vectorStore,
                           @Autowired DashScopeRerankModel dashScopeRerankModel,
                           @Autowired DashScopeChatModel dashScopeChatModel) {
        final ChatClient chatClient = ChatClient.builder(dashScopeChatModel).build();

        // 粗排建议设置较大的topK(例如200),精排可以小一些(默认5)
        final RetrievalRerankAdvisor rerankAdvisor = new RetrievalRerankAdvisor(
                vectorStore,
                dashScopeRerankModel,
                SearchRequest.builder().topK(100).build()
        );

        final String content = chatClient.prompt()
                .user("怎么联系运维")
                .advisors(rerankAdvisor)
                .call()
                .content();
        System.out.println(content);
    }

    private List<Document> getDocuments() {
        final String text = """
                Java 程序员入职实操手册
                一、入职基础认知
                （一）团队与业务认知
                组织架构：明确所在技术团队定位（如后端研发组、微服务架构组）、直接对接的产品 / 测试同事及汇报路径，可通过企业微信组织架构表快速查询。
                业务概览：重点熟悉当前负责产品线的核心逻辑（参考《产品业务白皮书》），例如电商项目需掌握 “下单 - 支付 - 履约” 全流程，OA 系统需明晰 “权限审批 - 数据流转” 规则，可结合历史项目文档及产品演示视频快速入门。
                （二）核心制度须知
                考勤与办公：执行弹性考勤（核心工作时间 9:30-18:00），请假需通过 OA 系统提交申请；办公设备故障联系 IT 运维组（邮箱：it_support@company.com），申领开发工具需走行政物品申请流程。
                保密规范：严禁将代码、数据库配置、业务数据等敏感信息存储至私人设备或外部云盘，对外沟通需遵守《信息披露管理办法》。
                二、开发环境搭建指南
                （一）基础工具配置
                工具类别
                推荐版本
                配置要点
                JDK
                11/17
                配置 JAVA_HOME 环境变量，通过java -version验证
                IDE
                IntelliJ IDEA 2023+
                安装 Lombok、Alibaba Java Coding Guidelines 等插件
                版本控制
                Git 2.30+
                配置用户信息git config --global user.name "姓名"，绑定企业 GitLab 账号
                
                （二）核心环境部署
                本地开发环境：
                从 GitLab 克隆项目代码（地址：http://gitlab.company.com/xxx/project），切换至开发分支（格式：feature / 姓名缩写 - 功能名称）。
                配置数据库连接：开发环境 MySQL 地址为 jdbc:mysql://dev-mysql.company.com:3306/xxx_db，账号密码从《开发环境配置手册》获取；Redis 连接使用 dev-redis 集群，端口 6379。
                启动 Spring Boot 项目：通过 IDEA 的 Spring Boot Run 配置启动类，VM 参数参考项目 README 中的 “本地启动配置”。
                测试与生产环境：
                测试环境通过 Jenkins 触发构建（地址：http://jenkins.company.com），选择对应项目的 “测试环境部署” 任务，构建参数填写分支名称。
                生产环境部署需提交审批单至运维组，附上线方案及回滚预案，由运维团队执行部署。
                三、技术开发规范
                （一）代码编写规范
                命名规则：
                类名采用 UpperCamelCase（如 OrderService），方法名、变量名采用 lowerCamelCase（如 createOrder），常量全大写且用下划线分隔（如 MAX_ORDER_COUNT）。
                包结构遵循 “com.company. 产品线。模块。功能”（如 com.company.ecommerce.order.service）。
                编码要求：
                遵循《Alibaba Java 开发手册》，通过 IDEA 插件实时检测违规项，禁止魔法值、未关闭流等问题。
                核心业务逻辑需编写单元测试（使用 JUnit 5），覆盖率不低于 70%，提交代码前执行mvn test验证。
                接口返回统一使用 Result 封装类，格式为{"code":200,"msg":"success","data":{}}，异常通过全局异常处理器统一捕获。
                （二）数据库操作规范
                表设计：表名前缀为 “t_”（如 t_order），字段名用下划线分隔（如 order_create_time），必须包含 id（主键）、create_time、update_time 字段。
                SQL 优化：
                避免 select *，禁止在 where 子句使用函数操作索引字段，复杂查询需通过 EXPLAIN 分析执行计划。
                批量操作使用 MyBatis 的 foreach 标签，批量插入一次不超过 1000 条，高并发场景优先使用分库分表中间件（如 Sharding-JDBC）。
                （三）微服务开发规范
                服务拆分：遵循 “单一职责” 原则，服务粒度控制在 “一个业务域一个服务”，如订单服务、库存服务独立部署。
                通信与注册：
                服务间调用优先使用 Feign，超时时间配置为 1s（特殊场景可调整至 3s），失败重试采用熔断机制（使用 Sentinel）。
                服务注册至 Nacos 集群，配置文件中 spring.cloud.nacos.discovery.server-addr 填写nacos1.company.com:8848,nacos2.company.com:8848。
                四、工作流程与协作
                （一）开发流程
                需求对接：参加需求评审会，明确需求边界，确认技术实现方案，同步更新《需求技术拆解文档》。
                迭代开发：
                从 Jira 认领任务（地址：http://jira.company.com），将任务状态更新为 “进行中”，预估开发周期并同步至项目组。
                每日提交代码至少 1 次，提交信息格式为 “[任务 ID] 功能描述”（如 “[PROJ-123] 实现订单超时取消功能”）。
                测试与上线：
                开发完成后提交 “测试申请” 至测试组，附自测报告，修复 Bug 后将 Jira 任务状态更新为 “已解决”。
                测试通过后参与上线评审，确认无误后配合运维完成生产环境部署，上线后观察 15 分钟系统监控（地址：http://monitor.company.com）。
                （二）协作机制
                会议制度：每日 10:00 参加站会，汇报 “昨日进展、今日计划、阻塞问题”；每周五参与迭代复盘会，总结开发过程中的问题与改进方案。
                文档协作：技术方案、接口说明等文档统一存储在 Confluence（地址：http://confluence.company.com），文档需标注版本号及更新时间。
                问题求助：技术问题优先在团队钉钉群提问，@对应领域专家（如 JVM 问题 @张三，微服务问题 @李四）；紧急问题可直接联系导师或组长。
                五、问题排查与支持
                （一）常见问题解决
                本地启动失败：
                检查 JDK 版本与项目要求是否一致，环境变量配置是否正确。
                排查端口冲突（使用netstat -ano | findstr 端口号查看占用进程，结束对应进程）。
                核对数据库、Redis 连接信息，确认开发环境服务是否正常运行（可通过运维监控平台查看）。
                接口调用异常：
                查看 Nacos 控制台确认服务是否正常注册，Feign 客户端接口与服务端是否一致。
                检查 Sentinel 控制台是否触发熔断，调整熔断阈值或优化接口响应时间。
                （二）支持渠道
                导师支持：入职后分配专属导师，负责解答技术疑问、指导工作流程，每周进行 1 次一对一沟通。
                资源库：
                技术文档：Confluence “开发知识库”（含规范手册、故障案例）。
                培训视频：企业大学 “Java 开发系列课程”（地址：http://edu.company.com/java）。
                运维支持：IT 运维组电话 400-888-8888，工作时间 10 分钟内响应。
                六、成长与发展
                技术提升：每月组织 2 次技术分享会，可主动报名分享技术心得；鼓励参与开源项目或技术社区交流（如 Java 社区、Spring 社区）。
                晋升路径：初级开发→中级开发→高级开发→技术专家 / 架构师，每年 4 月、10 月开展晋升评审，评审依据包括技术能力、项目贡献、团队协作等。
                """;

        return splitStringByLength(text, 100)
                .stream()
                .map(e -> Document.builder().text(e).build())
                .toList();
    }

    /**
     * 将字符串按固定位数切分
     *
     * @param input     要切分的字符串
     * @param chunkSize 每个子字符串的长度
     * @return 切分后的子字符串列表
     */
    public static List<String> splitStringByLength(String input, int chunkSize) {
        final List<String> result = new ArrayList<>();

        // 验证输入参数
        if (input == null || input.isEmpty()) {
            return result;
        }

        if (chunkSize <= 0) {
            throw new IllegalArgumentException("切分长度必须大于0");
        }

        // 计算需要切分的次数
        int length = input.length();
        int chunks = (length + chunkSize - 1) / chunkSize;

        // 进行切分
        for (int i = 0; i < chunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, length);
            result.add(input.substring(start, end));
        }

        return result;
    }

}
