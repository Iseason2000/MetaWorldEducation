package top.iseason.metaworldeducation;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableCaching
@SpringBootApplication
@EnableTransactionManagement
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MetaWorldEducationApplication {
    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(MetaWorldEducationApplication.class, args);
//        documentGeneration();
    }

    /**
     * 文档生成
     */
    static void documentGeneration() {

        //数据源
//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/MetaverseDataBase?useSSL=false&allowPublicKeyRetrieval=true");
//        hikariConfig.setUsername("root");
//        hikariConfig.setPassword("password");
//        //设置可以获取tables remarks信息
//        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
//        hikariConfig.setMinimumIdle(2);
//        hikariConfig.setMaximumPoolSize(5);
//        DataSource dataSource = new HikariDataSource(hikariConfig);

        //生成配置
        EngineConfig engineConfig = EngineConfig.builder()
                //生成文件路径
                .fileOutputDir("C:\\Users\\Iseason2000\\Desktop")
                //打开目录
                .openOutputDir(false)
                //文件类型
                .fileType(EngineFileType.HTML)
                //生成模板实现
                .produceType(EngineTemplateType.freemarker)
                //自定义文件名称
                .fileName("元宇宙教育平台数据库设计文档").build();
        //配置
        Configuration config = Configuration.builder()
                //版本
                .version("1.0.0")
                //描述
                .description("元宇宙教育平台数据库设计文档")
                //数据源
                .dataSource(applicationContext.getBean(DataSource.class))
                //生成配置
                .engineConfig(engineConfig)
                .build();
        //执行生成
        new DocumentationExecute(config).execute();
    }

}
