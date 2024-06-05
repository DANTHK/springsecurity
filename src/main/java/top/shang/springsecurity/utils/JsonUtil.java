package top.shang.springsecurity.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * jackson工具类
 *
 * @author shangzp
 * @since 2023/11/1 10:26
 */
@Slf4j
public class JsonUtil {
    private static final ObjectMapper objectMapper;
    private static final ObjectMapper objectMapperIgnoreNull;

    static {
        objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        objectMapperIgnoreNull = new ObjectMapper();
        objectMapperIgnoreNull.registerModule(javaTimeModule);
        objectMapperIgnoreNull.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapperIgnoreNull.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * 对象转json字符串
     *
     * @return json字符串 转换失败返回{}，而不是null或者""
     */
    public static String toJsonStr(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.info("obj转json字符串失败｜错误信息：", e);
            return "{}";
        }
    }

    /**
     * json字符串转obj
     *
     * @param jsonStr json字符串
     * @param clazz   转换的对象类型
     * @return 转换失败返回null
     */
    public static <T> T toObj(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            log.info("json字符串转obj失败｜错误信息：", e);
            return null;
        }
    }

    /**
     * json字符串转obj
     *
     * @param jsonStr       json字符串
     * @param typeReference 类型
     * @return 转换失败返回null
     */
    public static <T> T toObj(String jsonStr, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(jsonStr, typeReference);
        } catch (JsonProcessingException e) {
            log.info("json字符串转bean失败|错误信息：", e);
            return null;
        }
    }

    /**
     * json字符串转list
     *
     * @param jsonStr json字符串
     * @param clazz   转换的对象类型
     * @return 转换失败返回Collections.emptyList()
     */
    public static <T> List<T> toList(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            log.info("json字符串转list失败｜错误信息：", e);
            return Collections.emptyList();
        }
    }

    /**
     * obj转map
     *
     * @param obj 要转换的对象
     * @return {@code  Map<String, Object>}
     */
    public static Map<String, Object> toMap(Object obj) {
        try {
            return objectMapper.convertValue(obj, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.info("obj转map失败｜错误信息：", e);
            return Map.of();
        }
    }

    /**
     * obj转map
     *
     * @param obj 要转换的对象
     * @return {@code  Map<String, Object>}
     */
    public static Map<String, Object> toMapIgnoreNull(Object obj) {
        try {
            return objectMapperIgnoreNull.convertValue(obj, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.info("obj转map失败｜错误信息：", e);
            return Map.of();
        }
    }

}
