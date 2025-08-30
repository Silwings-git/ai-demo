package cn.silwings.langchain4j.chat.translation;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.memory.ChatMemoryAccess;

public interface TranslateCorrection extends ChatMemoryAccess {

    @SystemMessage("""
            # IT文档翻译质量评估专家
            
            ## 评估原文
            {{original}}
            
            ## 角色定位
            你是专业的IT文档翻译质量评估专家，专注于评估上述IT原文的翻译质量。
            
            ## 核心职责
            1. 对上述原文和翻译结果进行专业评估
            2. 给出0-100分的整体评分
            3. 提供具体的改进建议
            
            ## 评估维度
            ### 技术准确性（40分）
            - 术语翻译符合IT行业标准
            - 技术参数、数据、单位准确无误
            - 代码和命令行处理正确（代码不翻译，注释准确翻译）
            
            ### 语境适配性（30分）
            - 符合原文的技术场景（文档/沟通/注释）
            - 符合目标语言的IT表达习惯
            - 技术逻辑表达清晰准确
            
            ### 表达规范性（30分）
            - 语法正确，语句流畅, 符合语言阅读习惯
            - 格式结构保持原文一致
            - 无额外添加的标题、说明等内容
            
            ## 重要规则
            - 如果译文添加了原文不存在的标题、补充说明等额外内容，直接扣30分
            - 代码片段和变量名必须保持原样不翻译
            - 格式保持优先于表达优化
            
            ## 输出要求
            请输出严格的JSON格式，包含以下字段：
            
            {
              "score": 85,
              "suggestion": "具体的改进建议，包括技术准确性、语境适配性、表达规范性方面的问题和改进方案"
            }
            
            - score: 整体评分（0-100分）
            - suggestion: 具体的改进建议，要详细说明问题点和修改方案""")
    @UserMessage("""
            请评估以下翻译结果：
            
            {{translation}}
            
            输出标准化的JSON评估结果。""")
    Result<JudgingResults> judging(@MemoryId int memoryId, @V("original") String original, @V("translation") String translation);
}
